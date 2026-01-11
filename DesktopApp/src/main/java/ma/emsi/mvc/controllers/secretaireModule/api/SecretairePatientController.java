package ma.emsi.mvc.controllers.secretaireModule.api;

import java.util.List;
import ma.emsi.mvc.dto.common.PageResponse;
import ma.emsi.mvc.dto.common.ResponseWrapper;
import ma.emsi.mvc.dto.patientDtos.PatientRequest;
import ma.emsi.mvc.dto.patientDtos.PatientResponse;

/**
 * Controller pour la gestion des patients par la secrétaire.
 * Use Cases: [139-153]
 */
public interface SecretairePatientController {
    
    /**
     * Récupère tous les patients.
     */
    ResponseWrapper<List<PatientResponse>> getAllPatients();
    
    /**
     * Récupère un patient par ID.
     */
    ResponseWrapper<PatientResponse> getPatient(Long id);
    
    /**
     * Crée un nouveau patient.
     */
    ResponseWrapper<PatientResponse> createPatient(PatientRequest request);
    
    /**
     * Met à jour un patient.
     */
    ResponseWrapper<PatientResponse> updatePatient(Long id, PatientRequest request);
    
    /**
     * Supprime un patient.
     */
    ResponseWrapper<Void> deletePatient(Long id);
    
    /**
     * Recherche de patients par nom.
     */
    ResponseWrapper<List<PatientResponse>> searchPatients(String keyword);
    
    /**
     * Pagination des patients.
     */
    ResponseWrapper<PageResponse<PatientResponse>> getPatients Page(int page, int pageSize);
}
