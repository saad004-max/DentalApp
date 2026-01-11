package ma.emsi.repository.api.cabinet;

import java.time.LocalDateTime;
import java.util.List;
import ma.emsi.entities.modules.cabinet.Charges;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des charges (dépenses).
 */
public interface ChargesRepo extends CrudRepo<Charges, Long> {
    
    /**
     * Trouve les charges dans une période.
     */
    List<Charges> findByDateRange(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Calcule le total des charges dans une période.
     */
    Double getTotalByDateRange(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Recherche de charges par titre.
     */
    List<Charges> searchByTitre(String keyword);
}
