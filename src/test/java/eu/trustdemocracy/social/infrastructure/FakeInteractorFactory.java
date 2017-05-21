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
  public CreateEvent createCreateEventInteractor() {
    return new CreateEvent(getFakeEventDAO());
  }

  @Override
  public GetEvents createGetEventsInteractor() {
    return new GetEvents(getFakeEventDAO(), getFakeRelationshipDAO());
  }

  @Override
  public FollowUser createFollowUserInteractor() {
    return new FollowUser(getFakeRelationshipDAO());
  }

  @Override
  public AcceptFollow createAcceptFollowInteractor() {
    return new AcceptFollow(getFakeRelationshipDAO());
  }

  @Override
  public CancelFollow createCancelFollowInteractor() {
    return new CancelFollow(getFakeRelationshipDAO());
  }

  @Override
  public UnFollow createUnFollowInteractor() {
    return new UnFollow(getFakeRelationshipDAO());
  }

  @Override
  public GetFollowRequests createGetFollowRequests() {
    return new GetFollowRequests(getFakeRelationshipDAO());
  }

  @Override
  public TrustUser createTrustUserInteractor() {
    return new TrustUser(getFakeRelationshipDAO());
  }

  @Override
  public AcceptTrust createAcceptTrustInteractor() {
    return new AcceptTrust(getFakeRelationshipDAO());
  }

  @Override
  public CancelTrust createCancelTrustInteractor() {
    return new CancelTrust(getFakeRelationshipDAO());
  }

  @Override
  public UnTrust createUnTrustnteractor() {
    return new UnTrust(getFakeRelationshipDAO());
  }

  @Override
  public GetTrustRequests createGetTrustRequests() {
    return new GetTrustRequests(getFakeRelationshipDAO());
  }

  @Override
  public GetRelationships createGetRelationships() {
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
