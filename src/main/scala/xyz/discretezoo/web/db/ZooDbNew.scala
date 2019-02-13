package xyz.discretezoo.web.db

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import xyz.discretezoo.web.db.ZooGraph._
import xyz.discretezoo.web.db.ZooManiplex.ManiplexTable
import xyz.discretezoo.web.db.ZooPostgresProfile.api._
import slick.collection.heterogeneous.HNil

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object ZooDbNew {

  implicit val system: ActorSystem = ActorSystem("ZooActors")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext: ExecutionContext = system.dispatcher

  private val db: ZooPostgresProfile.backend.DatabaseDef = Database.forURL(
    scala.util.Properties.envOrElse("ZOODB_JDBC", "jdbc:postgresql://localhost:5432/discretezoo2"),
    scala.util.Properties.envOrElse("ZOODB_USER", "discretezoo"),
    scala.util.Properties.envOrElse("ZOODB_PASS", "D!screteZ00"),
    null,
    "org.postgresql.Driver")

  private val graphs = TableQuery[GraphTable]
  private val maniplexes = TableQuery[ManiplexTable]

  def t(g: GraphTable) = g.indexCVT

  def getGraphs(collections: Seq[String], filters: Seq[Condition], limit: Int, page: Int): Future[Seq[Graph]] = {  // Seq[OrderBy]
    val offset = (page - 1) * limit
    val future = for {
      graph <- graphs
        .filterIf(collections.nonEmpty)(g => GraphColumns.filterCollections(g, collections))
        .filter(g => GraphColumns.filter(g, filters))
        .drop(offset).take(limit)
//        .sortBy(g => (t(g).desc.nullsFirst :: g.isSPX.asc.nullsDefault :: HNil)) //(t(g).desc.nullsFirst, g.isSPX.asc.nullsDefault)
        .result
    } yield graph
    db.run(future)
  }

  def create(): Unit = {
    val setup = DBIO.seq(graphs.schema.create, maniplexes.schema.create)
    try {
      Await.result(ZooDbNew.db.run(setup), Duration.Inf)
    }
  }

}
