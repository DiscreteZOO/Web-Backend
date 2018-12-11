package xyz.discretezoo.web.db

import slick.jdbc.{GetResult, SQLActionBuilder}
import xyz.discretezoo.web.db.ZooPostgresProfile.api._
import xyz.discretezoo.web.db.v1._

//import xyz.discretezoo.web.db.v1.Graphs
//import xyz.discretezoo.web.db.M2orbit.M2orbitManiplex
//import xyz.discretezoo.web.db.ManiplexData

import scala.concurrent.duration.{Duration, DurationLong}
import scala.concurrent.{Await, Future}
import scala.concurrent._

object ZooDB {

  private val graphs: TableQuery[Graphs] = TableQuery[Graphs]
  private val graphsVT: TableQuery[GraphsVT] = TableQuery[GraphsVT]
  private val graphsCVT: TableQuery[GraphsCVT] = TableQuery[GraphsCVT]
  private val graphsSPX: TableQuery[GraphsSPX] = TableQuery[GraphsSPX]

  private def connect: ZooPostgresProfile.backend.DatabaseDef = Database.forURL(
    scala.util.Properties.envOrElse("ZOODB_JDBC", "jdbc:postgresql://localhost:5432/discretezoo2"),
    scala.util.Properties.envOrElse("ZOODB_USER", "discretezoo"),
    scala.util.Properties.envOrElse("ZOODB_PASS", "D!screteZ00"),
    null,
    "org.postgresql.Driver")

  // https://medium.com/pragmatic-scala/filter-ops-with-slick-3-1-1b0c4ec59bb1

  private def getWhere(collections: Seq[String], filters: Seq[String]): String = {
    val collectionFilters = collections.map({
      case "VT" => "vt_index"
      case "CVT" => "cvt_index"
      case "CAT" => "symcubic_index"
      case "M2" => "\"UUID\""  // TODO: temporary
    }).map(c => s"($c IS NOT NULL)").mkString(" OR ")
    val actualFilters = filters ++ Seq(collectionFilters)
    if (actualFilters.nonEmpty) s"WHERE ${actualFilters.map(f => s"($f)").mkString(" AND ")}" else ""
  }
  def getGraphs(collections: Seq[String], filters: Seq[String], limit: Int, order: Seq[OrderBy], page: Int): Future[Seq[GraphAllColumns]] = {
    val offset = (page - 1) * limit
    def orderBy: String = {
      val columns = order.flatMap(_.toSQL).mkString(",")
      if (columns.nonEmpty) s"ORDER BY $columns" else ""
    }
    val db = connect
    val q = sql"""
         SELECT graph.zooid, #${GraphColumns.getColumnList} FROM graph
         LEFT JOIN graph_cvt ON graph.zooid = graph_cvt.zooid
         LEFT JOIN graph_spx ON graph.zooid = graph_spx.zooid
         LEFT JOIN graph_vt ON graph.zooid = graph_vt.zooid
         #${getWhere(collections, filters)}
         #$orderBy
         LIMIT #$limit OFFSET #$offset;
       """.as[GraphAllColumns]
    db.run(q)
//    val f: Future[Seq[GraphAllColumns]] =
//    val result = Await.result(f, Duration("Inf"))
//    db.close()
//    result
  }

  def getManiplexes(collections: Seq[String], filters: Seq[String], limit: Int, order: Seq[OrderBy], page: Int): Seq[ManiplexAllColumns] = {
    val offset = (page - 1) * limit
    def orderBy: String = {
      val columns = order.flatMap(_.toSQL).mkString(",")
      if (columns.nonEmpty) s"ORDER BY $columns" else ""
    }
    val db = connect
    val q = sql"""
         SELECT "ORBITS", "SMALL_GROUP_ORDER", "IS_POLYTOPE", "IS_REGULAR", "SYMMETRY_TYPE" FROM maniplexes
         #${getWhere(collections, filters)}
         #$orderBy
         LIMIT #$limit OFFSET #$offset;
       """.as[ManiplexAllColumns]
    val f: Future[Seq[ManiplexAllColumns]] = db.run(q)
    val result = Await.result(f, Duration("Inf"))
    db.close()
    result
  }

