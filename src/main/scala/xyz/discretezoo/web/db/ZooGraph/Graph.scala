package xyz.discretezoo.web.db.ZooGraph

import xyz.discretezoo.web.db.ZooObject

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
  trianglesCount: Option[Int])
extends ZooObject