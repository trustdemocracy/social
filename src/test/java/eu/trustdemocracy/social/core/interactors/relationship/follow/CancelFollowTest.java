package eu.trustdemocracy.social.core.interactors.relationship.follow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.util.RelationshipMapper;
import eu.trustdemocracy.social.core.interactors.exceptions.InvalidTokenException;
import eu.trustdemocracy.social.core.interactors.util.TokenUtils;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipRepository;
import eu.trustdemocracy.social.gateways.fake.FakeRelationshipRepository;
import java.util.UUID;
import lombok.val;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CancelFollowTest {

  private static RelationshipResponseDTO acceptedRelationship;
  private RelationshipRepository relationshipRepository;
  private UUID originUserId = UUID.randomUUID();
  private String originUserUsername = "username";
  private UUID targetUserId = UUID.randomUUID();
  private String targetUserUsername = "targetUsername";

  @BeforeEach
  public void init() throws JoseException {
    TokenUtils.generateKeys();

    relationshipRepository = new FakeRelationshipRepository();
    val createdRelationship = new FollowUser(relationshipRepository)
        .execute(new OriginRelationshipRequestDTO()
            .setOriginUserToken(TokenUtils.createToken(originUserId, originUserUsername))
            .setTargetUserId(targetUserId));

    val toBeAcceptedRelationship = new TargetRelationshipRequestDTO()
        .setOriginUserId(createdRelationship.getOriginUserId())
        .setTargetUserToken(TokenUtils.createToken(targetUserId, targetUserUsername));

    acceptedRelationship = new AcceptFollow(relationshipRepository).execute(toBeAcceptedRelationship);
  }

  @Test
  public void cancelFollowNonTokenUser() {
    val cancelFollowRelationship = new TargetRelationshipRequestDTO()
        .setOriginUserId(acceptedRelationship.getOriginUserId())
        .setTargetUserToken("");

    assertThrows(InvalidTokenException.class,
        () -> new CancelFollow(relationshipRepository).execute(cancelFollowRelationship));
  }

  @Test
  public void cancelFollow() {
    val cancelFollowRelationship = new TargetRelationshipRequestDTO()
        .setOriginUserId(acceptedRelationship.getOriginUserId())
        .setTargetUserToken(TokenUtils.createToken(targetUserId, targetUserUsername));


    val relationship = RelationshipMapper.createEntity(cancelFollowRelationship);
    relationship.setRelationshipType(RelationshipType.FOLLOW);
    assertNotNull(relationshipRepository.find(relationship));
    val responseRelationship = new CancelFollow(relationshipRepository).execute(cancelFollowRelationship);
    assertNull(relationshipRepository.find(relationship));

    assertEquals(originUserId, responseRelationship.getOriginUserId());
    assertEquals(originUserUsername, responseRelationship.getOriginUserUsername());
    assertEquals(targetUserId, responseRelationship.getTargetUserId());
    assertEquals(targetUserUsername, responseRelationship.getTargetUserUsername());
    assertEquals(RelationshipType.FOLLOW, responseRelationship.getRelationshipType());
    assertEquals(RelationshipStatus.ACCEPTED, responseRelationship.getRelationshipStatus());
  }


}
