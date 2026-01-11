package ma.emsi.repository.api.medical;

import java.time.LocalDate;
import java.util.List;
import ma.emsi.entities.modules.medical.Ordonnance;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des ordonnances.
 */
public interface OrdonnanceRepo extends CrudRepo<Ordonnance, Long> {
    
    /**
     * Trouve l'ordonnance d'une consultation.
     */
    Ordonnance findByConsultationId(Long consultationId);
    
    /**
     * Trouve les ordonnances dans une période.
     */
    List<Ordonnance> findByDateRange(LocalDate debut, LocalDate fin);
    
    /**
     * Trouve les ordonnances d'un médecin.
     */
    List<Ordonnance> findByMedecinId(Long medecinId);
}
