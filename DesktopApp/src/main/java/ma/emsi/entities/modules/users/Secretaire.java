package ma.emsi.entities.modules.users;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.modules.enums.*;

/**
 * Entité représentant une secrétaire du cabinet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Secretaire extends Staff {

    private String numCNSS;
    private Double commission;

    public Secretaire(String nom,String prenom, String email, String adresse, String cin, String tel,
                      String avatar, Sexe sexe, String login, String motDePasse,
                      LocalDate lastLoginDate, LocalDate dateNaissance, Double salaire, Double prime,
                      LocalDate dateRecrutement, Integer soldeCongé, String numCNSS, Double commission) {
        super( nom, prenom,email, adresse, cin, tel, avatar,sexe, login, motDePasse, lastLoginDate,
                dateNaissance, salaire, prime, dateRecrutement, soldeCongé);
        this.numCNSS = numCNSS;
        this.commission = commission;
    }

    @Override
    public String toString() {
        return """
            Secretaire {
                id = %d,
                nom = '%s',
                numCNSS = '%s',
                commission = %.2f
            }
            """.formatted(getId(), getNom(), numCNSS, commission != null ? commission : 0.0);
    }
}
