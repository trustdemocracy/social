package eu.trustdemocracy.social.infrastructure;

import eu.trustdemocracy.social.core.interactors.event.CreateEvent;
import eu.trustdemocracy.social.core.interactors.event.GetEvents;
import eu.trustdemocracy.social.core.interactors.relationship.follow.AcceptFollow;
import eu.trustdemocracy.social.core.interactors.relationship.follow.FollowUser;

public interface InteractorFactory {

  CreateEvent createCreateEventInteractor();

  GetEvents createGetEventsInteractor();

  FollowUser createFollowUserInteractor();

  AcceptFollow createAcceptFollowInteractor();
}
