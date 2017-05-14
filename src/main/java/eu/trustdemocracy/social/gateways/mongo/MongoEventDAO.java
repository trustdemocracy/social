package eu.trustdemocracy.social.gateways.mongo;

import com.mongodb.client.MongoDatabase;
import eu.trustdemocracy.social.core.entities.Event;
import eu.trustdemocracy.social.core.entities.User;
import eu.trustdemocracy.social.gateways.EventDAO;
import java.util.List;
import java.util.UUID;

public class MongoEventDAO implements EventDAO {

  public MongoEventDAO(MongoDatabase db) {
  }

  @Override
  public Event create(Event event) {
    return null;
  }

  @Override
  public List<Event> getUserEvents(User user) {
    return null;
  }

  @Override
  public List<Event> getUserEvents(User user, UUID targetUserId) {
    return null;
  }
}
