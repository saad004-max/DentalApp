package ma.emsi.repository.api.medical;

import java.util.List;
import ma.emsi.entities.modules.medical.Acte;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion du catalogue des actes médicaux.
 */
public interface ActeRepo extends CrudRepo<Acte, Long> {
    
    /**
     * Trouve les actes par catégorie.
     */
    List<Acte> findByCategorie(String categorie);
    
    /**
     * Recherche d'actes par libellé (LIKE %keyword%).
     */
    List<Acte> searchByLibelle(String keyword);
    
    /**
     * Récupère toutes les catégories distinctes.
     */
    List<String> findAllCategories();
    
    /**
     * Trouve les actes dans une fourchette de prix.
     */
    List<Acte> findByPrixRange(Double minPrix, Double maxPrix);
}
