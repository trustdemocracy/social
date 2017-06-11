package eu.trustdemocracy.social.gateways.out;

import eu.trustdemocracy.social.core.entities.Relationship;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.client.WebClient;
import lombok.val;

public class RankerGatewayImpl implements RankerGateway {

  private Vertx vertx = Vertx.vertx();
  private static String host;
  private static Integer port;

  @Override
  public void addRelationship(Relationship relationship) {
    val json = new JsonObject()
        .put("originId", relationship.getOriginUser().getId().toString())
        .put("targetId", relationship.getTargetUser().getId().toString());

    WebClient.create(vertx)
        .post(getRankerPort(), getRankerHost(), "/relationships")
        .rxSendJson(json)
        .subscribe();
  }

  @Override
  public void removeRelationship(Relationship relationship) {
    val json = new JsonObject()
        .put("originId", relationship.getOriginUser().getId().toString())
        .put("targetId", relationship.getTargetUser().getId().toString());

    WebClient.create(vertx)
        .post(getRankerPort(), getRankerHost(), "/relationships/remove")
        .rxSendJson(json)
        .subscribe();
  }

  private static String getRankerHost() {
    if (host == null) {
      host = System.getenv("ranker_host");
    }
    return host;
  }

  private static int getRankerPort() {
    if (port == null) {
      port = Integer.valueOf(System.getenv("ranker_port"));
    }
    return port;
  }
}
