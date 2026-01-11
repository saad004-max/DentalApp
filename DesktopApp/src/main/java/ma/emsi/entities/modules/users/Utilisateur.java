package ma.emsi.entities.modules.users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;
import ma.emsi.entities.modules.enums.Sexe;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Utilisateur extends BaseEntity {

    private String nom;
    private String prenom;
    private String email;
    private String adresse;
    private String cin;
    private String tel;
    private Sexe sexe;
    private String login;
    private String motDePasse;
    private LocalDate lastLoginDate;
    private LocalDate dateNaissance;
    private String avatar;

    private List<Role> roles = new ArrayList<>();

    public Utilisateur(String nom, String prenom, String email, String adresse,
                       String cin, String tel, String avatar, Sexe sexe, String login,
                       String motDePasse, LocalDate lastLoginDate,
                       LocalDate dateNaissance) {

        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
        this.cin = cin;
        this.tel = tel;
        this.avatar = avatar;
        this.sexe = sexe;
        this.login = login;
        this.motDePasse = motDePasse;
        this.lastLoginDate = lastLoginDate;
        this.dateNaissance = dateNaissance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilisateur)) return false;
        Utilisateur that = (Utilisateur) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return """
            Utilisateur {
                id = %d,
                nom = '%s',
                email = '%s',
                login = '%s'
            }
            """.formatted(id, nom, email, login);
    }
}
