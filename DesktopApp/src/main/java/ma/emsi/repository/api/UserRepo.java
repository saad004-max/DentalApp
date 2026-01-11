package ma.emsi.repository.api;

import java.util.List;
import java.util.Optional;
import ma.emsi.entities.modules.users.Utilisateur;
import ma.emsi.repository.api.common.CrudRepo;

public interface UserRepo extends CrudRepo<Utilisateur, Long>
{


    void updatePassword(Long userId, String encodedPassword);

    List<Utilisateur> findByName(String name);
    Utilisateur findByEmail(String email);
    List<Utilisateur> findByAdresse(String adresse);
    Utilisateur findByLogin(String login);
    Utilisateur findByLoginAndPassword(String login, String password);


    boolean existsByEmail(String email);
    boolean existsByLogin(String login);

    List<Utilisateur> searchByNom(String keyword); // LIKE %keyword%
    List<Utilisateur> findPage(int limit, int offset);

    // Rôles
    List<String> getRoleLibellesOfUser(Long utilisateurId);
    void addRoleToUser(Long utilisateurId, Long roleId);
    void removeRoleFromUser(Long utilisateurId, Long roleId);

}
