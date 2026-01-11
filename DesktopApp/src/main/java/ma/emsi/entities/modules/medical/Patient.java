package ma.emsi.entities.modules.medical;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;
import ma.emsi.entities.modules.enums.Assurance;
import ma.emsi.entities.modules.enums.Sexe;

/**
 * Entité représentant un patient.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Patient extends BaseEntity {

    private String nom;
    private LocalDate dateDeNaissance;
    private Sexe sexe;
    private String adresse;
    private String telephone;
    private Assurance assurance;

    // Relation avec SituationFinanciere (1 Patient a 1 Situation Financière)
    // @OneToOne(mappedBy = "patient")
    // private SituationFinanciere situationFinanciere;
    private Long situationFinanciereId;

    // Relation avec DossierMedical
    // @OneToOne(mappedBy = "patient")
    // private DossierMedical dossierMedical;
    private Long dossierMedicalId;

    @Override
    public String toString() {
        return """
            Patient {
                id = %d,
                nom = '%s',
                dateDeNaissance = %s,
                sexe = '%s',
                telephone = '%s'
            }
            """.formatted(id, nom, dateDeNaissance, sexe, telephone);
    }
}
