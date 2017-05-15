package eu.trustdemocracy.social.gateways;

import eu.trustdemocracy.social.core.entities.Event;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EventDAO {

  Event create(Event event);

  List<Event> getEvents(Set<UUID> targetUsersIds);
}
