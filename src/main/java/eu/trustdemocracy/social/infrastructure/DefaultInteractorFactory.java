package eu.trustdemocracy.social.infrastructure;

import eu.trustdemocracy.social.core.interactors.event.CreateEvent;
import eu.trustdemocracy.social.core.interactors.event.GetEvents;
import eu.trustdemocracy.social.core.interactors.relationship.GetRelationships;
import eu.trustdemocracy.social.core.interactors.relationship.follow.AcceptFollow;
import eu.trustdemocracy.social.core.interactors.relationship.follow.CancelFollow;
import eu.trustdemocracy.social.core.interactors.relationship.follow.FollowUser;
import eu.trustdemocracy.social.core.interactors.relationship.follow.GetFollowRequests;
import eu.trustdemocracy.social.core.interactors.relationship.follow.UnFollow;
import eu.trustdemocracy.social.core.interactors.relationship.trust.AcceptTrust;
import eu.trustdemocracy.social.core.interactors.relationship.trust.CancelTrust;
import eu.trustdemocracy.social.core.interactors.relationship.trust.GetTrustRequests;
import eu.trustdemocracy.social.core.interactors.relationship.trust.TrustUser;
import eu.trustdemocracy.social.core.interactors.relationship.trust.UnTrust;

public class DefaultInteractorFactory implements InteractorFactory {

  private static DefaultInteractorFactory instance;

  private DefaultInteractorFactory() {
  }

  public static DefaultInteractorFactory getInstance() {
    if (instance == null) {
      instance = new DefaultInteractorFactory();
    }
    return instance;
  }

  @Override
  public CreateEvent createCreateEventInteractor() {
    return new CreateEvent(DAOFactory.getEventDAO());
  }

  @Override
  public GetEvents createGetEventsInteractor() {
    return new GetEvents(DAOFactory.getEventDAO(), DAOFactory.getRelationshipDAO());
  }

  @Override
  public FollowUser createFollowUserInteractor() {
    return new FollowUser(DAOFactory.getRelationshipDAO());
  }

  @Override
  public AcceptFollow createAcceptFollowInteractor() {
    return new AcceptFollow(DAOFactory.getRelationshipDAO());
  }

  @Override
  public CancelFollow createCancelFollowInteractor() {
    return new CancelFollow(DAOFactory.getRelationshipDAO());
  }

  @Override
  public UnFollow createUnFollowInteractor() {
    return new UnFollow(DAOFactory.getRelationshipDAO());
  }

  @Override
  public GetFollowRequests createGetFollowRequests() {
    return new GetFollowRequests(DAOFactory.getRelationshipDAO());
  }

  @Override
  public TrustUser createTrustUserInteractor() {
    return new TrustUser(DAOFactory.getRelationshipDAO());
  }

  @Override
  public AcceptTrust createAcceptTrustInteractor() {
    return new AcceptTrust(DAOFactory.getRelationshipDAO());
  }

  @Override
  public CancelTrust createCancelTrustInteractor() {
    return new CancelTrust(DAOFactory.getRelationshipDAO());
  }

  @Override
  public UnTrust createUnTrustnteractor() {
    return new UnTrust(DAOFactory.getRelationshipDAO());
  }

  @Override
  public GetTrustRequests createGetTrustRequests() {
    return new GetTrustRequests(DAOFactory.getRelationshipDAO());
  }

  @Override
  public GetRelationships createGetRelationships() {
    return new GetRelationships(DAOFactory.getRelationshipDAO());
  }
}
