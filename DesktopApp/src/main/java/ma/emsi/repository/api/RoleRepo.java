package ma.emsi.repository.api;

import java.util.List;
import java.util.Optional;
import ma.emsi.entities.modules.enums.RoleType;
import ma.emsi.entities.modules.users.Role;
import ma.emsi.repository.api.common.CrudRepo;

public interface RoleRepo extends CrudRepo<Role, Long> {

    Optional<Role> findByLibelle(String libelle);

    Optional<Role> findByType(RoleType type);
    List<String> getPrivileges(Long roleId);
    void addPrivilege(Long roleId, String privilege);
    void removePrivilege(Long roleId, String privilege);
    boolean existsByLibelle(String libelle);

    List<Role> findRolesByUtilisateurId(Long utilisateurId);
    // à ajouter dans RoleRepository (repository layer)
    void assignRoleToUser(Long utilisateurId, Long roleId);
    void removeRoleFromUser(Long utilisateurId, Long roleId);
}
