package xyz.discretezoo.web

import xyz.discretezoo.web.ZooJsonAPI.Parameter

case class SearchParam(collections: Seq[String], filter: Seq[Condition])

object SearchParam {
  def get(collections: List[String], filter: List[Parameter]) = SearchParam(
    collections,
    filter.map(p => {
      if (p.value.filter(_ > ' ') == "true") Some(BooleanCondition(p.name, b = true))
      else if (p.value.filter(_ > ' ') == "false") Some(BooleanCondition(p.name, b = false))
      else {
        val s = p.value.filter(_ > ' ')
        val operator = Seq("=", "<=", ">=", "<", ">", "<>", "!=").map(op => (s.startsWith(op), op)).filter(_._1)
          .map(_._2).headOption
        operator.flatMap(op => {
          val maybeNumber = s.substring(op.length)
          if (maybeNumber.forall(_.isDigit)) Some(NumericCondition(p.name, op, maybeNumber.toInt))
          else None
        })
      }
    }).collect({ case Some(c) => c })
  )
}
