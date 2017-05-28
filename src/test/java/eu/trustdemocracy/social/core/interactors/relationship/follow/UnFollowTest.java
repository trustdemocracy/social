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
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipRepository;
import eu.trustdemocracy.social.gateways.fake.FakeRelationshipRepository;
import java.util.UUID;
import lombok.val;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UnFollowTest {

  private static RelationshipResponseDTO createdRelationship;
  private RelationshipRepository relationshipRepository;
  private UUID originUserId = UUID.randomUUID();
  private String originUserUsername = "username";
  private UUID targetUserId = UUID.randomUUID();

  @BeforeEach
  public void init() throws JoseException {
    TokenUtils.generateKeys();

    relationshipRepository = new FakeRelationshipRepository();
    createdRelationship = new FollowUser(relationshipRepository)
        .execute(new OriginRelationshipRequestDTO()
            .setOriginUserToken(TokenUtils.createToken(originUserId, originUserUsername))
            .setTargetUserId(targetUserId));
  }

  @Test
  public void unFollowUserNonTokenUser() {
    val unFollowRelationship = new OriginRelationshipRequestDTO()
        .setOriginUserToken("")
        .setTargetUserId(targetUserId);

    assertThrows(InvalidTokenException.class,
        () -> new UnFollow(relationshipRepository).execute(unFollowRelationship));
  }

  @Test
  public void unFollow() {
    val unFollowRelationship = new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(originUserId, originUserUsername))
        .setTargetUserId(targetUserId);


    val relationship = RelationshipMapper.createEntity(unFollowRelationship);
    relationship.setRelationshipType(RelationshipType.FOLLOW);
    assertNotNull(relationshipRepository.find(relationship));
    val responseRelationship = new UnFollow(relationshipRepository).execute(unFollowRelationship);
    assertNull(relationshipRepository.find(relationship));

    assertEquals(originUserId, responseRelationship.getOriginUserId());
    assertEquals(originUserUsername, responseRelationship.getOriginUserUsername());
    assertEquals(targetUserId, responseRelationship.getTargetUserId());
    assertEquals(RelationshipType.FOLLOW, responseRelationship.getRelationshipType());
    assertEquals(RelationshipStatus.PENDING, responseRelationship.getRelationshipStatus());

  }


}
