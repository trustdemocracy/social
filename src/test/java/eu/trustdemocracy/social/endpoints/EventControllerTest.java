package eu.trustdemocracy.social.endpoints;

import eu.trustdemocracy.social.core.models.request.EventRequestDTO;
import eu.trustdemocracy.social.core.models.response.EventResponseDTO;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import java.util.UUID;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class EventControllerTest extends ControllerTest {

  @Test
  public void createEvent(TestContext context) {
    val async = context.async();
    val inputEvent = createRandomEvent();

    val single = client.post(port, HOST, "/events/")
        .rxSendJson(inputEvent);

    single.subscribe(response -> {
      context.assertEquals(response.statusCode(), 201);
      context.assertTrue(response.headers().get("content-type").contains("application/json"));

      val responseEvent = Json
          .decodeValue(response.body().toString(), EventResponseDTO.class);
      context.assertEquals(inputEvent.getUserId(), responseEvent.getUserId());
      context.assertEquals(inputEvent.getTimestamp(), responseEvent.getTimestamp());
      context.assertEquals(inputEvent.getSerializedContent(), responseEvent.getSerializedContent());
      context.assertNotNull(responseEvent.getId());

      async.complete();
    }, error -> {
      context.fail(error);
      async.complete();
    });
  }


  private EventRequestDTO createRandomEvent() {
    return new EventRequestDTO()
        .setUserId(UUID.randomUUID())
        .setTimestamp(System.currentTimeMillis())
        .setSerializedContent(
            new JsonObject().put("type", "PROPOSAL").put("brief", "Brief of the proposal"));
  }
}
