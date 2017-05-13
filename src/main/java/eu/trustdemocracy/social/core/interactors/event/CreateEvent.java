package eu.trustdemocracy.social.core.interactors.event;

import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.EventRequestDTO;
import eu.trustdemocracy.social.core.models.response.EventResponseDTO;
import eu.trustdemocracy.social.gateways.EventDAO;

public class CreateEvent implements Interactor<EventRequestDTO, EventResponseDTO> {
    public CreateEvent(EventDAO eventDAO) {
    }

    @Override
    public EventResponseDTO execute(EventRequestDTO eventRequestDTO) {
        return null;
    }
}
