package ma.emsi.entities.modules.cabinet;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;
import ma.emsi.entities.modules.enums.PrioriteNotification;
import ma.emsi.entities.modules.enums.TitreNotification;
import ma.emsi.entities.modules.enums.TypeNotification;

/**
 * Entité représentant une notification système.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Notification extends BaseEntity {

    private TitreNotification titre;
    private String message;
    private LocalDate date;
    private LocalTime time;
    private TypeNotification type;
    private PrioriteNotification priorite;

    @Override
    public String toString() {
        return """
            Notification {
                id = %d,
                titre = '%s',
                type = '%s',
                priorite = '%s',
                date = %s
            }
            """.formatted(id, titre, type, priorite, date);
    }
}
