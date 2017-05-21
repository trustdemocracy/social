package eu.trustdemocracy.social.core.entities;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Relationship {

  private User originUser;
  private User targetUser;
  private RelationshipType relationshipType;
  private RelationshipStatus relationshipStatus;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Relationship that = (Relationship) o;

    if (!originUser.getId().equals(that.originUser.getId())) {
      return false;
    }
    if (!targetUser.getId().equals(that.targetUser.getId())) {
      return false;
    }
    return relationshipType == that.relationshipType;
  }

  @Override
  public int hashCode() {
    int result = originUser.getId().hashCode();
    result = 31 * result + targetUser.getId().hashCode();
    result = 31 * result + relationshipType.hashCode();
    return result;
  }

  public boolean isAccepted() {
    return RelationshipStatus.ACCEPTED.equals(relationshipStatus);
  }
}
