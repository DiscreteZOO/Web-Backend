package xyz.discretezoo.web.app

import org.json4s.JsonAST.{JObject, JValue}
import org.json4s.{DefaultFormats, Formats, JsonDSL}
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.scalatra._
import org.scalatra.scalate.ScalateSupport
import xyz.discretezoo.web.db.ZooDB.OrderBy
import xyz.discretezoo.web.db.{GraphColumns, ZooDB}

class ZooServlet extends ScalatraServlet with ScalateSupport {

  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  private def maybeFilters: Seq[String] =
    params.filter(GraphColumns.isValidQueryFilter).map(GraphColumns.queryCondition).toSeq

  get("/") {
    contentType="text/html"
    ssp("/index")
  }

  get("/search/object-count") {
    contentType="json"
    val objects = params.get("objects")
    val collections = params.get("collections").map(parse(_).extract[Seq[String]]).getOrElse(Seq())
    objects match {
      case Some("graphs") => Ok(ZooDB.countGraphs(collections, maybeFilters))
      case Some("maniplexes") => Ok(ZooDB.countManiplexes(collections, maybeFilters))
    }
  }

  get("/results") {
    contentType="json"
    val limit = 20
    val filters = maybeFilters
    val objects = params.get("objects")
    val page = params.get("display_page").map(_.toInt).getOrElse(1)
    val order = params.get("order_by").map(parse(_).extract[Seq[OrderBy]]).getOrElse(Seq())
    val collections = params.get("collections").map(parse(_).extract[Seq[String]]).getOrElse(Seq())
    val pages = objects match {
      case Some("graphs") => (ZooDB.countGraphs(collections, filters) / limit).ceil.toInt
      case Some("maniplexes") => (ZooDB.countManiplexes(collections, filters) / limit).ceil.toInt
    }
    val actualPage = if (page >= 1 && page <= pages) page else 1
    val results: JArray = objects match {
      case Some("graphs") => ZooDB.getGraphs(collections, filters, limit, order, actualPage).map(_.toJSON)
      case Some("maniplexes") => ZooDB.getManiplexes(collections, filters, limit, order, actualPage).map(_.toJSON)
    }
    val json: JObject = ("pages" -> pages) ~ ("data" -> results)
    compact(render(json))

  }

}
