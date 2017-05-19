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
    getRouter().get("/events/:userId").handler(this::getEventsByUser);
  }

  private void createEvent(RoutingContext routingContext) {
    EventRequestDTO requestEvent;
    try {
      requestEvent = decodeEventRequest(routingContext.getBodyAsJson());
    } catch (Exception e) {
      serveBadRequest(routingContext);
      return;
    }

    val interactor = getInteractorFactory().createCreateEventInteractor();
    val event = interactor.execute(requestEvent);

    serveJsonResponse(routingContext, 201, Json.encodePrettily(event));
  }

  private void getEvents(RoutingContext routingContext) {
    GetEventsRequestDTO getEventsRequest;
    try {
      getEventsRequest = decodeGetEventsRequest(routingContext.getBodyAsJson());
    } catch (Exception e) {
      serveBadRequest(routingContext);
      return;
    }

    val interactor = getInteractorFactory().createGetEventsInteractor();
    val events = interactor.execute(getEventsRequest);

    serveJsonResponse(routingContext, 200, Json.encodePrettily(events));
  }

  private void getEventsByUser(RoutingContext routingContext) {
    GetEventsRequestDTO getEventsRequest;
    try {
      getEventsRequest = decodeGetEventsRequest(routingContext.getBodyAsJson());
      getEventsRequest.setTargetUserId(UUID.fromString(routingContext.pathParam("userId")));
    } catch (Exception e) {
      serveBadRequest(routingContext);
      return;
    }
    val interactor = getInteractorFactory().createGetEventsInteractor();
    val events = interactor.execute(getEventsRequest);

    serveJsonResponse(routingContext, 200, Json.encodePrettily(events));
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
