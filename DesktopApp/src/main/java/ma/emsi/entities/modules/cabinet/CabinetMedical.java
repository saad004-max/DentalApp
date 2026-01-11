package ma.emsi.entities.modules.cabinet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;

/**
 * Entité représentant un cabinet médical dentaire.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CabinetMedical extends BaseEntity {

    private String nom;
    private String email;
    private String logo;
    private String adresse;
    private String cin;
    private String tel1;
    private String tel2;
    private String siteWeb;
    private String instagram;
    private String facebook;
    private String description;

    @Override
    public String toString() {
        return """
            CabinetMedical {
                id = %d,
                nom = '%s',
                email = '%s',
                tel1 = '%s'
            }
            """.formatted(id, nom, email, tel1);
    }
}
