package xyz.discretezoo.web.db

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import xyz.discretezoo.web.db.ZooDB.OrderBy
import xyz.discretezoo.web.db.ZooGraph.{GraphFilters, GraphMain, GraphTableMain, GraphTable}
import xyz.discretezoo.web.db.ZooManiplex.ManiplexTableMain
import xyz.discretezoo.web.db.ZooPostgresProfile.api._

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

  private val graphs = TableQuery[GraphTableMain]
  private val maniplexes = TableQuery[ManiplexTableMain]

  def getGraphs(collections: Seq[String], filters: Seq[Condition], order: Seq[OrderBy], limit: Int, page: Int): Future[Seq[GraphMain]] = {
    val offset = (page - 1) * limit
    val future = for {
      graph <- graphs.filter(g => GraphFilters.filter(g, filters)).drop(offset).take(limit).result

    } yield graph
    db.run(future)
  }

}