  def countGraphs(collections: Seq[String], filters: Seq[String]): Int = {
    val db = connect
    val q = sql"""
         SELECT COUNT(graph.zooid) FROM graph
         LEFT JOIN graph_cvt ON graph.zooid = graph_cvt.zooid
         LEFT JOIN graph_spx ON graph.zooid = graph_spx.zooid
         LEFT JOIN graph_vt ON graph.zooid = graph_vt.zooid
         #${getWhere(collections, filters)};
       """.as[Int]
    val f: Future[Seq[Int]] = db.run(q)
    val result = Await.result(f, Duration("Inf")).head
    db.close()
    result
  }

  def countManiplexes(collections: Seq[String], filters: Seq[String]): Int = {
    val db = connect
    val q = sql"""
         SELECT COUNT("UUID") FROM maniplexes
         #${getWhere(collections, filters)};
       """.as[Int]
    val f: Future[Seq[Int]] = db.run(q)
    val result = Await.result(f, Duration("Inf")).head
    db.close()
    result
  }

  object Filters {

    def relevant: Seq[Property] = GraphColumns.all.filter(p => hasDistinctValues("graph", p.name) > 1)

    def hasDistinctValues(select: String, column: String): Int = {
      val db = connect
      val q = sql"""SELECT COUNT(*) FROM (SELECT DISTINCT "#$column" FROM "#$select") AS temp;""".as[Int].head
      val f: Future[Int] = db.run(q)
      Await.result(f, Duration("Inf"))
    }

  }

  final case class OrderBy(column: String, ord: String) {
    def toSQL: Option[String] = {
      if (GraphColumns.isValidFilterColumnName(column) && Seq("ASC", "DESC").contains(ord))
        Some(s""""$column" $ord""")
      else None
    }
  }

  /* * * * * * * * * * * * * * * * * * * * * * * * * * * *
   * Case class to send graph results to the client
   */

  case class GraphBooleanColumns(
                                  hasMultipleEdges: Boolean,
                                  isArcTransitive: Boolean,
                                  isBipartite: Boolean,
                                  isCayley: Boolean,
                                  isDistanceRegular: Boolean,
                                  isDistanceTransitive: Boolean,
                                  isEdgeTransitive: Boolean,
                                  isEulerian: Boolean,
                                  isForest: Boolean,
                                  isHamiltonian: Option[Boolean],
                                  isOverfull: Boolean,
                                  isPartialCube: Boolean,
                                  isSplit: Boolean,
                                  isStronglyRegular: Boolean,
                                  isTree: Option[Boolean],
                                  // CVT
                                  isMoebiusLadder: Option[Boolean],
                                  isPrism: Option[Boolean],
                                  isSPX: Option[Boolean]
                                )

  case class GraphNumericColumns(
                                  order: Int,
                                  chromaticIndex: Option[Int],
                                  cliqueNumber: Int,
                                  connectedComponentsNumber: Int,
                                  diameter: Option[Int],
                                  girth: Option[Int],
                                  numberOfLoops: Int,
                                  oddGirth: Option[Int],
                                  size: Int,
                                  trianglesCount: Int
                                )

  case class GraphIndexColumns(
                                cvt: Option[Int],
                                vt: Option[Int],
                                symcubic: Option[Int]
                                )


  case class GraphAllColumns(zooid: Int, index: GraphIndexColumns, bool: GraphBooleanColumns, numeric: GraphNumericColumns)

  implicit val getGraphResult: GetResult[GraphAllColumns] = GetResult(r => {
    GraphAllColumns(r.<<,
      GraphIndexColumns(r.<<, r.<<, r.<<),
      GraphBooleanColumns(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<),
      GraphNumericColumns(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  })

  case class ManiplexNumericColumns(orbits: Int, smallGroupOrder: Int)

  case class ManiplexBoolColumns(isPolytope: Boolean, isRegular: Boolean)

  case class ManiplexStringColumns(symmetryType: String)

  case class ManiplexAllColumns(numeric: ManiplexNumericColumns, bool: ManiplexBoolColumns, string: ManiplexStringColumns)

  implicit val getManiplexResult: GetResult[ManiplexAllColumns] = GetResult(r => {
    ManiplexAllColumns(
      ManiplexNumericColumns(r.<<, r.<<),
      ManiplexBoolColumns(r.<<, r.<<),
      ManiplexStringColumns(r.<<))
  })

}
