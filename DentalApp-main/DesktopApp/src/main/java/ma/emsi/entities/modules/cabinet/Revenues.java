package ma.emsi.entities.modules.cabinet;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;

/**
 * Entité représentant les revenus du cabinet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Revenues extends BaseEntity {

    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime date;

    @Override
    public String toString() {
        return """
            Revenues {
                id = %d,
                titre = '%s',
                montant = %.2f,
                date = %s
            }
            """.formatted(id, titre, montant, date);
    }
}
