package eu.trustdemocracy.social.core.interactors.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.trustdemocracy.social.core.interactors.exceptions.InvalidTokenException;
import eu.trustdemocracy.social.core.interactors.relationship.follow.AcceptFollow;
import eu.trustdemocracy.social.core.interactors.relationship.follow.FollowUser;
import eu.trustdemocracy.social.core.interactors.util.TokenUtils;
import eu.trustdemocracy.social.core.models.request.EventRequestDTO;
import eu.trustdemocracy.social.core.models.request.GetEventsRequestDTO;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.EventResponseDTO;
import eu.trustdemocracy.social.gateways.out.FakeRankerGateway;
import eu.trustdemocracy.social.gateways.repositories.fake.FakeEventRepository;
import eu.trustdemocracy.social.gateways.repositories.fake.FakeRelationshipRepository;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.val;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetEventsTest {

  private static List<EventResponseDTO> createdEvents;
  private FakeEventRepository eventRepository;
  private FakeRelationshipRepository relationshipRepository;
  private UUID userId = UUID.randomUUID();
  private Map<UUID, Boolean> followedUsersIds;

  @BeforeEach
  public void init() throws InterruptedException, JoseException {
    TokenUtils.generateKeys();
    eventRepository = new FakeEventRepository();
    relationshipRepository = new FakeRelationshipRepository();
    createdEvents = new ArrayList<>();
    followedUsersIds = new HashMap<>();

    for (int i = 0; i < 20; i++) {
      UUID id;
      do {
        id = UUID.randomUUID();
      } while (followedUsersIds.containsKey(id));
      followedUsersIds.put(id, i < 10);
    }

    val createEvent = new CreateEvent(eventRepository);
    for (int i = 0; i < 5; i++) {
      val event = new EventRequestDTO()
          .setUserId(userId)
          .setTimestamp(System.currentTimeMillis())
          .setSerializedContent(
              new JsonObject().put("type", "PROPOSAL")
                  .put("brief", "Brief of the proposal"));
      createdEvents.add(createEvent.execute(event));
      Thread.sleep(100);
    }

    for (val followedUserId : followedUsersIds.keySet()) {
      val event = new EventRequestDTO()
          .setUserId(followedUserId)
          .setTimestamp(System.currentTimeMillis())
          .setSerializedContent(
              new JsonObject().put("type", "PROPOSAL")
                  .put("brief", "Brief of the proposal"));
      createdEvents.add(createEvent.execute(event));
      Thread.sleep(100);
    }

    val followUser = new FollowUser(relationshipRepository);
    val acceptFollow = new AcceptFollow(relationshipRepository, new FakeRankerGateway());

    for (val followedUserId : followedUsersIds.keySet()) {
      if (followedUsersIds.get(followedUserId)) {
        followUser.execute(new OriginRelationshipRequestDTO()
            .setOriginUserToken(TokenUtils.createToken(userId, "originUsername"))
            .setTargetUserId(followedUserId));
        acceptFollow.execute(new TargetRelationshipRequestDTO()
            .setOriginUserId(userId)
            .setTargetUserToken(TokenUtils.createToken(followedUserId, "targetUsername")));
      }
    }
  }

  @Test
  public void getUserEventsNonTokenUser() {
    val getEventsRequest = new GetEventsRequestDTO()
        .setUserToken("");

    assertThrows(InvalidTokenException.class,
        () -> new GetEvents(eventRepository, relationshipRepository).execute(getEventsRequest));
  }

  @Test
  public void getTimeline() {
    val getEventsRequest = new GetEventsRequestDTO()
        .setUserToken(TokenUtils.createToken(userId, "originUsername"));

    val eventsDTO = new GetEvents(eventRepository, relationshipRepository).execute(getEventsRequest);
    val events = eventsDTO.getEvents();

    assertNotEquals(0, createdEvents.size());

    int remainingEvents = events.size();
    assertNotEquals(0, remainingEvents);

    for (val createdEvent : createdEvents) {
      if (createdEvent.getUserId().equals(userId)
          || followedUsersIds.get(createdEvent.getUserId())) {
        assertNotEquals(-1, events.indexOf(createdEvent));
        remainingEvents--;
      }
    }
    assertEquals(0, remainingEvents);
  }

  @Test
  public void getUserEvents() {
    val getEventsRequest = new GetEventsRequestDTO()
        .setUserToken(TokenUtils.createToken(userId, "originUsername"))
        .setTargetUserId(userId);

    val eventsDTO = new GetEvents(eventRepository, relationshipRepository).execute(getEventsRequest);
    val events = eventsDTO.getEvents();

    assertNotEquals(0, createdEvents.size());

    int remainingEvents = events.size();
    assertNotEquals(0, remainingEvents);

    for (val createdEvent : createdEvents) {
      if (createdEvent.getUserId().equals(userId)) {
        assertNotEquals(-1, events.indexOf(createdEvent));
        remainingEvents--;
      }
    }
    assertEquals(0, remainingEvents);
  }

  @Test
  public void getFollowedUserEvents() {
    UUID followedUserId = null;

    for (val id : followedUsersIds.keySet()) {
      if (followedUsersIds.get(id)) {
        followedUserId = id;
      }
    }
    assert followedUserId != null;

    val getEventsRequest = new GetEventsRequestDTO()
        .setUserToken(TokenUtils.createToken(userId, "originUsername"))
        .setTargetUserId(followedUserId);

    val eventsDTO = new GetEvents(eventRepository, relationshipRepository).execute(getEventsRequest);
    val events = eventsDTO.getEvents();

    assertNotEquals(0, createdEvents.size());

    int remainingEvents = events.size();
    assertNotEquals(0, remainingEvents);

    for (val createdEvent : createdEvents) {
      if (createdEvent.getUserId().equals(followedUserId)) {
        assertNotEquals(-1, events.indexOf(createdEvent));
        remainingEvents--;
      }
    }
    assertEquals(0, remainingEvents);
  }

  @Test
  public void getNonFollowedUserEvents() {
    UUID followedUserId = null;

    for (val id : followedUsersIds.keySet()) {
      if (!followedUsersIds.get(id)) {
        followedUserId = id;
      }
    }
    assert followedUserId != null;

    val getEventsRequest = new GetEventsRequestDTO()
        .setUserToken(TokenUtils.createToken(userId, "originUsername"))
        .setTargetUserId(followedUserId);

    val eventsDTO = new GetEvents(eventRepository, relationshipRepository).execute(getEventsRequest);
    val events = eventsDTO.getEvents();

    assertNotEquals(0, createdEvents.size());
    assertEquals(0, events.size());
  }

}
