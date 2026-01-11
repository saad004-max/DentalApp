package ma.emsi.mvc.dto.patientDtos;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.Assurance;
import ma.emsi.entities.modules.enums.Sexe;

/**
 * DTO pour les réponses contenant les données d'un patient.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    
    private Long id;
    private String nom;
    private LocalDate dateDeNaissance;
    private Integer age; // Calculé
    private Sexe sexe;
    private String adresse;
    private String telephone;
    private Assurance assurance;
    private Long dossierMedicalId;
    private Long situationFinanciereId;
    private Double creditTotal; // Depuis situation financière
}
