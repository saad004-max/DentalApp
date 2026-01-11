package ma.emsi.service.authentificationService.impl;

import ma.emsi.common.consoleLog.ConsoleLogger;
import ma.emsi.service.authentificationService.api.PasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordEncoder implements PasswordEncoder {

    private final int strength;

    public BCryptPasswordEncoder() {
        this(10); // coût par défaut
    }

    public BCryptPasswordEncoder(int strength) {
        this.strength = strength;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        ConsoleLogger.info("Call for Form Password Encoder Service to encode password...");
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword ne doit pas être null");
        }
        String salt = BCrypt.gensalt(strength);
        return BCrypt.hashpw(rawPassword.toString(), salt);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        ConsoleLogger.info("Call for Form Password Encoder Service to matches passwords...");
        if (rawPassword == null || encodedPassword == null) return false;
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }


}
