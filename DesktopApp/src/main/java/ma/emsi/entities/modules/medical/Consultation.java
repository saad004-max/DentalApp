package ma.emsi.entities.modules.medical;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;
import ma.emsi.entities.modules.enums.StatutConsultation;

/**
 * Entité représentant une consultation médicale.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Consultation extends BaseEntity {

    private LocalDate date;
    private StatutConsultation statut;
    private String observationMedecin;

    // Relation avec DossierMedical (1 Consultation est liée à 1 Dossier Médical)
    // @ManyToOne
    // private DossierMedical dossierMedical;
    private Long dossierMedicalId;

    // Relation avec Médecin
    // @ManyToOne
    // private Medecin medecin;
    private Long medecinId;

    // Relation avec InterventionMedecin (1 Consultation a plusieurs Interventions)
    // @OneToMany(mappedBy = "consultation")
    // private List<InterventionMedecin> interventions = new ArrayList<>();

    // Relation avec RDV (1 RDV est lié à 1 Consultation)
    // @OneToOne(mappedBy = "consultation")
    // private RDV rdv;
    private Long rdvId;

    // Relation avec Ordonnance (1 Ordonnance est liée à 1 Consultation)
    // @OneToOne(mappedBy = "consultation")
    // private Ordonnance ordonnance;
    private Long ordonnanceId;

    @Override
    public String toString() {
        return """
            Consultation {
                id = %d,
                date = %s,
                statut = '%s',
                medecinId = %d
            }
            """.formatted(id, date, statut, medecinId);
    }
}
