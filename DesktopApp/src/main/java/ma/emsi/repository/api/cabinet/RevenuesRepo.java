package ma.emsi.repository.api.cabinet;

import java.time.LocalDateTime;
import java.util.List;
import ma.emsi.entities.modules.cabinet.Revenues;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des revenus.
 */
public interface RevenuesRepo extends CrudRepo<Revenues, Long> {
    
    /**
     * Trouve les revenus dans une période.
     */
    List<Revenues> findByDateRange(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Calcule le total des revenus dans une période.
     */
    Double getTotalByDateRange(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Recherche de revenus par titre.
     */
    List<Revenues> searchByTitre(String keyword);
}
