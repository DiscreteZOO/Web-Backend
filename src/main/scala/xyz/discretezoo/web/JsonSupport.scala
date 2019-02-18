package xyz.discretezoo.web

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import xyz.discretezoo.web.ZooJsonAPI._

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol with NullOptions {

  implicit val formatCount: RootJsonFormat[Count] = jsonFormat1(Count)
  implicit val formatParameter: RootJsonFormat[Parameter] = jsonFormat2(Parameter)
  implicit val formatSearchParameters: RootJsonFormat[SearchParameters] = jsonFormat3(SearchParameters)
  implicit val formatResultsParameters: RootJsonFormat[ResultsParameters] = jsonFormat4(ResultsParameters)
  implicit def formatSearchResults[T: JsonFormat]: RootJsonFormat[SearchResult[T]] =
    rootFormat(lazyFormat(jsonFormat2(SearchResult.apply[T])))

  def writeZooObject(g: ZooObject): JsValue = {
    val columnList = g.select.collect({
      case (s, u: UUID) => Some(s -> u.toString.toJson)
      case (s, v: Boolean) => Some(s -> v.toJson)
      case (s, v: Int) => Some(s -> v.toJson)
      case (s, Some(v: Boolean)) => Some(s -> v.toJson)
      case (s, Some(v: Int)) => Some(s -> v.toJson)
      case (s, None) => Some(s -> JsNull)
    }).toList
    JsObject(columnList.flatten: _*)
  }

//  https://stackoverflow.com/questions/53713834/parsing-more-than-22-fields-with-spray-json-without-nesting-case-classes

}