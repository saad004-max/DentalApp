package ma.emsi.entities.modules.financial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;
import ma.emsi.entities.modules.enums.EnPromo;
import ma.emsi.entities.modules.enums.StatutFinancier;

/**
 * Entité représentant la situation financière d'un patient.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class SituationFinanciere extends BaseEntity {

    private Double totaleDesActes;
    private Double totalePaye;
    private Double credit;
    private StatutFinancier statut;
    private EnPromo enPromo;

    // Relation avec Patient (1 Situation Financière est liée à 1 Patient)
    // @OneToOne
    // private Patient patient;
    private Long patientId;

    @Override
    public String toString() {
        return """
            SituationFinanciere {
                id = %d,
                patientId = %d,
                totaleDesActes = %.2f,
                totalePaye = %.2f,
                credit = %.2f,
                statut = '%s'
            }
            """.formatted(id, patientId, totaleDesActes, totalePaye, credit, statut);
    }
}
