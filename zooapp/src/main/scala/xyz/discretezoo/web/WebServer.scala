package xyz.discretezoo.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.headers._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.io.StdIn

import db.ZooDB._

object WebServer extends Directives with JsonSupport {

  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem("ZooActors")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContext = system.dispatcher

    val correctOrigin = HttpOrigin("http://localhost:3000")

//    val requestHandler: HttpRequest => Future[HttpResponse] = {
//      case HttpRequest(
//        POST,
//        Uri.Path("/"),
//        _, // matches any headers
//        _, // matches any HTTP entity (HTTP body)
//        _  // matches any HTTP protocol
//      ) => {
//        val m = Marshal(User("Richard Imaoka", 120))
//        m.to[HttpResponse]
//      }
//    }

    lazy val routes: Route = cors() {
      get {
        pathSingleSlash {
            complete(Item(Some(3), 42))
        }
      }
      post {
        pathPrefix("count") {
          path("graphs") {
            entity(as[SearchParameters]) {
              p => {
                val count = countGraphs(p.collections, Seq())
                println(p)
                complete(Count(count))
              }
            }
          }
        }
      }

    }

    val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}

final case class Item(name: Option[Int], id: Long)
final case class Order(items: List[Item])
final case class Count(value: Int)

final case class SearchParameters(collections: List[String], filters: List[SearchFilter])
final case class SearchFilter(name: String, value: String)