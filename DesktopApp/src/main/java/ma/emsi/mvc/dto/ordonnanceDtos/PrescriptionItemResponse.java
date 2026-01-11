package ma.emsi.mvc.dto.ordonnanceDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour un item de prescription dans une réponse d'ordonnance.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionItemResponse {
    
    private Long id;
    private Long medicamentId;
    private String medicamentNom;
    private String laboratoire;
    private Integer quantite;
    private String frequence;
    private Integer dureeEnJours;
}
