package ma.emsi.repository.api.cabinet;

import java.util.List;
import ma.emsi.entities.modules.cabinet.AgendaMensuel;
import ma.emsi.entities.modules.enums.Mois;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des agendas mensuels des médecins.
 */
public interface AgendaMensuelRepo extends CrudRepo<AgendaMensuel, Long> {
    
    /**
     * Trouve les agendas d'un médecin.
     */
    List<AgendaMensuel> findByMedecinId(Long medecinId);
    
    /**
     * Trouve l'agenda d'un médecin pour un mois donné.
     */
    AgendaMensuel findByMedecinAndMois(Long medecinId, Mois mois);
    
    /**
     * Vérifie si un agenda existe pour un médecin et un mois.
     */
    boolean existsByMedecinAndMois(Long medecinId, Mois mois);
}
