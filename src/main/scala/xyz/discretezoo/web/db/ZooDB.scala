package xyz.discretezoo.web.db

import slick.jdbc.GetResult
import xyz.discretezoo.web.db.ZooPostgresProfile.api._
import xyz.discretezoo.web.db.model.Graph.{GraphBoolean, GraphIndex, GraphNumeric}
import xyz.discretezoo.web.db.model.Maniplex.{ManiplexBoolean, ManiplexNumeric, ManiplexString}
import xyz.discretezoo.web.db.model.{Graph, GraphRecord, Maniplex, ManiplexRecord}
import xyz.discretezoo.web.db.v1._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object ZooDB {

  val db: ZooPostgresProfile.backend.DatabaseDef = Database.forURL(
    scala.util.Properties.envOrElse("ZOODB_JDBC", "jdbc:postgresql://localhost:5432/discretezoo2"),
    scala.util.Properties.envOrElse("ZOODB_USER", "discretezoo"),
    scala.util.Properties.envOrElse("ZOODB_PASS", "D!screteZ00"),
    null,
    "org.postgresql.Driver")

  private val graphs: TableQuery[Graphs] = TableQuery[Graphs]
  private val graphsVT: TableQuery[GraphsVT] = TableQuery[GraphsVT]
  private val graphsCVT: TableQuery[GraphsCVT] = TableQuery[GraphsCVT]
  private val graphsSPX: TableQuery[GraphsSPX] = TableQuery[GraphsSPX]

  val g: TableQuery[Graphs] = TableQuery[Graphs]

  // https://medium.com/pragmatic-scala/filter-ops-with-slick-3-1-1b0c4ec59bb1

  protected def getWhere(collections: Seq[String], filters: Seq[String]): String = {
    val collectionFilters = collections.map({
      case "VT" => "vt_index"
      case "CVT" => "cvt_index"
      case "CAT" => "symcubic_index"
      case "M2" => "\"UUID\""
    }).map(c => s"($c IS NOT NULL)").mkString(" OR ")
    val actualFilters = filters ++ Seq(collectionFilters)
    if (actualFilters.nonEmpty) s"WHERE ${actualFilters.map(f => s"($f)").mkString(" AND ")}" else ""
  }

  def getGraphs(collections: Seq[String], filters: Seq[String], limit: Int, order: Seq[OrderBy], page: Int): Future[Seq[GraphRecord]] = {
    val offset = (page - 1) * limit
    def orderBy: String = {
      val columns = order.flatMap(_.toSQL).mkString(",")
      if (columns.nonEmpty) s"ORDER BY $columns" else ""
    }
    val q = sql"""
         SELECT graph.zooid, #${Graph.getColumnList} FROM graph
         LEFT JOIN graph_cvt ON graph.zooid = graph_cvt.zooid
         LEFT JOIN graph_spx ON graph.zooid = graph_spx.zooid
         LEFT JOIN graph_vt ON graph.zooid = graph_vt.zooid
         #${getWhere(collections, filters)}
         #$orderBy
         LIMIT #$limit OFFSET #$offset;
       """.as[GraphRecord]
    db.run(q)
  }

  def getManiplexes(collections: Seq[String], filters: Seq[String], limit: Int, order: Seq[OrderBy], page: Int): Future[Seq[ManiplexRecord]] = {
    val offset = (page - 1) * limit
    def orderBy: String = {
      val columns = order.flatMap(_.toSQL).mkString(",")
      if (columns.nonEmpty) s"ORDER BY $columns" else ""
    }
    val q = sql"""
         SELECT #${Maniplex.getColumnList} FROM maniplexes
         #${getWhere(collections, filters)}
         #$orderBy
         LIMIT #$limit OFFSET #$offset;
       """.as[ManiplexRecord]
    db.run(q)
  }

  def count(objects: String, collections: Seq[String], filters: Seq[String]): Future[Int] = {
    val q = objects match {
      case "graphs" => sql"""
         SELECT COUNT(graph.zooid) FROM graph
         LEFT JOIN graph_cvt ON graph.zooid = graph_cvt.zooid
         LEFT JOIN graph_spx ON graph.zooid = graph_spx.zooid
         LEFT JOIN graph_vt ON graph.zooid = graph_vt.zooid
         #${getWhere(collections, filters)};
       """.as[Int]
      case "maniplexes" => sql"""
         SELECT COUNT("UUID") FROM maniplexes
         #${getWhere(collections, filters)};
       """.as[Int]
    }
    db.run(q.head)
  }

  object Filters {

//    def relevant: Seq[Property] = Graph.all.filter(p => hasDistinctValues("graph", p.name) > 1)

    def hasDistinctValues(select: String, column: String): Int = {
      val q = sql"""SELECT COUNT(*) FROM (SELECT DISTINCT "#$column" FROM "#$select") AS temp;""".as[Int].head
      val f: Future[Int] = db.run(q)
      Await.result(f, Duration("Inf"))
    }

  }

  final case class OrderBy(column: String, ord: String) {
    def toSQL: Option[String] = {
      if (Graph.isValidFilterColumnName(column) && Seq("ASC", "DESC").contains(ord))
        Some(s""""$column" $ord""")
      else None
    }
  }

  implicit val getGraphResult: GetResult[GraphRecord] = GetResult(r => {
    GraphRecord(r.<<,
      GraphIndex(r.<<, r.<<, r.<<),
      GraphBoolean(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<),
      GraphNumeric(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  })

  implicit val getManiplexResult: GetResult[ManiplexRecord] = GetResult(r => {
    ManiplexRecord(
      ManiplexNumeric(r.<<, r.<<, r.<<, r.<<),
      ManiplexBoolean(r.<<, r.<<),
      ManiplexString(r.<<))
  })

}
