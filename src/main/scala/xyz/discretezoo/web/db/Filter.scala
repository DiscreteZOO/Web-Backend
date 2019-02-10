package xyz.discretezoo.web.db

import slick.lifted.Rep
import xyz.discretezoo.web.db.ZooPostgresProfile.api._

trait Filter[T <: Table[_ <: ZooObject]] {

  def boolean(o: T, condition: BooleanCondition): Rep[Boolean]
  def numeric(o: T, condition: NumericCondition): Rep[Boolean]

  private def evaluate(o: T, condition: Condition): Rep[Boolean] = condition match {
    case b: BooleanCondition => boolean(o, b)
    case n: NumericCondition => numeric(o, n)
  }

  def filter(o: T, filters: Seq[Condition]): Rep[Boolean] = filters.map(c => evaluate(o, c)).reduceLeft(_ && _)

  protected def booleanNotNullableFilter(value: Rep[Boolean], b: Boolean): Rep[Boolean] = {
    value === b
  }

  protected def booleanNullableFilter(value: Rep[Option[Boolean]], b: Boolean): Rep[Boolean] = {
    (value === b).getOrElse(false)
}

  protected def numericNotNullableFilter(value: Rep[Int], op: String, i: Int): Rep[Boolean] = op match {
    case "=" | "==" => value === i
    case "<" => value < i
    case ">" => value > i
    case "<=" => value <= i
    case ">=" => value >= i
    case "<>" | "!=" => value =!= i
  }

  protected def numericNullableFilter(value: Rep[Option[Int]], op: String, i: Int): Rep[Boolean] = {
    val evaluate: Rep[Option[Boolean]] = op match {
      case "=" | "==" => value === i
      case "<" => value < i
      case ">" => value > i
      case "<=" => value <= i
      case ">=" => value >= i
      case "<>" | "!=" => value =!= i
    }
    evaluate.getOrElse(false)
  }

}
