package com.kanokun.cts.rest

import spray.routing.HttpService
import shapeless.get
import akka.actor.Actor


//no exception handling or timeout handling for now
class TradeManagerRestActor extends Actor with TradeManagerRest with StaticAPI with TradeAPI {
  def actorRefFactory = context
  def receive = runRoute(defaultRoutes ~ staticDataRoute ~ tradeRoutes)
}

trait TradeManagerRest extends HttpService {
  
  val defaultRoutes =
    path("") {
      getFromResource("index.html")
    } ~
      get {
        getFromResourceDirectory("")
      } ~
      path("rest" / "user" / "authenticate") {
        formFields("username", "password") { (username, password) =>
          if (!username.equalsIgnoreCase("admin") && !password.equalsIgnoreCase("admin")) {
            throw new Exception("Invalid user password")
          }
          //TODO implement this later
          complete("{\"token\":\"admin:1444025198141:7660919dddfe366a62bd4538195d6301\"}")
        }
      } ~
      path("rest" / "user") {
        complete("{\"name\":\"admin\",\"roles\":{\"admin\":true,\"user\":true}}")
      }

}
