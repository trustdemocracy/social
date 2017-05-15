package eu.trustdemocracy.social.infrastructure;

import com.github.fakemongo.Fongo;
import com.mongodb.client.MongoDatabase;
import eu.trustdemocracy.social.core.interactors.event.CreateEvent;
import eu.trustdemocracy.social.core.interactors.event.GetEvents;
import eu.trustdemocracy.social.core.interactors.relationship.follow.AcceptFollow;
import eu.trustdemocracy.social.core.interactors.relationship.follow.CancelFollow;
import eu.trustdemocracy.social.core.interactors.relationship.follow.FollowUser;
import eu.trustdemocracy.social.core.interactors.relationship.follow.UnFollow;
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
