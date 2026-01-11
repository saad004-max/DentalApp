package ma.emsi.entities.modules.medical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;
import ma.emsi.entities.modules.enums.NiveauRisque;

/**
 * Entité représentant un antécédent médical.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Antecedent extends BaseEntity {

    private String nom;
    private String categorie;
    private NiveauRisque niveauDeRisque;

    // Relation avec DossierMedical
    // @ManyToOne
    // private DossierMedical dossierMedical;
    private Long dossierMedicalId;

    @Override
    public String toString() {
        return """
            Antecedent {
                id = %d,
                nom = '%s',
                categorie = '%s',
                niveauDeRisque = '%s'
            }
            """.formatted(id, nom, categorie, niveauDeRisque);
    }
}
