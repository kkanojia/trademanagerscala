package com.kanokun.cts.utils

import spray.json.RootJsonFormat
import org.joda.time.format.ISODateTimeFormat
import spray.json.JsString
import spray.json.JsValue
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import spray.json.DeserializationException
import org.joda.time.format.DateTimeFormat

/**
 * @author Kunal
 */
trait JodaDateTimeMarshaller {
  
   implicit object DateJsonFormat extends RootJsonFormat[DateTime] {
    private val parserISO : DateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy");
    override def write(obj: DateTime) = JsString(parserISO.print(obj))
    override def read(json: JsValue) : DateTime = json match {
      case JsString(s) => parserISO.parseDateTime(s)
      case _ => throw new DeserializationException("Error info you want here ...")
    }
  }
 
  
}