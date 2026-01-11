package ma.emsi.entities.modules.cabinet;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;
import ma.emsi.entities.modules.enums.CategorieStatistique;

/**
 * Entité représentant des statistiques calculées.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Statistiques extends BaseEntity {

    private String nom;
    private CategorieStatistique categorie;
    private Double chiffre;
    private LocalDate dateCalcul;

    @Override
    public String toString() {
        return """
            Statistiques {
                id = %d,
                nom = '%s',
                categorie = '%s',
                chiffre = %.2f,
                dateCalcul = %s
            }
            """.formatted(id, nom, categorie, chiffre, dateCalcul);
    }
}
