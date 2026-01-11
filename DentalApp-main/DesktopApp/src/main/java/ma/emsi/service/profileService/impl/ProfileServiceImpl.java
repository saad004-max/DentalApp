package ma.emsi.service.profileService.impl;



import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.emsi.common.utils.RepoFactory;
import ma.emsi.entities.modules.enums.RoleType;
import ma.emsi.entities.modules.users.*;
import ma.emsi.mvc.dto.profileDtos.*;
import ma.emsi.repository.api.*;
import ma.emsi.service.authentificationService.api.PasswordEncoder;
import ma.emsi.service.common.Transaction;
import ma.emsi.service.profileService.api.ChangePasswordValidator;
import ma.emsi.service.profileService.api.ProfileService;
import ma.emsi.service.profileService.api.ProfileValidator;

@Data
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final RepoFactory<UserRepo> userRepoFactory;
    private final RepoFactory<StaffRepo> staffRepoFactory;
    private final RepoFactory<MedecinRepo> medecinRepoFactory;
    private final RepoFactory<SecretaireRepo> secretaireRepoFactory;
    private final RepoFactory<AdminRepo> adminRepoFactory;
    private final RepoFactory<RoleRepo> roleRepoFactory; // pour déterminer role principal si besoin
    private final ProfileValidator validator;
    private final ChangePasswordValidator changePasswordValidator;
    private final PasswordEncoder  passwordEncoder;

    @Override
    public ProfileData loadByUserId(Long userId) {
        if (userId == null) return null;

        return Transaction.initTransaction(cnx -> {

            UserRepo userRepo = userRepoFactory.create(cnx);
            RoleRepo roleRepo = roleRepoFactory.create(cnx);

            // déterminer role principal via roles (comme AuthServiceImpl)
            RoleType rolePrincipal = roleRepo.findRolesByUtilisateurId(userId)
                    .stream()
                    .map(Role::getType)
                    .filter(t -> t != null)
                    .findFirst()
                    .orElse(null);

            // Charger selon rôle (multi-table)
            if (rolePrincipal == RoleType.MEDECIN) {
                MedecinRepo medRepo = medecinRepoFactory.create(cnx);
                Medecin m = medRepo.findById(userId);
                return (m == null) ? null : map(m, rolePrincipal);
            }

            if (rolePrincipal == RoleType.SECRETAIRE) {
                SecretaireRepo secRepo = secretaireRepoFactory.create(cnx);
                Secretaire s = secRepo.findById(userId);
                return (s == null) ? null : map(s, rolePrincipal);
            }

            if (rolePrincipal == RoleType.ADMIN) {
                AdminRepo adminRepo = adminRepoFactory.create(cnx);
                Admin a = adminRepo.findById(userId);
                // Admin n'a pas de champs spécifiques, mais c'est un Staff
                return (a == null) ? null : map(a, rolePrincipal);
            }

            // fallback : simple utilisateur
            Utilisateur u = userRepo.findById(userId);
            return (u == null) ? null : map(u, rolePrincipal);
        });
    }

    @Override
    public ProfileUpdateResult update(ProfileUpdateRequest req) {

        Map<String, String> errors = validator.validate(req);
        if (!errors.isEmpty()) return ProfileUpdateResult.failure("Formulaire invalide", errors);

        return Transaction.initTransaction(cnx -> {

            UserRepo userRepo = userRepoFactory.create(cnx);
            StaffRepo staffRepo = staffRepoFactory.create(cnx);
            MedecinRepo medRepo = medecinRepoFactory.create(cnx);
            SecretaireRepo secRepo = secretaireRepoFactory.create(cnx);
            AdminRepo adminRepo = adminRepoFactory.create(cnx);
            RoleRepo roleRepo = roleRepoFactory.create(cnx);

            // role principal
            RoleType rolePrincipal = roleRepo.findRolesByUtilisateurId(req.id())
                    .stream().map(Role::getType).filter(t -> t != null).findFirst().orElse(null);

            // Unicité email/login/cin sans changer ton API repo
            Map<String, String> uniqErrors = validateUniqueness(userRepo, req);
            if (!uniqErrors.isEmpty()) return ProfileUpdateResult.failure("Conflit d'unicité", uniqErrors);

            // 1) UPDATE Utilisateurs
            Utilisateur u = userRepo.findById(req.id());
            if (u == null) return ProfileUpdateResult.failure("Utilisateur introuvable", Map.of("_global", "Utilisateur introuvable."));

            applyUserFields(u, req);
            userRepo.update(u); // ✅ doit exister dans ton UserRepoImpl

            // 2) UPDATE Staffs + table spécifique selon rôle
            if (rolePrincipal == RoleType.MEDECIN) {
                Medecin m = medRepo.findById(req.id());
                if (m == null) return ProfileUpdateResult.failure("Médecin introuvable", Map.of("_global", "Médecin introuvable."));

                applyStaffFields(m, req);
                applyMedecinFields(m, req);

                staffRepo.updateStaffFields(m);
                medRepo.updateMedecinFields(m);

                return ProfileUpdateResult.success("Profil mis à jour", map(medRepo.findById(req.id()), rolePrincipal));
            }

            if (rolePrincipal == RoleType.SECRETAIRE) {
                Secretaire s = secRepo.findById(req.id());
                if (s == null) return ProfileUpdateResult.failure("Secrétaire introuvable", Map.of("_global", "Secrétaire introuvable."));

                applyStaffFields(s, req);
                applySecretaireFields(s, req);

                staffRepo.updateStaffFields(s);
                secRepo.updateSecretaireFields(s);

                return ProfileUpdateResult.success("Profil mis à jour", map(secRepo.findById(req.id()), rolePrincipal));
            }

            if (rolePrincipal == RoleType.ADMIN) {
                Admin a = adminRepo.findById(req.id());
                if (a == null) return ProfileUpdateResult.failure("Admin introuvable", Map.of("_global", "Admin introuvable."));

                applyStaffFields(a, req);
                staffRepo.updateStaffFields(a);

                return ProfileUpdateResult.success("Profil mis à jour", map(adminRepo.findById(req.id()), rolePrincipal));
            }

            // Utilisateur simple
            return ProfileUpdateResult.success("Profil mis à jour", loadByUserId(req.id()));
        });
    }



    @Override
    public ChangePasswordResult changePassword(ChangePasswordRequest req) {

        var errors = changePasswordValidator.validate(req);
        if (!errors.isEmpty()) {
            return ChangePasswordResult.failure("Formulaire invalide", errors);
        }

        return Transaction.initTransaction(cnx -> {
            UserRepo userRepo = userRepoFactory.create(cnx);

            var u = userRepo.findById(req.userId());
            if (u == null) {
                return ChangePasswordResult.failure("Utilisateur introuvable.", Map.of("_global", "Utilisateur introuvable."));
            }

            boolean ok = passwordEncoder.matches(req.currentPassword(), u.getMotDePasse());
            if (!ok) {
                return ChangePasswordResult.failure("Mot de passe actuel incorrect.",
                        Map.of("currentPassword", "Mot de passe actuel incorrect."));
            }

            String encoded = passwordEncoder.encode(req.newPassword());
            userRepo.updatePassword(req.userId(), encoded);

            return ChangePasswordResult.success();
        });
    }



    // ----------------- Helpers -----------------

    private Map<String, String> validateUniqueness(UserRepo userRepo, ProfileUpdateRequest req) {
        Map<String, String> e = new LinkedHashMap<>();

        String email = trim(req.email());
        if (email != null) {
            Utilisateur byEmail = userRepo.findByEmail(email);
            if (byEmail != null && byEmail.getId() != null && !byEmail.getId().equals(req.id())) {
                e.put("email", "Email déjà utilisé.");
            }
        }

        // login rarement modifié (mais si tu le rends modifiable plus tard)
        // Utilisateur byLogin = userRepo.findByLogin(trim(req.login()));
        // ...

        String cin = trim(req.cin());
        if (cin != null && !cin.isBlank()) {
            // pas de findByCin dans ton API, donc on ne peut pas vérifier sans ajouter une méthode.
            // Option 1: ajouter findByCin / existsByCin au UserRepo.
            // Option 2: ignorer ici.
        }

        return e;
    }

    private void applyUserFields(Utilisateur u, ProfileUpdateRequest req) {
        u.setPrenom(trim(req.prenom()));
        u.setNom(trim(req.nom()));
        u.setEmail(trim(req.email()));
        u.setAdresse(trim(req.adresse()));
        u.setCin(trim(req.cin()));
        u.setTel(trim(req.tel()));
        u.setSexe(req.sexe());
        u.setDateNaissance(req.dateNaissance());
        u.setAvatar(trim(req.avatar()));
    }

    private void applyStaffFields(Staff s, ProfileUpdateRequest req) {
        s.setSalaire(req.salaire());
        s.setPrime(req.prime());
        s.setDateRecrutement(req.dateRecrutement());
        s.setSoldeConge(req.soldeConge());
    }

    private void applyMedecinFields(Medecin m, ProfileUpdateRequest req) {
        m.setSpecialite(trim(req.specialite()));
    }

    private void applySecretaireFields(Secretaire s, ProfileUpdateRequest req) {
        s.setNumCNSS(trim(req.numCNSS()));
        s.setCommission(req.commission());
    }

    private ProfileData map(Utilisateur u, RoleType rolePrincipal) {
        return ProfileData.builder()
                .id(u.getId())
                .rolePrincipal(rolePrincipal)
                .prenom(u.getPrenom())
                .nom(u.getNom())
                .email(u.getEmail())
                .adresse(u.getAdresse())
                .cin(u.getCin())
                .tel(u.getTel())
                .sexe(u.getSexe())
                .login(u.getLogin())
                .lastLoginDate(u.getLastLoginDate())
                .dateNaissance(u.getDateNaissance())
                .avatar(u.getAvatar())
                .build();
    }

    private ProfileData map(Staff s, RoleType rolePrincipal) {
        ProfileData base = map((Utilisateur) s, rolePrincipal);
        return ProfileData.builder()
                .id(base.id())
                .rolePrincipal(base.rolePrincipal())
                .prenom(base.prenom())
                .nom(base.nom())
                .email(base.email())
                .adresse(base.adresse())
                .cin(base.cin())
                .tel(base.tel())
                .sexe(base.sexe())
                .login(base.login())
                .lastLoginDate(base.lastLoginDate())
                .dateNaissance(base.dateNaissance())
                .avatar(base.avatar())
                .salaire(s.getSalaire())
                .prime(s.getPrime())
                .dateRecrutement(s.getDateRecrutement())
                .soldeConge(s.getSoldeConge())
                .cabinetId(s.getCabinetId())
                .build();
    }

    private ProfileData map(Medecin m, RoleType rolePrincipal) {
        ProfileData base = map((Staff) m, rolePrincipal);
        return ProfileData.builder()
                .id(base.id())
                .rolePrincipal(base.rolePrincipal())
                .prenom(base.prenom())
                .nom(base.nom())
                .email(base.email())
                .adresse(base.adresse())
                .cin(base.cin())
                .tel(base.tel())
                .sexe(base.sexe())
                .login(base.login())
                .lastLoginDate(base.lastLoginDate())
                .dateNaissance(base.dateNaissance())
                .avatar(base.avatar())
                .salaire(base.salaire())
                .prime(base.prime())
                .dateRecrutement(base.dateRecrutement())
                .soldeConge(base.soldeConge())
                .cabinetId(base.cabinetId())
                .specialite(m.getSpecialite())
                .build();
    }

    private ProfileData map(Secretaire s, RoleType rolePrincipal) {
        ProfileData base = map((Staff) s, rolePrincipal);
        return ProfileData.builder()
                .id(base.id())
                .rolePrincipal(base.rolePrincipal())
                .prenom(base.prenom())
                .nom(base.nom())
                .email(base.email())
                .adresse(base.adresse())
                .cin(base.cin())
                .tel(base.tel())
                .sexe(base.sexe())
                .login(base.login())
                .lastLoginDate(base.lastLoginDate())
                .dateNaissance(base.dateNaissance())
                .avatar(base.avatar())
                .salaire(base.salaire())
                .prime(base.prime())
                .dateRecrutement(base.dateRecrutement())
                .soldeConge(base.soldeConge())
                .cabinetId(base.cabinetId())
                .numCNSS(s.getNumCNSS())
                .commission(s.getCommission())
                .build();
    }

    private String trim(String s) { return s == null ? null : s.trim(); }
}
