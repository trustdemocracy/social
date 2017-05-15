package eu.trustdemocracy.social.endpoints;

import eu.trustdemocracy.social.core.interactors.util.TokenUtils;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import io.vertx.core.json.Json;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import java.util.UUID;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class FollowControllerTest extends ControllerTest {

  private static final String REL_TYPE = "FOLLOW";
  private UUID originUserId = UUID.randomUUID();
  private String originUserUsername = "username";
  private UUID targetUserId = UUID.randomUUID();
  private String targetUserUsername = "username";

  @Test
  public void follow(TestContext context) {
    val async = context.async();
    val followRequest = new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(originUserId, originUserUsername))
        .setTargetUserId(targetUserId);

    val single = client.post(port, HOST, "/follow/")
        .rxSendJson(followRequest);

    single.subscribe(response -> {
      context.assertEquals(response.statusCode(), 201);
      context.assertTrue(response.headers().get("content-type").contains("application/json"));

      val responseRelationship = Json
          .decodeValue(response.body().toString(), RelationshipResponseDTO.class);

      context.assertEquals(originUserId, responseRelationship.getOriginUserId());
      context.assertEquals(originUserUsername, responseRelationship.getOriginUserUsername());
      context.assertEquals(targetUserId, responseRelationship.getTargetUserId());
      context.assertEquals(REL_TYPE, responseRelationship.getRelationshipType().toString());
      context.assertEquals("PENDING", responseRelationship.getRelationshipStatus().toString());
      async.complete();
    }, error -> {
      context.fail(error);
      async.complete();
    });
  }


  @Test
  public void acceptFollow(TestContext context) {
    val async = context.async();
    val followRequest = new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(originUserId, originUserUsername))
        .setTargetUserId(targetUserId);

    val acceptRequest = new TargetRelationshipRequestDTO()
        .setOriginUserId(originUserId)
        .setTargetUserToken(TokenUtils.createToken(targetUserId, targetUserUsername));


    val single = client.post(port, HOST, "/follow/")
        .rxSendJson(followRequest);

    single.subscribe(followResponse -> {
      context.assertEquals(followResponse.statusCode(), 201);

      client.post(port, HOST, "/follow/accept")
          .rxSendJson(acceptRequest)
          .subscribe(response -> {
            context.assertEquals(response.statusCode(), 200);
            context.assertTrue(response.headers().get("content-type").contains("application/json"));


            val responseRelationship = Json
                .decodeValue(response.body().toString(), RelationshipResponseDTO.class);
            context.assertEquals(originUserId, responseRelationship.getOriginUserId());
            context.assertEquals(originUserUsername, responseRelationship.getOriginUserUsername());
            context.assertEquals(targetUserId, responseRelationship.getTargetUserId());
            context.assertEquals(targetUserUsername, responseRelationship.getTargetUserId());
            context.assertEquals(REL_TYPE, responseRelationship.getRelationshipType().toString());
            context
                .assertEquals("ACCEPTED", responseRelationship.getRelationshipStatus().toString());
            async.complete();
          }, error -> {
            context.fail(error);
            async.complete();
          });
    }, error -> {
      context.fail(error);
      async.complete();
    });
  }
}
