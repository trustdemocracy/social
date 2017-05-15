package eu.trustdemocracy.social.endpoints.controllers;

import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
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

}
