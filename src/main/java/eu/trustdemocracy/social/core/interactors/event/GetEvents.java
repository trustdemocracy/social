package eu.trustdemocracy.social.core.interactors.event;

import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.GetEventsRequestDTO;
import eu.trustdemocracy.social.core.models.response.GetEventsResponseDTO;
import eu.trustdemocracy.social.gateways.EventDAO;

public class GetEvents implements Interactor<GetEventsRequestDTO, GetEventsResponseDTO> {

  public GetEvents(EventDAO eventDAO) {
  }

  @Override
  public GetEventsResponseDTO execute(GetEventsRequestDTO getEventsRequestDTO) {
    return null;
  }
}
