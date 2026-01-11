package ma.emsi.mvc.controllers.secretaireModule.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.medical.Patient;
import main.java.ma.emsi.mvc.controllers.secretaireModule.api.SecretairePatientController;
import ma.emsi.mvc.dto.common.PageResponse;
import ma.emsi.mvc.dto.common.ResponseWrapper;
import ma.emsi.mvc.dto.patientDtos.PatientRequest;
import ma.emsi.mvc.dto.patientDtos.PatientResponse;
import ma.emsi.repository.api.financial.SituationFinanciereRepo;
import ma.emsi.repository.api.medical.DossierMedicalRepo;
import ma.emsi.repository.api.medical.PatientRepo;

/**
 * Implémentation du controller de gestion des patients par la secrétaire.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecretairePatientControllerImpl implements SecretairePatientController {

    private PatientRepo patientRepo;
    private DossierMedicalRepo dossierMedicalRepo;
    private SituationFinanciereRepo situationFinanciereRepo;

    @Override
    public ResponseWrapper<List<PatientResponse>> getAllPatients() {
        try {
            List<Patient> patients = patientRepo.findAll();
            List<PatientResponse> responses = patients.stream()
                .map(this::toPatientResponse)
                .collect(Collectors.toList());
            return ResponseWrapper.success(responses);
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la récupération des patients: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<PatientResponse> getPatient(Long id) {
        try {
            Patient patient = patientRepo.findById(id);
            if (patient == null) {
                return ResponseWrapper.error("Patient non trouvé");
            }
            return ResponseWrapper.success(toPatientResponse(patient));
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la récupération du patient: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<PatientResponse> createPatient(PatientRequest request) {
        try {
            // Validation
            if (request.getNom() == null || request.getNom().trim().isEmpty()) {
                return ResponseWrapper.error("Le nom du patient est obligatoire");
            }
            
            Patient patient = new Patient();
            patient.setNom(request.getNom());
            patient.setDateDeNaissance(request.getDateDeNaissance());
            patient.setSexe(request.getSexe());
            patient.setAdresse(request.getAdresse());
            patient.setTelephone(request.getTelephone());
            patient.setAssurance(request.getAssurance());
            patient.setCreePar("SECRETAIRE"); // TODO: Get from session
            patient.setModifiePar("SECRETAIRE");
            
            patientRepo.create(patient);
            
            // Créer automatiquement le dossier médical
            var dossier = new ma.emsi.entities.modules.medical.DossierMedical();
            dossier.setPatientId(patient.getId());
            dossier.setDateDeCréation(LocalDate.now());
            dossier.setCreePar("SECRETAIRE");
            dossier.setModifiePar("SECRETAIRE");
            dossierMedicalRepo.create(dossier);
            
            // Créer la situation financière
            var situation = new ma.emsi.entities.modules.financial.SituationFinanciere();
            situation.setPatientId(patient.getId());
            situation.setTotaleDesActes(0.0);
            situation.setTotalePaye(0.0);
            situation.setCredit(0.0);
            situation.setStatut(ma.emsi.entities.modules.enums.StatutFinancier.SOLVABLE);
            situation.setEnPromo(ma.emsi.entities.modules.enums.EnPromo.NON);
            situation.setCreePar("SECRETAIRE");
            situation.setModifiePar("SECRETAIRE");
            situationFinanciereRepo.create(situation);
            
            return ResponseWrapper.success(toPatientResponse(patient), "Patient créé avec succès");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la création du patient: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<PatientResponse> updatePatient(Long id, PatientRequest request) {
        try {
            Patient patient = patientRepo.findById(id);
            if (patient == null) {
                return ResponseWrapper.error("Patient non trouvé");
            }
            
            patient.setNom(request.getNom());
            patient.setDateDeNaissance(request.getDateDeNaissance());
            patient.setSexe(request.getSexe());
            patient.setAdresse(request.getAdresse());
            patient.setTelephone(request.getTelephone());
            patient.setAssurance(request.getAssurance());
            patient.setModifiePar("SECRETAIRE"); // TODO: Get from session
            
            patientRepo.update(patient);
            
            return ResponseWrapper.success(toPatientResponse(patient), "Patient modifié avec succès");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la modification du patient: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<Void> deletePatient(Long id) {
        try {
            Patient patient = patientRepo.findById(id);
            if (patient == null) {
                return ResponseWrapper.error("Patient non trouvé");
            }
            
            patientRepo.deleteById(id);
            return ResponseWrapper.success(null, "Patient supprimé avec succès");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la suppression du patient: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<List<PatientResponse>> searchPatients(String keyword) {
        try {
            List<Patient> patients = patientRepo.searchByNom(keyword);
            List<PatientResponse> responses = patients.stream()
                .map(this::toPatientResponse)
                .collect(Collectors.toList());
            return ResponseWrapper.success(responses);
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la recherche: " + e.getMessage());
        }
    }

    @Override
    public void Page(int page, int pageSize) {

    }

    @Override
    public ResponseWrapper<PageResponse<PatientResponse>> getPatientsPage(int page, int pageSize) {
        try {
            long total = patientRepo.count();
            int offset = page * pageSize;
            List<Patient> patients = patientRepo.findPage(pageSize, offset);
            List<PatientResponse> responses = patients.stream()
                .map(this::toPatientResponse)
                .collect(Collectors.toList());
            
            PageResponse<PatientResponse> pageResponse = new PageResponse<>(responses, page, pageSize, total);
            return ResponseWrapper.success(pageResponse);
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la pagination: " + e.getMessage());
        }
    }

    // Helper method to convert Patient to PatientResponse
    private PatientResponse toPatientResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setNom(patient.getNom());
        response.setDateDeNaissance(patient.getDateDeNaissance());
        
        // Calculate age
        if (patient.getDateDeNaissance() != null) {
            response.setAge(Period.between(patient.getDateDeNaissance(), LocalDate.now()).getYears());
        }
        
        response.setSexe(patient.getSexe());
        response.setAdresse(patient.getAdresse());
        response.setTelephone(patient.getTelephone());
        response.setAssurance(patient.getAssurance());
        
        // Get dossier medical ID
        var dossier = dossierMedicalRepo.findByPatientId(patient.getId());
        if (dossier != null) {
            response.setDossierMedicalId(dossier.getId());
        }
        
        // Get situation financiere
        var situation = situationFinanciereRepo.findByPatientId(patient.getId());
        if (situation != null) {
            response.setSituationFinanciereId(situation.getId());
            response.setCreditTotal(situation.getCredit());
        }
        
        return response;
    }
}
