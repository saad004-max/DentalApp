package ma.emsi.mvc.controllers.secretaireModule.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.StatutRDV;
import ma.emsi.entities.modules.medical.RDV;
import ma.emsi.mvc.controllers.secretaireModule.api.SecretaireRDVController;
import ma.emsi.mvc.dto.common.ResponseWrapper;
import ma.emsi.mvc.dto.rdvDtos.RDVPlanificationRequest;
import ma.emsi.mvc.dto.rdvDtos.RDVResponse;
import ma.emsi.repository.api.medical.PatientRepo;
import ma.emsi.repository.api.medical.RDVRepo;
import ma.emsi.repository.api.users.MedecinRepo;

/**
 * Implémentation du controller de gestion des RDV par la secrétaire.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecretaireRDVControllerImpl implements SecretaireRDVController {

    private RDVRepo rdvRepo;
    private PatientRepo patientRepo;
    // private MedecinRepo medecinRepo; // TODO: Create this repo

    @Override
    public ResponseWrapper<RDVResponse> planifierRDV(RDVPlanificationRequest request) {
        try {
            // Validation
            if (request.getPatientId() == null || request.getMedecinId() == null) {
                return ResponseWrapper.error("Patient et médecin sont obligatoires");
            }
            
            if (request.getDate() == null || request.getHeure() == null) {
                return ResponseWrapper.error("Date et heure sont obligatoires");
            }
            
            // Vérifier conflit horaire
            boolean hasConflict = rdvRepo.hasConflict(request.getMedecinId(), request.getDate(), request.getHeure());
            if (hasConflict) {
                return ResponseWrapper.error("Conflit horaire: un RDV existe déjà à cette heure");
            }
            
            RDV rdv = new RDV();
            rdv.setPatientId(request.getPatientId());
            rdv.setMedecinId(request.getMedecinId());
            rdv.setDate(request.getDate());
            rdv.setHeure(request.getHeure());
            rdv.setMotif(request.getMotif());
            rdv.setStatut(StatutRDV.CONFIRME);
            rdv.setCreePar("SECRETAIRE"); // TODO: Get from session
            rdv.setModifiePar("SECRETAIRE");
            
            rdvRepo.create(rdv);
            
            return ResponseWrapper.success(toRDVResponse(rdv), "Rendez-vous planifié avec succès");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la planification: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<RDVResponse> modifierRDV(Long rdvId, RDVPlanificationRequest request) {
        try {
            RDV rdv = rdvRepo.findById(rdvId);
            if (rdv == null) {
                return ResponseWrapper.error("Rendez-vous non trouvé");
            }
            
            // Vérifier conflit horaire (sauf pour le RDV actuel)
            if (!rdv.getDate().equals(request.getDate()) || !rdv.getHeure().equals(request.getHeure())) {
                boolean hasConflict = rdvRepo.hasConflict(request.getMedecinId(), request.getDate(), request.getHeure());
                if (hasConflict) {
                    return ResponseWrapper.error("Conflit horaire: un RDV existe déjà à cette heure");
                }
            }
            
            rdv.setPatientId(request.getPatientId());
            rdv.setMedecinId(request.getMedecinId());
            rdv.setDate(request.getDate());
            rdv.setHeure(request.getHeure());
            rdv.setMotif(request.getMotif());
            rdv.setModifiePar("SECRETAIRE");
            
            rdvRepo.update(rdv);
            
            return ResponseWrapper.success(toRDVResponse(rdv), "Rendez-vous modifié avec succès");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la modification: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<Void> annulerRDV(Long rdvId) {
        try {
            RDV rdv = rdvRepo.findById(rdvId);
            if (rdv == null) {
                return ResponseWrapper.error("Rendez-vous non trouvé");
            }
            
            rdv.setStatut(StatutRDV.ANNULE);
            rdv.setModifiePar("SECRETAIRE");
            rdvRepo.update(rdv);
            
            return ResponseWrapper.success(null, "Rendez-vous annulé avec succès");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de l'annulation: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<RDVResponse> confirmerRDV(Long rdvId) {
        try {
            RDV rdv = rdvRepo.findById(rdvId);
            if (rdv == null) {
                return ResponseWrapper.error("Rendez-vous non trouvé");
            }
            
            rdv.setStatut(StatutRDV.CONFIRME);
            rdv.setModifiePar("SECRETAIRE");
            rdvRepo.update(rdv);
            
            return ResponseWrapper.success(toRDVResponse(rdv), "Rendez-vous confirmé avec succès");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la confirmation: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<RDVResponse> getRDV(Long rdvId) {
        try {
            RDV rdv = rdvRepo.findById(rdvId);
            if (rdv == null) {
                return ResponseWrapper.error("Rendez-vous non trouvé");
            }
            return ResponseWrapper.success(toRDVResponse(rdv));
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la récupération: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<List<RDVResponse>> getHistoriqueRDV(Long patientId) {
        try {
            List<RDV> rdvs = rdvRepo.findByPatientId(patientId);
            List<RDVResponse> responses = rdvs.stream()
                .map(this::toRDVResponse)
                .collect(Collectors.toList());
            return ResponseWrapper.success(responses);
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la récupération de l'historique: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<List<RDVResponse>> getRDVByMedecinAndDate(Long medecinId, LocalDate date) {
        try {
            List<RDV> rdvs = rdvRepo.findByMedecinAndDate(medecinId, date);
            List<RDVResponse> responses = rdvs.stream()
                .map(this::toRDVResponse)
                .collect(Collectors.toList());
            return ResponseWrapper.success(responses);
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la récupération: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<List<RDVResponse>> getFileAttente() {
        try {
            List<RDV> rdvs = rdvRepo.findEnAttente();
            List<RDVResponse> responses = rdvs.stream()
                .map(this::toRDVResponse)
                .collect(Collectors.toList());
            return ResponseWrapper.success(responses);
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la récupération de la file d'attente: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<Void> envoyerEmailRappel(Long rdvId) {
        try {
            RDV rdv = rdvRepo.findById(rdvId);
            if (rdv == null) {
                return ResponseWrapper.error("Rendez-vous non trouvé");
            }
            
            // TODO: Implement email sending logic
            // EmailService.sendRappel(rdv);
            
            return ResponseWrapper.success(null, "Email de rappel envoyé avec succès");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de l'envoi de l'email: " + e.getMessage());
        }
    }

    // Helper method to convert RDV to RDVResponse
    private RDVResponse toRDVResponse(RDV rdv) {
        RDVResponse response = new RDVResponse();
        response.setId(rdv.getId());
        response.setPatientId(rdv.getPatientId());
        response.setMedecinId(rdv.getMedecinId());
        response.setDate(rdv.getDate());
        response.setHeure(rdv.getHeure());
        response.setMotif(rdv.getMotif());
        response.setStatut(rdv.getStatut());
        response.setNoteMedecin(rdv.getNoteMedecin());
        
        // Get patient name
        var patient = patientRepo.findById(rdv.getPatientId());
        if (patient != null) {
            response.setPatientNom(patient.getNom());
        }
        
        // Get medecin name
        // TODO: Implement when MedecinRepo is available
        // var medecin = medecinRepo.findById(rdv.getMedecinId());
        // if (medecin != null) {
        //     response.setMedecinNom(medecin.getNom() + " " + medecin.getPrenom());
        // }
        response.setMedecinNom("Dr. [TODO]");
        
        return response;
    }
}
