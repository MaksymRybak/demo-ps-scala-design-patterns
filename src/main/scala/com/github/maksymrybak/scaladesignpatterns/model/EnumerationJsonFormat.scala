package com.github.maksymrybak.scaladesignpatterns.model

import spray.json.{JsString, JsValue, RootJsonFormat}

class EnumerationJsonFormat[A](enum: Enumeration) extends RootJsonFormat[A] {
  def write(obj: A): JsValue = JsString(obj.toString)

  def read(json: JsValue): A = json match {
    case JsString(str) => enum.withName(str).asInstanceOf[A]
    case x => throw new RuntimeException(s"unknown enumeration value: $x")
  }
}
