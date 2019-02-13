package xyz.discretezoo.web.db

import slick.lifted.{ColumnOrdered, Rep}
import xyz.discretezoo.web.db.ZooPostgresProfile.api._

trait ColumnActions[T <: Table[_ <: ZooObject]] {

  protected def isInCollection(o: T, column: String): Rep[Boolean]
  protected def getBooleanColumn(o: T, column: String): Option[Rep[_ >: Boolean with Option[Boolean]]]
  protected def getIntColumn(o: T, column: String): Option[Rep[_ >: Int with Option[Int]]]

  // defined methods

//  def sortBy(o: T, orderBy: OrderBy) = {
//
//  }

  def filterCollections(o: T, collections: Seq[String]): Rep[Boolean] = {
    collections.map(c => isInCollection(o, c)).reduceLeft(_ || _)
  }

  def filter(o: T, filters: Seq[Condition]): Rep[Boolean] = {
    filters.map({
      case b: BooleanCondition => booleanFilter(o, b)
      case n: NumericCondition => numericFilter(o, n)
    }).collect({ case Some(v) => v }).reduceLeft(_ && _)
  }

//  def sort(o: T, sortSeq: Seq[OrderBy]) = {
//    val relevant = sortSeq.flatMap(s => booleanSort(o, s) ++ numericSort(o, s)).take(5)
//    val length = relevant.length
//    length match {
//      case 1 => relevant.head
//      case 2
//    }
//  }

  // defined private helper methods

  private def booleanFilter(o: T, condition: BooleanCondition): Option[Rep[Boolean]] = {
    getBooleanColumn(o, condition.field).map({
      case v: Rep[Boolean] => v === condition.b
      case v: Rep[Option[Boolean]] => (v === condition.b).getOrElse(false)
    })
  }

  private def numericFilter(o: T, condition: NumericCondition): Option[Rep[Boolean]] = {
    getIntColumn(o, condition.field).map({
      case v: Rep[Int] => condition.op match {
        case "=" | "==" => v === condition.i
        case "<" => v < condition.i
        case ">" => v > condition.i
        case "<=" => v <= condition.i
        case ">=" => v >= condition.i
        case "<>" | "!=" => v =!= condition.i
      }
      case v: Rep[Option[Int]] => {
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
    })
  }

//  def booleanSort(o: T, orderBy: OrderBy): Option[ColumnOrdered[_ >: Boolean with Option[Boolean]]] = {
//    getBooleanColumn(o, orderBy.field).flatMap({
//      case f: Rep[Boolean] => orderBy.ord match {
//        case "ASC" => Some(f.asc)
//        case "DESC" => Some(f.desc)
//        case _ => None
//      }
//      case f: Rep[Option[Boolean]] => orderBy.ord match {
//        case "ASC" => Some(f.asc)
//        case "DESC" => Some(f.desc)
//        case _ => None
//      }
//    })
//  }
//
//  def numericSort(o: T, orderBy: OrderBy): Option[ColumnOrdered[_ >: Int with Option[Int]]] = {
//    getIntColumn(o, orderBy.field).flatMap({
//      case f: Rep[Int] => orderBy.ord match {
//        case "ASC" => Some(f.asc)
//        case "DESC" => Some(f.desc)
//        case _ => None
//      }
//      case f: Rep[Option[Int]] => orderBy.ord match {
//        case "ASC" => Some(f.asc)
//        case "DESC" => Some(f.desc)
//        case _ => None
//      }
//    })
//  }

}
