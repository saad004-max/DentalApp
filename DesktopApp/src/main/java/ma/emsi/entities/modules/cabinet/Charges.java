package ma.emsi.entities.modules.cabinet;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;

/**
 * Entité représentant les charges (dépenses) du cabinet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Charges extends BaseEntity {

    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime date;

    @Override
    public String toString() {
        return """
            Charges {
                id = %d,
                titre = '%s',
                montant = %.2f,
                date = %s
            }
            """.formatted(id, titre, montant, date);
    }
}
