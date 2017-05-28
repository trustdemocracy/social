package eu.trustdemocracy.social.gateways.mongo;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.fakemongo.Fongo;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import eu.trustdemocracy.social.core.entities.Event;
import eu.trustdemocracy.social.gateways.EventRepository;
import io.vertx.core.json.JsonObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.val;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MongoEventRepositoryTest {

  private MongoCollection<Document> collection;
  private EventRepository eventRepository;

  @BeforeEach
  public void init() {
    val fongo = new Fongo("test server");
    val db = fongo.getDatabase("test_database");
    collection = db.getCollection("events");
    eventRepository = new MongoEventRepository(db);
  }

  @Test
  public void createEvent() {
    val event = getRandomEvent();

    assertEquals(0L, collection.count());

    val createdEvent = eventRepository.create(event);
    event.setId(createdEvent.getId());
    assertEquals(event, createdEvent);
    assertNotEquals(0L, collection.count(eq("id", event.getId().toString())));


    collection.find(eq("id", event.getId().toString()))
        .forEach(assertEqualsBlock(event));
  }

  @Test
  public void getEvents() {
    assertEquals(0L, collection.count());

    Set<UUID> ids = new HashSet<>();
    Set<Event> createdEvents = new HashSet<>();

    for (int i = 0; i < 30; i++) {
      val event = eventRepository.create(getRandomEvent());
      if (i % 3 == 0) {
        ids.add(event.getUserId());
        createdEvents.add(event);
      }
    }
    assertEquals(30L, collection.count());

    List<Event> events = eventRepository.getEvents(ids);
    assertEquals(10, events.size());

    for (val event : events) {
      assertTrue(createdEvents.remove(event));
    }
  }

  private Event getRandomEvent() {
    val content = new JsonObject()
        .put("brief", "Brief of the proposal")
        .put("title", "Title of the proposal");
    return new Event()
        .setUserId(UUID.randomUUID())
        .setUsername("testUsername")
        .setType("PROPOSAL")
        .setTimestamp(System.currentTimeMillis())
        .setSerializedContent(content);
  }

  private Block<Document> assertEqualsBlock(Event event) {
    return document -> {
      assertNotNull(document.get("id"));
      assertEquals(event.getUserId(), UUID.fromString(document.getString("user_id")));
      assertEquals(event.getUsername(), document.getString("user_username"));
      assertEquals(event.getType(), document.getString("type"));
      assertEquals(new Long(event.getTimestamp()), document.getLong("timestamp"));
      assertEquals(event.getSerializedContent().encode(), document.getString("serialized_content"));
    };
  }

}
