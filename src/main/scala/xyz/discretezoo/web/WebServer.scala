package xyz.discretezoo.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.{Directives, Route, StandardRoute}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.headers._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.util.{Failure, Success}
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.io.StdIn
import db.ZooDB._
import xyz.discretezoo.web.db.{GraphColumns, ManiplexColumns, ZooDB}

object WebServer extends Directives with JsonSupport {

  private def maybeFilters(objects: String, filters: Seq[Parameter]): Seq[String] = {
    objects match {
      case "graphs" => filters.filter(GraphColumns.isValidQueryFilter).map(GraphColumns.queryCondition)
      case "maniplexes" => filters.filter(ManiplexColumns.isValidQueryFilter).map(ManiplexColumns.queryCondition)
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
        pathPrefix("count") {
          path("graphs") {
            entity(as[SearchParameters]) {
              p => {
                val f = maybeFilters("graphs", p.filters)
                val count = countGraphs(p.collections, f)
                complete(Count(count))
              }
            }
          } ~ path("maniplexes") {
            entity(as[SearchParameters]) {
              p => {
                val f = maybeFilters("maniplexes", p.filters)
                val count = countManiplexes(p.collections, f)
                complete(Count(count))
              }
            }
          }
        } ~ pathPrefix("results") {
          path("graphs") {
            entity(as[ResultsParameters]) { p => {
                ctx => {
                  val filters = maybeFilters("graphs", p.parameters.filters)
                  val count = countGraphs(p.parameters.collections, filters)
                  val pages = (count / p.pageSize).ceil.toInt
                  val order = p.orderBy.map({ case Parameter(column, ord) => OrderBy(column, ord) })
                  val actualPage = if (p.page >= 1 && p.page <= pages) p.page else 1
                  val resultsFuture = ZooDB.getGraphs(p.parameters.collections, filters, p.pageSize, order, actualPage)

                  def local(m: Seq[GraphAllColumns]) = { ctx.complete(m) }
                  resultsFuture.flatMap(local)

//                  def local(m: GraphResult) = { ctx.complete(m) }
//                  resultsFuture.map(d => GraphResult(pages, d)).flatMap(local)
                }
            } }
          } ~ path("maniplexes") {
            entity(as[ResultsParameters]) { p => {
              ctx => {
                val filters = maybeFilters("maniplexes", p.parameters.filters)
                val count = countManiplexes(p.parameters.collections, filters)
                val pages = (count / p.pageSize).ceil.toInt
                val order = p.orderBy.map({ case Parameter(column, ord) => OrderBy(column, ord) })
                val actualPage = if (p.page >= 1 && p.page <= pages) p.page else 1
                val resultsFuture = ZooDB.getManiplexes(p.parameters.collections, filters, p.pageSize, order, actualPage)

                def local(m: Seq[ManiplexAllColumns]) = { ctx.complete(m) }
                resultsFuture.flatMap(local)
              }
            } }
          }
        }
      }

    }

    val argsList = args.toList.lift
    val hostname = argsList(1).getOrElse("localhost")
    val port = argsList(0).getOrElse("8080").toInt

    val bindingFuture = Http().bindAndHandle(routes, hostname, port)

    println(s"Server online at http://$hostname:$port/\nPress CTRL+C to stop...")

    // add a hook to shutdown via shutdown
    val mainThread = Thread.currentThread();
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

final case class Count(value: Int)

final case class ResultsParameters(page: Int, pageSize: Int, parameters: SearchParameters, orderBy: List[Parameter])
final case class SearchParameters(collections: List[String], filters: List[Parameter])
final case class Parameter(name: String, value: String)
//final case class GraphResult(pages: Int, data: Seq[GraphAllColumns])
//final case class ManiplexResult(pages: Int, data: Seq[ManiplexAllColumns])