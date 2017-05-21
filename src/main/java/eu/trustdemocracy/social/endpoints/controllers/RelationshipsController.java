package eu.trustdemocracy.social.endpoints.controllers;

import eu.trustdemocracy.social.core.interactors.exceptions.InvalidTokenException;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.endpoints.App;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import lombok.val;

public class RelationshipsController extends Controller {

  public RelationshipsController(App app) {
    super(app);
  }

  @Override
  public void buildRoutes() {
    getRouter().post("/relationships/").handler(this::getRelationships);
  }

  private void getRelationships(RoutingContext routingContext) {
    OriginRelationshipRequestDTO originDTO;
    try {
      originDTO = Json.decodeValue(routingContext.getBodyAsString(),
          OriginRelationshipRequestDTO.class);
    } catch (Exception e) {
      serveBadRequest(routingContext);
      return;
    }

    val authToken = getAuthorizationToken(routingContext.request());
    originDTO.setOriginUserToken(authToken);

    val interactor = getInteractorFactory().getGetRelationships();

    try {
      val relationships = interactor.execute(originDTO);
      serveJsonResponse(routingContext, 200, Json.encodePrettily(relationships));
    } catch (InvalidTokenException e) {
      serveBadCredentials(routingContext);
    }
  }
}
