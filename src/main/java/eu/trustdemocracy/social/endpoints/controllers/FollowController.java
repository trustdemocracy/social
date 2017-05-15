package eu.trustdemocracy.social.endpoints.controllers;

import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.endpoints.App;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import lombok.val;

public class FollowController extends Controller {

  public FollowController(App app) {
    super(app);
  }

  @Override
  public void buildRoutes() {
    getRouter().post("/follow/").handler(this::follow);
    getRouter().post("/follow/accept").handler(this::acceptFollow);
    getRouter().post("/follow/cancel").handler(this::cancelFollow);
  }

  private void follow(RoutingContext routingContext) {
    val originRequest = Json.decodeValue(routingContext.getBodyAsString(),
        OriginRelationshipRequestDTO.class);
    val interactor = getInteractorFactory().createFollowUserInteractor();
    val relationship = interactor.execute(originRequest);

    routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(201)
        .end(Json.encodePrettily(relationship));
  }

  private void acceptFollow(RoutingContext routingContext) {
    val targetRequest = Json.decodeValue(routingContext.getBodyAsString(),
        TargetRelationshipRequestDTO.class);
    val interactor = getInteractorFactory().createAcceptFollowInteractor();
    val relationship = interactor.execute(targetRequest);

    routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(Json.encodePrettily(relationship));
  }

  private void cancelFollow(RoutingContext routingContext) {
    val targetRequest = Json.decodeValue(routingContext.getBodyAsString(),
        TargetRelationshipRequestDTO.class);
    val interactor = getInteractorFactory().createCancelFollowInteractor();
    val relationship = interactor.execute(targetRequest);

    routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(Json.encodePrettily(relationship));
  }

}
