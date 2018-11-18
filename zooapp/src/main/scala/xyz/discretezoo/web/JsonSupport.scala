package xyz.discretezoo.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import xyz.discretezoo.web.db.ZooDB.{GraphAllColumns, GraphBooleanColumns, GraphIndexColumns, GraphNumericColumns}

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat: RootJsonFormat[Item] = jsonFormat2(Item)
  implicit val orderFormat: RootJsonFormat[Order] = jsonFormat1(Order) // contains List[Item]
  implicit val countFormat: RootJsonFormat[Count] = jsonFormat1(Count)

  implicit val searchFilterFormat: RootJsonFormat[SearchFilter] = jsonFormat2(SearchFilter)
  implicit val searchParametersFormat: RootJsonFormat[SearchParameters] = jsonFormat2(SearchParameters)

  implicit val formatGraphBooleanColumns: RootJsonFormat[GraphBooleanColumns] = jsonFormat18(GraphBooleanColumns)
  implicit val formatGraphNumericColumns: RootJsonFormat[GraphNumericColumns] = jsonFormat10(GraphNumericColumns)
  implicit val formatGraphIndexColumns: RootJsonFormat[GraphIndexColumns] = jsonFormat3(GraphIndexColumns)
  implicit val formatGraphAllColumns: RootJsonFormat[GraphAllColumns] = jsonFormat4(GraphAllColumns)
}
