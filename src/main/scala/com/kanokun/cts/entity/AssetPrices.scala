package com.kanokun.cts.entity

import java.util.Date
import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat
import org.joda.time.format.ISODateTimeFormat
import spray.json.JsString
import spray.json.JsValue
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import spray.json.DeserializationException
import spray.httpx.marshalling.Marshaller
import com.kanokun.cts.utils.JodaDateTimeMarshaller

case class AssetPrices(dataDate: DateTime, assetABC: Double, assetDEF: Double, assetXYZ: Double)

object AssetPricesJsonProtocol extends DefaultJsonProtocol with JodaDateTimeMarshaller {

  implicit val assetPriceFormat = jsonFormat4(AssetPrices.apply)

}
