package ma.emsi.mvc.dto.patientDtos;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.Assurance;
import ma.emsi.entities.modules.enums.Sexe;

/**
 * DTO pour les requêtes de création/modification de patient.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest {
    
    private String nom;
    private LocalDate dateDeNaissance;
    private Sexe sexe;
    private String adresse;
    private String telephone;
    private Assurance assurance;
}
