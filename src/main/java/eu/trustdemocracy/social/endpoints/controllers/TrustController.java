package eu.trustdemocracy.social.endpoints.controllers;

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
  }

  private void trust(RoutingContext routingContext) {
    val originRequest = Json.decodeValue(routingContext.getBodyAsString(),
        OriginRelationshipRequestDTO.class);
    val interactor = getInteractorFactory().createTrustUserInteractor();
    val relationship = interactor.execute(originRequest);

    routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(201)
        .end(Json.encodePrettily(relationship));
  }

  private void acceptTrust(RoutingContext routingContext) {
    val targetRequest = Json.decodeValue(routingContext.getBodyAsString(),
        TargetRelationshipRequestDTO.class);
    val interactor = getInteractorFactory().createAcceptTrustInteractor();
    val relationship = interactor.execute(targetRequest);

    routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(Json.encodePrettily(relationship));
  }

  private void cancelTrust(RoutingContext routingContext) {
    val targetRequest = Json.decodeValue(routingContext.getBodyAsString(),
        TargetRelationshipRequestDTO.class);
    val interactor = getInteractorFactory().createCancelTrustInteractor();
    val relationship = interactor.execute(targetRequest);

    routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(Json.encodePrettily(relationship));
  }

  private void unTrust(RoutingContext routingContext) {
    val originRequest = Json.decodeValue(routingContext.getBodyAsString(),
        OriginRelationshipRequestDTO.class);
    val interactor = getInteractorFactory().createUnTrustnteractor();
    val relationship = interactor.execute(originRequest);

    routingContext.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(Json.encodePrettily(relationship));
  }
}
