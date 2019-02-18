package xyz.discretezoo.web.db.ZooManiplex

import java.util.UUID

case class Maniplex(
 UUID: UUID,
 // boolean
 isPolytope: Option[Boolean],
 isRegular: Option[Boolean],
 // numeric
 orbits: Option[Int],
 rank: Option[Int],
 smallGroupId: Option[Int],
 smallGroupOrder: Option[Int]
 // string
// symmetryType: Option[String]
)

