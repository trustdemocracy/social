package eu.trustdemocracy.social.core.interactors.event;

import eu.trustdemocracy.social.core.entities.util.EventMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.EventRequestDTO;
import eu.trustdemocracy.social.core.models.response.EventResponseDTO;
import eu.trustdemocracy.social.gateways.repositories.EventRepository;
import lombok.val;

public class CreateEvent implements Interactor<EventRequestDTO, EventResponseDTO> {

  private EventRepository eventRepository;

  public CreateEvent(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  @Override
  public EventResponseDTO execute(EventRequestDTO eventRequestDTO) {
    val event = EventMapper.createEntity(eventRequestDTO);
    return EventMapper.createResponse(eventRepository.create(event));
  }
}
