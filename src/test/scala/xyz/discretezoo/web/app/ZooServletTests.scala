package xyz.discretezoo.web.app

import org.scalatra.test.scalatest._

class ZooServletTests extends ScalatraFunSuite {

  addServlet(classOf[ZooServlet], "/*")

  test("GET / on ZooServlet should return status 200"){
    get("/"){
      status should equal (200)
    }
  }

}
