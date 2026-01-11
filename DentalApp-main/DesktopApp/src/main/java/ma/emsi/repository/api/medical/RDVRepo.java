package ma.emsi.repository.api.medical;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import ma.emsi.entities.modules.enums.StatutRDV;
import ma.emsi.entities.modules.medical.RDV;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des rendez-vous.
 */
public interface RDVRepo extends CrudRepo<RDV, Long> {
    
    /**
     * Trouve les RDV d'un patient.
     */
    List<RDV> findByPatientId(Long patientId);
    
    /**
     * Trouve les RDV d'un médecin.
     */
    List<RDV> findByMedecinId(Long medecinId);
    
    /**
     * Trouve les RDV par statut.
     */
    List<RDV> findByStatut(StatutRDV statut);
    
    /**
     * Trouve les RDV pour une date donnée.
     */
    List<RDV> findByDate(LocalDate date);
    
    /**
     * Trouve les RDV d'un médecin pour une date.
     */
    List<RDV> findByMedecinAndDate(Long medecinId, LocalDate date);
    
    /**
     * Trouve les RDV dans une période.
     */
    List<RDV> findByDateRange(LocalDate debut, LocalDate fin);
    
    /**
     * Vérifie s'il y a un conflit horaire pour un médecin.
     */
    boolean hasConflict(Long medecinId, LocalDate date, LocalTime heure);
    
    /**
     * Trouve les RDV en attente (file d'attente).
     */
    List<RDV> findEnAttente();
}
