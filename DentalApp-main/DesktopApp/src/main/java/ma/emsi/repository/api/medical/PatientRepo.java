package ma.emsi.repository.api.medical;

import java.time.LocalDate;
import java.util.List;
import ma.emsi.entities.modules.enums.Assurance;
import ma.emsi.entities.modules.medical.Patient;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des patients.
 */
public interface PatientRepo extends CrudRepo<Patient, Long> {
    
    /**
     * Recherche des patients par nom (LIKE %keyword%).
     */
    List<Patient> searchByNom(String keyword);
    
    /**
     * Trouve les patients par type d'assurance.
     */
    List<Patient> findByAssurance(Assurance assurance);
    
    /**
     * Trouve un patient par son CIN.
     */
    Patient findByCin(String cin);
    
    /**
     * Vérifie si un patient existe avec ce CIN.
     */
    boolean existsByCin(String cin);
    
    /**
     * Pagination des patients.
     */
    List<Patient> findPage(int limit, int offset);
    
    /**
     * Compte total des patients.
     */
    long count();
}
