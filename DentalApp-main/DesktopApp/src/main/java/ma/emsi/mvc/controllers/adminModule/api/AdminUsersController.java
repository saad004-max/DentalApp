package ma.emsi.mvc.controllers.adminModule.api;

import java.util.List;
import ma.emsi.mvc.dto.common.ResponseWrapper;

/**
 * Controller pour la gestion des utilisateurs par l'admin.
 * Use Cases: [8-25]
 */
public interface AdminUsersController {
    
    /**
     * Récupère tous les utilisateurs.
     */
    ResponseWrapper<List<UserDto>> getAllUsers();
    
    /**
     * Récupère un utilisateur par ID.
     */
    ResponseWrapper<UserDto> getUserById(Long id);
    
    /**
     * Crée un nouvel utilisateur.
     */
    ResponseWrapper<UserDto> createUser(UserCreateRequest request);
    
    /**
     * Modifie un utilisateur.
     */
    ResponseWrapper<UserDto> updateUser(Long id, UserUpdateRequest request);
    
    /**
     * Supprime un utilisateur.
     */
    ResponseWrapper<Void> deleteUser(Long id);
    
    /**
     * Recherche d'utilisateurs par nom.
     */
    ResponseWrapper<List<UserDto>> searchUsers(String keyword);
    
    /**
     * Active un compte utilisateur.
     */
    ResponseWrapper<Void> activerCompte(Long userId);
    
    /**
     * Désactive un compte utilisateur.
     */
    ResponseWrapper<Void> desactiverCompte(Long userId);
    
    /**
     * Réinitialise le mot de passe d'un utilisateur.
     */
    ResponseWrapper<Void> reinitialiserMotDePasse(Long userId, String nouveauMotDePasse);
    
    /**
     * Consulte l'historique des connexions d'un utilisateur.
     */
    ResponseWrapper<List<ConnexionHistoryDto>> getHistoriqueConnexions(Long userId);
}

/**
 * DTOs pour ce controller.
 */
class UserDto {
    public Long id;
    public String nom;
    public String prenom;
    public String email;
    public String login;
    public List<String> roles;
    public Boolean actif;
}

class UserCreateRequest {
    public String nom;
    public String prenom;
    public String email;
    public String login;
    public String motDePasse;
    public String typeUtilisateur; // ADMIN, MEDECIN, SECRETAIRE
}

class UserUpdateRequest {
    public String nom;
    public String prenom;
    public String email;
}

class ConnexionHistoryDto {
    public java.time.LocalDateTime dateConnexion;
    public String ipAddress;
}
