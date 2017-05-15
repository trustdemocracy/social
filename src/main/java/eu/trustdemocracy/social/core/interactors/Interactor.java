package eu.trustdemocracy.social.core.interactors;

public interface Interactor<RequestDTO, ResponseDTO> {

  ResponseDTO execute(RequestDTO requestDTO);
}
