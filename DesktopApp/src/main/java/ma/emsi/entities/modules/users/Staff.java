package ma.emsi.entities.modules.users;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.modules.enums.*;

/**
 * Entité représentant un membre du personnel (staff).
 * Classe parent pour Admin, Médecin et Secrétaire.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Staff extends Utilisateur {

    private Double salaire;
    private Double prime;
    private LocalDate dateRecrutement;
    private Integer soldeConge;

    private Long cabinetId;

    public Staff(String nom,String prenom, String email, String adresse, String cin, String tel,
                 String avatar, Sexe sexe, String login, String motDePasse,
                 LocalDate lastLoginDate, LocalDate dateNaissance, Double salaire, Double prime,
                 LocalDate dateRecrutement, Integer soldeConge) {
        super(nom,prenom, email, adresse, cin, tel,avatar, sexe, login, motDePasse, lastLoginDate, dateNaissance);
        this.salaire = salaire;
        this.prime = prime;
        this.dateRecrutement = dateRecrutement;
        this.soldeConge = soldeConge;
    }

    @Override
    public String toString() {
        return """
            Staff {
                id = %d,
                nom = '%s',
                salaire = %.2f,
                dateRecrutement = %s
            }
            """.formatted(getId(), getNom(), salaire, dateRecrutement);
    }
}
