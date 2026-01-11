package ma.emsi.repository.api.cabinet;

import ma.emsi.entities.modules.cabinet.CabinetMedical;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion du cabinet médical (singleton).
 */
public interface CabinetMedicalRepo extends CrudRepo<CabinetMedical, Long> {
    
    /**
     * Récupère l'instance unique du cabinet.
     */
    CabinetMedical getInstance();
    
    /**
     * Met à jour les informations du cabinet.
     */
    void updateInstance(CabinetMedical cabinet);
}
