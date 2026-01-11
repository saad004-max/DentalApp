package ma.emsi.mvc.controllers.medecinModule.api;

import java.util.List;
import ma.emsi.mvc.dto.common.ResponseWrapper;
import ma.emsi.mvc.dto.consultationDtos.ConsultationCreateRequest;
import ma.emsi.mvc.dto.consultationDtos.ConsultationResponse;

/**
 * Controller pour la gestion des consultations par le médecin.
 * Use Cases: [78, 82-83, 89, 99, 109-110]
 */
public interface MedecinConsultationController {
    
    /**
     * Crée et commence une nouvelle consultation.
     */
    ResponseWrapper<ConsultationResponse> creerConsultation(ConsultationCreateRequest request);
    
    /**
     * Démarre une consultation (change le statut à EN_COURS).
     */
    ResponseWrapper<ConsultationResponse> demarrerConsultation(Long consultationId);
    
    /**
     * Termine une consultation (change le statut à TERMINEE).
     */
    ResponseWrapper<ConsultationResponse> terminerConsultation(Long consultationId);
    
    /**
     * Ajoute une observation à la consultation.
     */
    ResponseWrapper<ConsultationResponse> ajouterObservation(Long consultationId, String observation);
    
    /**
     * Récupère une consultation par ID.
     */
    ResponseWrapper<ConsultationResponse> getConsultation(Long consultationId);
    
    /**
     * Modifie une consultation.
     */
    ResponseWrapper<ConsultationResponse> modifierConsultation(Long consultationId, ConsultationCreateRequest request);
    
    /**
     * Supprime une consultation.
     */
    ResponseWrapper<Void> supprimerConsultation(Long consultationId);
    
    /**
     * Récupère les consultations d'un patient.
     */
    ResponseWrapper<List<ConsultationResponse>> getConsultationsByPatient(Long patientId);
    
    /**
     * Récupère les consultations du médecin connecté.
     */
    ResponseWrapper<List<ConsultationResponse>> getMesConsultations();
}
