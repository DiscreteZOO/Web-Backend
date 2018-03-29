package xyz.discretezoo.web.app

import org.scalatra._
import org.scalatra.scalate.ScalateSupport

class ZooServlet extends ScalatraServlet with ScalateSupport {

  get("/") {
    contentType="text/html"
    jade("/index")
  }

}
