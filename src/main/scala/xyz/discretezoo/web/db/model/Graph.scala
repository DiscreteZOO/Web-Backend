package xyz.discretezoo.web.db.model

import slick.jdbc.GetResult
import xyz.discretezoo.web.db.ZooPostgresProfile.api._
import slick.sql.SqlStreamingAction
import xyz.discretezoo.web.Parameter
import xyz.discretezoo.web.db.{Property, ZooPostgresProfile}
import xyz.discretezoo.web.db.ZooDB.getWhere

object Graph extends ZooObject {

  override def isValidFilterColumnName(s: String): Boolean = isValidIntColumnName(s) || isValidBoolColumnName(s)

  // CVT: "truncation"

  override protected def transformParameter(p: Parameter): Parameter = p

  override protected val indexColumnNames: Seq[String] = Seq(
    "cvt_index", "symcubic_index", // CVT
    "vt_index" // VT
  )

  override protected val boolColumnNames: Seq[String] = Seq(
    "has_multiple_edges", "is_arc_transitive", "is_bipartite", "is_cayley", "is_distance_regular",
    "is_distance_transitive", "is_edge_transitive", "is_eulerian", "is_forest", "is_hamiltonian",
    "is_overfull", "is_partial_cube",
//    "is_regular",
    "is_split", "is_strongly_regular", "is_tree",
//    "is_vertex_transitive",
    // CVT
    "is_moebius_ladder", "is_prism", "is_spx"
  )

  override protected val intColumnNames: Seq[String] = Seq(
    "order",
//    "average_degree",
    "chromatic_index", "clique_number", "connected_components_number", "diameter", "girth",
    "number_of_loops", "odd_girth", "size", "triangles_count"
  )

  override protected val stringColumnNames: Seq[String] = Seq()

  case class GraphBoolean(
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

  case class GraphNumeric(
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

  case class GraphIndex(
                                cvt: Option[Int],
                                vt: Option[Int],
                                symcubic: Option[Int]
                              )


}
