package xyz.discretezoo.web.db

import slick.ast.Ordering
import slick.ast.Ordering.Direction
import xyz.discretezoo.web.{Parameter, SearchParameters}

case class ResultParam(page: Int, limit: Int, parameters: SearchParam, sort: Seq[(String, Direction)]) {

  val offset: Int = (page - 1) * limit

}

object ResultParam {
  def get(page: Int, limit: Int, parameters: SearchParam, sort: List[Parameter]) =
    ResultParam(
      page,
      limit,
      parameters,
      sort.map(s => s.name match {
        case "ASC" => Some((s.value, Ordering.Asc))
        case "DESC" => Some((s.value, Ordering.Desc))
        case _ => None
      }).collect({ case Some(v) => v })
    )
}