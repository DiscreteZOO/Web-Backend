package xyz.discretezoo.web

object ZooJsonAPI {
  case class Count(value: Int)
  case class ResultsParameters(page: Int, pageSize: Int, parameters: SearchParameters, orderBy: List[Parameter])
  case class SearchParameters(objects: String, collections: List[String], filters: List[Parameter])
  case class Parameter(name: String, value: String)
  case class SearchResult(pages: Int, data: List[ZooObject])
}
