package eu.trustdemocracy.social.gateways.mongo;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.fakemongo.Fongo;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import eu.trustdemocracy.social.core.entities.Event;
import eu.trustdemocracy.social.gateways.EventDAO;
import io.vertx.core.json.JsonObject;
import java.util.UUID;
import lombok.val;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MongoEventDAOTest {

  private MongoCollection<Document> collection;
  private EventDAO eventDAO;

  @BeforeEach
  public void init() {
    val fongo = new Fongo("test server");
    val db = fongo.getDatabase("test_database");
    collection = db.getCollection("events");
    eventDAO = new MongoEventDAO(db);
  }

  @Test
  public void createEvent() {
    val event = getRandomEvent();

    assertEquals(0L, collection.count());

    val createdEvent = eventDAO.create(event);
    event.setId(createdEvent.getId());
    assertEquals(event, createdEvent);
    assertNotEquals(0L, collection.count(eq("id", event.getId().toString())));


    collection.find(eq("id", event.getId().toString()))
        .forEach(assertEqualsBlock(event));
  }

  private Event getRandomEvent() {
    val content = new JsonObject()
        .put("type", "PROPOSAL")
        .put("brief", "Brief of the proposal")
        .put("title", "Title of the proposal");
    return new Event()
        .setUserId(UUID.randomUUID())
        .setTimestamp(System.currentTimeMillis())
        .setSerializedContent(content);
  }

  private Block<Document> assertEqualsBlock(Event event) {
    return document -> {
      assertNotNull(document.get("id"));
      assertEquals(event.getUserId(), UUID.fromString(document.getString("user_id")));
      assertEquals(new Long(event.getTimestamp()), document.getLong("timestamp"));
      assertEquals(event.getSerializedContent().encode(), document.getString("serialized_content"));
    };
  }

}
