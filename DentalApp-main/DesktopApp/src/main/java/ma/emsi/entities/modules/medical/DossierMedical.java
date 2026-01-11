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
 * Entité représentant un dossier médical d'un patient.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DossierMedical extends BaseEntity {

    private LocalDate dateDeCréation;

    // Relation avec Patient
    // @OneToOne
    // private Patient patient;
    private Long patientId;

    // Relation avec Consultations
    // @OneToMany(mappedBy = "dossierMedical")
    // private List<Consultation> consultations = new ArrayList<>();

    // Relation avec Antécédents
    // @OneToMany(mappedBy = "dossierMedical")
    // private List<Antecedent> antecedents = new ArrayList<>();

    @Override
    public String toString() {
        return """
            DossierMedical {
                id = %d,
                patientId = %d,
                dateDeCréation = %s
            }
            """.formatted(id, patientId, dateDeCréation);
    }
}
