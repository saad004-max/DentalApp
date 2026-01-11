package ma.emsi.repository.api.medical;

import java.time.LocalDate;
import java.util.List;
import ma.emsi.entities.modules.medical.Certificat;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des certificats médicaux.
 */
public interface CertificatRepo extends CrudRepo<Certificat, Long> {
    
    /**
     * Trouve les certificats d'un patient.
     */
    List<Certificat> findByPatientId(Long patientId);
    
    /**
     * Trouve les certificats émis par un médecin.
     */
    List<Certificat> findByMedecinId(Long medecinId);
    
    /**
     * Trouve les certificats dans une période.
     */
    List<Certificat> findByDateRange(LocalDate debut, LocalDate fin);
}
