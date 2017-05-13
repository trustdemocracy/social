package eu.trustdemocracy.social.core.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class User {

  private UUID id;
  private String username;
}
