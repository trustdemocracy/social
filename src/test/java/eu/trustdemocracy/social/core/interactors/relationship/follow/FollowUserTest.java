package eu.trustdemocracy.social.core.interactors.relationship.follow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.interactors.exceptions.InvalidTokenException;
import eu.trustdemocracy.social.core.interactors.util.TokenUtils;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.gateways.RelationshipRepository;
import eu.trustdemocracy.social.gateways.fake.FakeRelationshipRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.val;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FollowUserTest {

  private static List<OriginRelationshipRequestDTO> inputRelationships;
  private RelationshipRepository relationshipRepository;
  private UUID originUserId = UUID.randomUUID();
  private String originUserUsername = "username";

  @BeforeEach
  public void init() throws JoseException {
    TokenUtils.generateKeys();

    relationshipRepository = new FakeRelationshipRepository();
    inputRelationships = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      inputRelationships.add(new OriginRelationshipRequestDTO()
          .setOriginUserToken(TokenUtils.createToken(originUserId, originUserUsername))
          .setTargetUserId(UUID.randomUUID()));
    }
  }

  @Test
  public void followUserNonTokenUser() {
    val inputRelationship = inputRelationships.get(0)
        .setOriginUserToken("");

    assertThrows(InvalidTokenException.class,
        () -> new FollowUser(relationshipRepository).execute(inputRelationship));
  }

  @Test
  public void followUser() {
    val inputRelationship = inputRelationships.get(0);

    val responseRelationship = new FollowUser(relationshipRepository).execute(inputRelationship);

    assertEquals(originUserId, responseRelationship.getOriginUserId());
    assertEquals(originUserUsername, responseRelationship.getOriginUserUsername());
    assertEquals(inputRelationship.getTargetUserId(), responseRelationship.getTargetUserId());
    assertEquals(RelationshipType.FOLLOW, responseRelationship.getRelationshipType());
    assertEquals(RelationshipStatus.PENDING, responseRelationship.getRelationshipStatus());
  }


}
