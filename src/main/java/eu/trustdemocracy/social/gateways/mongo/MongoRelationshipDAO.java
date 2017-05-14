package eu.trustdemocracy.social.gateways.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import java.util.Set;
import java.util.UUID;
import lombok.val;
import org.bson.Document;

public class MongoRelationshipDAO implements RelationshipDAO {

  private static final String RELATIONSHIPS_COLLECTION = "relationships";
  private MongoCollection<Document> collection;

  public MongoRelationshipDAO(MongoDatabase db) {
    this.collection = db.getCollection(RELATIONSHIPS_COLLECTION);
  }

  @Override
  public Relationship create(Relationship relationship) {
    val originUser = new Document("id", relationship.getOriginUser().getId().toString())
        .append("username", relationship.getOriginUser().getUsername());
    val targetUser = new Document("id", relationship.getTargetUser().getId().toString())
        .append("username", relationship.getTargetUser().getUsername());

    val document = new Document()
        .append("origin_user", originUser)
        .append("target_user", targetUser)
        .append("type", relationship.getRelationshipType().toString())
        .append("status", relationship.getRelationshipStatus().toString());

    collection.insertOne(document);
    return relationship;
  }

  @Override
  public Relationship update(Relationship relationship) {
    return null;
  }

  @Override
  public Relationship find(Relationship relationship) {
    return null;
  }

  @Override
  public Relationship remove(Relationship foundRelationship) {
    return null;
  }

  @Override
  public Set<Relationship> getAllOriginRelationships(UUID id, RelationshipType relationshipType) {
    return null;
  }
}
