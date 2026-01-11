package ma.emsi.repository.api.financial;

import java.time.LocalDateTime;
import java.util.List;
import ma.emsi.entities.modules.enums.StatutFacture;
import ma.emsi.entities.modules.financial.Facture;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des factures.
 */
public interface FactureRepo extends CrudRepo<Facture, Long> {
    
    /**
     * Trouve les factures d'un patient.
     */
    List<Facture> findByPatientId(Long patientId);
    
    /**
     * Trouve les factures par statut.
     */
    List<Facture> findByStatut(StatutFacture statut);
    
    /**
     * Trouve les factures dans une période.
     */
    List<Facture> findByDateRange(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Calcule le total des factures dans une période.
     */
    Double getTotalByDateRange(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Calcule le total impayé.
     */
    Double getTotalImpaye();
    
    /**
     * Trouve la facture d'une consultation.
     */
    Facture findByConsultationId(Long consultationId);
}
