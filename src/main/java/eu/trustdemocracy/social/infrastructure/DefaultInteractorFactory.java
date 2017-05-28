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
import eu.trustdemocracy.social.gateways.out.RankerGateway;
import eu.trustdemocracy.social.gateways.out.RankerGatewayImpl;
import eu.trustdemocracy.social.gateways.repositories.EventRepository;
import eu.trustdemocracy.social.gateways.repositories.RelationshipRepository;

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
    return new CreateEvent(getEventRepository());
  }

  @Override
  public GetEvents getGetEvents() {
    return new GetEvents(getEventRepository(), getRelationshipRepository());
  }

  @Override
  public FollowUser getFollowUser() {
    return new FollowUser(getRelationshipRepository());
  }

  @Override
  public AcceptFollow getAcceptFollow() {
    return new AcceptFollow(getRelationshipRepository());
  }

  @Override
  public CancelFollow getCancelFollow() {
    return new CancelFollow(getRelationshipRepository());
  }

  @Override
  public UnFollow getUnFollow() {
    return new UnFollow(getRelationshipRepository());
  }

  @Override
  public GetFollowRequests getGetFollow() {
    return new GetFollowRequests(getRelationshipRepository());
  }

  @Override
  public TrustUser getTrustUser() {
    return new TrustUser(getRelationshipRepository());
  }

  @Override
  public AcceptTrust getAcceptTrust() {
    return new AcceptTrust(getRelationshipRepository(), getRankerGateway() );
  }

  @Override
  public CancelTrust getCancelTrust() {
    return new CancelTrust(getRelationshipRepository(), getRankerGateway() );
  }

  @Override
  public UnTrust getUnTrust() {
    return new UnTrust(getRelationshipRepository(), getRankerGateway() );
  }

  @Override
  public GetTrustRequests getGetTrustRequests() {
    return new GetTrustRequests(getRelationshipRepository());
  }

  @Override
  public GetRelationships getGetRelationships() {
    return new GetRelationships(getRelationshipRepository());
  }

  private EventRepository getEventRepository() {
    return RepositoryFactory.getEventRepository();
  }

  private RelationshipRepository getRelationshipRepository() {
    return RepositoryFactory.getRelationshipRepository();
  }

  private RankerGateway getRankerGateway() {
    return new RankerGatewayImpl();
  }
}
