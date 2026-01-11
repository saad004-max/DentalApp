package ma.emsi.entities.modules.medical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;
import ma.emsi.entities.modules.enums.FormeMedicament;

/**
 * Entité représentant un médicament (catalogue).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Medicament extends BaseEntity {

    private String nom;
    private String laboratoire;
    private String type;
    private FormeMedicament forme;
    private Boolean remboursable;
    private Double prixUnitaire;
    private String description;

    @Override
    public String toString() {
        return """
            Medicament {
                id = %d,
                nom = '%s',
                laboratoire = '%s',
                forme = '%s',
                prixUnitaire = %.2f
            }
            """.formatted(id, nom, laboratoire, forme, prixUnitaire);
    }

    public boolean isRemboursable() {
        return remboursable;
    }
}
