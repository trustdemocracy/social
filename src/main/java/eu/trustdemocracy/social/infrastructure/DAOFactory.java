package eu.trustdemocracy.social.infrastructure;

import eu.trustdemocracy.social.gateways.EventDAO;

public class DAOFactory {

  private static EventDAO eventDAO;

  public static EventDAO getEventDAO() {
    return eventDAO;
  }
}
