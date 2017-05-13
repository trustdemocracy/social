package eu.trustdemocracy.social.gateways;

import eu.trustdemocracy.social.core.entities.Event;
import eu.trustdemocracy.social.core.entities.User;
import java.util.List;
import java.util.UUID;

public interface EventDAO {

  Event create(Event event);

  List<Event> getUserEvents(User user);

  List<Event> getUserEvents(User user, UUID targetUserId);
}
