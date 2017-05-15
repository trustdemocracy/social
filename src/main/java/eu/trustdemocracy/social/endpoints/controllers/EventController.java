package eu.trustdemocracy.social.endpoints.controllers;

import eu.trustdemocracy.social.core.models.request.EventRequestDTO;
import eu.trustdemocracy.social.core.models.request.GetEventsRequestDTO;
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
    getRouter().get("/events/").handler(this::getEvents);
  }

  private void createEvent(RoutingContext routingContext) {
    val requestEvent = decodeEventRequest(routingContext.getBodyAsJson());
    val interactor = getInteractorFactory().createCreateEventInteractor();
    val event = interactor.execute(requestEvent);

    routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(201)
        .end(Json.encodePrettily(event));
  }

  private void getEvents(RoutingContext routingContext) {
    val getEventsRequest = decodeGetEventsRequest(routingContext.getBodyAsJson());
    val interactor = getInteractorFactory().createGetEventsInteractor();
    val events = interactor.execute(getEventsRequest);

    routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(Json.encodePrettily(events));
  }

  private EventRequestDTO decodeEventRequest(JsonObject object) {
    return new EventRequestDTO()
        .setUserId(UUID.fromString(object.getString("userId")))
        .setTimestamp(object.getLong("timestamp"))
        .setSerializedContent(object.getJsonObject("serializedContent"));
  }

  private GetEventsRequestDTO decodeGetEventsRequest(JsonObject object) {
    return new GetEventsRequestDTO()
        .setUserToken(object.getString("userToken"));
  }
}
