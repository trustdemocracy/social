package eu.trustdemocracy.social.core.interactors.relationship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.trustdemocracy.social.core.interactors.exceptions.InvalidTokenException;
import eu.trustdemocracy.social.core.interactors.relationship.follow.FollowUser;
import eu.trustdemocracy.social.core.interactors.relationship.trust.TrustUser;
import eu.trustdemocracy.social.core.interactors.util.TokenUtils;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.gateways.repositories.RelationshipRepository;
import eu.trustdemocracy.social.gateways.repositories.fake.FakeRelationshipRepository;
import java.util.UUID;
import lombok.val;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetRelationshipsTest {

  private RelationshipRepository relationshipRepository;
  private UUID originUserId = UUID.randomUUID();
  private UUID targetUserId = UUID.randomUUID();

  @BeforeEach
  public void init() throws JoseException {
    TokenUtils.generateKeys();

    relationshipRepository = new FakeRelationshipRepository();

    val follow = new FollowUser(relationshipRepository);

    follow.execute(new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(originUserId, "username"))
        .setTargetUserId(targetUserId));
    follow.execute(new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(targetUserId, "username"))
        .setTargetUserId(originUserId));


    follow.execute(new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(targetUserId, "username"))
        .setTargetUserId(UUID.randomUUID()));
    follow.execute(new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(originUserId, "username"))
        .setTargetUserId(UUID.randomUUID()));


    follow.execute(new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(UUID.randomUUID(), "username"))
        .setTargetUserId(targetUserId));
    follow.execute(new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(UUID.randomUUID(), "username"))
        .setTargetUserId(originUserId));

    val trust = new TrustUser(relationshipRepository);

    trust.execute(new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(originUserId, "username"))
        .setTargetUserId(targetUserId));
    trust.execute(new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(targetUserId, "username"))
        .setTargetUserId(originUserId));
  }

  @Test
  public void getRelationshipsNonTokenUser() {
    val originRelationship = new OriginRelationshipRequestDTO()
        .setOriginUserToken("");

    assertThrows(InvalidTokenException.class,
        () -> new GetRelationships(relationshipRepository).execute(originRelationship));
  }

  @Test
  public void getRelationships() {
    val originRelationship = new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(originUserId, "username"));

    val responseRelationships = new GetRelationships(relationshipRepository)
        .execute(originRelationship)
        .getRelationships();

    assertEquals(6, responseRelationships.size());
  }

  @Test
  public void getRelationshipsByUser() {
    val originRelationship = new OriginRelationshipRequestDTO()
        .setOriginUserToken(TokenUtils.createToken(originUserId, "username"))
        .setTargetUserId(targetUserId);

    val responseRelationships = new GetRelationships(relationshipRepository)
        .execute(originRelationship)
        .getRelationships();

    assertEquals(4, responseRelationships.size());
  }


}
