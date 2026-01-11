package ma.emsi.repository.api.medical;

import java.util.List;
import ma.emsi.entities.modules.enums.FormeMedicament;
import ma.emsi.entities.modules.medical.Medicament;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion du catalogue des médicaments.
 */
public interface MedicamentRepo extends CrudRepo<Medicament, Long> {
    
    /**
     * Recherche de médicaments par nom (LIKE %keyword%).
     */
    List<Medicament> searchByNom(String keyword);
    
    /**
     * Trouve les médicaments d'un laboratoire.
     */
    List<Medicament> findByLaboratoire(String laboratoire);
    
    /**
     * Trouve les médicaments par forme pharmaceutique.
     */
    List<Medicament> findByForme(FormeMedicament forme);
    
    /**
     * Trouve les médicaments remboursables.
     */
    List<Medicament> findRemboursables();
    
    /**
     * Trouve les médicaments par type.
     */
    List<Medicament> findByType(String type);
}
