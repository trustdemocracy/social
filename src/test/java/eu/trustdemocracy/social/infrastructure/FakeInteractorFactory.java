package eu.trustdemocracy.social.infrastructure;

import com.github.fakemongo.Fongo;
import com.mongodb.client.MongoDatabase;
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
import eu.trustdemocracy.social.gateways.EventRepository;
import eu.trustdemocracy.social.gateways.RelationshipRepository;
import eu.trustdemocracy.social.gateways.mongo.MongoEventRepository;
import eu.trustdemocracy.social.gateways.mongo.MongoRelationshipRepository;
import lombok.val;

public class FakeInteractorFactory implements InteractorFactory {

  private MongoDatabase db;

  @Override
  public CreateEvent getCreateEvent() {
    return new CreateEvent(getFakeEventRepository());
  }

  @Override
  public GetEvents getGetEvents() {
    return new GetEvents(getFakeEventRepository(), getFakeRelationshipRepository());
  }

  @Override
  public FollowUser getFollowUser() {
    return new FollowUser(getFakeRelationshipRepository());
  }

  @Override
  public AcceptFollow getAcceptFollow() {
    return new AcceptFollow(getFakeRelationshipRepository());
  }

  @Override
  public CancelFollow getCancelFollow() {
    return new CancelFollow(getFakeRelationshipRepository());
  }

  @Override
  public UnFollow getUnFollow() {
    return new UnFollow(getFakeRelationshipRepository());
  }

  @Override
  public GetFollowRequests getGetFollow() {
    return new GetFollowRequests(getFakeRelationshipRepository());
  }

  @Override
  public TrustUser getTrustUser() {
    return new TrustUser(getFakeRelationshipRepository());
  }

  @Override
  public AcceptTrust getAcceptTrust() {
    return new AcceptTrust(getFakeRelationshipRepository());
  }

  @Override
  public CancelTrust getCancelTrust() {
    return new CancelTrust(getFakeRelationshipRepository());
  }

  @Override
  public UnTrust getUnTrust() {
    return new UnTrust(getFakeRelationshipRepository());
  }

  @Override
  public GetTrustRequests getGetTrustRequests() {
    return new GetTrustRequests(getFakeRelationshipRepository());
  }

  @Override
  public GetRelationships getGetRelationships() {
    return new GetRelationships(getFakeRelationshipRepository());
  }

  private RelationshipRepository getFakeRelationshipRepository() {
    return new MongoRelationshipRepository(getDB());
  }

  private EventRepository getFakeEventRepository() {
    return new MongoEventRepository(getDB());
  }

  private MongoDatabase getDB() {
    if (db == null) {
      val fongo = new Fongo("test server");
      db = fongo.getDatabase("test_database");
    }
    return db;
  }
}
