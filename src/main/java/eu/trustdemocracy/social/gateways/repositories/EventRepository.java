package eu.trustdemocracy.social.gateways.repositories;

import eu.trustdemocracy.social.core.entities.Event;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EventRepository {

  Event create(Event event);

  List<Event> getEvents(Set<UUID> targetUsersIds);
}
