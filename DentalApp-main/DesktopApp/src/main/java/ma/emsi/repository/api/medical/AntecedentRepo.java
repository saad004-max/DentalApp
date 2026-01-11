package ma.emsi.repository.api.medical;

import java.util.List;
import ma.emsi.entities.modules.enums.NiveauRisque;
import ma.emsi.entities.modules.medical.Antecedent;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des antécédents médicaux.
 */
public interface AntecedentRepo extends CrudRepo<Antecedent, Long> {
    
    /**
     * Trouve tous les antécédents d'un dossier médical.
     */
    List<Antecedent> findByDossierMedicalId(Long dossierId);
    
    /**
     * Trouve les antécédents par niveau de risque.
     */
    List<Antecedent> findByNiveauRisque(NiveauRisque niveau);
    
    /**
     * Trouve les antécédents par catégorie.
     */
    List<Antecedent> findByCategorie(String categorie);
    
    /**
     * Recherche d'antécédents par nom.
     */
    List<Antecedent> searchByNom(String keyword);
}
