package xyz.discretezoo.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import xyz.discretezoo.web.db.ZooDB.{GraphAllColumns, GraphBooleanColumns, GraphIndexColumns, GraphNumericColumns}

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat = jsonFormat2(Item)
  implicit val orderFormat = jsonFormat1(Order) // contains List[Item]

  implicit val formatGraphBooleanColumns = jsonFormat18(GraphBooleanColumns)
  implicit val formatGraphNumericColumns = jsonFormat10(GraphNumericColumns)
  implicit val formatGraphIndexColumns = jsonFormat3(GraphIndexColumns)
  implicit val formatGraphAllColumns = jsonFormat4(GraphAllColumns)
}
