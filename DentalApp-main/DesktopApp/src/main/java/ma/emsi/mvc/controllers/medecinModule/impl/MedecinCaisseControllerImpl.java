package ma.emsi.mvc.controllers.medecinModule.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.StatutFacture;
import ma.emsi.entities.modules.financial.Facture;
import ma.emsi.entities.modules.financial.SituationFinanciere;
import ma.emsi.mvc.controllers.medecinModule.api.MedecinCaisseController;
import ma.emsi.mvc.controllers.medecinModule.api.StatistiquesCaisseDto;
import ma.emsi.mvc.dto.common.ResponseWrapper;
import ma.emsi.mvc.dto.financialDtos.FactureResponse;
import ma.emsi.mvc.dto.financialDtos.PaiementRequest;
import ma.emsi.repository.api.financial.FactureRepo;
import ma.emsi.repository.api.financial.SituationFinanciereRepo;
import ma.emsi.repository.api.medical.ConsultationRepo;
import ma.emsi.repository.api.medical.DossierMedicalRepo;
import ma.emsi.repository.api.medical.InterventionMedecinRepo;
import ma.emsi.repository.api.medical.PatientRepo;

/**
 * Implémentation du controller de gestion de la caisse par le médecin.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedecinCaisseControllerImpl implements MedecinCaisseController {

    private FactureRepo factureRepo;
    private ConsultationRepo consultationRepo;
    private InterventionMedecinRepo interventionRepo;
    private SituationFinanciereRepo situationRepo;
    private DossierMedicalRepo dossierRepo;
    private PatientRepo patientRepo;

    @Override
    public ResponseWrapper<FactureResponse> genererFacture(Long consultationId) {
        try {
            var consultation = consultationRepo.findById(consultationId);
            if (consultation == null) {
                return ResponseWrapper.error("Consultation non trouvée");
            }
            
            // Check if facture already exists
            var existingFacture = factureRepo.findByConsultationId(consultationId);
            if (existingFacture != null) {
                return ResponseWrapper.error("Une facture existe déjà pour cette consultation");
            }
            
            // Calculate total from interventions
            Double total = interventionRepo.getTotalByConsultation(consultationId);
            if (total == null || total == 0.0) {
                return ResponseWrapper.error("Aucune intervention trouvée pour cette consultation");
            }
            
            // Get patient ID
            var dossier = dossierRepo.findById(consultation.getDossierMedicalId());
            if (dossier == null) {
                return ResponseWrapper.error("Dossier médical non trouvé");
            }
            
            // Create facture
            Facture facture = new Facture();
            facture.setPatientId(dossier.getPatientId());
            facture.setConsultationId(consultationId);
            facture.setTotaleFacture(total);
            facture.setTotalePaye(0.0);
            facture.setReste(total);
            facture.setStatut(StatutFacture.IMPAYEE);
            facture.setDateFacture(LocalDateTime.now());
            facture.setCreePar("MEDECIN");
            facture.setModifiePar("MEDECIN");
            
            factureRepo.create(facture);
            
            // Update situation financière
            updateSituationFinanciere(dossier.getPatientId(), total, 0.0);
            
            return ResponseWrapper.success(toFactureResponse(facture), "Facture générée avec succès");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la génération de la facture: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<FactureResponse> appliquerRemise(Long factureId, Double pourcentageRemise) {
        try {
            var facture = factureRepo.findById(factureId);
            if (facture == null) {
                return ResponseWrapper.error("Facture non trouvée");
            }
            
            if (pourcentageRemise < 0 || pourcentageRemise > 100) {
                return ResponseWrapper.error("Pourcentage de remise invalide (0-100)");
            }
            
            Double montantRemise = facture.getTotaleFacture() * (pourcentageRemise / 100.0);
            Double nouveauTotal = facture.getTotaleFacture() - montantRemise;
            Double nouveauReste = nouveauTotal - facture.getTotalePaye();
            
            facture.setTotaleFacture(nouveauTotal);
            facture.setReste(nouveauReste);
            facture.setModifiePar("MEDECIN");
            
            factureRepo.update(facture);
            
            // Update situation financière
            updateSituationFinanciereAfterRemise(facture.getPatientId(), montantRemise);
            
            return ResponseWrapper.success(toFactureResponse(facture), 
                String.format("Remise de %.2f%% appliquée (%.2f DH)", pourcentageRemise, montantRemise));
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de l'application de la remise: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<FactureResponse> annulerRemise(Long factureId) {
        try {
            // TODO: Store original amount to restore
            return ResponseWrapper.error("Fonctionnalité non implémentée - nécessite stockage du montant original");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<List<FactureResponse>> getAllFactures() {
        try {
            List<Facture> factures = factureRepo.findAll();
            List<FactureResponse> responses = factures.stream()
                .map(this::toFactureResponse)
                .collect(Collectors.toList());
            return ResponseWrapper.success(responses);
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la récupération des factures: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<FactureResponse> creerFacture(FactureResponse factureData) {
        try {
            Facture facture = new Facture();
            facture.setPatientId(factureData.getPatientId());
            facture.setConsultationId(factureData.getConsultationId());
            facture.setTotaleFacture(factureData.getTotaleFacture());
            facture.setTotalePaye(factureData.getTotalePaye());
            facture.setReste(factureData.getReste());
            facture.setStatut(factureData.getStatut());
            facture.setDateFacture(factureData.getDateFacture());
            facture.setCreePar("MEDECIN");
            facture.setModifiePar("MEDECIN");
            
            factureRepo.create(facture);
            
            return ResponseWrapper.success(toFactureResponse(facture), "Facture créée");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de la création: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<FactureResponse> getFacture(Long factureId) {
        try {
            var facture = factureRepo.findById(factureId);
            if (facture == null) {
                return ResponseWrapper.error("Facture non trouvée");
            }
            return ResponseWrapper.success(toFactureResponse(facture));
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<FactureResponse> modifierFacture(Long factureId, FactureResponse factureData) {
        try {
            var facture = factureRepo.findById(factureId);
            if (facture == null) {
                return ResponseWrapper.error("Facture non trouvée");
            }
            
            facture.setTotaleFacture(factureData.getTotaleFacture());
            facture.setTotalePaye(factureData.getTotalePaye());
            facture.setReste(factureData.getReste());
            facture.setStatut(factureData.getStatut());
            facture.setModifiePar("MEDECIN");
            
            factureRepo.update(facture);
            
            return ResponseWrapper.success(toFactureResponse(facture), "Facture modifiée");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<byte[]> imprimerFacture(Long factureId) {
        try {
            // TODO: Implement PDF generation
            return ResponseWrapper.error("Génération PDF non implémentée");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<Void> supprimerFacture(Long factureId) {
        try {
            var facture = factureRepo.findById(factureId);
            if (facture == null) {
                return ResponseWrapper.error("Facture non trouvée");
            }
            
            factureRepo.deleteById(factureId);
            return ResponseWrapper.success(null, "Facture supprimée");
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<StatistiquesCaisseDto> getStatistiquesCaisse(LocalDateTime debut, LocalDateTime fin) {
        try {
            List<Facture> factures = factureRepo.findByDateRange(debut, fin);

            StatistiquesCaisseDto stats = new StatistiquesCaisseDto();
            stats.nombreFactures = factures.size();
            stats.totalFactures = factures.stream().mapToDouble(Facture::getTotaleFacture).sum();
            stats.totalPaye = factures.stream().mapToDouble(Facture::getTotalePaye).sum();
            stats.totalImpaye = factureRepo.getTotalImpaye();

            return ResponseWrapper.success(stats);
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors du calcul des statistiques: " + e.getMessage());
        }
    }

    @Override
    public ResponseWrapper<FactureResponse> enregistrerPaiement(PaiementRequest request) {
        try {
            var facture = factureRepo.findById(request.getFactureId());
            if (facture == null) {
                return ResponseWrapper.error("Facture non trouvée");
            }
            
            if (request.getMontant() <= 0) {
                return ResponseWrapper.error("Montant invalide");
            }
            
            if (request.getMontant() > facture.getReste()) {
                return ResponseWrapper.error("Le montant dépasse le reste à payer");
            }
            
            // Update facture
            facture.setTotalePaye(facture.getTotalePaye() + request.getMontant());
            facture.setReste(facture.getReste() - request.getMontant());
            
            // Update status
            if (facture.getReste() == 0) {
                facture.setStatut(StatutFacture.PAYEE);
            } else {
                facture.setStatut(StatutFacture.PARTIELLE);
            }
            
            facture.setModifiePar("MEDECIN");
            factureRepo.update(facture);
            
            // Update situation financière
            updateSituationFinanciereAfterPaiement(facture.getPatientId(), request.getMontant());
            
            return ResponseWrapper.success(toFactureResponse(facture), 
                String.format("Paiement de %.2f DH enregistré", request.getMontant()));
        } catch (Exception e) {
            return ResponseWrapper.error("Erreur lors de l'enregistrement du paiement: " + e.getMessage());
        }
    }

    // Helper methods
    private FactureResponse toFactureResponse(Facture facture) {
        FactureResponse response = new FactureResponse();
        response.setId(facture.getId());
        response.setPatientId(facture.getPatientId());
        response.setConsultationId(facture.getConsultationId());
        response.setTotaleFacture(facture.getTotaleFacture());
        response.setTotalePaye(facture.getTotalePaye());
        response.setReste(facture.getReste());
        response.setStatut(facture.getStatut());
        response.setDateFacture(facture.getDateFacture());
        
        // Get patient name
        var patient = patientRepo.findById(facture.getPatientId());
        if (patient != null) {
            response.setPatientNom(patient.getNom());
        }
        
        return response;
    }

    private void updateSituationFinanciere(Long patientId, Double montantActes, Double montantPaye) {
        var situation = situationRepo.findByPatientId(patientId);
        if (situation != null) {
            situation.setTotaleDesActes(situation.getTotaleDesActes() + montantActes);
            situation.setTotalePaye(situation.getTotalePaye() + montantPaye);
            situation.setCredit(situation.getTotaleDesActes() - situation.getTotalePaye());
            situation.setModifiePar("MEDECIN");
            situationRepo.update(situation);
        }
    }

    private void updateSituationFinanciereAfterRemise(Long patientId, Double montantRemise) {
        var situation = situationRepo.findByPatientId(patientId);
        if (situation != null) {
            situation.setTotaleDesActes(situation.getTotaleDesActes() - montantRemise);
            situation.setCredit(situation.getTotaleDesActes() - situation.getTotalePaye());
            situation.setModifiePar("MEDECIN");
            situationRepo.update(situation);
        }
    }

    private void updateSituationFinanciereAfterPaiement(Long patientId, Double montantPaye) {
        var situation = situationRepo.findByPatientId(patientId);
        if (situation != null) {
            situation.setTotalePaye(situation.getTotalePaye() + montantPaye);
            situation.setCredit(situation.getTotaleDesActes() - situation.getTotalePaye());
            situation.setModifiePar("MEDECIN");
            situationRepo.update(situation);
        }
    }
}
