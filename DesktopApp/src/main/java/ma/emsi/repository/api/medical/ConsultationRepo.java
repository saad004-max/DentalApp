package ma.emsi.repository.api.medical;

import java.time.LocalDate;
import java.util.List;
import ma.emsi.entities.modules.enums.StatutConsultation;
import ma.emsi.entities.modules.medical.Consultation;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des consultations.
 */
public interface ConsultationRepo extends CrudRepo<Consultation, Long> {
    
    /**
     * Trouve les consultations d'un patient.
     */
    List<Consultation> findByPatientId(Long patientId);
    
    /**
     * Trouve les consultations d'un médecin.
     */
    List<Consultation> findByMedecinId(Long medecinId);
    
    /**
     * Trouve les consultations d'un dossier médical.
     */
    List<Consultation> findByDossierMedicalId(Long dossierId);
    
    /**
     * Trouve les consultations par statut.
     */
    List<Consultation> findByStatut(StatutConsultation statut);
    
    /**
     * Trouve les consultations dans une période.
     */
    List<Consultation> findByDateRange(LocalDate debut, LocalDate fin);
    
    /**
     * Trouve les consultations d'un médecin pour une date donnée.
     */
    List<Consultation> findByMedecinAndDate(Long medecinId, LocalDate date);
    
    /**
     * Compte les consultations par statut.
     */
    long countByStatut(StatutConsultation statut);
}
