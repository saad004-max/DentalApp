package ma.emsi.mvc.dto.financialDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.EnPromo;
import ma.emsi.entities.modules.enums.StatutFinancier;

/**
 * DTO pour les réponses contenant une situation financière.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SituationFinanciereResponse {
    
    private Long id;
    private Long patientId;
    private String patientNom;
    private Double totaleDesActes;
    private Double totalePaye;
    private Double credit;
    private StatutFinancier statut;
    private EnPromo enPromo;
    private Double tauxPaiement; // Calculated: (totalePaye / totaleDesActes) * 100
}
