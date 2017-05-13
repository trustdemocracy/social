package eu.trustdemocracy.social.gateways.fake;

import eu.trustdemocracy.social.core.entities.Event;
import eu.trustdemocracy.social.core.entities.User;
import eu.trustdemocracy.social.gateways.EventDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.val;

public class FakeEventDAO implements EventDAO {

  private Map<UUID, Event> events = new HashMap<>();

  @Override
  public Event create(Event event) {
    UUID id;
    do {
      id = UUID.randomUUID();
    } while (events.containsKey(id));
    event.setId(id);
    events.put(id, event);

    return event;
  }

  @Override
  public List<Event> getUserEvents(User user) {
    List<Event> userEvents = new ArrayList<>();
    for (val event : events.values()) {
      if (user.follows(event.getUserId())) {
        userEvents.add(event);
      }
    }
    return userEvents;
  }

  @Override
  public List<Event> getUserEvents(User user, UUID targetUserId) {
    List<Event> userEvents = new ArrayList<>();

    if (!user.follows(targetUserId)) {
      return userEvents;
    }

    for (val event : events.values()) {
      if (event.getUserId().equals(targetUserId)) {
        userEvents.add(event);
      }
    }
    return userEvents;
  }
}
