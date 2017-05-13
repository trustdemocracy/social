package eu.trustdemocracy.social.gateways;

import eu.trustdemocracy.social.core.entities.Event;

public interface EventDAO {
    Event create(Event event);
}
