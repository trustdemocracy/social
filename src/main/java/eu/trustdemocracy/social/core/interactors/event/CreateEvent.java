package eu.trustdemocracy.social.core.interactors.event;

import eu.trustdemocracy.social.core.entities.util.EventMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.EventRequestDTO;
import eu.trustdemocracy.social.core.models.response.EventResponseDTO;
import eu.trustdemocracy.social.gateways.EventDAO;
import lombok.val;

public class CreateEvent implements Interactor<EventRequestDTO, EventResponseDTO> {

    private EventDAO eventDAO;

    public CreateEvent(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    @Override
    public EventResponseDTO execute(EventRequestDTO eventRequestDTO) {
        val event = EventMapper.createEntity(eventRequestDTO);
        return EventMapper.createResponse(eventDAO.create(event));
    }
}
