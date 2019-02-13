package xyz.discretezoo.web.db.ZooManiplex

import java.util.UUID

import xyz.discretezoo.web.db.ZooObject

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
extends ZooObject
