package xyz.discretezoo.web.db.ZooManiplex

import java.util.UUID

import xyz.discretezoo.web.ZooObject

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
) extends ZooObject {

  def select: Map[String, _] = Map(
    "UUID" -> UUID,
    "is_polytope" -> isPolytope,
    "is_regular" -> isRegular,
    "orbits" -> orbits,
    "rank" -> rank,
    "small_group_id" -> smallGroupId,
    "small_group_order" -> smallGroupOrder
  )

}