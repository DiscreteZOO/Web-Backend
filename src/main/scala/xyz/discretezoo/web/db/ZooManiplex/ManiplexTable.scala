package xyz.discretezoo.web.db.ZooManiplex

import slick.collection.heterogeneous.HNil
import slick.lifted.ProvenShape
import xyz.discretezoo.web.db.TableMetaData
import xyz.discretezoo.web.db.ZooPostgresProfile.api._

final class ManiplexTable(tag: Tag) extends Table[Maniplex](tag, "ZOO_MANIPLEX") {

  def zooid: Rep[Int] = column[Int]("ZOOID", O.PrimaryKey)

  def isPolytope: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_POLYTOPE")
  def isRegular: Rep[Option[Boolean]] = column[Option[Boolean]]("IS_REGULAR")

  def orbits: Rep[Option[Int]] = column[Option[Int]]("ORBITS")
  def rank: Rep[Option[Int]] = column[Option[Int]]("RANK")
  def smallGroupId: Rep[Option[Int]] = column[Option[Int]]("SMALL_GROUP_ID")
  def smallGroupOrder: Rep[Option[Int]] = column[Option[Int]]("SMALL_GROUP_ID")

  def * : ProvenShape[Maniplex] = (
    zooid ::
      // booleans
      isPolytope :: isRegular ::
      // numeric
      orbits :: rank :: smallGroupId :: smallGroupOrder ::
      HNil
    ).mapTo[Maniplex]

}

object ManiplexTable extends TableMetaData {
  override val booleanNotNullable: Seq[String] = Seq()
  override val numericNotNullable: Seq[String] = Seq("zooid")
}
