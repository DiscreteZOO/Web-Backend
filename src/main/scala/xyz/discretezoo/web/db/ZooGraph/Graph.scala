package xyz.discretezoo.web.db.ZooGraph

import xyz.discretezoo.web.db.ZooObject

case class Graph(
  zooid: Int,
  order: Int,
  // booleans
  isArcTransitive: Option[Boolean],
  isBipartite: Option[Boolean],
  isCayley: Option[Boolean],
  isDistanceRegular: Option[Boolean],
  isDistanceTransitive: Option[Boolean],
  isEdgeTransitive: Option[Boolean],
  isEulerian: Option[Boolean],
  isForest: Option[Boolean],
  isHamiltonian: Option[Boolean],
  isOverfull: Option[Boolean],
  isPartialCube: Option[Boolean],
  isSplit: Option[Boolean],
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

