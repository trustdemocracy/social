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
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import eu.trustdemocracy.social.gateways.fake.FakeRelationshipDAO;
import java.util.UUID;
import lombok.val;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UnTrustTest {

  private static RelationshipResponseDTO createdRelationship;
  private RelationshipDAO relationshipDAO;
  private UUID originUserId = UUID.randomUUID();
  private String originUserUsername = "username";
  private UUID targetUserId = UUID.randomUUID();

  @BeforeEach
  public void init() throws JoseException {
    TokenUtils.generateKeys();

    relationshipDAO = new FakeRelationshipDAO();
    createdRelationship = new TrustUser(relationshipDAO)
        .execute(new OriginRelationshipRequestDTO()
            .setOriginUserToken(TokenUtils.createToken(originUserId, originUserUsername))
            .setTargetUserId(targetUserId));
  }

  @Test
  public void unTrustUserNonTokenUser() {
    val unFollowRelationship = new OriginRelationshipRequestDTO()
        .setOriginUserToken("")
        .setTargetUserId(targetUserId);

    assertThrows(InvalidTokenException.class,
        () -> new UnTrust(relationshipDAO).execute(unFollowRelationship));
  }

  @Test
  public void unTrust() {
    val unTrustRelationship = new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(originUserId, originUserUsername))
        .setTargetUserId(targetUserId);


    val relationship = RelationshipMapper.createEntity(unTrustRelationship);
    relationship.setRelationshipType(RelationshipType.TRUST);
    assertNotNull(relationshipDAO.find(relationship));
    val responseRelationship = new UnTrust(relationshipDAO).execute(unTrustRelationship);
    assertNull(relationshipDAO.find(relationship));

    assertEquals(originUserId, responseRelationship.getOriginUserId());
    assertEquals(originUserUsername, responseRelationship.getOriginUserUsername());
    assertEquals(targetUserId, responseRelationship.getTargetUserId());
    assertEquals(RelationshipType.TRUST, responseRelationship.getRelationshipType());
    assertEquals(RelationshipStatus.PENDING, responseRelationship.getRelationshipStatus());
  }


}
