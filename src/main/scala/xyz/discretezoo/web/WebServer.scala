package xyz.discretezoo.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.concurrent.ExecutionContext
import db.ZooDB._
import xyz.discretezoo.web.db.model.{Graph, GraphRecord, Maniplex, ManiplexRecord}

object WebServer extends Directives with JsonSupport {

  private def maybeFilters(objects: String, filters: Seq[Parameter]): Seq[String] = {
    objects match {
      case "graphs" => filters.filter(Graph.isValidQueryFilter).map(Graph.queryCondition)
      case "maniplexes" => filters.filter(Maniplex.isValidQueryFilter).map(Maniplex.queryCondition)
    }
  }


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
                  val f = maybeFilters(p.objects, p.filters)
                  val cnt = for {
                    c <- count(p.objects, p.collections, f)
                  } yield Count(c)
                  def local(m: Count) = { ctx.complete(m) }
                  cnt.flatMap(local)
                }
              }
          }
        } ~ path("results") {
            entity(as[ResultsParameters]) { p => {
                ctx => {
                  val filters = maybeFilters(p.parameters.objects, p.parameters.filters)
                  val order = p.orderBy.map({ case Parameter(column, ord) => OrderBy(column, ord) })

                  p.parameters.objects match {
                    case "graphs" => {
                      val f = count("graphs", p.parameters.collections, filters).flatMap(count => {
                        val pages = (count / p.pageSize).ceil.toInt
                        val actualPage = if (p.page >= 1 && p.page <= pages) p.page else 1
                        getGraphs(p.parameters.collections, filters, p.pageSize, order, actualPage).map(rows => {
                          SearchResult[GraphRecord](pages, rows.toList)
                        })
                      })
                      def local(m: SearchResult[GraphRecord]) = { ctx.complete(m) }
                      f.flatMap(local)
                    }
                    case "maniplexes" => {
                      val f = count("maniplexes", p.parameters.collections, filters).flatMap(count => {
                        val pages = (count / p.pageSize).ceil.toInt
                        val actualPage = if (p.page >= 1 && p.page <= pages) p.page else 1
                        getManiplexes(p.parameters.collections, filters, p.pageSize, order, actualPage).map(rows => {
                          SearchResult[ManiplexRecord](pages, rows.toList)
                        })
                      })
                      def local(m: SearchResult[ManiplexRecord]) = { ctx.complete(m) }
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

case class Count(value: Int)

case class ResultsParameters(page: Int, pageSize: Int, parameters: SearchParameters, orderBy: List[Parameter])
case class SearchParameters(objects: String, collections: List[String], filters: List[Parameter])
case class Parameter(name: String, value: String)
case class SearchResult[T](pages: Int, data: List[T])