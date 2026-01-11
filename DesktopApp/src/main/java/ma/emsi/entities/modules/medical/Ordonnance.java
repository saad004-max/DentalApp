package ma.emsi.entities.modules.medical;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;

/**
 * Entité représentant une ordonnance médicale.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Ordonnance extends BaseEntity {

    private LocalDate date;

    // Relation avec Consultation (1 Ordonnance est liée à 1 Consultation)
    // @OneToOne
    // private Consultation consultation;
    private Long consultationId;

    // Relation avec Prescriptions (1 Ordonnance a plusieurs Prescriptions)
    // @OneToMany(mappedBy = "ordonnance")
    // private List<Prescription> prescriptions = new ArrayList<>();

    @Override
    public String toString() {
        return """
            Ordonnance {
                id = %d,
                date = %s,
                consultationId = %d
            }
            """.formatted(id, date, consultationId);
    }
}
