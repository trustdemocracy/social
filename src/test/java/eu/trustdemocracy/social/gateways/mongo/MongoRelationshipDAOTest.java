package eu.trustdemocracy.social.gateways.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.github.fakemongo.Fongo;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.User;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import java.util.UUID;
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

    relationshipDAO.create(relationship);

    val foundRelationship = relationshipDAO.find(relationship);

    assertEquals(relationship, foundRelationship);
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
