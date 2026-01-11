package ma.emsi.entities.modules.cabinet;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Classe représentant un jour dans l'agenda.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Jour {

    private LocalDate date;
    private Boolean disponible;
    private String motif;

    @Override
    public String toString() {
        return """
            Jour {
                date = %s,
                disponible = %s,
                motif = '%s'
            }
            """.formatted(date, disponible, motif);
    }
}
