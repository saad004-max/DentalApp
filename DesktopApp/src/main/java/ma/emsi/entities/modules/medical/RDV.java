package ma.emsi.entities.modules.medical;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;
import ma.emsi.entities.modules.enums.StatutRDV;

/**
 * Entité représentant un rendez-vous.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class RDV extends BaseEntity {

    private LocalDate date;
    private LocalTime heure;
    private String motif;
    private StatutRDV statut;
    private String noteMedecin;

    // Relation avec Patient
    // @ManyToOne
    // private Patient patient;
    private Long patientId;

    // Relation avec Médecin
    // @ManyToOne
    // private Medecin medecin;
    private Long medecinId;

    // Relation avec Consultation (1 RDV est lié à 1 Consultation)
    // @OneToOne
    // private Consultation consultation;
    private Long consultationId;

    @Override
    public String toString() {
        return """
            RDV {
                id = %d,
                date = %s,
                heure = %s,
                motif = '%s',
                statut = '%s'
            }
            """.formatted(id, date, heure, motif, statut);
    }
}
