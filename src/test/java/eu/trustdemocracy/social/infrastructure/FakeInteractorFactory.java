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
import eu.trustdemocracy.social.gateways.EventDAO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import eu.trustdemocracy.social.gateways.mongo.MongoEventDAO;
import eu.trustdemocracy.social.gateways.mongo.MongoRelationshipDAO;
import lombok.val;

public class FakeInteractorFactory implements InteractorFactory {

  private MongoDatabase db;

  @Override
  public CreateEvent getCreateEvent() {
    return new CreateEvent(getFakeEventDAO());
  }

  @Override
  public GetEvents getGetEvents() {
    return new GetEvents(getFakeEventDAO(), getFakeRelationshipDAO());
  }

  @Override
  public FollowUser getFollowUser() {
    return new FollowUser(getFakeRelationshipDAO());
  }

  @Override
  public AcceptFollow getAcceptFollow() {
    return new AcceptFollow(getFakeRelationshipDAO());
  }

  @Override
  public CancelFollow getCancelFollow() {
    return new CancelFollow(getFakeRelationshipDAO());
  }

  @Override
  public UnFollow getUnFollow() {
    return new UnFollow(getFakeRelationshipDAO());
  }

  @Override
  public GetFollowRequests getGetFollow() {
    return new GetFollowRequests(getFakeRelationshipDAO());
  }

  @Override
  public TrustUser getTrustUser() {
    return new TrustUser(getFakeRelationshipDAO());
  }

  @Override
  public AcceptTrust getAcceptTrust() {
    return new AcceptTrust(getFakeRelationshipDAO());
  }

  @Override
  public CancelTrust getCancelTrust() {
    return new CancelTrust(getFakeRelationshipDAO());
  }

  @Override
  public UnTrust getUnTrust() {
    return new UnTrust(getFakeRelationshipDAO());
  }

  @Override
  public GetTrustRequests getGetTrustRequests() {
    return new GetTrustRequests(getFakeRelationshipDAO());
  }

  @Override
  public GetRelationships getGetRelationships() {
    return new GetRelationships(getFakeRelationshipDAO());
  }

  private RelationshipDAO getFakeRelationshipDAO() {
    return new MongoRelationshipDAO(getDB());
  }

  private EventDAO getFakeEventDAO() {
    return new MongoEventDAO(getDB());
  }

  private MongoDatabase getDB() {
    if (db == null) {
      val fongo = new Fongo("test server");
      db = fongo.getDatabase("test_database");
    }
    return db;
  }
}
