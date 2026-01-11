package ma.emsi.service.authentificationService.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.emsi.common.consoleLog.ConsoleLogger;
import ma.emsi.common.utils.RepoFactory;
import ma.emsi.entities.modules.enums.RoleType;
import ma.emsi.entities.modules.users.Role;
import ma.emsi.entities.modules.users.Utilisateur;
import ma.emsi.mvc.dto.authentificationDtos.AuthRequest;
import ma.emsi.mvc.dto.authentificationDtos.AuthResult;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.repository.api.RoleRepo;
import ma.emsi.repository.api.UserRepo;
import ma.emsi.service.authentificationService.api.AuthService;
import ma.emsi.service.authentificationService.api.LoginFormValidator;
import ma.emsi.service.authentificationService.api.PasswordEncoder;
import ma.emsi.service.common.Transaction;

@Data @AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RepoFactory<UserRepo> userRepoFactory;
    private final RepoFactory<RoleRepo> roleRepoFactory;
    private final LoginFormValidator validator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResult authenticate(AuthRequest request) {
        ConsoleLogger.info("Call for Authentification Service ...");
        Map<String, String> errors = validator.validate(request);

        if (!errors.isEmpty()) {

            ConsoleLogger.warn("Form Validation Failed");
            ConsoleLogger.error("Form contains Errors : " + errors.values().toString());
            return AuthResult.failure("Formulaire invalide", errors);
        }

        return Transaction.initTransaction(cnx -> {

            UserRepo userRepo = userRepoFactory.create(cnx);
            RoleRepo roleRepo = roleRepoFactory.create(cnx);



            //Utilisateur user = userRepo.findByLoginAndPassword(request.login(), request.password());
            Utilisateur user = userRepo.findByLogin(request.login());

            if (user == null) {
                ConsoleLogger.error("Authentification échouée :: Utilisateur Login introuvable");
                return AuthResult.failure("Authentification échouée :: Utilisateur introuvable");
            }

            boolean ok = passwordEncoder.matches(request.password(), user.getMotDePasse());
            if (!ok) {
                ConsoleLogger.error("Authentification échouée :: Mot de passe incorrect");
                return AuthResult.failure("Mot de passe incorrect");
            }

            UserPrincipal principal = buildUserPrincipal(user, roleRepo);

            ConsoleLogger.info("Authentification réussie ^_^");
            return AuthResult.success(principal);
        });

    }







    private UserPrincipal buildUserPrincipal(Utilisateur u, RoleRepo roleRepo) {
        // ————————— récupérer les roles affecté au utilisateur courant
        List<Role>      roles = roleRepo.findRolesByUtilisateurId(u.getId());
        // ————————— Transformer en set de RoleType affecté au utilisateur courant
        var roleTypes = roles   .stream()
                                .map(Role::getType)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toCollection(LinkedHashSet::new));
        // ————————— récupérer comme set les privileges affectés aux roles affectés au utilisateur courant
        Set<String> privileges = roles.stream()
                                    .filter(Objects::nonNull)
                                    .flatMap(r -> {
                                        if(r.getPrivileges() != null )
                                               return r.getPrivileges().stream();
                                        else
                                               return Stream.empty();
                                    })
                                    .collect(Collectors.toSet());

        // ————————— on suppose que le premier rôle affecté à un utilisateur est son rôle principale
        RoleType rolePrincipal = roleTypes.stream().findFirst().orElse(null);

        return new UserPrincipal( u.getId(),  u.getNom(),  u.getEmail(),  u.getLogin(),
                                    rolePrincipal, roleTypes, privileges);
    }


}
