package ma.emsi.entities.modules.medical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;

/**
 * Entité représentant une intervention médicale effectuée lors d'une consultation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class InterventionMedecin extends BaseEntity {

    private Double prixDePatient;
    private Integer numDent;

    // Relation avec Consultation (1 Consultation a plusieurs Interventions)
    // @ManyToOne
    // private Consultation consultation;
    private Long consultationId;

    // Relation avec Acte (1 Intervention Médecin est liée à 1 Acte)
    // @ManyToOne
    // private Acte acte;
    private Long acteId;

    @Override
    public String toString() {
        return """
            InterventionMedecin {
                id = %d,
                acteId = %d,
                numDent = %d,
                prixDePatient = %.2f
            }
            """.formatted(id, acteId, numDent, prixDePatient);
    }
}
