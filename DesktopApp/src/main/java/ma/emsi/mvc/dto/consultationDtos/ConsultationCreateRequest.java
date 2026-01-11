package ma.emsi.mvc.dto.consultationDtos;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création d'une consultation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationCreateRequest {
    
    private Long dossierMedicalId;
    private Long medecinId;
    private LocalDate date;
    private String observationMedecin;
    private Long rdvId; // Optionnel, si la consultation est liée à un RDV
}
