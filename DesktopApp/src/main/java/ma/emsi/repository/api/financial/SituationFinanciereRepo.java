package ma.emsi.repository.api.financial;

import java.util.List;
import ma.emsi.entities.modules.enums.StatutFinancier;
import ma.emsi.entities.modules.financial.SituationFinanciere;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des situations financières des patients.
 */
public interface SituationFinanciereRepo extends CrudRepo<SituationFinanciere, Long> {
    
    /**
     * Trouve la situation financière d'un patient.
     */
    SituationFinanciere findByPatientId(Long patientId);
    
    /**
     * Trouve les situations par statut.
     */
    List<SituationFinanciere> findByStatut(StatutFinancier statut);
    
    /**
     * Trouve les patients en promotion.
     */
    List<SituationFinanciere> findEnPromo();
    
    /**
     * Calcule le total du crédit de tous les patients.
     */
    Double getTotalCredit();
    
    /**
     * Trouve les patients avec crédit supérieur à un montant.
     */
    List<SituationFinanciere> findByCreditGreaterThan(Double montant);
}
