package ma.emsi.service.authentificationService.impl;

import java.util.Map;
import ma.emsi.common.consoleLog.ConsoleLogger;
import ma.emsi.mvc.dto.authentificationDtos.AuthRequest;
import ma.emsi.service.authentificationService.api.LoginFormValidator;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class LoginFormValidatorImpl implements LoginFormValidator {

    // Choisis une politique réaliste (à adapter selon ton besoin)
    private static final int LOGIN_MIN = 3;
    private static final int LOGIN_MAX = 60;

    private static final int PASSWORD_MIN = 4;
    private static final int PASSWORD_MAX = 128;

    // Optionnel : si tu acceptes email OU username
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // username: lettres/chiffres/._- (pas d'espaces)
    private static final Pattern USERNAME =
            Pattern.compile("^[A-Za-z0-9._-]+$");

    @Override
    public Map<String, String> validate(AuthRequest request) {

        ConsoleLogger.info("Call for Form Validation Service ...");
        Map<String, String> errors = new LinkedHashMap<>();

        // 1) Request null => erreur globale (ou throw, mais tu as demandé map)
        if (request == null) {
            errors.put("_global", "Formulaire invalide (requête null).");
            return errors;
        }

        String login = normalize(request.login());
        String password = request.password(); // ne pas trim un mot de passe

        // 2) LOGIN validations
        if (login == null || login.isEmpty()) {
            errors.put("login", "Login est obligatoire.");
        } else {
            if (login.length() < LOGIN_MIN) {
                errors.put("login", "Login doit contenir au moins " + LOGIN_MIN + " caractères.");
            } else if (login.length() > LOGIN_MAX) {
                errors.put("login", "Login ne doit pas dépasser " + LOGIN_MAX + " caractères.");
            } else if (containsControlChars(login)) {
                errors.put("login", "Login contient des caractères invalides.");
            } else if (login.contains(" ")) {
                errors.put("login", "Login ne doit pas contenir d'espaces.");
            } else {
                // Si tu veux accepter email OU username :
                boolean isEmail = EMAIL.matcher(login).matches();
                boolean isUsername = USERNAME.matcher(login).matches();

                if (!isEmail && !isUsername) {
                    errors.put("login", "Login invalide : utilisez un email ou un identifiant (lettres/chiffres/._-).");
                }
            }
        }

        // 3) PASSWORD validations
        if (password == null || password.isBlank()) {
            errors.put("password", "Mot de passe est obligatoire.");
        } else {
            if (password.length() < PASSWORD_MIN) {
                errors.put("password", "Mot de passe doit contenir au moins " + PASSWORD_MIN + " caractères.");
            } else if (password.length() > PASSWORD_MAX) {
                errors.put("password", "Mot de passe est trop long (max " + PASSWORD_MAX + ").");
            } else if (containsControlChars(password)) {
                errors.put("password", "Mot de passe contient des caractères invalides.");
            } else {
                // Options sécurité (active/désactive selon ta politique)
                if (!hasUpper(password)) errors.put("password", "Mot de passe doit contenir au moins une majuscule.");
                else if (!hasLower(password)) errors.put("password", "Mot de passe doit contenir au moins une minuscule.");
                else if (!hasDigit(password)) errors.put("password", "Mot de passe doit contenir au moins un chiffre.");
                else if (!hasSpecial(password)) errors.put("password", "Mot de passe doit contenir au moins un caractère spécial.");
            }
        }

        // 4) Bonus : erreurs “métier”/cohérence
        // Exemple : login == password (mauvaise pratique)
        if (login != null && password != null && !login.isEmpty() && !password.isBlank()) {
            if (login.equals(password)) {
                errors.put("_global", "Login et mot de passe ne doivent pas être identiques.");
            }
        }

        return errors;
    }

    private String normalize(String s) {
        return (s == null) ? null : s.trim();
    }

    private boolean containsControlChars(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isISOControl(s.charAt(i))) return true;
        }
        return false;
    }

    private boolean hasUpper(String s) {
        for (int i = 0; i < s.length(); i++) if (Character.isUpperCase(s.charAt(i))) return true;
        return false;
    }

    private boolean hasLower(String s) {
        for (int i = 0; i < s.length(); i++) if (Character.isLowerCase(s.charAt(i))) return true;
        return false;
    }

    private boolean hasDigit(String s) {
        for (int i = 0; i < s.length(); i++) if (Character.isDigit(s.charAt(i))) return true;
        return false;
    }

    private boolean hasSpecial(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) return true;
        }
        return false;
    }
}
