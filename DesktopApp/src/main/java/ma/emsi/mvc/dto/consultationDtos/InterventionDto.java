package ma.emsi.mvc.dto.consultationDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour une intervention médicale.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterventionDto {
    
    private Long id;
    private Long acteId;
    private String acteLibelle;
    private Integer numDent;
    private Double prixDePatient;
}
