package eu.trustdemocracy.social.core.models.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetEventsResponseDTO {

  private List<EventResponseDTO> events = new ArrayList<>();
}
