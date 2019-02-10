package xyz.discretezoo.web.db.ZooManiplex

import slick.lifted.Rep
import xyz.discretezoo.web.db.{BooleanCondition, Filter, NumericCondition}

object ManiplexFilters extends Filter[ManiplexTable]  {

  override def boolean(g: ManiplexTable, condition: BooleanCondition): Rep[Boolean] = {
    val value: Rep[Option[Boolean]] = condition.field match {
      case "is_polytope" => g.isPolytope
      case "is_regular" => g.isRegular
    }
    booleanNullableFilter(value, condition.b)
  }

  override def numeric(g: ManiplexTable, condition: NumericCondition): Rep[Boolean] = {
    if (ManiplexTable.numericNotNullable.contains(condition.field)) {
      val value: Rep[Int] = condition.field match {
        case "zooid" => g.zooid
      }
      numericNotNullableFilter(value, condition.op, condition.i)
    }
    else {
      val value: Rep[Option[Int]] = condition.field match {
        case "orbits" => g.orbits
        case "rank" => g.rank
        case "small_group_id" => g.smallGroupId
        case "small_group_order" => g.smallGroupOrder
      }
      numericNullableFilter(value, condition.op, condition.i)
    }
  }

}
