package xyz.discretezoo.web.db.ZooManiplex

import slick.collection.heterogeneous.HNil
import slick.lifted.{ProvenShape, Rep}
import xyz.discretezoo.web.db.ZooTable
import xyz.discretezoo.web.db.ZooPostgresProfile.api._

sealed trait ManiplexTable extends ZooTable {
  def zooid: Rep[Int]
}

final class ManiplexTableMain(tag: Tag) extends Table[ManiplexMain](tag, "ZOO_MANIPLEX") with ManiplexTable {

  override def zooid: Rep[Int] = column[Int]("ZOOID", O.PrimaryKey)

  def isPolytope: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_POLYTOPE")
  def isRegular: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_REGULAR")

  def orbits: Rep[Option[Int]] = column[Option[Int]]("ORBITS")
  def rank: Rep[Option[Int]] = column[Option[Int]]("RANK")
  def smallGroupId: Rep[Option[Int]] = column[Option[Int]]("SMALL_GROUP_ID")
  def smallGroupOrder: Rep[Option[Int]] = column[Option[Int]]("SMALL_GROUP_ID")

  def * : ProvenShape[ManiplexMain] = (
    zooid ::
      // booleans
      isPolytope :: isRegular ::
      // numeric
      orbits :: rank :: smallGroupId :: smallGroupOrder ::
      HNil
    ).mapTo[ManiplexMain]

}
