package ma.emsi.entities.modules.cabinet;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;
import ma.emsi.entities.modules.enums.Mois;

/**
 * Entité représentant l'agenda mensuel d'un médecin.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AgendaMensuel extends BaseEntity {

    private Mois mois;
    private List<Jour> joursNonDisponible = new ArrayList<>();

    // Relation avec Médecin (commentée pour usage futur avec JPA)
    // @ManyToOne
    // private Medecin medecin;
    private Long medecinId;

    @Override
    public String toString() {
        return """
            AgendaMensuel {
                id = %d,
                mois = '%s',
                joursNonDisponible = %d jours
            }
            """.formatted(id, mois, joursNonDisponible != null ? joursNonDisponible.size() : 0);
    }
}
