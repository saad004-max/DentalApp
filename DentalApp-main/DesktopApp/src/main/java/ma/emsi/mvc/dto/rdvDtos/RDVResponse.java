package ma.emsi.mvc.dto.rdvDtos;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.StatutRDV;

/**
 * DTO pour les réponses contenant les données d'un RDV.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RDVResponse {
    
    private Long id;
    private Long patientId;
    private String patientNom;
    private Long medecinId;
    private String medecinNom;
    private LocalDate date;
    private LocalTime heure;
    private String motif;
    private StatutRDV statut;
    private String noteMedecin;
}
