package xyz.discretezoo.web

import slick.ast.Ordering
import slick.ast.Ordering.Direction
import xyz.discretezoo.web.ZooJsonAPI.Parameter

case class ResultParam(page: Int, limit: Int, parameters: SearchParam, sort: Seq[(String, Direction)]) {
  val offset: Int = (page - 1) * limit
}

object ResultParam {
  def get(page: Int, limit: Int, parameters: SearchParam, sort: List[Parameter]): ResultParam = {
    val actualSort = sort.map(s => s.value match {
      case "ASC" => Some((s.name, Ordering.Asc))
      case "DESC" => Some((s.name, Ordering.Desc))
      case _ => None
    }).collect({ case Some(v) => v })
    ResultParam(page, limit, parameters, actualSort)
  }
}