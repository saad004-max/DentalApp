package ma.emsi.config;

import java.util.*;
import java.util.Properties;
import ma.emsi.common.consoleLog.*;
import ma.emsi.common.utils.FactoryUtils;
import ma.emsi.common.utils.RepoFactory;
import ma.emsi.mvc.controllers.dashboardModule.api.DashboardController;
import ma.emsi.mvc.controllers.authentificationModule.api.LoginController;
import ma.emsi.mvc.controllers.otherModules.api.*;
import ma.emsi.mvc.controllers.profileModule.api.ProfileController;
import ma.emsi.repository.api.*;
import ma.emsi.service.authentificationService.api.AuthService;
import ma.emsi.service.authentificationService.api.AuthorizationService;
import ma.emsi.service.authentificationService.api.LoginFormValidator;
import ma.emsi.service.authentificationService.api.PasswordEncoder;
import ma.emsi.service.profileService.api.*;

public final class ApplicationContext
{
    private static Map<Class<?>, Object> context = new LinkedHashMap<Class<?>, Object>();
    private static volatile ApplicationContext INSTANCE;
    private ClassLoader cl = Thread .currentThread() .getContextClassLoader();

    private Properties props;

    private ApplicationContext()
    {
        var propertiesFile = cl.getResourceAsStream("config/application.properties");

        if(propertiesFile != null) {
            try
            {
                Properties properties = new Properties();
                properties.load(propertiesFile);

                // on garde les properties accessibles partout
                this.props = properties;
                context.put(Properties.class, this.props); // optionnel mais utile

                // module user Repo
                var userRepoFactory         =   buildRepoFactory(properties, "userRepo",    UserRepo.class);
                // RepoFactories nécessaires pour charger "profil et jutilisateurs" selon rôle réel (Admin/Medecin/Secretaire)
                var staffRepoFactory      = buildRepoFactory(properties, "staffRepo",      StaffRepo.class);
                var adminRepoFactory      = buildRepoFactory(properties, "adminRepo",      AdminRepo.class);
                var medecinRepoFactory    = buildRepoFactory(properties, "medecinRepo",    MedecinRepo.class);
                var secretaireRepoFactory = buildRepoFactory(properties, "secretaireRepo", SecretaireRepo.class);

                context.put(UserRepo.class ,        userRepoFactory);
                context.put(StaffRepo.class,        staffRepoFactory);
                context.put(AdminRepo.class,        adminRepoFactory);
                context.put(MedecinRepo.class,      medecinRepoFactory);
                context.put(SecretaireRepo.class,   secretaireRepoFactory);


                var roleRepoFactory         =   buildRepoFactory(properties, "roleRepo",    RoleRepo.class);
                context.put(RoleRepo.class , roleRepoFactory);


                var loginValidator                = FactoryUtils.buildImplInstance(properties,"loginFormValidator", LoginFormValidator.class);
                var passwordEncoder               = FactoryUtils.buildImplInstance(properties, "passwordEncoder",    PasswordEncoder.class);
                var userAuthService               = FactoryUtils.buildImplInstance(properties, "userAuthService",    AuthService.class,
                                                                                    userRepoFactory, roleRepoFactory, loginValidator, passwordEncoder );


                var userAuthorization               = FactoryUtils.buildImplInstance(properties, "userAuthorizationService", AuthorizationService.class);


                context.put(AuthService.class, userAuthService);
                context.put(AuthorizationService.class, userAuthorization);

                var loginController               = FactoryUtils.buildImplInstance(properties, "userAuthController",    LoginController.class,
                                                            userAuthService);


                context.put(LoginController.class, loginController);


                var patientsController = FactoryUtils.buildImplInstance(properties, "patientsController", PatientsController.class);
                var caisseController   = FactoryUtils.buildImplInstance(properties, "caisseController",   CaisseController.class);
                var usersController    = FactoryUtils.buildImplInstance(properties, "usersController",    UsersController.class);
                var cabinetsController = FactoryUtils.buildImplInstance(properties, "cabinetsController", CabinetsController.class);
                var dossiersController = FactoryUtils.buildImplInstance(properties, "dossiersController", DossiersController.class);
                var parametrageController = FactoryUtils.buildImplInstance(properties, "parametrageController", ParametrageController.class);

                context.put(PatientsController.class, patientsController);
                context.put(CaisseController.class, caisseController);
                context.put(UsersController.class, usersController);
                context.put(CabinetsController.class, cabinetsController);
                context.put(DossiersController.class, dossiersController);
                context.put(ParametrageController.class, parametrageController);


                // ———————————————————— DASHBOARD Module

                var dashboardController               = FactoryUtils.buildImplInstance
                        (properties, "dashboardController",    DashboardController.class,
                                        userAuthorization, loginController);

                context.put(DashboardController.class, dashboardController);


                // —————————— PROFILE MODULE :  Validator + Service + Controller
                // Validator Profile (pour update : email/tel/cin etc.)
                var profileValidator = FactoryUtils.buildImplInstance( properties, "profileValidator", ProfileValidator.class);

                context.put(ProfileValidator.class, profileValidator);

                var changePasswordValidator = FactoryUtils.buildImplInstance(properties, "changePasswordValidator", ChangePasswordValidator.class);
                context.put(ChangePasswordValidator.class, changePasswordValidator);

                // Service Profile (charge + update multi-tables via Transaction, etc.)
                var profileService = FactoryUtils.buildImplInstance(properties, "profileService", ProfileService.class,
                                        userRepoFactory, staffRepoFactory, medecinRepoFactory,
                                        secretaireRepoFactory, adminRepoFactory, roleRepoFactory, profileValidator,
                                        changePasswordValidator, passwordEncoder);
                context.put(ProfileService.class, profileService);


                    // Controller Profile (getView(principal) => service.loadByUserId(principal.id()))
                var profileController = FactoryUtils.buildImplInstance( properties,  "profileController", ProfileController.class,
                        profileService );
                context.put(ProfileController.class, profileController);

                ConsoleLogger.info("ApplicationContext initialisé avec succès.");

            }
            catch (Exception e) {
                ConsoleLogger.error("Erreur init ApplicationContext: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public static ApplicationContext getInstance() {
        if (INSTANCE == null) {
            synchronized (ApplicationContext.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ApplicationContext();
                }
            }
        }
        return INSTANCE;
    }




    public static <T> T getBean(Class<T> beanClass) {
        ConsoleLogger.info("Call for " +  splitCamelCase(beanClass.getSimpleName()));
        return beanClass.cast(context.get(beanClass));
    }


    // ——————————— Helper Methods
    public String getProperty(String key, String defaultValue)
    {
        if (props == null) return defaultValue;
        String v = props.getProperty(key);
        return (v == null || v.isBlank()) ? defaultValue : v.trim();
    }


    private <T> RepoFactory<T> buildRepoFactory(Properties props,
                                                String key,
                                                Class<T> apiType) {

        String implClassName = props.getProperty(key);
        if (implClassName == null || implClassName.isBlank()) {
            throw new IllegalArgumentException("Property manquante: " + key);
        }

        return (java.sql.Connection c) -> {
            try {
                Class<?> implClass = Class.forName(implClassName);
                Object obj = implClass.getDeclaredConstructor(java.sql.Connection.class)
                        .newInstance(c);
                return apiType.cast(obj);
            } catch (Exception e) {
                throw new RuntimeException("Impossible de créer " + implClassName +
                                           " avec constructeur(Connection)", e);
            }
        };
    }




    // ————— utilisé pour le ConsolLog pour affichage des noms des contrôleurs normalisés
    public static String splitCamelCase(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }

        return input
                // Cas : lettre minuscule suivie d’une majuscule → aB → a B
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                // Cas : suite de majuscules suivie d’une minuscule → XMLParser → XML Parser
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1 $2");
    }

}
