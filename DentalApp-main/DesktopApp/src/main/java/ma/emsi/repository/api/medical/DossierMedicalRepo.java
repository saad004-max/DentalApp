package ma.emsi.repository.api.medical;

import java.time.LocalDate;
import java.util.List;
import ma.emsi.entities.modules.medical.DossierMedical;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des dossiers médicaux.
 */
public interface DossierMedicalRepo extends CrudRepo<DossierMedical, Long> {
    
    /**
     * Trouve le dossier médical d'un patient.
     */
    DossierMedical findByPatientId(Long patientId);
    
    /**
     * Trouve les dossiers créés dans une période.
     */
    List<DossierMedical> findByDateRange(LocalDate debut, LocalDate fin);
    
    /**
     * Vérifie si un patient a déjà un dossier médical.
     */
    boolean existsByPatientId(Long patientId);
}
