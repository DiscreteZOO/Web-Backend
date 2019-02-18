package xyz.discretezoo.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer

import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import scala.concurrent.ExecutionContext
import spray.json._

import xyz.discretezoo.web.ZooJsonAPI._
import xyz.discretezoo.web.db.ZooGraph.Graph
import xyz.discretezoo.web.db.ZooManiplex.Maniplex
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
            entity(as[SearchParameters]) {
              p => {
                ctx => {
                  val qp = SearchParam.get(p.collections, p.filters)
                  val cnt = for {
                    c <- p.objects match {
                      case "graphs" => ZooDb.countGraphs(qp)
                      case "maniplexes" => ZooDb.countManiplexes(qp)
                    }
                  } yield Count(c)
                  def local(m: Count) = { ctx.complete(m) }
                  cnt.flatMap(local)
                }
              }
          }
        } ~ path("results") {
            entity(as[ResultsParameters]) { p => {
                ctx => {
                  val qp = SearchParam.get(p.parameters.collections, p.parameters.filters)
                  p.parameters.objects match {
                    case "graphs" => {
                      val f = ZooDb.countGraphs(qp).flatMap(count => {
                        val pages = (count / p.pageSize).ceil.toInt
                        val actualPage = if (p.page >= 1 && p.page <= pages) p.page else 1
                        ZooDb.getGraphs(ResultParam.get(actualPage, p.pageSize, qp, p.orderBy))
                          .map(rows => (pages, rows.toList))
                      })
                      def local(result: (Int, Seq[Graph])) = {
                        ctx.complete(SearchResult(result._1, result._2.map(_.toJson).toList))
                      }
                      f.flatMap(local)
                    }
                    case "maniplexes" => {
                      val f = ZooDb.countManiplexes(qp).flatMap(count => {
                        val pages = (count / p.pageSize).ceil.toInt
                        val actualPage = if (p.page >= 1 && p.page <= pages) p.page else 1
                        ZooDb.getManiplexes(ResultParam.get(actualPage, p.pageSize, qp, p.orderBy))
                      })
                      def local(result: Seq[Maniplex]) = { ctx.complete(result.map(_.toJson)) }
                      f.flatMap(local)
                    }
                  }


                }
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
    Runtime.getRuntime.addShutdownHook(new Thread() {override def run = {
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