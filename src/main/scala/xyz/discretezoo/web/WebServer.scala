package xyz.discretezoo.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer

import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import scala.concurrent.ExecutionContext

import xyz.discretezoo.web.ZooJsonAPI._
import xyz.discretezoo.web.db.ZooDb

object WebServer extends Directives with JsonSupport {

  @volatile var keepRunning = true

  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem("ZooActors")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContext = system.dispatcher

//     val correctOrigin = HttpOrigin("http://localhost:3000")

    lazy val routes: Route = cors() {
      post {
        path("count") {
          entity(as[SearchParameters]) { p => ctx => ZooDb.count(p).flatMap(c => ctx.complete(c)) }
        } ~ path("results") {
          entity(as[ResultsParameters]) { p => { ctx =>
            ZooDb.get(p).flatMap(r => ctx.complete(SearchResult(r._1, r._2.map(writeZooObject).toList)))
          } }
        }
      }
    }

    val argsList = args.toList.lift
    val hostname = argsList(1).getOrElse("localhost")
    val port = argsList(0).getOrElse("8080").toInt

    val bindingFuture = Http().bindAndHandle(routes, hostname, port)

    println(s"Server online at http://$hostname:$port/\nPress CTRL+C to stop...")

    // add a hook to shutdown via shutdown
    val mainThread = Thread.currentThread()
    Runtime.getRuntime.addShutdownHook(new Thread() {override def run: Unit = {
      println(s"Received CTRL-C, exiting")
      keepRunning = false
      mainThread.join()
    }})
    // sleep until we receive ctrl+c
    while (keepRunning) {Thread.sleep(1000)}
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}