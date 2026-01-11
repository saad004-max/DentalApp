package ma.emsi.entities.modules.medical;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;

/**
 * Entité représentant un certificat médical.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Certificat extends BaseEntity {

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer duree;
    private String noteMedecin;

    // Relation avec Patient
    // @ManyToOne
    // private Patient patient;
    private Long patientId;

    // Relation avec Médecin
    // @ManyToOne
    // private Medecin medecin;
    private Long medecinId;

    @Override
    public String toString() {
        return """
            Certificat {
                id = %d,
                dateDebut = %s,
                dateFin = %s,
                duree = %d jours
            }
            """.formatted(id, dateDebut, dateFin, duree);
    }
}
