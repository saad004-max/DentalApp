package ma.emsi.mvc.dto.financialDtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.StatutFacture;

/**
 * DTO pour les réponses contenant les données d'une facture.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactureResponse {
    
    private Long id;
    private Long patientId;
    private String patientNom;
    private Long consultationId;
    private Double totaleFacture;
    private Double totalePaye;
    private Double reste;
    private StatutFacture statut;
    private LocalDateTime dateFacture;
}
