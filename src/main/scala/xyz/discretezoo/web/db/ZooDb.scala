package xyz.discretezoo.web.db

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.{Await, ExecutionContext, Future}
import slick.lifted.TableQuery
import xyz.discretezoo.web.{ResultParam, SearchParam, ZooPostgresProfile}
import xyz.discretezoo.web.db.ZooGraph._
import xyz.discretezoo.web.db.ZooManiplex.{Maniplex, ManiplexTable}
import xyz.discretezoo.web.ZooPostgresProfile.api._

object ZooDb {

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
  object maniplexes extends TableQuery(new ManiplexTable(_))

  def countGraphs(qp: SearchParam): Future[Int] = db.run(graphs.dynamicQueryCount(qp).length.result)
  def countManiplexes(qp: SearchParam): Future[Int] = db.run(maniplexes.dynamicQueryCount(qp).length.result)

  def getGraphs(qp: ResultParam): Future[Seq[Graph]] = db.run(graphs.dynamicQueryResults(qp).result)
  def getManiplexes(qp: ResultParam): Future[Seq[Maniplex]] = db.run(maniplexes.dynamicQueryResults(qp).result)

  def checkSQLSearch(qp: SearchParam): Future[Int] = {
    println(graphs.dynamicQueryCount(qp).length.result.statements)
    db.run(graphs.dynamicQueryCount(qp).length.result)
  }

//  def create(): Unit = {
//    val setup = DBIO.seq(graphs.schema.create, maniplexes.schema.create)
//    try {
//      Await.result(ZooDbNew.db.run(setup), Duration.Inf)
//    }
//  }

}
