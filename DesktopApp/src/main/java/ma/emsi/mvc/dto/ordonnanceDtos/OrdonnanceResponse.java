package ma.emsi.mvc.dto.ordonnanceDtos;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les réponses contenant une ordonnance.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdonnanceResponse {
    
    private Long id;
    private Long consultationId;
    private LocalDate date;
    private String patientNom;
    private String medecinNom;
    private List<PrescriptionItemResponse> prescriptions;
}
