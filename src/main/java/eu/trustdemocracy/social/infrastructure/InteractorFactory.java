package eu.trustdemocracy.social.infrastructure;

import eu.trustdemocracy.social.core.interactors.event.CreateEvent;

public interface InteractorFactory {

  CreateEvent createCreateEventInteractor();
}
