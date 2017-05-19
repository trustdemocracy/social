package eu.trustdemocracy.social.endpoints;

import eu.trustdemocracy.social.core.interactors.util.TokenUtils;
import eu.trustdemocracy.social.core.models.request.EventRequestDTO;
import eu.trustdemocracy.social.core.models.request.GetEventsRequestDTO;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.EventResponseDTO;
import eu.trustdemocracy.social.core.models.response.GetEventsResponseDTO;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class EventControllerTest extends ControllerTest {

  @Test
  public void createEvent(TestContext context) {
    val async = context.async();
    val inputEvent = createRandomEvent();

    val single = client.post(port, HOST, "/events/")
        .rxSendJson(inputEvent);

    single.subscribe(response -> {
      context.assertEquals(response.statusCode(), 201);
      context.assertTrue(response.headers().get("content-type").contains("application/json"));

      val responseEvent = decodeEventResponse(response.body().toJsonObject());
      context.assertEquals(inputEvent.getUserId(), responseEvent.getUserId());
      context.assertEquals(inputEvent.getTimestamp(), responseEvent.getTimestamp());
      context.assertEquals(inputEvent.getSerializedContent(), responseEvent.getSerializedContent());
      context.assertNotNull(responseEvent.getId());

      async.complete();
    }, error -> {
      context.fail(error);
      async.complete();
    });
  }

  @Test
  public void getTimeline(TestContext context) throws InterruptedException {
    val userId = UUID.randomUUID();
    List<UUID> followedIds = new ArrayList<>();
    Map<EventResponseDTO, UUID> followedEvents = new HashMap<>();
    createEventsAndRelationships(userId, followedIds, followedEvents);

    val async = context.async();
    val getEventsRequest = new GetEventsRequestDTO()
        .setUserToken(TokenUtils.createToken(userId, "username"));

    val single = client.get(port, HOST, "/events/")
        .putHeader("Authorization", "Bearer " + getEventsRequest.getUserToken())
        .rxSendJson(getEventsRequest);

    single.subscribe(response -> {
      context.assertEquals(response.statusCode(), 200);
      context.assertTrue(response.headers().get("content-type").contains("application/json"));

      val responseEvents = decodeGetEventResponse(response.body().toJsonObject()).getEvents();

      context.assertNotEquals(0, followedEvents.size());
      context.assertEquals(followedEvents.size(), responseEvents.size());

      for (val event : responseEvents) {
        context.assertEquals(followedEvents.remove(event), event.getUserId());
      }

      context.assertEquals(0, followedEvents.size());

      async.complete();
    }, error -> {
      context.fail(error);
      async.complete();
    });
  }

  @Test
  public void getEventsByUser(TestContext context) throws InterruptedException {
    val userId = UUID.randomUUID();
    List<UUID> followedIds = new ArrayList<>();
    Map<EventResponseDTO, UUID> followedEvents = new HashMap<>();
    createEventsAndRelationships(userId, followedIds, followedEvents);

    val randomAuthorId = followedEvents.values().iterator().next();
    List<EventResponseDTO> authorEvents = followedEvents.entrySet().stream()
        .filter(entry -> entry.getValue() == randomAuthorId)
        .map(Entry::getKey)
        .collect(Collectors.toList());

    val async = context.async();
    val getEventsRequest = new GetEventsRequestDTO()
        .setUserToken(TokenUtils.createToken(userId, "username"))
        .setTargetUserId(randomAuthorId);

    val single = client.get(port, HOST, "/events/" + randomAuthorId)
        .putHeader("Authorization", "Bearer " + getEventsRequest.getUserToken())
        .rxSendJson(getEventsRequest);

    single.subscribe(response -> {
      context.assertEquals(response.statusCode(), 200);
      context.assertTrue(response.headers().get("content-type").contains("application/json"));

      val responseEvents = decodeGetEventResponse(response.body().toJsonObject()).getEvents();

      context.assertNotEquals(0, authorEvents.size());
      context.assertEquals(authorEvents.size(), responseEvents.size());

      for (val event : responseEvents) {
        context.assertTrue(authorEvents.remove(event));
      }

      context.assertEquals(0, authorEvents.size());

      async.complete();
    }, error -> {
      context.fail(error);
      async.complete();
    });
  }

  private GetEventsResponseDTO decodeGetEventResponse(JsonObject object) {
    List<EventResponseDTO> events = object.getJsonArray("events").stream()
        .map(event -> decodeEventResponse((JsonObject) event))
        .collect(Collectors.toList());
    return new GetEventsResponseDTO().setEvents(events);
  }

  private EventResponseDTO decodeEventResponse(JsonObject object) {
    return new EventResponseDTO()
        .setId(UUID.fromString(object.getString("id")))
        .setUserId(UUID.fromString(object.getString("userId")))
        .setTimestamp(object.getLong("timestamp"))
        .setSerializedContent(object.getJsonObject("serializedContent"));
  }

  private EventRequestDTO createRandomEvent() {
    return new EventRequestDTO()
        .setUserId(UUID.randomUUID())
        .setTimestamp(System.currentTimeMillis())
        .setSerializedContent(
            new JsonObject().put("type", "PROPOSAL").put("brief", "Brief of the proposal"));
  }

  private void createEventsAndRelationships(UUID userId, List<UUID> followedUsers,
      Map<EventResponseDTO, UUID> followedEvents) throws InterruptedException {
    val createEvent = interactorFactory.createCreateEventInteractor();
    val followUser = interactorFactory.createFollowUserInteractor();
    val acceptFollow = interactorFactory.createAcceptFollowInteractor();

    followedUsers.add(userId);

    for (int i = 0; i < 10; i++) {
      val followedId = UUID.randomUUID();
      followUser.execute(new OriginRelationshipRequestDTO()
          .setOriginUserToken(TokenUtils.createToken(userId, "username"))
          .setTargetUserId(followedId));
      acceptFollow.execute(new TargetRelationshipRequestDTO()
          .setOriginUserId(userId)
          .setTargetUserToken(TokenUtils.createToken(followedId, "username")));
      followedUsers.add(followedId);
    }

    val r = new Random();

    for (int i = 0; i < 50; i++) {
      val event = createRandomEvent();

      if (i % 2 == 0) {
        event.setUserId(followedUsers.get(r.nextInt(followedUsers.size())));
      }
      val createdEvent = createEvent.execute(event);

      if (i % 2 == 0) {
        followedEvents.put(createdEvent, createdEvent.getUserId());
      }

      Thread.sleep(100);
    }
  }
}
