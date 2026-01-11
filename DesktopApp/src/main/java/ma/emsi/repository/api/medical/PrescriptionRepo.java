package ma.emsi.repository.api.medical;

import java.util.List;
import ma.emsi.entities.modules.medical.Prescription;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des prescriptions.
 */
public interface PrescriptionRepo extends CrudRepo<Prescription, Long> {
    
    /**
     * Trouve les prescriptions d'une ordonnance.
     */
    List<Prescription> findByOrdonnanceId(Long ordonnanceId);
    
    /**
     * Trouve les prescriptions d'un médicament.
     */
    List<Prescription> findByMedicamentId(Long medicamentId);
}
