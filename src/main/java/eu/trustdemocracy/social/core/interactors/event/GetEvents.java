package eu.trustdemocracy.social.core.interactors.event;

import eu.trustdemocracy.social.core.entities.Event;
import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.User;
import eu.trustdemocracy.social.core.entities.util.EventMapper;
import eu.trustdemocracy.social.core.entities.util.UserMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.GetEventsRequestDTO;
import eu.trustdemocracy.social.core.models.response.GetEventsResponseDTO;
import eu.trustdemocracy.social.gateways.repositories.EventRepository;
import eu.trustdemocracy.social.gateways.repositories.RelationshipRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.val;

public class GetEvents implements Interactor<GetEventsRequestDTO, GetEventsResponseDTO> {

  private EventRepository eventRepository;
  private RelationshipRepository relationshipRepository;

  public GetEvents(EventRepository eventRepository, RelationshipRepository relationshipRepository) {
    this.eventRepository = eventRepository;
    this.relationshipRepository = relationshipRepository;
  }

  @Override
  public GetEventsResponseDTO execute(GetEventsRequestDTO getEventsRequestDTO) {
    val user = UserMapper.createEntity(getEventsRequestDTO.getUserToken());
    val followed = relationshipRepository.getAllOriginRelationships(user.getId(), RelationshipType.FOLLOW);
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

    return eventRepository.getEvents(targetIds);
  }

  private List<Event> getTimeline(User user) {
    Set<UUID> followedUsersIds = user.getFollowedUsers().stream()
        .filter(relationship ->
            relationship.getRelationshipStatus().equals(RelationshipStatus.ACCEPTED))
        .map(relationship -> relationship.getTargetUser().getId())
        .collect(Collectors.toSet());

    followedUsersIds.add(user.getId());

    return eventRepository.getEvents(followedUsersIds);
  }
}
