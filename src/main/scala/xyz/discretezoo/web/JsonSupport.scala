package xyz.discretezoo.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import xyz.discretezoo.web.db.ZooDB.{GraphAllColumns, GraphBooleanColumns, GraphIndexColumns, GraphNumericColumns}

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val formatCount: RootJsonFormat[Count] = jsonFormat1(Count)

  implicit val formatSearchFilter: RootJsonFormat[SearchFilter] = jsonFormat2(SearchFilter)
  implicit val formatSearchParameters: RootJsonFormat[SearchParameters] = jsonFormat2(SearchParameters)
  implicit val formatResultsParameters: RootJsonFormat[ResultsParameters] = jsonFormat3(ResultsParameters)

  implicit val formatGraphResults: RootJsonFormat[GraphResult] = jsonFormat2(GraphResult)

  implicit val formatGraphBooleanColumns: RootJsonFormat[GraphBooleanColumns] = jsonFormat18(GraphBooleanColumns)
  implicit val formatGraphNumericColumns: RootJsonFormat[GraphNumericColumns] = jsonFormat10(GraphNumericColumns)
  implicit val formatGraphIndexColumns: RootJsonFormat[GraphIndexColumns] = jsonFormat3(GraphIndexColumns)
  implicit val formatGraphAllColumns: RootJsonFormat[GraphAllColumns] = jsonFormat4(GraphAllColumns)

}
