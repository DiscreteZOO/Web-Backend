package xyz.discretezoo.web.db

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.{Await, ExecutionContext, Future}
import slick.lifted.TableQuery
import xyz.discretezoo.web.ZooJsonAPI.{Count, ResultsParameters, SearchParameters}
import xyz.discretezoo.web.{ResultParam, SearchParam, ZooObject, ZooPostgresProfile}
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

  val graphTable = new GraphTable(_)

  object graphs extends TableQuery(graphTable)
  object maniplexes extends TableQuery(new ManiplexTable(_))

  def count(sp: SearchParameters): Future[Count] = {
    val qp = SearchParam.get(sp.collections, sp.filters)
    val r = sp.objects match {
      case "graphs" => db.run(graphs.dynamicQueryCount(qp).length.result)
      case "maniplexes" => db.run(maniplexes.dynamicQueryCount(qp).length.result)
    }
    r.map(Count)
  }

  def get(rp: ResultsParameters): Future[(Int, Seq[ZooObject])] = {
    val qp = SearchParam.get(rp.parameters.collections, rp.parameters.filters)
    val count: DBIO[Int] = graphs.dynamicQueryCount(qp).length.result
    val get: DBIO[(Int, Seq[ZooObject])] = count.flatMap(c => {
      val pages = (c / rp.pageSize).ceil.toInt
      val actualPage = if (rp.page >= 1 && rp.page <= pages) rp.page else 1
      val query = rp.parameters.objects match {
        case "graphs" => graphs.dynamicQueryResults(ResultParam.get(actualPage, rp.pageSize, qp, rp.orderBy))
        case "maniplexes" => maniplexes.dynamicQueryResults(ResultParam.get(actualPage, rp.pageSize, qp, rp.orderBy))
      }
      query.result.map(seq => (pages, seq))
    })
    db.run(get)
  }

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
