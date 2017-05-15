package eu.trustdemocracy.social.infrastructure;

import eu.trustdemocracy.social.core.interactors.event.CreateEvent;
import eu.trustdemocracy.social.core.interactors.event.GetEvents;
import eu.trustdemocracy.social.core.interactors.relationship.follow.AcceptFollow;
import eu.trustdemocracy.social.core.interactors.relationship.follow.CancelFollow;
import eu.trustdemocracy.social.core.interactors.relationship.follow.FollowUser;
import eu.trustdemocracy.social.core.interactors.relationship.follow.UnFollow;
import eu.trustdemocracy.social.core.interactors.relationship.trust.AcceptTrust;
import eu.trustdemocracy.social.core.interactors.relationship.trust.CancelTrust;
import eu.trustdemocracy.social.core.interactors.relationship.trust.TrustUser;
import eu.trustdemocracy.social.core.interactors.relationship.trust.UnTrust;

public interface InteractorFactory {

  CreateEvent createCreateEventInteractor();

  GetEvents createGetEventsInteractor();

  FollowUser createFollowUserInteractor();

  AcceptFollow createAcceptFollowInteractor();

  CancelFollow createCancelFollowInteractor();

  UnFollow createUnFollowInteractor();

  TrustUser createTrustUserInteractor();

  AcceptTrust createAcceptTrustInteractor();

  CancelTrust createCancelTrustInteractor();

  UnTrust createUnTrustnteractor();
}
