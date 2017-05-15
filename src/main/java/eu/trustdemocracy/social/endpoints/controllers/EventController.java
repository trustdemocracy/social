package eu.trustdemocracy.social.endpoints.controllers;

import eu.trustdemocracy.social.core.models.request.EventRequestDTO;
import eu.trustdemocracy.social.endpoints.App;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.UUID;
import lombok.val;

public class EventController extends Controller {

  public EventController(App app) {
    super(app);
  }

  @Override
  public void buildRoutes() {
    getRouter().post("/events/").handler(this::createEvent);
  }

  private void createEvent(RoutingContext routingContext) {
    val requestProposal = decodeValue(routingContext.getBodyAsJson());
    val interactor = getInteractorFactory().createCreateEventInteractor();
    val event = interactor.execute(requestProposal);

    routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(201)
        .end(Json.encodePrettily(event));
  }

  private EventRequestDTO decodeValue(JsonObject object) {
     return new EventRequestDTO()
         .setUserId(UUID.fromString(object.getString("userId")))
         .setTimestamp(object.getLong("timestamp"))
         .setSerializedContent(object.getJsonObject("serializedContent"));
  }
}
