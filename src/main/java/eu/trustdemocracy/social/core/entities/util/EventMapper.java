package eu.trustdemocracy.social.core.entities.util;

import eu.trustdemocracy.social.core.entities.Event;
import eu.trustdemocracy.social.core.models.request.EventRequestDTO;
import eu.trustdemocracy.social.core.models.response.EventResponseDTO;
import eu.trustdemocracy.social.core.models.response.GetEventsResponseDTO;
import java.util.ArrayList;
import java.util.List;

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

  public static GetEventsResponseDTO createResponse(List<Event> events) {
    List<EventResponseDTO> responseEvents = new ArrayList<>();
    for (Event event : events) {
      responseEvents.add(createResponse(event));
    }
    return new GetEventsResponseDTO()
        .setEvents(responseEvents);
  }
}
