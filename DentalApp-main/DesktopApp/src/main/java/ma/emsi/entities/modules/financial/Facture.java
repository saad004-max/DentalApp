package ma.emsi.entities.modules.financial;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;
import ma.emsi.entities.modules.enums.StatutFacture;

/**
 * Entité représentant une facture.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Facture extends BaseEntity {

    private Double totaleFacture;
    private Double totalePaye;
    private Double reste;
    private StatutFacture statut;
    private LocalDateTime dateFacture;

    // Relation avec Patient
    // @ManyToOne
    // private Patient patient;
    private Long patientId;

    // Relation avec Consultation
    // @ManyToOne
    // private Consultation consultation;
    private Long consultationId;

    @Override
    public String toString() {
        return """
            Facture {
                id = %d,
                totaleFacture = %.2f,
                totalePaye = %.2f,
                reste = %.2f,
                statut = '%s',
                dateFacture = %s
            }
            """.formatted(id, totaleFacture, totalePaye, reste, statut, dateFacture);
    }
}
