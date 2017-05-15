package eu.trustdemocracy.social.infrastructure;

import eu.trustdemocracy.social.core.interactors.event.CreateEvent;
import eu.trustdemocracy.social.core.interactors.event.GetEvents;
import eu.trustdemocracy.social.core.interactors.relationship.follow.AcceptFollow;
import eu.trustdemocracy.social.core.interactors.relationship.follow.FollowUser;

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
}
