package ma.emsi.repository.api.medical;

import java.util.List;
import ma.emsi.entities.modules.medical.InterventionMedecin;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des interventions médicales.
 */
public interface InterventionMedecinRepo extends CrudRepo<InterventionMedecin, Long> {
    
    /**
     * Trouve les interventions d'une consultation.
     */
    List<InterventionMedecin> findByConsultationId(Long consultationId);
    
    /**
     * Trouve les interventions par type d'acte.
     */
    List<InterventionMedecin> findByActeId(Long acteId);
    
    /**
     * Calcule le total des interventions pour une consultation.
     */
    Double getTotalByConsultation(Long consultationId);
}
