package eu.trustdemocracy.social.core.interactors.relationship.trust;

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
import eu.trustdemocracy.social.gateways.out.FakeRankerGateway;
import eu.trustdemocracy.social.gateways.repositories.RelationshipRepository;
import eu.trustdemocracy.social.gateways.repositories.fake.FakeRelationshipRepository;
import java.util.UUID;
import lombok.val;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CancelTrustTest {

  private static RelationshipResponseDTO acceptedRelationship;
  private RelationshipRepository relationshipRepository;
  private FakeRankerGateway rankerGateway;
  private UUID originUserId = UUID.randomUUID();
  private String originUserUsername = "username";
  private UUID targetUserId = UUID.randomUUID();
  private String targetUserUsername = "targetUsername";

  @BeforeEach
  public void init() throws JoseException {
    TokenUtils.generateKeys();

    relationshipRepository = new FakeRelationshipRepository();
    rankerGateway = new FakeRankerGateway();
    val createdRelationship = new TrustUser(relationshipRepository)
        .execute(new OriginRelationshipRequestDTO()
            .setOriginUserToken(TokenUtils.createToken(originUserId, originUserUsername))
            .setTargetUserId(targetUserId));

    val toBeAcceptedRelationship = new TargetRelationshipRequestDTO()
        .setOriginUserId(createdRelationship.getOriginUserId())
        .setTargetUserToken(TokenUtils.createToken(targetUserId, targetUserUsername));

    acceptedRelationship = new AcceptTrust(relationshipRepository, rankerGateway)
        .execute(toBeAcceptedRelationship);
  }

  @Test
  public void cancelTrustNonTokenUser() {
    val cancelTrustRelationship = new TargetRelationshipRequestDTO()
        .setOriginUserId(acceptedRelationship.getOriginUserId())
        .setTargetUserToken("");

    assertThrows(InvalidTokenException.class,
        () -> new CancelTrust(relationshipRepository, rankerGateway)
            .execute(cancelTrustRelationship));
  }

  @Test
  public void cancelTrust() {
    val cancelTrustRelationship = new TargetRelationshipRequestDTO()
        .setOriginUserId(acceptedRelationship.getOriginUserId())
        .setTargetUserToken(TokenUtils.createToken(targetUserId, targetUserUsername));

    val relationship = RelationshipMapper.createEntity(cancelTrustRelationship);
    relationship.setRelationshipType(RelationshipType.TRUST);
    assertNotNull(relationshipRepository.find(relationship));
    val responseRelationship = new CancelTrust(relationshipRepository, rankerGateway)
        .execute(cancelTrustRelationship);
    assertNull(relationshipRepository.find(relationship));

    assertEquals(originUserId, responseRelationship.getOriginUserId());
    assertEquals(originUserUsername, responseRelationship.getOriginUserUsername());
    assertEquals(targetUserId, responseRelationship.getTargetUserId());
    assertEquals(targetUserUsername, responseRelationship.getTargetUserUsername());
    assertEquals(RelationshipType.TRUST, responseRelationship.getRelationshipType());
    assertEquals(RelationshipStatus.ACCEPTED, responseRelationship.getRelationshipStatus());
  }


}
