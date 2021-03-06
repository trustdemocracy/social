package eu.trustdemocracy.social.core.interactors.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import eu.trustdemocracy.social.core.models.request.EventRequestDTO;
import eu.trustdemocracy.social.gateways.repositories.fake.FakeEventRepository;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateEventTest {

  private static List<EventRequestDTO> inputEvents;
  private FakeEventRepository eventRepository;

  @BeforeEach
  public void init() {
    eventRepository = new FakeEventRepository();
    inputEvents = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      inputEvents.add(new EventRequestDTO()
          .setUserId(UUID.randomUUID())
          .setUsername("username")
          .setType("PROPOSAL")
          .setTimestamp(System.currentTimeMillis())
          .setSerializedContent(
              new JsonObject().put("brief", "Brief of the proposal")));
    }
  }

  @Test
  public void createEvent() {
    val inputEvent = inputEvents.get(0);

    val interactor = new CreateEvent(eventRepository);
    val responseEvent = interactor.execute(inputEvent);

    assertNotNull(responseEvent.getId());
    assertEquals(inputEvent.getUserId(), responseEvent.getUserId());
    assertEquals(inputEvent.getUsername(), responseEvent.getUsername());
    assertEquals(inputEvent.getTimestamp(), responseEvent.getTimestamp());
    assertEquals(inputEvent.getSerializedContent(), responseEvent.getSerializedContent());
  }
}
