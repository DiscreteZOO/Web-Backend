package xyz.discretezoo.web.db

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContext, Future}
import slick.lifted.TableQuery
import xyz.discretezoo.web.ZooJsonAPI.{Count, ResultsParameters, SearchParameters}
import xyz.discretezoo.web.{ResultParam, SearchParam, ZooObject, ZooPostgresProfile}
import xyz.discretezoo.web.ZooPostgresProfile.api._
import xyz.discretezoo.web.db.ZooExample.{ExamplePlainQuery, ExampleTable}
import xyz.discretezoo.web.db.ZooGraph.{GraphPlainQuery, GraphTable}
import xyz.discretezoo.web.db.ZooManiplex.{ManiplexPlainQuery, ManiplexTable}

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
  object example extends TableQuery(new ExampleTable(_))

  def count(sp: SearchParameters, plain: Boolean = true): Future[Count] = {
    val qp = SearchParam.get(sp.collections, sp.filters)
    val q: DBIO[Int] = countDBIO(sp.objects, qp, plain)
    db.run(q).map(Count)
  }

  def get(rp: ResultsParameters, plain: Boolean = true): Future[(Int, Seq[ZooObject])] = {
    val qp = SearchParam.get(rp.parameters.collections, rp.parameters.filters)
    val count: DBIO[Int] = countDBIO(rp.parameters.objects, qp, plain)
    val get: DBIO[(Int, Seq[ZooObject])] = count.flatMap(c => {
      val pages = (c / rp.pageSize).ceil.toInt
      val actualPage = if (rp.page >= 1 && rp.page <= pages) rp.page else 1
      val parameters = ResultParam.get(actualPage, rp.pageSize, qp, rp.orderBy)
      val query: DBIO[Seq[ZooObject]] = getDBIO(rp.parameters.objects, parameters, plain)
      query.map(seq => (pages, seq))
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

  private def getDBIO(objects: String, rp: ResultParam, plain: Boolean = true): DBIO[Seq[ZooObject]] = {
    (objects, plain) match {
      case ("graphs", true) => GraphPlainQuery.get(rp)
      case ("maniplexes", true) => ManiplexPlainQuery.get(rp)
      case ("example", true) => ExamplePlainQuery.get(rp)
      case ("graphs", false) => graphs.dynamicQueryResults(rp).result
      case ("maniplexes", false) => maniplexes.dynamicQueryResults(rp).result
      case ("example", false) => example.dynamicQueryResults(rp).result
    }
  }

  private def countDBIO(objects: String, qp: SearchParam, plain: Boolean = true): DBIO[Int] = {
    (objects, plain) match {
      case ("graphs", true) => GraphPlainQuery.count(qp)
      case ("maniplexes", true) => ManiplexPlainQuery.count(qp)
      case ("example", true) => ExamplePlainQuery.count(qp)
      case ("graphs", false) => graphs.dynamicQueryCount(qp).length.result
      case ("maniplexes", false) => maniplexes.dynamicQueryCount(qp).length.result
      case ("example", false) => example.dynamicQueryCount(qp).length.result
    }
  }

}
