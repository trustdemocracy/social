package eu.trustdemocracy.social.core.interactors.relationship.follow;

import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.GetRelationshipsResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;

public class GetFollowRequests implements
    Interactor<TargetRelationshipRequestDTO, GetRelationshipsResponseDTO> {

  public GetFollowRequests(RelationshipDAO relationshipDAO) {
  }

  @Override
  public GetRelationshipsResponseDTO execute(
      TargetRelationshipRequestDTO targetRelationshipRequestDTO) {
    return null;
  }
}
