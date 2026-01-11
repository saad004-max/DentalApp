package ma.emsi.mvc.dto.authentificationDtos;


import java.util.Set;
import ma.emsi.entities.modules.enums.RoleType;

// Représentation “sécurisée” de l’utilisateur authentifié côté service et UI (pas de mot de passe).
 public record UserPrincipal(
         Long id,
         String nom,
         String email,
         String login,
         RoleType rolePrincipal,
         Set<RoleType> roles,
         Set<String> privileges
 ) {
 }
