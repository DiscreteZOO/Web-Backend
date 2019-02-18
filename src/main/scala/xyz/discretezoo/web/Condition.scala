package xyz.discretezoo.web

sealed trait Condition {
  val field: String
}

case class BooleanCondition(override val field: String, b: Boolean) extends Condition
case class NumericCondition(override val field: String, op: String, i: Int) extends Condition {
  require(Seq("=", "==", "<", ">", "<=", ">=", "<>", "!=").contains(op), "Invalid operator for numeric condition: "+ op)
}
