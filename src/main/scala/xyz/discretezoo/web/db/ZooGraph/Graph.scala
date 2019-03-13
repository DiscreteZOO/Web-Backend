package xyz.discretezoo.web.db.ZooGraph

import xyz.discretezoo.web.ZooObject

case class Graph(
  zooid: Int,
  order: Int,
  // index
  indexCVT: Option[Int],
  indexSymCubic: Option[Int],
  indexVT: Option[Int],
  // booleans
  isArcTransitive: Option[Boolean],
  isBipartite: Option[Boolean],
  isCayley: Option[Boolean],
  isDistanceRegular: Option[Boolean],
  isDistanceTransitive: Option[Boolean],
  isEdgeTransitive: Option[Boolean],
  isEulerian: Option[Boolean],
  isHamiltonian: Option[Boolean],
  isMoebiusLadder: Option[Boolean],
  isOverfull: Option[Boolean],
  isPartialCube: Option[Boolean],
  isPrism: Option[Boolean],
  isSplit: Option[Boolean],
  isSPX: Option[Boolean],
  isStronglyRegular: Option[Boolean],
  // numeric
  chromaticIndex: Option[Int],
  cliqueNumber: Option[Int],
  connectedComponentsNumber: Option[Int],
  diameter: Option[Int],
  girth: Option[Int],
  oddGirth: Option[Int],
  size: Option[Int],
  trianglesCount: Option[Int]
) extends ZooObject {

  def select: Map[String, _] = Map(
  "zooid" -> zooid,
  "order" -> order,

  "index_CVT" -> indexCVT,
  "index_symcubic" -> indexSymcubic,
  "index_VT" -> indexVT,

  "is_arc_transitive" -> isArcTransitive,
  "is_bipartite" -> isBipartite,
  "is_cayley" -> isCayley,
  "is_distance_regular" -> isDistanceRegular,
  "is_distance_transitive" -> isDistanceTransitive,
  "is_edge_transitive" -> isEdgeTransitive,
  "is_eulerian" -> isEulerian,
  "is_hamiltonian" -> isHamiltonian,
  "is_moebius_ladder" -> isMoebiusLadder,
  "is_overfull" -> isOverfull,
  "is_partial_cube" -> isPartialCube,
  "is_prism" -> isPrism,
  "is_split" -> isSplit,
  "is_spx" -> isSPX,
  "is_strongly_regular" -> isStronglyRegular,

  "chromatic_index" -> chromaticIndex,
  "clique_number" -> cliqueNumber,
  "connected_components_number" -> connectedComponentsNumber,
  "diameter" -> diameter,
  "girth" -> girth,
  "odd_girth" -> oddGirth,
  "size" -> size,
  "triangles_count" -> trianglesCount
  )

}