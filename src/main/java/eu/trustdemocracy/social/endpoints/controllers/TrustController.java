package eu.trustdemocracy.social.endpoints.controllers;

import eu.trustdemocracy.social.core.interactors.exceptions.InvalidTokenException;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.endpoints.App;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import lombok.val;

public class TrustController extends Controller {

  public TrustController(App app) {
    super(app);
  }

  @Override
  public void buildRoutes() {
    getRouter().post("/trust/").handler(this::trust);
    getRouter().delete("/trust/").handler(this::unTrust);
    getRouter().post("/trust/accept").handler(this::acceptTrust);
    getRouter().post("/trust/cancel").handler(this::cancelTrust);
    getRouter().post("/trust/requests").handler(this::getTrustRequests);
  }

  private void trust(RoutingContext routingContext) {
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

    val interactor = getInteractorFactory().createTrustUserInteractor();

    try {
      val relationship = interactor.execute(originRequest);
      serveJsonResponse(routingContext, 201, Json.encodePrettily(relationship));
    } catch (InvalidTokenException e) {
      serveBadCredentials(routingContext);
    }
  }

  private void acceptTrust(RoutingContext routingContext) {
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

    val interactor = getInteractorFactory().createAcceptTrustInteractor();

    try {
      val relationship = interactor.execute(targetRequest);
      serveJsonResponse(routingContext, 200, Json.encodePrettily(relationship));
    } catch (InvalidTokenException e) {
      serveBadCredentials(routingContext);
    }
  }

  private void cancelTrust(RoutingContext routingContext) {
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

    val interactor = getInteractorFactory().createCancelTrustInteractor();

    try {
      val relationship = interactor.execute(targetRequest);
      serveJsonResponse(routingContext, 200, Json.encodePrettily(relationship));
    } catch (InvalidTokenException e) {
      serveBadCredentials(routingContext);
    }
  }

  private void unTrust(RoutingContext routingContext) {
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

    val interactor = getInteractorFactory().createUnTrustnteractor();

    try {
      val relationship = interactor.execute(originRequest);
      serveJsonResponse(routingContext, 200, Json.encodePrettily(relationship));
    } catch (InvalidTokenException e) {
      serveBadCredentials(routingContext);
    }
  }

  private void getTrustRequests(RoutingContext routingContext) {
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

    val interactor = getInteractorFactory().createGetTrustRequests();

    try {
      val relationships = interactor.execute(targetRequest);
      serveJsonResponse(routingContext, 200, Json.encodePrettily(relationships));
    } catch (InvalidTokenException e) {
      serveBadCredentials(routingContext);
    }
  }
}
