package xyz.discretezoo.web.db.ZooManiplex

import xyz.discretezoo.web.db.ZooObject

case class Maniplex(
 zooid: Int,
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
