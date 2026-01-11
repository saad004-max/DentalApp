package ma.emsi.entities.modules.medical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;

/**
 * Entité représentant une prescription individuelle dans une ordonnance.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Prescription extends BaseEntity {

    private Integer quantite;
    private String frequence;
    private Integer dureeEnJours;

    // Relation avec Ordonnance (1 Ordonnance a plusieurs Prescriptions)
    // @ManyToOne
    // private Ordonnance ordonnance;
    private Long ordonnanceId;

    // Relation avec Médicament (1 Prescription est liée à 1 Médicament)
    // @ManyToOne
    // private Medicament medicament;
    private Long medicamentId;

    @Override
    public String toString() {
        return """
            Prescription {
                id = %d,
                medicamentId = %d,
                quantite = %d,
                frequence = '%s',
                dureeEnJours = %d
            }
            """.formatted(id, medicamentId, quantite, frequence, dureeEnJours);
    }
}
