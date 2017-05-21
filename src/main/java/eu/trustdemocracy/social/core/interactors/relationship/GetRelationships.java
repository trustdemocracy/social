package eu.trustdemocracy.social.core.interactors.relationship;

import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.GetRelationshipsResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;

public class GetRelationships implements Interactor<OriginRelationshipRequestDTO, GetRelationshipsResponseDTO> {

  public GetRelationships(RelationshipDAO relationshipDAO) {
  }

  @Override
  public GetRelationshipsResponseDTO execute(
      OriginRelationshipRequestDTO requestDTO) {
    return null;
  }
}
