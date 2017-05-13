package eu.trustdemocracy.social.core.interactors.event;

import eu.trustdemocracy.social.core.entities.Event;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.User;
import eu.trustdemocracy.social.core.entities.util.EventMapper;
import eu.trustdemocracy.social.core.entities.util.UserMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.GetEventsRequestDTO;
import eu.trustdemocracy.social.core.models.response.GetEventsResponseDTO;
import eu.trustdemocracy.social.gateways.EventDAO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import java.util.List;
import java.util.UUID;
import lombok.val;

public class GetEvents implements Interactor<GetEventsRequestDTO, GetEventsResponseDTO> {

  private EventDAO eventDAO;
  private RelationshipDAO relationshipDAO;

  public GetEvents(EventDAO eventDAO, RelationshipDAO relationshipDAO) {
    this.eventDAO = eventDAO;
    this.relationshipDAO = relationshipDAO;
  }

  @Override
  public GetEventsResponseDTO execute(GetEventsRequestDTO getEventsRequestDTO) {
    val user = UserMapper.createEntity(getEventsRequestDTO.getUserToken());
    val followed = relationshipDAO.getAllOriginRelationships(user.getId(), RelationshipType.FOLLOW);
    user.setFollowedUsers(followed);

    return getUserEvents(user, getEventsRequestDTO.getTargetUserId());
  }

  private GetEventsResponseDTO getUserEvents(User user, UUID targetUserId) {
    List<Event> events;
    if (targetUserId == null) {
      events = eventDAO.getUserEvents(user);
    } else {
      events = eventDAO.getUserEvents(user, targetUserId);
    }

    return EventMapper.createResponse(events);
  }
}
