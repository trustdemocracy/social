package eu.trustdemocracy.social.infrastructure;

import com.github.fakemongo.Fongo;
import eu.trustdemocracy.social.core.interactors.event.CreateEvent;
import eu.trustdemocracy.social.gateways.EventDAO;
import eu.trustdemocracy.social.gateways.mongo.MongoEventDAO;
import lombok.val;

public class FakeInteractorFactory implements InteractorFactory {

  private EventDAO eventDAO;

  @Override
  public CreateEvent createCreateEventInteractor() {
    return new CreateEvent(getFakeEventDAO());
  }


  private EventDAO getFakeEventDAO() {
    if (eventDAO == null) {
      val fongo = new Fongo("test server");
      val db = fongo.getDatabase("test_database");
      eventDAO = new MongoEventDAO(db);
    }
    return eventDAO;
  }
}
