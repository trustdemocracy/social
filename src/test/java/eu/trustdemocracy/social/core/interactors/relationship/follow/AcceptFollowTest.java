package eu.trustdemocracy.social.core.interactors.relationship.follow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.interactors.util.TokenUtils;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import eu.trustdemocracy.social.gateways.fake.FakeRelationshipDAO;
import java.util.UUID;
import lombok.val;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AcceptFollowTest {

  private static RelationshipResponseDTO createdRelationship;
  private RelationshipDAO relationshipDAO;
  private UUID originUserId = UUID.randomUUID();
  private String originUserUsername = "username";
  private UUID targetUserId = UUID.randomUUID();
  private String targetUserUsername = "targetUsername";

  @BeforeEach
  public void init() throws JoseException {
    TokenUtils.generateKeys();

    relationshipDAO = new FakeRelationshipDAO();
    createdRelationship = new FollowUser(relationshipDAO)
        .execute(new OriginRelationshipRequestDTO()
            .setOriginUserToken(TokenUtils.createToken(originUserId, originUserUsername))
            .setTargetUserId(targetUserId));
  }

  @Test
  public void acceptFollow() {
    val toBeAcceptedRelationship = new TargetRelationshipRequestDTO()
        .setOriginUserId(createdRelationship.getOriginUserId())
        .setTargetUserToken(TokenUtils.createToken(targetUserId, targetUserUsername));

    val responseRelationship = new AcceptFollow(relationshipDAO).execute(toBeAcceptedRelationship);

    assertEquals(originUserId, responseRelationship.getOriginUserId());
    assertEquals(originUserUsername, responseRelationship.getOriginUserUsername());
    assertEquals(targetUserId, responseRelationship.getTargetUserId());
    assertEquals(targetUserUsername, responseRelationship.getTargetUserUsername());
    assertEquals(RelationshipType.FOLLOW, responseRelationship.getRelationshipType());
    assertEquals(RelationshipStatus.ACCEPTED, responseRelationship.getRelationshipStatus());
  }


}
