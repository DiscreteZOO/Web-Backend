package xyz.discretezoo.web.app

import org.scalatra._

class ZooServlet extends ScalatraServlet {

  get("/") {
    views.html.hello()
  }

}
