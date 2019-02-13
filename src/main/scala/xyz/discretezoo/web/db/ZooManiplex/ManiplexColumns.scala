package xyz.discretezoo.web.db.ZooManiplex

import slick.lifted.Rep
import xyz.discretezoo.web.db.ColumnActions
import xyz.discretezoo.web.db.ZooPostgresProfile.api._

object ManiplexColumns extends ColumnActions[ManiplexTable]  {

  override protected def isInCollection(m: ManiplexTable, collection: String): Rep[Boolean] = {
    val value: Option[Rep[Boolean]] = collection match {
      case "M2" => Some(true)
      case _ => None
    }
    value.getOrElse(false)
  }

  override protected def getBooleanColumn(m: ManiplexTable, column: String): Option[Rep[_ >: Boolean with Option[Boolean]]] = column match {
    case "is_polytope" => Some(m.isPolytope)
    case "is_regular" => Some(m.isRegular)
    case _ => None
  }

  override protected def getIntColumn(m: ManiplexTable, column: String): Option[Rep[_ >: Int with Option[Int]]] = column match {
    case "orbits" => Some(m.orbits)
    case "rank" => Some(m.rank)
    case "small_group_id" => Some(m.smallGroupId)
    case "small_group_order" => Some(m.smallGroupOrder)
    case _ => None
  }
}
