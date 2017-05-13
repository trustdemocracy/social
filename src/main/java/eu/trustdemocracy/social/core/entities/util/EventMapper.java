package eu.trustdemocracy.social.core.entities.util;

import eu.trustdemocracy.social.core.entities.Event;
import eu.trustdemocracy.social.core.models.request.EventRequestDTO;
import eu.trustdemocracy.social.core.models.response.EventResponseDTO;

public class EventMapper {

  public static Event createEntity(EventRequestDTO eventRequestDTO) {
    return new Event()
        .setUserId(eventRequestDTO.getUserId())
        .setTimestamp(eventRequestDTO.getTimestamp())
        .setSerializedContent(eventRequestDTO.getSerializedContent());
  }

  public static EventResponseDTO createResponse(Event event) {
    return new EventResponseDTO()
        .setId(event.getId())
        .setUserId(event.getUserId())
        .setTimestamp(event.getTimestamp())
        .setSerializedContent(event.getSerializedContent());
  }
}
