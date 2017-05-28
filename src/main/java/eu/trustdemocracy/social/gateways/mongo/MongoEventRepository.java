package eu.trustdemocracy.social.gateways.mongo;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import eu.trustdemocracy.social.core.entities.Event;
import eu.trustdemocracy.social.gateways.EventRepository;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.val;
import org.bson.Document;

public class MongoEventRepository implements EventRepository {

  private static final String EVENTS_COLLECTION = "events";
  private MongoCollection<Document> collection;

  public MongoEventRepository(MongoDatabase db) {
    this.collection = db.getCollection(EVENTS_COLLECTION);
  }

  @Override
  public Event create(Event event) {
    event.setId(getUniqueUUID());

    val document = new Document("id", event.getId().toString())
        .append("user_id", event.getUserId().toString())
        .append("user_username", event.getUsername())
        .append("type", event.getType())
        .append("timestamp", event.getTimestamp())
        .append("serialized_content", event.getSerializedContent().encode());

    collection.insertOne(document);
    return event;
  }

  @Override
  public List<Event> getEvents(Set<UUID> targetUsersIds) {
    List<String> ids = targetUsersIds.stream()
        .map(UUID::toString)
        .collect(Collectors.toList());

    val documents = collection.find(in("user_id", ids));

    List<Event> events = new ArrayList<>();

    for (val document : documents) {
      events.add(buildFromDocument(document));
    }

    return events;
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
        .setUsername(document.getString("user_username"))
        .setType(document.getString("type"))
        .setTimestamp(document.getLong("timestamp"))
        .setSerializedContent(new JsonObject(document.getString("serialized_content")));
  }
}
