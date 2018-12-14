package xyz.discretezoo.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import xyz.discretezoo.web.db.ZooDB._

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val formatCount: RootJsonFormat[Count] = jsonFormat1(Count)

  implicit val formatParameter: RootJsonFormat[Parameter] = jsonFormat2(Parameter)
  implicit val formatSearchParameters: RootJsonFormat[SearchParameters] = jsonFormat2(SearchParameters)
  implicit val formatResultsParameters: RootJsonFormat[ResultsParameters] = jsonFormat4(ResultsParameters)
//  implicit val formatGraphResults: RootJsonFormat[GraphResult] = jsonFormat2(GraphResult)

  implicit val formatGraphBooleanColumns: RootJsonFormat[GraphBooleanColumns] = jsonFormat18(GraphBooleanColumns)
  implicit val formatGraphNumericColumns: RootJsonFormat[GraphNumericColumns] = jsonFormat10(GraphNumericColumns)
  implicit val formatGraphIndexColumns: RootJsonFormat[GraphIndexColumns] = jsonFormat3(GraphIndexColumns)
  implicit val formatGraphAllColumns: RootJsonFormat[GraphAllColumns] = jsonFormat4(GraphAllColumns)

  implicit val formatManiplexBooleanColumns: RootJsonFormat[ManiplexBooleanColumns] = jsonFormat2(ManiplexBooleanColumns)
  implicit val formatManiplexNumericColumns: RootJsonFormat[ManiplexNumericColumns] = jsonFormat4(ManiplexNumericColumns)
  implicit val formatManiplexStringColumns: RootJsonFormat[ManiplexStringColumns] = jsonFormat1(ManiplexStringColumns)
  implicit val formatManiplexAllColumns: RootJsonFormat[ManiplexAllColumns] = jsonFormat3(ManiplexAllColumns)

}
