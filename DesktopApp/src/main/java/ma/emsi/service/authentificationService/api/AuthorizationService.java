package ma.emsi.service.authentificationService.api;


import ma.emsi.entities.modules.enums.*;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;

public interface AuthorizationService {

    boolean hasRole(UserPrincipal principal, RoleType role);

    boolean hasAnyRole(UserPrincipal principal, RoleType... roles);

    boolean hasPrivilege(UserPrincipal principal, String privilege);

    /**
     * Lève AuthorizationException si le rôle / privilège est absent.
     */
    void checkRole(UserPrincipal principal, RoleType role);

    void checkAnyRole(UserPrincipal principal, RoleType... roles);

    void checkPrivilege(UserPrincipal principal, String privilege);
}
