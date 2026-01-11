package ma.emsi.entities.modules.users;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.modules.enums.Sexe;

/**
 * Entité représentant un médecin dentiste.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Medecin extends Staff {

    private String specialite;
    
    // Relation avec AgendaMensuel
    // @OneToMany(mappedBy = "medecin")
    // private List<AgendaMensuel> agendasMensuels = new ArrayList<>();

    public Medecin(String nom,String prenom, String email, String adresse, String cin, String tel,
                   String avatar, Sexe sexe, String login, String motDePasse,
                   LocalDate lastLoginDate, LocalDate dateNaissance, Double salaire, Double prime,
                   LocalDate dateRecrutement, Integer soldeCongé, String specialite
                   ) {
        super( nom, prenom,email, adresse, cin, tel, avatar,sexe, login, motDePasse, lastLoginDate,
                dateNaissance, salaire, prime, dateRecrutement, soldeCongé);
        this.specialite = specialite;
    }

    @Override
    public String toString() {
        return """
            Medecin {
                id = %d,
                nom = '%s',
                specialite = '%s'
            }
            """.formatted(getId(), getNom(), specialite);
    }
}
