package ma.emsi.mvc.controllers.medecinModule.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.StatutConsultation;
import ma.emsi.entities.modules.medical.Consultation;
import ma.emsi.mvc.controllers.medecinModule.api.MedecinConsultationController;
import ma.emsi.mvc.dto.common.ResponseWrapper;
import ma.emsi.mvc.dto.consultationDtos.ConsultationCreateRequest;
import ma.emsi.mvc.dto.consultationDtos.ConsultationResponse;
import ma.emsi.mvc.dto.consultationDtos.InterventionDto;
import ma.emsi.repository.api.medical.ConsultationRepo;
import ma.emsi.repository.api.medical.DossierMedicalRepo;
import ma.emsi.repository.api.medical.InterventionMedecinRepo;
import ma.emsi.repository.api.medical.PatientRepo;

/**
 * Implémentation du controller de gestion des consultations par le médecin.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedecinConsultationControllerImpl implements MedecinConsultationController {

    private ConsultationRepo consultationRepo;
    private DossierMedicalRepo dossierMedicalRepo;
    private PatientRepo patientRepo;
    private InterventionMedecinRepo interventionRepo;

    @Override
    public ResponseWrapper<ConsultationResponse> creerConsultation(ConsultationCreateRequest request) {
        try {
            // Validation
            if (request.getDossierMedicalId() == null || request.getMedecinId() == null) {
                return ResponseWrapper.error("Dossier médical et médecin sont obligatoires");
            }
            
            Consultation consultation = new Consultation();
            consultation.setDossierMedicalId(request.getDossierMedicalId());
            consultation.setMedecinId(request.getMedecinId());
            consultation.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
            consultation.setStatut(StatutConsultation.PLANIFIEE);
            consultation.setObservationMedecin(request.getObservationMedecin());
            consultation.setCreePar("MEDECIN"); // TODO: Get from session
            consultation.setModifiePar("MEDECIN");
            
            consultationRepo.create(consultation);
            
            return ResponseWrapper.success(toConsultationResponse(consultation), "Consultation créée avec succès");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la création de la consultation: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<ConsultationResponse> demarrerConsultation(Long consultationId) {
        try {
            Consultation consultation = consultationRepo.findById(consultationId);
            if (consultation == null) {
                return ResponseWrapper.error("Consultation non trouvée");
            }
            
            consultation.setStatut(StatutConsultation.EN_COURS);
            consultation.setModifiePar("MEDECIN");
            consultationRepo.update(consultation);
            
            return ResponseWrapper.success(toConsultationResponse(consultation), "Consultation démarrée");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors du démarrage: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<ConsultationResponse> terminerConsultation(Long consultationId) {
        try {
            Consultation consultation = consultationRepo.findById(consultationId);
            if (consultation == null) {
                return ResponseWrapper.error("Consultation non trouvée");
            }
            
            consultation.setStatut(StatutConsultation.TERMINEE);
            consultation.setModifiePar("MEDECIN");
            consultationRepo.update(consultation);
            
            return ResponseWrapper.success(toConsultationResponse(consultation), "Consultation terminée");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la fin de consultation: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<ConsultationResponse> ajouterObservation(Long consultationId, String observation) {
        try {
            Consultation consultation = consultationRepo.findById(consultationId);
            if (consultation == null) {
                return ResponseWrapper.error("Consultation non trouvée");
            }
            
            String currentObs = consultation.getObservationMedecin();
            String newObs = (currentObs != null ? currentObs + "\n" : "") + observation;
            consultation.setObservationMedecin(newObs);
            consultation.setModifiePar("MEDECIN");
            consultationRepo.update(consultation);
            
            return ResponseWrapper.success(toConsultationResponse(consultation), "Observation ajoutée");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de l'ajout de l'observation: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<ConsultationResponse> getConsultation(Long consultationId) {
        try {
            Consultation consultation = consultationRepo.findById(consultationId);
            if (consultation == null) {
                return ResponseWrapper.error("Consultation non trouvée");
            }
            return ResponseWrapper.success(toConsultationResponse(consultation));
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la récupération: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<ConsultationResponse> modifierConsultation(Long consultationId, ConsultationCreateRequest request) {
        try {
            Consultation consultation = consultationRepo.findById(consultationId);
            if (consultation == null) {
                return ResponseWrapper.error("Consultation non trouvée");
            }
            
            consultation.setDossierMedicalId(request.getDossierMedicalId());
            consultation.setMedecinId(request.getMedecinId());
            consultation.setDate(request.getDate());
            consultation.setObservationMedecin(request.getObservationMedecin());
            consultation.setModifiePar("MEDECIN");
            
            consultationRepo.update(consultation);
            
            return ResponseWrapper.success(toConsultationResponse(consultation), "Consultation modifiée");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la modification: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<Void> supprimerConsultation(Long consultationId) {
        try {
            Consultation consultation = consultationRepo.findById(consultationId);
            if (consultation == null) {
                return ResponseWrapper.error("Consultation non trouvée");
            }
            
            consultationRepo.deleteById(consultationId);
            return ResponseWrapper.success(null, "Consultation supprimée");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<List<ConsultationResponse>> getConsultationsByPatient(Long patientId) {
        try {
            List<Consultation> consultations = consultationRepo.findByPatientId(patientId);
            List<ConsultationResponse> responses = consultations.stream()
                .map(this::toConsultationResponse)
                .collect(Collectors.toList());
            return ResponseWrapper.success(responses);
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la récupération: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<List<ConsultationResponse>> getMesConsultations() {
        try {
            // TODO: Get medecin ID from session
            Long medecinId = 1L; // Placeholder
            List<Consultation> consultations = consultationRepo.findByMedecinId(medecinId);
            List<ConsultationResponse> responses = consultations.stream()
                .map(this::toConsultationResponse)
                .collect(Collectors.toList());
            return ResponseWrapper.success(responses);
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la récupération: " + e.getMessage());
        }
    }

    // Helper method
    private ConsultationResponse toConsultationResponse(Consultation consultation) {
        ConsultationResponse response = new ConsultationResponse();
        response.setId(consultation.getId());
        response.setDossierMedicalId(consultation.getDossierMedicalId());
        response.setMedecinId(consultation.getMedecinId());
        response.setDate(consultation.getDate());
        response.setStatut(consultation.getStatut());
        response.setObservationMedecin(consultation.getObservationMedecin());
        
        // Get patient info
        var dossier = dossierMedicalRepo.findById(consultation.getDossierMedicalId());
        if (dossier != null) {
            var patient = patientRepo.findById(dossier.getPatientId());
            if (patient != null) {
                response.setPatientId(patient.getId());
                response.setPatientNom(patient.getNom());
            }
        }
        
        // Get interventions
        var interventions = interventionRepo.findByConsultationId(consultation.getId());
        List<InterventionDto> interventionDtos = interventions.stream()
            .map(i -> {
                InterventionDto dto = new InterventionDto();
                dto.setId(i.getId());
                dto.setActeId(i.getActeId());
                dto.setNumDent(i.getNumDent());
                dto.setPrixDePatient(i.getPrixDePatient());
                // TODO: Get acte libelle
                dto.setActeLibelle("Acte #" + i.getActeId());
                return dto;
            })
            .collect(Collectors.toList());
        response.setInterventions(interventionDtos);
        
        // Calculate total
        Double total = interventionRepo.getTotalByConsultation(consultation.getId());
        response.setTotalInterventions(total);
        
        // TODO: Get medecin name
        response.setMedecinNom("Dr. [TODO]");
        
        return response;
    }
}
