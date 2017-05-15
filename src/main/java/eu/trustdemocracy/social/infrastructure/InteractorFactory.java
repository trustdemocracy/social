package eu.trustdemocracy.social.infrastructure;

import eu.trustdemocracy.social.core.interactors.event.CreateEvent;
import eu.trustdemocracy.social.core.interactors.event.GetEvents;
import eu.trustdemocracy.social.core.interactors.relationship.follow.AcceptFollow;
import eu.trustdemocracy.social.core.interactors.relationship.follow.CancelFollow;
import eu.trustdemocracy.social.core.interactors.relationship.follow.FollowUser;
import eu.trustdemocracy.social.core.interactors.relationship.follow.UnFollow;

public interface InteractorFactory {

  CreateEvent createCreateEventInteractor();

  GetEvents createGetEventsInteractor();

  FollowUser createFollowUserInteractor();

  AcceptFollow createAcceptFollowInteractor();

  CancelFollow createCancelFollowInteractor();

  UnFollow createUnFollowInteractor();
}
