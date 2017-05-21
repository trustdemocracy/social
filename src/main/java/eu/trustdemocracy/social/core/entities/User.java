package eu.trustdemocracy.social.core.entities;

import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.val;

@Data
@Accessors(chain = true)
public class User {

  private UUID id;
  private String username;
  private Set<Relationship> followedUsers;

  public boolean follows(UUID userId) {
    if (id.equals(userId)) {
      return true;
    }

    for (val relationship : followedUsers) {
      if (relationship.isAccepted() && relationship.getTargetUser().getId().equals(userId)) {
        return true;
      }
    }
    return false;
  }
}
