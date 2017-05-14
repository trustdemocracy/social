package eu.trustdemocracy.social.gateways.mongo;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import eu.trustdemocracy.social.core.entities.Event;
import eu.trustdemocracy.social.core.entities.User;
import eu.trustdemocracy.social.gateways.EventDAO;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.UUID;
import lombok.val;
import org.bson.Document;

public class MongoEventDAO implements EventDAO {

  private static final String EVENTS_COLLECTION = "events";
  private MongoCollection<Document> collection;

  public MongoEventDAO(MongoDatabase db) {
    this.collection = db.getCollection(EVENTS_COLLECTION);
  }

  @Override
  public Event create(Event event) {
    event.setId(getUniqueUUID());

    val document = new Document("id", event.getId().toString())
        .append("user_id", event.getUserId().toString())
        .append("timestamp", event.getTimestamp())
        .append("serialized_content", event.getSerializedContent().encode());

    collection.insertOne(document);
    return event;
  }

  @Override
  public List<Event> getUserEvents(User user) {
    return null;
  }

  @Override
  public List<Event> getUserEvents(User user, UUID targetUserId) {
    return null;
  }

  private Event findById(UUID id) {
    val document = collection.find(eq("id", id.toString())).first();
    if (document == null) {
      return null;
    }

    return buildFromDocument(document);
  }

  private UUID getUniqueUUID() {
    UUID id;
    do {
      id = UUID.randomUUID();
    } while (findById(id) != null);
    return id;
  }

  private Event buildFromDocument(Document document) {
    return new Event()
        .setId(UUID.fromString(document.getString("id")))
        .setUserId(UUID.fromString(document.getString("user_id")))
        .setTimestamp(document.getLong("timestamp"))
        .setSerializedContent(new JsonObject(document.getString("serialized_content")));
  }
}
