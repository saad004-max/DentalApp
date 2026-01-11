package ma.emsi.repository.api.cabinet;

import java.time.LocalDate;
import java.util.List;
import ma.emsi.entities.modules.cabinet.Statistiques;
import ma.emsi.entities.modules.enums.CategorieStatistique;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des statistiques.
 */
public interface StatistiquesRepo extends CrudRepo<Statistiques, Long> {
    
    /**
     * Trouve les statistiques par catégorie.
     */
    List<Statistiques> findByCategorie(CategorieStatistique categorie);
    
    /**
     * Trouve les statistiques calculées dans une période.
     */
    List<Statistiques> findByDateRange(LocalDate debut, LocalDate fin);
    
    /**
     * Trouve la dernière statistique d'une catégorie.
     */
    Statistiques findLatestByCategorie(CategorieStatistique categorie);
}
