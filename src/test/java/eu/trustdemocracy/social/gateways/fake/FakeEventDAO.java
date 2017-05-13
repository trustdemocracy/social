package eu.trustdemocracy.social.gateways.fake;

import eu.trustdemocracy.social.core.entities.Event;
import eu.trustdemocracy.social.gateways.EventDAO;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
}
