package ma.emsi.mvc.controllers.secretaireModule.api;

import java.time.LocalDate;
import java.util.List;
import ma.emsi.entities.modules.enums.StatutRDV;
import ma.emsi.mvc.dto.common.ResponseWrapper;
import ma.emsi.mvc.dto.rdvDtos.RDVPlanificationRequest;
import ma.emsi.mvc.dto.rdvDtos.RDVResponse;

/**
 * Controller pour la gestion des rendez-vous par la secrétaire.
 * Use Cases: [157-158, 166-167, 170-171, 173, 175, 185, 208]
 */
public interface SecretaireRDVController {
    
    /**
     * Planifie un nouveau rendez-vous.
     */
    ResponseWrapper<RDVResponse> planifierRDV(RDVPlanificationRequest request);
    
    /**
     * Modifie un rendez-vous.
     */
    ResponseWrapper<RDVResponse> modifierRDV(Long rdvId, RDVPlanificationRequest request);
    
    /**
     * Annule un rendez-vous.
     */
    ResponseWrapper<Void> annulerRDV(Long rdvId);
    
    /**
     * Confirme un rendez-vous.
     */
    ResponseWrapper<RDVResponse> confirmerRDV(Long rdvId);
    
    /**
     * Récupère un rendez-vous par ID.
     */
    ResponseWrapper<RDVResponse> getRDV(Long rdvId);
    
    /**
     * Récupère l'historique des RDV d'un patient.
     */
    ResponseWrapper<List<RDVResponse>> getHistoriqueRDV(Long patientId);
    
    /**
     * Récupère les RDV d'un médecin pour une date.
     */
    ResponseWrapper<List<RDVResponse>> getRDVByMedecinAndDate(Long medecinId, LocalDate date);
    
    /**
     * Récupère la file d'attente (RDV en attente).
     */
    ResponseWrapper<List<RDVResponse>> getFileAttente();
    
    /**
     * Envoie un email de rappel au patient.
     */
    ResponseWrapper<Void> envoyerEmailRappel(Long rdvId);
}
