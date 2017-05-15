package eu.trustdemocracy.social.gateways.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.User;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.val;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoRelationshipDAO implements RelationshipDAO {

  private static final String RELATIONSHIPS_COLLECTION = "relationships";
  private MongoCollection<Document> collection;

  public MongoRelationshipDAO(MongoDatabase db) {
    this.collection = db.getCollection(RELATIONSHIPS_COLLECTION);
  }

  @Override
  public Relationship create(Relationship relationship) {
    val document = buildDocument(relationship);
    collection.insertOne(document);
    return relationship;
  }

  @Override
  public Relationship update(Relationship relationship) {
    val document = buildDocument(relationship);
    collection.replaceOne(equalityConditions(relationship), document);
    return relationship;
  }

  @Override
  public Relationship find(Relationship relationship) {
    val document = collection.find(equalityConditions(relationship)).first();

    if (document == null) {
      return null;
    }

    return buildFromDocument(document);
  }

  @Override
  public Relationship remove(Relationship relationship) {
    val result = collection.deleteOne(equalityConditions(relationship));

    if (result.getDeletedCount() > 0) {
      return relationship;
    }

    return null;
  }

  @Override
  public Set<Relationship> getAllOriginRelationships(UUID id, RelationshipType relationshipType) {
    Set<Relationship> relationships = new HashSet<>();
    val documents = collection.find(and(
        eq("origin_user.id", id.toString()),
        eq("type", relationshipType.toString())
    ));

    for (val document : documents) {
      relationships.add(buildFromDocument(document));
    }

    return relationships;
  }

  @Override
  public List<Relationship> findByTargetId(UUID id, RelationshipType relationshipType,
      RelationshipStatus relationshipStatus) {
    return null;
  }

  private Bson equalityConditions(Relationship relationship) {
    return and(
        eq("origin_user.id", relationship.getOriginUser().getId().toString()),
        eq("target_user.id", relationship.getTargetUser().getId().toString()),
        eq("type", relationship.getRelationshipType().toString())
    );
  }

  private Document buildDocument(Relationship relationship) {
    val originUser = new Document("id", relationship.getOriginUser().getId().toString())
        .append("username", relationship.getOriginUser().getUsername());
    val targetUser = new Document("id", relationship.getTargetUser().getId().toString())
        .append("username", relationship.getTargetUser().getUsername());

    return new Document()
        .append("origin_user", originUser)
        .append("target_user", targetUser)
        .append("type", relationship.getRelationshipType().toString())
        .append("status", relationship.getRelationshipStatus().toString());
  }

  private Relationship buildFromDocument(Document document) {
    val originUserDocument = (Document) document.get("origin_user");
    val originUser = new User()
        .setId(UUID.fromString(originUserDocument.getString("id")))
        .setUsername(originUserDocument.getString("username"));
    val targetUserDocument = (Document) document.get("target_user");
    val targetUser = new User()
        .setId(UUID.fromString(targetUserDocument.getString("id")))
        .setUsername(targetUserDocument.getString("username"));

    return new Relationship()
        .setOriginUser(originUser)
        .setTargetUser(targetUser)
        .setRelationshipType(RelationshipType.valueOf(document.getString("type")))
        .setRelationshipStatus(RelationshipStatus.valueOf(document.getString("status")));
  }
}
