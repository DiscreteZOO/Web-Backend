package xyz.discretezoo.web.db

import java.util.UUID

import com.sun.net.httpserver.Authenticator.Success
import slick.jdbc.{GetResult, SQLActionBuilder}
import xyz.discretezoo.web.db.ZooPostgresProfile.api._
import xyz.discretezoo.web.db.v1._

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import org.json4s._
import org.json4s.JsonDSL._

import scala.util.parsing.json.JSONObject
//import xyz.discretezoo.web.db.v1.Graphs
//import xyz.discretezoo.web.db.M2orbit.M2orbitManiplex
//import xyz.discretezoo.web.db.ManiplexData

import scala.concurrent.duration.{Duration, DurationLong}
import scala.concurrent.{Await, Future}
import scala.concurrent._
import ExecutionContext.Implicits.global

object ZooDB {

  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  private val graphs: TableQuery[Graphs] = TableQuery[Graphs]
  private val graphsVT: TableQuery[GraphsVT] = TableQuery[GraphsVT]
  private val graphsCVT: TableQuery[GraphsCVT] = TableQuery[GraphsCVT]
  private val graphsSPX: TableQuery[GraphsSPX] = TableQuery[GraphsSPX]

  private def connect: ZooPostgresProfile.backend.DatabaseDef = Database.forURL(
    "jdbc:postgresql://localhost:5432/discretezoo2",
    "discretezoo",
    "D!screteZ00",
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

  def getGraphs(collections: Seq[String], filters: Seq[String], limit: Int, order: Seq[OrderBy], page: Int): Seq[GraphAllColumns] = {
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
    val f: Future[Seq[GraphAllColumns]] = db.run(q)
    val result = Await.result(f, Duration("Inf"))
    db.close()
    result
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

  case class OrderBy(column: String, order: String) {
    def toSQL: Option[String] = {
      if (GraphColumns.isValidFilterColumnName(column) && Seq("ASC", "DESC").contains(order))
        Some(s""""$column" $order""")
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
                                  isSPX: Option[Boolean],
                                )
  {
    def toJSON: JObject = {
      val json =
        ("has_multiple_edges" -> hasMultipleEdges) ~
          ("is_arc_transitive" -> isArcTransitive) ~
          ("is_bipartite" -> isBipartite) ~
          ("is_cayley" -> isCayley) ~
          ("is_distance_regular" -> isDistanceRegular) ~
          ("is_distance_transitive" -> isDistanceTransitive) ~
          ("is_edge_transitive" -> isEdgeTransitive) ~
          ("is_eulerian" -> isEulerian) ~
          ("is_forest" -> isForest) ~
          ("is_hamiltonian" -> isHamiltonian) ~
          ("is_overfull" -> isOverfull) ~
          ("is_partial_cube" -> isPartialCube) ~
          ("is_split" -> isSplit) ~
          ("is_strongly_regular" -> isStronglyRegular) ~
          ("is_tree" -> isTree) ~
      // CVT
          ("is_moebius_ladder" -> isMoebiusLadder) ~
          ("is_prism" -> isPrism) ~
          ("is_spx" -> isSPX)
      json
    }
  }

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
  {
    def toJSON: JObject = {
      val json =
        ("order" -> order) ~
          ("chromatic_index" -> chromaticIndex) ~
          ("clique_number" -> cliqueNumber) ~
          ("connected_components_number" -> connectedComponentsNumber) ~
          ("diameter" -> diameter) ~
          ("girth" -> girth) ~
          ("number_of_loops" -> numberOfLoops) ~
          ("odd_girth" -> oddGirth) ~
          ("size" -> size) ~
          ("triangles_count" -> trianglesCount)
      json
    }
  }

  case class GraphIndexColumns(
                                cvt: Option[Int],
                                vt: Option[Int],
                                symcubic: Option[Int]
                                )
  {
    def toJSON: JObject = {
      val json =
        ("cvt_index" -> cvt) ~
          ("symcubic_index" -> symcubic) ~
          ("vt_index" -> vt)
      json
    }
  }

  case class GraphAllColumns(zooid: Int, index: GraphIndexColumns, bool: GraphBooleanColumns, numeric: GraphNumericColumns) {
    def toJSON: JObject = {
      val json = ("zooid" -> zooid) ~ ("index" -> index.toJSON) ~ ("bool" -> bool.toJSON) ~ ("numeric" -> numeric.toJSON)
      json
    }
  }

  implicit val getGraphResult: GetResult[GraphAllColumns] = GetResult(r => {
    GraphAllColumns(r.<<,
      GraphIndexColumns(r.<<, r.<<, r.<<),
      GraphBooleanColumns(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<),
      GraphNumericColumns(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  })

  case class ManiplexNumericColumns(orbits: Int, smallGroupOrder: Int)
  {
    def toJSON: JObject = {
      val json = ("ORBITS" -> orbits) ~ ("SMALL_GROUP_ORDER" -> smallGroupOrder)
      json
    }
  }

  case class ManiplexBoolColumns(isPolytope: Boolean, isRegular: Boolean)
  {
    def toJSON: JObject = {
      val json = ("IS_POLYTOPE" -> isPolytope) ~ ("IS_REGULAR" -> isRegular)
      json
    }
  }

  case class ManiplexStringColumns(symmetryType: String)
  {
    def toJSON: JObject = {
      val json = "SYMMETRY_TYPE" -> symmetryType
      json
    }
  }

  case class ManiplexAllColumns(numeric: ManiplexNumericColumns, bool: ManiplexBoolColumns, string: ManiplexStringColumns) {
    def toJSON: JObject = {
      val json = ("bool" -> bool.toJSON) ~ ("numeric" -> numeric.toJSON) ~ ("string" -> string.toJSON)
      json
    }
  }

  implicit val getManiplexResult: GetResult[ManiplexAllColumns] = GetResult(r => {
    ManiplexAllColumns(
      ManiplexNumericColumns(r.<<, r.<<),
      ManiplexBoolColumns(r.<<, r.<<),
      ManiplexStringColumns(r.<<))
  })

}
