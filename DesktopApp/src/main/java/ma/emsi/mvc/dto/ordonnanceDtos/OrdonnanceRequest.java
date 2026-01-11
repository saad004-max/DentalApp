package ma.emsi.mvc.dto.ordonnanceDtos;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création d'une ordonnance.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdonnanceRequest {
    
    private Long consultationId;
    private LocalDate date;
    private List<PrescriptionItemRequest> prescriptions;
}
