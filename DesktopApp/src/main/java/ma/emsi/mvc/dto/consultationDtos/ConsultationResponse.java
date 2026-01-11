package ma.emsi.mvc.dto.consultationDtos;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.StatutConsultation;

/**
 * DTO pour les réponses contenant les données d'une consultation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationResponse {
    
    private Long id;
    private Long dossierMedicalId;
    private Long patientId;
    private String patientNom;
    private Long medecinId;
    private String medecinNom;
    private LocalDate date;
    private StatutConsultation statut;
    private String observationMedecin;
    private List<InterventionDto> interventions;
    private Double totalInterventions;
}
