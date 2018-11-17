package xyz.discretezoo.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.model.headers.RawHeader

import scala.io.StdIn

object WebServer extends Directives with JsonSupport {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route = {

      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      } ~
        get {
          pathSingleSlash {
            respondWithHeaders(RawHeader("Access-Control-Allow-Origin", "http://localhost:3000")) {
              complete(Item(Some(3), 42))
//            respondWithHeaders(RawHeader("Access-Control-Allow-Origin", "http://localhost:3000")) {
//              complete("beep")
//            complete(HttpResponse(StatusCodes.OK, Seq(allowAccess), entity = HttpEntity(ContentType(MediaTypes.`application/json`), """{"id":"1"}"""))) // will render as JSON
//             complete(Item("thing", 42)) // will render as JSON
          } }
        } ~
        post {
          entity(as[Order]) { order => // will unmarshal JSON to Order
            val itemsCount = order.items.size
            val itemNames = order.items.map(_.name).mkString(", ")
            complete(s"Ordered $itemsCount items: $itemNames")
          }
        }

    }


    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}

final case class Item(name: Option[Int], id: Long)
final case class Order(items: List[Item])