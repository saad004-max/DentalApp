package ma.emsi.mvc.dto.financialDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour l'enregistrement d'un paiement.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementRequest {
    
    private Long factureId;
    private Double montant;
    private String modePaiement; // ESPECES, CARTE, CHEQUE, VIREMENT
    private String reference; // Numéro de chèque, référence virement, etc.
}
