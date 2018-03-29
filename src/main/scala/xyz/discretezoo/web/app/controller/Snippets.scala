package xyz.discretezoo.web.app.controller

import scala.xml._
import org.fusesource.scalate.RenderContext.captureNodeSeq

object Snippets {

  private def mapToMeta(map: Map[String, String]): MetaData = {
    val seed: MetaData = Null // Don't ask.
    map.toList.foldLeft(seed)({
      case (collector, (k, v)) => new UnprefixedAttribute(key = k, value = v, next = collector)
    })
  }

  private def element(label: String, meta: Map[String, String]): Elem =
    Elem(prefix = null, label, mapToMeta(meta), TopScope, minimizeEmpty = false)

  def introToggle(tag: String, att: (String, String)*)(body: => Unit): Elem = {
    val attributes = att.toMap ++ Map(
      "href" -> ".intro-toggle",
      "role" -> "button",
      "type" -> (if (tag == "button") "button" else ""),
      "data-target" -> ".intro-toggle",
      "data-toggle" -> "collapse",
      "aria-controls" -> "introduction brand",
      "aria-expanded" -> "true",
      "aria-label" -> "Toggle introduction visibility"
    ).filter(_._2.nonEmpty)
    element(tag, attributes).copy(child = captureNodeSeq(body))
  }

}
