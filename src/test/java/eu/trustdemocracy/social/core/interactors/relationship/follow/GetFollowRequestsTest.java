package eu.trustdemocracy.social.core.interactors.relationship.follow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.interactors.exceptions.InvalidTokenException;
import eu.trustdemocracy.social.core.interactors.util.TokenUtils;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import eu.trustdemocracy.social.gateways.fake.FakeRelationshipDAO;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.val;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetFollowRequestsTest {

  private RelationshipDAO relationshipDAO;
  private Set<UUID> originUserIds = new HashSet<>();
  private UUID targetUserId = UUID.randomUUID();
  private String targetUserUsername = "targetUsername";

  private int REQUESTS_COUNT = 10;

  @BeforeEach
  public void init() throws JoseException {
    TokenUtils.generateKeys();

    relationshipDAO = new FakeRelationshipDAO();
    val interactor = new FollowUser(relationshipDAO);
    for (int i = 0; i < REQUESTS_COUNT; i++) {
      val userId = UUID.randomUUID();
      originUserIds.add(userId);
      interactor.execute(new OriginRelationshipRequestDTO()
          .setOriginUserToken(TokenUtils.createToken(userId, "username"))
          .setTargetUserId(targetUserId));
    }

  }

  @Test
  public void getFollowRequestsNonTokenUser() {
    val targetRelationship = new TargetRelationshipRequestDTO()
        .setTargetUserToken("");

    assertThrows(InvalidTokenException.class,
        () -> new CancelFollow(relationshipDAO).execute(targetRelationship));
  }

  @Test
  public void getFollowRequests() {
    val targetRelationship = new TargetRelationshipRequestDTO()
        .setTargetUserToken(TokenUtils.createToken(targetUserId, targetUserUsername));

    val responseRelationships = new GetFollowRequests(relationshipDAO)
        .execute(targetRelationship)
        .getRelationships();

    assertEquals(REQUESTS_COUNT, responseRelationships.size());

    for (val relationship : responseRelationships) {
      assertTrue(originUserIds.contains(relationship.getOriginUserId()));
      assertEquals(targetUserId, relationship.getTargetUserId());
      assertEquals(RelationshipType.FOLLOW, relationship.getRelationshipType());
      assertEquals(RelationshipStatus.PENDING, relationship.getRelationshipStatus());
    }
  }


}
