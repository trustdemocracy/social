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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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

    val events = getEvents(user, getEventsRequestDTO.getTargetUserId());
    return EventMapper.createResponse(events);
  }

  private List<Event> getEvents(User user, UUID targetUserId) {
    if (targetUserId == null) {
      return getTimeline(user);
    }

    if (!user.follows(targetUserId)) {
      return new ArrayList<>();
    }

    val targetIds = new HashSet<UUID>();
    targetIds.add(targetUserId);

    return eventDAO.getEvents(targetIds);
  }

  private List<Event> getTimeline(User user) {
    Set<UUID> followedUsersIds = user.getFollowedUsers().stream()
        .map(relationship -> relationship.getTargetUser().getId())
        .collect(Collectors.toSet());

    followedUsersIds.add(user.getId());

    return eventDAO.getEvents(followedUsersIds);
  }
}
