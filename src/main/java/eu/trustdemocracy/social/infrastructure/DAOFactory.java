package eu.trustdemocracy.social.infrastructure;

import eu.trustdemocracy.social.gateways.EventDAO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;

public class DAOFactory {

  private static EventDAO eventDAO;
  private static RelationshipDAO relationshipDAO;

  public static EventDAO getEventDAO() {
    return eventDAO;
  }

  public static RelationshipDAO getRelationshipDAO() {
    return relationshipDAO;
  }
}
