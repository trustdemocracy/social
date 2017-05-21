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
  public CreateEvent getCreateEvent() {
    return new CreateEvent(DAOFactory.getEventDAO());
  }

  @Override
  public GetEvents getGetEvents() {
    return new GetEvents(DAOFactory.getEventDAO(), DAOFactory.getRelationshipDAO());
  }

  @Override
  public FollowUser getFollowUser() {
    return new FollowUser(DAOFactory.getRelationshipDAO());
  }

  @Override
  public AcceptFollow getAcceptFollow() {
    return new AcceptFollow(DAOFactory.getRelationshipDAO());
  }

  @Override
  public CancelFollow getCancelFollow() {
    return new CancelFollow(DAOFactory.getRelationshipDAO());
  }

  @Override
  public UnFollow getUnFollow() {
    return new UnFollow(DAOFactory.getRelationshipDAO());
  }

  @Override
  public GetFollowRequests getGetFollow() {
    return new GetFollowRequests(DAOFactory.getRelationshipDAO());
  }

  @Override
  public TrustUser getTrustUser() {
    return new TrustUser(DAOFactory.getRelationshipDAO());
  }

  @Override
  public AcceptTrust getAcceptTrust() {
    return new AcceptTrust(DAOFactory.getRelationshipDAO());
  }

  @Override
  public CancelTrust getCancelTrust() {
    return new CancelTrust(DAOFactory.getRelationshipDAO());
  }

  @Override
  public UnTrust getUnTrust() {
    return new UnTrust(DAOFactory.getRelationshipDAO());
  }

  @Override
  public GetTrustRequests getGetTrustRequests() {
    return new GetTrustRequests(DAOFactory.getRelationshipDAO());
  }

  @Override
  public GetRelationships getGetRelationships() {
    return new GetRelationships(DAOFactory.getRelationshipDAO());
  }
}
