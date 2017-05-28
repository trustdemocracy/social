package eu.trustdemocracy.social.gateways.fake;

import eu.trustdemocracy.social.core.entities.Event;
import eu.trustdemocracy.social.gateways.EventRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.val;

public class FakeEventRepository implements EventRepository {

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
  public List<Event> getEvents(Set<UUID> targetUsersIds) {
    List<Event> userEvents = new ArrayList<>();
    for (val event : events.values()) {
      if (targetUsersIds.contains(event.getUserId())) {
        userEvents.add(event);
      }
    }
    return userEvents;
  }
}
