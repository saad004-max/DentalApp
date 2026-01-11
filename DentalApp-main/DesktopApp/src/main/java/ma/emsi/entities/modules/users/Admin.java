package ma.emsi.entities.modules.users;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.modules.enums.Sexe;

/**
 * Entité représentant un administrateur du système.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Admin extends Staff {

    public Admin(String nom, String prenom, String email, String adresse, String cin, String tel,
                 String avatar, Sexe sexe, String login, String motDePasse,
                 LocalDate lastLoginDate, LocalDate dateNaissance, Double salaire, Double prime,
                 LocalDate dateRecrutement, Integer soldeConge) {
        super( nom, prenom,email, adresse, cin, tel, avatar,sexe, login, motDePasse, lastLoginDate,
                dateNaissance, salaire, prime, dateRecrutement, soldeConge);
    }

    @Override
    public String toString() {
        return """
            Admin {
                id = %d,
                nom = '%s',
                email = '%s'
            }
            """.formatted(getId(), getNom(), getEmail());
    }
}
