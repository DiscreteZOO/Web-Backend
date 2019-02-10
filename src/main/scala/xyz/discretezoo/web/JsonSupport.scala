package xyz.discretezoo.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import xyz.discretezoo.web.db.model.Graph._
import xyz.discretezoo.web.db.model.{GraphRecord, ManiplexRecord}
import xyz.discretezoo.web.db.model.Maniplex._

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val formatCount: RootJsonFormat[Count] = jsonFormat1(Count)

  implicit val formatParameter: RootJsonFormat[Parameter] = jsonFormat2(Parameter)
  implicit val formatSearchParameters: RootJsonFormat[SearchParameters] = jsonFormat3(SearchParameters)
  implicit val formatResultsParameters: RootJsonFormat[ResultsParameters] = jsonFormat4(ResultsParameters)

  implicit def formatSearchResults[T:JsonFormat]: RootJsonFormat[SearchResult[T]] =
    rootFormat(lazyFormat(jsonFormat2(SearchResult.apply[T])))

  implicit val formatGraphBooleanColumns: RootJsonFormat[GraphBoolean] = jsonFormat18(GraphBoolean)
  implicit val formatGraphNumericColumns: RootJsonFormat[GraphNumeric] = jsonFormat10(GraphNumeric)
  implicit val formatGraphIndexColumns: RootJsonFormat[GraphIndex] = jsonFormat3(GraphIndex)
  implicit val formatGraphAllColumns: RootJsonFormat[GraphRecord] = jsonFormat4(GraphRecord)

  implicit val formatManiplexBooleanColumns: RootJsonFormat[ManiplexBoolean] = jsonFormat2(ManiplexBoolean)
  implicit val formatManiplexNumericColumns: RootJsonFormat[ManiplexNumeric] = jsonFormat4(ManiplexNumeric)
  implicit val formatManiplexStringColumns: RootJsonFormat[ManiplexString] = jsonFormat1(ManiplexString)
  implicit val formatManiplexAllColumns: RootJsonFormat[ManiplexRecord] = jsonFormat3(ManiplexRecord)

}
