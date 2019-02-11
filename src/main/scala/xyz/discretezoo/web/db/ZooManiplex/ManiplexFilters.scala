package xyz.discretezoo.web.db.ZooManiplex

import slick.lifted.Rep
import xyz.discretezoo.web.db.{BooleanCondition, Filter, NumericCondition}

object ManiplexFilters extends Filter[ManiplexTable]  {

  private val notNullable: Seq[String] = Seq("zooid")

  override def boolean(m: ManiplexTable, condition: BooleanCondition): Rep[Boolean] = {
     val value: Rep[Option[Boolean]] = m match {
       case maniplex: ManiplexTableMain => condition.field match {
         case "is_polytope" => maniplex.isPolytope
         case "is_regular" => maniplex.isRegular
       }
     }
    booleanNullableFilter(value, condition.b)
  }

  override def numeric(m: ManiplexTable, condition: NumericCondition): Rep[Boolean] = {
    if (notNullable.contains(condition.field)) {
      val value: Rep[Int] = m match {
        case maniplex: ManiplexTableMain => condition.field match {
          case "zooid" => maniplex.zooid
        }
      }
      numericNotNullableFilter(value, condition.op, condition.i)
    }
    else {
      val value: Rep[Option[Int]] = m match {
        case maniplex: ManiplexTableMain => condition.field match {
          case "orbits" => maniplex.orbits
          case "rank" => maniplex.rank
          case "small_group_id" => maniplex.smallGroupId
          case "small_group_order" => maniplex.smallGroupOrder
        }
      }
      numericNullableFilter(value, condition.op, condition.i)
    }
  }

}
