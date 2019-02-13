package xyz.discretezoo.web.db

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import xyz.discretezoo.web.db.ZooGraph._
import xyz.discretezoo.web.db.ZooManiplex.ManiplexTable
import xyz.discretezoo.web.db.ZooPostgresProfile.api._
import slick.collection.heterogeneous.HNil
import slick.lifted.TableQuery

import slick.ast.Ordering
import scala.concurrent.{Await, ExecutionContext, Future}

object ZooDbNew {

  implicit val system: ActorSystem = ActorSystem("ZooActors")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext: ExecutionContext = system.dispatcher

  val db: ZooPostgresProfile.backend.DatabaseDef = Database.forURL(
    scala.util.Properties.envOrElse("ZOODB_JDBC", "jdbc:postgresql://localhost:5432/discretezoo2"),
    scala.util.Properties.envOrElse("ZOODB_USER", "discretezoo"),
    scala.util.Properties.envOrElse("ZOODB_PASS", "D!screteZ00"),
    null,
    "org.postgresql.Driver")

  object graphs extends TableQuery(new GraphTable(_))

  def t(g: GraphTable) = g.indexCVT

  def getGraphs(collections: Seq[String], filters: Seq[Condition], sort: Seq[OrderBy], limit: Int, page: Int): Future[Seq[Graph]] = {
    val offset = (page - 1) * limit
    val sortBy = sort.map(s => s.order match {
      case "ASC" => Some((s.field, Ordering.Asc))
      case "DESC" => Some((s.field, Ordering.Desc))
      case _ => None
    }).collect({ case Some(v) => v })
    val future = for {
      graph <- graphs
        .filterIf(collections.nonEmpty)(g => GraphColumns.filterCollections(g, collections))
        .filter(g => GraphColumns.filter(g, filters))
        .drop(offset).take(limit)
        .dynamicSortBy(sortBy)
        .result
    } yield graph
    db.run(future)
  }

//  def create(): Unit = {
//    val setup = DBIO.seq(graphs.schema.create, maniplexes.schema.create)
//    try {
//      Await.result(ZooDbNew.db.run(setup), Duration.Inf)
//    }
//  }

}
