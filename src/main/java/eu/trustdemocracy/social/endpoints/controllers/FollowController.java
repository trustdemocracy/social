package eu.trustdemocracy.social.endpoints.controllers;

import eu.trustdemocracy.social.core.interactors.exceptions.InvalidTokenException;
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
    getRouter().delete("/follow/").handler(this::unFollow);
    getRouter().post("/follow/accept").handler(this::acceptFollow);
    getRouter().post("/follow/cancel").handler(this::cancelFollow);
    getRouter().post("/follow/requests").handler(this::getFollowRequests);
  }

  private void follow(RoutingContext routingContext) {
    OriginRelationshipRequestDTO originRequest;
    try {
      originRequest = Json.decodeValue(routingContext.getBodyAsString(),
          OriginRelationshipRequestDTO.class);
    } catch (Exception e) {
      serveBadRequest(routingContext);
      return;
    }

    val authToken = getAuthorizationToken(routingContext.request());
    originRequest.setOriginUserToken(authToken);

    val interactor = getInteractorFactory().createFollowUserInteractor();

    try {
      val relationship = interactor.execute(originRequest);
      serveJsonResponse(routingContext, 201, Json.encodePrettily(relationship));
    } catch (InvalidTokenException e) {
      serveBadCredentials(routingContext);
    }
  }

  private void acceptFollow(RoutingContext routingContext) {
    TargetRelationshipRequestDTO targetRequest;
    try {
      targetRequest = Json.decodeValue(routingContext.getBodyAsString(),
          TargetRelationshipRequestDTO.class);
    } catch (Exception e) {
      serveBadRequest(routingContext);
      return;
    }

    val authToken = getAuthorizationToken(routingContext.request());
    targetRequest.setTargetUserToken(authToken);

    val interactor = getInteractorFactory().createAcceptFollowInteractor();

    try {
      val relationship = interactor.execute(targetRequest);
      serveJsonResponse(routingContext, 200, Json.encodePrettily(relationship));
    } catch (InvalidTokenException e) {
      serveBadCredentials(routingContext);
    }
  }

  private void cancelFollow(RoutingContext routingContext) {
    TargetRelationshipRequestDTO targetRequest;
    try {
      targetRequest = Json.decodeValue(routingContext.getBodyAsString(),
          TargetRelationshipRequestDTO.class);
    } catch (Exception e) {
      serveBadRequest(routingContext);
      return;
    }

    val authToken = getAuthorizationToken(routingContext.request());
    targetRequest.setTargetUserToken(authToken);

    val interactor = getInteractorFactory().createCancelFollowInteractor();

    try {
      val relationship = interactor.execute(targetRequest);
      serveJsonResponse(routingContext, 200, Json.encodePrettily(relationship));
    } catch (InvalidTokenException e) {
      serveBadCredentials(routingContext);
    }
  }

  private void unFollow(RoutingContext routingContext) {
    OriginRelationshipRequestDTO originRequest;
    try {
      originRequest = Json.decodeValue(routingContext.getBodyAsString(),
          OriginRelationshipRequestDTO.class);
    } catch (Exception e) {
      serveBadRequest(routingContext);
      return;
    }

    val authToken = getAuthorizationToken(routingContext.request());
    originRequest.setOriginUserToken(authToken);

    val interactor = getInteractorFactory().createUnFollowInteractor();

    try {
      val relationship = interactor.execute(originRequest);
      serveJsonResponse(routingContext, 200, Json.encodePrettily(relationship));
    } catch (InvalidTokenException e) {
      serveBadCredentials(routingContext);
    }
  }

  private void getFollowRequests(RoutingContext routingContext) {
    TargetRelationshipRequestDTO targetRequest;
    try {
      targetRequest = Json.decodeValue(routingContext.getBodyAsString(),
          TargetRelationshipRequestDTO.class);
    } catch (Exception e) {
      serveBadRequest(routingContext);
      return;
    }

    val authToken = getAuthorizationToken(routingContext.request());
    targetRequest.setTargetUserToken(authToken);

    val interactor = getInteractorFactory().createGetFollowRequests();

    try {
      val relationships = interactor.execute(targetRequest);
      serveJsonResponse(routingContext, 200, Json.encodePrettily(relationships));
    } catch (InvalidTokenException e) {
      serveBadCredentials(routingContext);
    }
  }
}