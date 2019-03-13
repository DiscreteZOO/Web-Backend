package xyz.discretezoo.web.db.ZooManiplex

import java.util.UUID

import slick.collection.heterogeneous.HNil
import slick.lifted.{ProvenShape, Rep}
import xyz.discretezoo.web.DynamicSupport.ColumnSelector
import xyz.discretezoo.web.ZooPostgresProfile.api._

final class ManiplexTable(tag: Tag) extends Table[Maniplex](tag, "ZOO_MANIPLEX") with ColumnSelector {

  def uuid: Rep[UUID] = column[UUID]("UUID", O.PrimaryKey)

  def isPolytope: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_POLYTOPE")
  def isRegular: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_REGULAR")

  def orbits: Rep[Option[Int]] = column[Option[Int]]("ORBITS")
  def rank: Rep[Option[Int]] = column[Option[Int]]("RANK")
  def smallGroupId: Rep[Option[Int]] = column[Option[Int]]("SMALL_GROUP_ID")
  def smallGroupOrder: Rep[Option[Int]] = column[Option[Int]]("SMALL_GROUP_ORDER")

  def * : ProvenShape[Maniplex] = (
    uuid ::
      // booleans
      isPolytope :: isRegular ::
      // numeric
      orbits :: rank :: smallGroupId :: smallGroupOrder ::
      HNil
    ).mapTo[Maniplex]

  val select: Map[String, Rep[_]] = Map(
    "UUID" -> this.uuid,

    "is_polytope" -> this.isPolytope,
    "is_regular" -> this.isRegular,

    "orbits" -> this.orbits,
    "rank" -> this.rank,
    "small_group_id" -> this.smallGroupId,
    "small_group_order" -> this.smallGroupOrder
  )

  val inCollection: Map[String, Rep[Boolean]] = Map(
    "M2" -> true
  )

}
