package ma.emsi.mvc.dto.rdvDtos;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la planification d'un rendez-vous.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RDVPlanificationRequest {
    
    private Long patientId;
    private Long medecinId;
    private LocalDate date;
    private LocalTime heure;
    private String motif;
}
