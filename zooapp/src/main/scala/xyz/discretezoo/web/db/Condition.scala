package xyz.discretezoo.web.db

// graph special cases: zooid, name, data

abstract class Condition {
  val column: String
}
abstract class NumericCondition extends Condition {

  private def isValidOperator(operator: String): Boolean = {
    Seq("<", "<=", ">", ">=", "=", "!=").contains(operator)
  }

  private def isValidValue(value: String): Boolean = {
    val v = value.split('.')
    (v.length == 1 || v.length == 2) && v.forall(_.forall(_.isDigit))
  }

  def toSQL(operator: String, value: String): String = {
    if (isValidOperator(operator) && isValidValue(value)) s""""$column" $operator $value"""
    else ""
  }
}

case class BoolCondition(override val column: String) extends Condition {
  def toSQL(value: Boolean): String = s"""${if (!value) "not " else ""}"$column""""
}

case class FloatCondition(override val column: String) extends NumericCondition

case class IntCondition(override val column: String) extends NumericCondition