package xyz.discretezoo.web

import slick.ast.Ordering
import slick.ast.Ordering.Direction
import slick.lifted.{ColumnOrdered, Ordered, Query, Rep}
import xyz.discretezoo.web.ZooPostgresProfile.api._

object DynamicSupport {

  type ColumnOrdering = (String, Direction) //Just a type alias

  trait ColumnSelector {
    val select: Map[String, Rep[_]] //The runtime map between string names and table columns
    val inCollection: Map[String, Rep[Boolean]]
  }

  implicit class MultiFilterSortableQuery[A <: ColumnSelector, B, C[_]](query: Query[A, B, C]) {

    def dynamicQueryCount(qp: SearchParam): Query[A, B, C] = {
      query.dynamicFilterBy(qp.collections, qp.filter)
    }

    def dynamicQueryResults(qp: ResultParam): Query[A, B, C] = {
      query
        .dynamicFilterBy(qp.parameters.collections, qp.parameters.filter)
        .drop(qp.offset)
        .take(qp.limit)
        .dynamicSortBy(qp.sort)
    }

    def dynamicFilterBy(collections: Seq[String], filterBy: Seq[Condition]): Query[A, B, C]  = {
      val filteredCollections = query.filter(o => collections.map(c => o.inCollection(c)).reduceLeft(_ || _))
      filterBy.foldRight(filteredCollections) { //Fold right is reversing order
        case (condition, queryToFilter) =>
          val filterColumnRep: A => Rep[_] = _.select(condition.field)
          queryToFilter.filter(filterColumnRep)(v => (v, condition) match {
            case (value: Rep[Int @unchecked], condition: NumericCondition) => numericFilter(value, condition)
            case (value: Rep[Boolean @unchecked], condition: BooleanCondition) => booleanFilter(value, condition)
            case _ => true
          })

      }
    }

    def dynamicSortBy(sortBy: Seq[ColumnOrdering]): Query[A, B, C]  = {
      sortBy.foldRight(query) { //Fold right is reversing order
        case ((sortColumn, sortOrder), queryToSort) =>
          val sortOrderRep: Rep[_] => Ordered = ColumnOrdered(_, Ordering(sortOrder))
          val sortColumnRep: A => Rep[_] = _.select(sortColumn)
          queryToSort.sortBy(sortColumnRep)(sortOrderRep)
      }
    }

    private def booleanFilter(value: Rep[_ >: Boolean with Option[Boolean]], condition: BooleanCondition): Rep[Boolean] = {
      value match {
        case v: Rep[Boolean @unchecked] => v === condition.b
        case v: Rep[Option[Boolean] @unchecked] => (v === condition.b).getOrElse(false)
      }
    }

    private def numericFilter(value: Rep[_ >: Int with Option[Int]], condition: NumericCondition): Rep[Boolean] = {
      value match {
        case v: Rep[Int @unchecked] => condition.op match {
          case "=" | "==" => v === condition.i
          case "<" => v < condition.i
          case ">" => v > condition.i
          case "<=" => v <= condition.i
          case ">=" => v >= condition.i
          case "<>" | "!=" => v =!= condition.i
        }
        case v: Rep[Option[Int] @unchecked] => {
          val evaluate: Rep[Option[Boolean]] = condition.op match {
            case "=" | "==" => v === condition.i
            case "<" => v < condition.i
            case ">" => v > condition.i
            case "<=" => v <= condition.i
            case ">=" => v >= condition.i
            case "<>" | "!=" => v =!= condition.i
          }
          evaluate.getOrElse(false)
        }
      }
    }

  }
}
