package eu.trustdemocracy.social.endpoints;

import eu.trustdemocracy.social.core.interactors.util.TokenUtils;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.GetRelationshipsResponseDTO;
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
        .putHeader("Authorization", "Bearer " + followRequest.getOriginUserToken())
        .rxSendJson(followRequest);

    single.subscribe(response -> {
      context.assertEquals(response.statusCode(), 201);
      context.assertTrue(response.headers().get("content-type").contains("application/json"));

      val responseRelationship = Json
          .decodeValue(response.body().toString(), RelationshipResponseDTO.class);

      context.assertEquals(originUserId, responseRelationship.getOriginUserId());
      context.assertEquals(originUserUsername, responseRelationship.getOriginUserUsername());
      context.assertEquals(targetUserId, responseRelationship.getTargetUserId());
      context.assertNull(responseRelationship.getTargetUserUsername());
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
        .putHeader("Authorization", "Bearer " + followRequest.getOriginUserToken())
        .rxSendJson(followRequest);

    single.subscribe(followResponse -> {
      context.assertEquals(followResponse.statusCode(), 201);

      client.post(port, HOST, "/follow/accept")
          .putHeader("Authorization", "Bearer " + acceptRequest.getTargetUserToken())
          .rxSendJson(acceptRequest)
          .subscribe(response -> {
            context.assertEquals(response.statusCode(), 200);
            context.assertTrue(response.headers().get("content-type").contains("application/json"));


            val responseRelationship = Json
                .decodeValue(response.body().toString(), RelationshipResponseDTO.class);
            context.assertEquals(originUserId, responseRelationship.getOriginUserId());
            context.assertEquals(originUserUsername, responseRelationship.getOriginUserUsername());
            context.assertEquals(targetUserId, responseRelationship.getTargetUserId());
            context.assertEquals(targetUserUsername, responseRelationship.getTargetUserUsername());
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

  @Test
  public void cancelFollow(TestContext context) {
    val async = context.async();
    val followRequest = new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(originUserId, originUserUsername))
        .setTargetUserId(targetUserId);

    val cancelRequest = new TargetRelationshipRequestDTO()
        .setOriginUserId(originUserId)
        .setTargetUserToken(TokenUtils.createToken(targetUserId, targetUserUsername));


    val single = client.post(port, HOST, "/follow/")
        .putHeader("Authorization", "Bearer " + followRequest.getOriginUserToken())
        .rxSendJson(followRequest);

    single.subscribe(followResponse -> {
      context.assertEquals(followResponse.statusCode(), 201);

      client.post(port, HOST, "/follow/cancel")
          .putHeader("Authorization", "Bearer " + cancelRequest.getTargetUserToken())
          .rxSendJson(cancelRequest)
          .subscribe(response -> {
            context.assertEquals(response.statusCode(), 200);
            context.assertTrue(response.headers().get("content-type").contains("application/json"));


            val responseRelationship = Json
                .decodeValue(response.body().toString(), RelationshipResponseDTO.class);
            context.assertEquals(originUserId, responseRelationship.getOriginUserId());
            context.assertEquals(originUserUsername, responseRelationship.getOriginUserUsername());
            context.assertEquals(targetUserId, responseRelationship.getTargetUserId());
            context.assertNull(responseRelationship.getTargetUserUsername());
            context.assertEquals(REL_TYPE, responseRelationship.getRelationshipType().toString());
            context
                .assertEquals("PENDING", responseRelationship.getRelationshipStatus().toString());
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

  @Test
  public void unFollow(TestContext context) {
    val async = context.async();
    val followRequest = new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(originUserId, originUserUsername))
        .setTargetUserId(targetUserId);


    val single = client.post(port, HOST, "/follow/")
        .putHeader("Authorization", "Bearer " + followRequest.getOriginUserToken())
        .rxSendJson(followRequest);

    single.subscribe(followResponse -> {
      context.assertEquals(followResponse.statusCode(), 201);

      client.post(port, HOST, "/follow/unfollow")
          .putHeader("Authorization", "Bearer " + followRequest.getOriginUserToken())
          .rxSendJson(followRequest)
          .subscribe(response -> {
            context.assertEquals(response.statusCode(), 200);
            context.assertTrue(response.headers().get("content-type").contains("application/json"));

            val responseRelationship = Json
                .decodeValue(response.body().toString(), RelationshipResponseDTO.class);
            context.assertEquals(originUserId, responseRelationship.getOriginUserId());
            context.assertEquals(originUserUsername, responseRelationship.getOriginUserUsername());
            context.assertEquals(targetUserId, responseRelationship.getTargetUserId());
            context.assertNull(responseRelationship.getTargetUserUsername());
            context.assertEquals(REL_TYPE, responseRelationship.getRelationshipType().toString());
            context
                .assertEquals("PENDING", responseRelationship.getRelationshipStatus().toString());
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

  @Test
  public void getFollowRequests(TestContext context) {
    val async = context.async();
    val followRequest = new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(originUserId, originUserUsername))
        .setTargetUserId(targetUserId);

    val getRequest = new TargetRelationshipRequestDTO()
        .setTargetUserToken(TokenUtils.createToken(targetUserId, targetUserUsername));


    val single = client.post(port, HOST, "/follow/")
        .putHeader("Authorization", "Bearer " + followRequest.getOriginUserToken())
        .rxSendJson(followRequest);

    single.subscribe(followResponse -> {
      context.assertEquals(followResponse.statusCode(), 201);

      client.get(port, HOST, "/follow/requests")
          .putHeader("Authorization", "Bearer " + getRequest.getTargetUserToken())
          .rxSendJson(getRequest)
          .subscribe(response -> {
            context.assertEquals(response.statusCode(), 200);
            context.assertTrue(response.headers().get("content-type").contains("application/json"));


            val responseRelationships = Json
                .decodeValue(response.body().toString(), GetRelationshipsResponseDTO.class);

            context.assertEquals(1, responseRelationships.getRelationships().size());
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
