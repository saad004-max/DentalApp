package ma.emsi.entities.modules.medical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;

/**
 * Entité représentant un acte médical (catalogue des procédures).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Acte extends BaseEntity {

    private String libelle;
    private String categorie;
    private Double prixDeBase;

    @Override
    public String toString() {
        return """
            Acte {
                id = %d,
                libelle = '%s',
                categorie = '%s',
                prixDeBase = %.2f
            }
            """.formatted(id, libelle, categorie, prixDeBase);
    }
}
