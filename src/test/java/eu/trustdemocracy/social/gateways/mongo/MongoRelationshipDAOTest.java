package eu.trustdemocracy.social.gateways.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.fakemongo.Fongo;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.User;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.val;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MongoRelationshipDAOTest {

  private MongoCollection<Document> collection;
  private RelationshipDAO relationshipDAO;

  @BeforeEach
  public void init() {
    val fongo = new Fongo("test server");
    val db = fongo.getDatabase("test_database");
    collection = db.getCollection("relationships");
    relationshipDAO = new MongoRelationshipDAO(db);
  }

  @Test
  public void createRelationship() {
    val relationship = getRandomRelationship();

    assertEquals(0L, collection.count());

    val createdRelationship = relationshipDAO.create(relationship);
    assertEquals(relationship, createdRelationship);
    assertNotEquals(0L, collection.count());

    collection.find(and(
        eq("origin_user.id", relationship.getOriginUser().getId().toString()),
        eq("target_user.id", relationship.getTargetUser().getId().toString()),
        eq("type", relationship.getRelationshipType().toString())
    )).forEach(assertEqualsBlock(relationship));
  }

  @Test
  public void findRelationship() {
    val relationship = getRandomRelationship();

    val nullRelationship = relationshipDAO.find(relationship);

    assertNull(nullRelationship);

    relationshipDAO.create(relationship);

    val foundRelationship = relationshipDAO.find(relationship);

    assertEquals(relationship, foundRelationship);
  }

  @Test
  public void updateRelationship() {
    val relationship = getRandomRelationship();
    relationshipDAO.create(relationship);

    assertEquals(relationship, relationshipDAO.find(relationship));

    relationship.setRelationshipStatus(RelationshipStatus.ACCEPTED);
    assertNotEquals(relationship.getRelationshipStatus(),
        relationshipDAO.find(relationship).getRelationshipStatus());

    assertEquals(relationship, relationshipDAO.update(relationship));

    assertEquals(relationship.getRelationshipStatus(),
        relationshipDAO.find(relationship).getRelationshipStatus());
  }

  @Test
  public void deleteRelationship() {
    val relationship = getRandomRelationship();
    relationshipDAO.create(relationship);

    assertEquals(relationship, relationshipDAO.find(relationship));

    assertNotEquals(0L, collection.count());
    assertEquals(relationship, relationshipDAO.remove(relationship));

    assertEquals(0L, collection.count());
    assertNull(relationshipDAO.find(relationship));
  }

  @Test
  public void getAllOriginRelationships() {
    val userId = UUID.randomUUID();

    List<Relationship> relationships = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      val relationship = getRandomRelationship();
      if (i % 2 == 0) {
        relationship.setRelationshipType(RelationshipType.FOLLOW);
      }
      if (i % 3 == 0) {
        relationship.getOriginUser().setId(userId);
        relationships.add(relationship);
      }
      relationshipDAO.create(relationship);
    }

    val followRelationships = relationships.stream()
        .filter(r -> r.getRelationshipType() == RelationshipType.FOLLOW)
        .collect(Collectors.toList());
    assertNotEquals(0, followRelationships.size());

    val foundFollowRelationships = relationshipDAO
        .getAllOriginRelationships(userId, RelationshipType.FOLLOW);
    assertEquals(followRelationships.size(), foundFollowRelationships.size());
    for (Relationship relationship : followRelationships) {
      assertTrue(foundFollowRelationships.contains(relationship));
    }

    val trustRelationships = relationships.stream()
        .filter(r -> r.getRelationshipType() == RelationshipType.TRUST)
        .collect(Collectors.toList());
    assertNotEquals(0, trustRelationships.size());

    val foundTrustRelationships = relationshipDAO
        .getAllOriginRelationships(userId, RelationshipType.TRUST);
    assertEquals(trustRelationships.size(), foundTrustRelationships.size());
    for (Relationship relationship : trustRelationships) {
      assertTrue(foundTrustRelationships.contains(relationship));
    }
  }

  @Test
  public void findByTargetId() {
    val userId = UUID.randomUUID();

    List<Relationship> relationships = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      val relationship = getRandomRelationship();
      if (i % 2 == 0) {
        relationship.setRelationshipType(RelationshipType.FOLLOW);
      }
      if (i % 3 == 0) {
        relationship.getTargetUser().setId(userId);
        relationships.add(relationship);
      }
      relationshipDAO.create(relationship);
    }


    val followRelationships = relationships.stream()
        .filter(r -> r.getRelationshipType() == RelationshipType.FOLLOW)
        .collect(Collectors.toList());
    assertNotEquals(0, followRelationships.size());

    val foundFollowRelationships = relationshipDAO
        .findByTargetId(userId, RelationshipType.FOLLOW, RelationshipStatus.PENDING);
    assertEquals(followRelationships.size(), foundFollowRelationships.size());
    for (Relationship relationship : followRelationships) {
      assertTrue(foundFollowRelationships.contains(relationship));
    }

    val trustRelationships = relationships.stream()
        .filter(r -> r.getRelationshipType() == RelationshipType.TRUST)
        .collect(Collectors.toList());
    assertNotEquals(0, trustRelationships.size());

    val foundTrustRelationships = relationshipDAO
        .findByTargetId(userId, RelationshipType.TRUST, RelationshipStatus.PENDING);
    assertEquals(trustRelationships.size(), foundTrustRelationships.size());
    for (Relationship relationship : trustRelationships) {
      assertTrue(foundTrustRelationships.contains(relationship));
    }
  }

  private Relationship getRandomRelationship() {
    return new Relationship()
        .setOriginUser(new User().setId(UUID.randomUUID()).setUsername("originUsername"))
        .setTargetUser(new User().setId(UUID.randomUUID()).setUsername("targetUsername"))
        .setRelationshipType(RelationshipType.TRUST)
        .setRelationshipStatus(RelationshipStatus.PENDING);
  }

  private Block<Document> assertEqualsBlock(Relationship relationship) {
    return document -> {
      val originUser = (Document) document.get("origin_user");
      val targetUser = (Document) document.get("target_user");

      assertEquals(relationship.getOriginUser().getId(),
          UUID.fromString(originUser.getString("id")));
      assertEquals(relationship.getOriginUser().getUsername(), originUser.getString("username"));
      assertEquals(relationship.getTargetUser().getId(),
          UUID.fromString(targetUser.getString("id")));
      assertEquals(relationship.getTargetUser().getUsername(), targetUser.getString("username"));
      assertEquals(relationship.getRelationshipType(),
          RelationshipType.valueOf(document.getString("type")));
      assertEquals(relationship.getRelationshipStatus(),
          RelationshipStatus.valueOf(document.getString("status")));
    };
  }

}
