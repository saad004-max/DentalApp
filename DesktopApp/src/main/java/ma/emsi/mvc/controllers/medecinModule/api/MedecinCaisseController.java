package ma.emsi.mvc.controllers.medecinModule.api;

import java.time.LocalDateTime;
import java.util.List;
import ma.emsi.mvc.dto.common.ResponseWrapper;
import ma.emsi.mvc.dto.financialDtos.FactureResponse;
import ma.emsi.mvc.dto.financialDtos.PaiementRequest;

/**
 * Controller pour la gestion de la caisse et des factures par le médecin.
 * Use Cases: [81, 96, 106, 108, 127-135]
 */
public interface MedecinCaisseController {
    
    /**
     * Génère une facture pour une consultation.
     */
    ResponseWrapper<FactureResponse> genererFacture(Long consultationId);
    
    /**
     * Applique une remise sur une facture.
     */
    ResponseWrapper<FactureResponse> appliquerRemise(Long factureId, Double pourcentageRemise);
    
    /**
     * Annule une remise.
     */
    ResponseWrapper<FactureResponse> annulerRemise(Long factureId);
    
    /**
     * Liste toutes les factures.
     */
    ResponseWrapper<List<FactureResponse>> getAllFactures();
    
    /**
     * Crée une facture manuelle.
     */
    ResponseWrapper<FactureResponse> creerFacture(FactureResponse factureData);
    
    /**
     * Consulte une facture.
     */
    ResponseWrapper<FactureResponse> getFacture(Long factureId);
    
    /**
     * Modifie une facture.
     */
    ResponseWrapper<FactureResponse> modifierFacture(Long factureId, FactureResponse factureData);
    
    /**
     * Imprime une facture (génère PDF).
     */
    ResponseWrapper<byte[]> imprimerFacture(Long factureId);
    
    /**
     * Supprime une facture.
     */
    ResponseWrapper<Void> supprimerFacture(Long factureId);
    
    /**
     * Consulte les statistiques de la caisse.
     */
    ResponseWrapper<StatistiquesCaisseDto> getStatistiquesCaisse(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Enregistre un paiement.
     */
    ResponseWrapper<FactureResponse> enregistrerPaiement(PaiementRequest request);
}

/**
 * DTO pour les statistiques de caisse.
 */
class StatistiquesCaisseDto {
    public Double totalFactures;
    public Double totalPaye;
    public Double totalImpaye;
    public Integer nombreFactures;
}
