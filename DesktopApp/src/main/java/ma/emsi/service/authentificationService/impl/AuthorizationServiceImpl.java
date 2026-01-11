package ma.emsi.service.authentificationService.impl;

import ma.emsi.entities.modules.enums.RoleType;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.service.authentificationService.api.AuthorizationService;
import ma.emsi.common.exceptions.AuthorizationException;

import java.util.Arrays;

public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public boolean hasRole(UserPrincipal principal, RoleType role) {
        if (principal == null || role == null) return false;
        return principal.roles() != null && principal.roles().contains(role);
    }

    @Override
    public boolean hasAnyRole(UserPrincipal principal, RoleType... roles) {
        if (principal == null || roles == null || roles.length == 0) return false;
        if (principal.roles() == null) return false;

        return Arrays.stream(roles).anyMatch(principal.roles()::contains);
    }

    @Override
    public boolean hasPrivilege(UserPrincipal principal, String privilege) {
        if (principal == null || privilege == null || privilege.isBlank()) return false;
        return principal.privileges() != null && principal.privileges().contains(privilege);
    }

    @Override
    public void checkRole(UserPrincipal principal, RoleType role) {
        if (!hasRole(principal, role)) {
            throw new AuthorizationException("Accès refusé : rôle requis = " + role);
        }
    }

    @Override
    public void checkAnyRole(UserPrincipal principal, RoleType... roles) {
        if (!hasAnyRole(principal, roles)) {
            throw new AuthorizationException("Accès refusé : un des rôles requis = " + Arrays.toString(roles));
        }
    }

    @Override
    public void checkPrivilege(UserPrincipal principal, String privilege) {
        if (!hasPrivilege(principal, privilege)) {
            throw new AuthorizationException("Accès refusé : privilège requis = " + privilege);
        }
    }
}
