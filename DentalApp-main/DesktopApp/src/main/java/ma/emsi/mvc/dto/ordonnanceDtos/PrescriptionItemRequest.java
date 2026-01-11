package ma.emsi.mvc.dto.ordonnanceDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour un item de prescription dans une ordonnance.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionItemRequest {
    
    private Long medicamentId;
    private Integer quantite;
    private String frequence; // Ex: "3 fois par jour", "Matin et soir"
    private Integer dureeEnJours;
}
