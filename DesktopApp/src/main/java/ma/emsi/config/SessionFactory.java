package ma.emsi.config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import ma.emsi.common.consoleLog.ConsoleLogger;

public final class SessionFactory {

    private static volatile SessionFactory INSTANCE;

    /** Propriétés de configuration (fichier .properties) */
    private static final String PROPS_PATH = "config/application.properties";
    private static final String URL_KEY    = "datasource.url";
    private static final String USER_KEY   = "datasource.username";
    private static final String PASS_KEY   = "datasource.password";
    private static final String DRIVER_KEY = "datasource.driver";

    /** Valeurs lues depuis le fichier de configuration */
    private String url;
    private String user;
    private String password;
    private String driver;
    private ClassLoader cl = Thread .currentThread().getContextClassLoader();

    private SessionFactory() {
        var propertiesFile =  cl .getResourceAsStream(PROPS_PATH);
            try {
                Properties props = new Properties();
                props.load(propertiesFile);
                this.url      = props.getProperty(URL_KEY);
                this.user     = props.getProperty(USER_KEY);
                this.password = props.getProperty(PASS_KEY);
                this.driver   = props.getProperty(DRIVER_KEY);

                Class.forName(driver);
                ConsoleLogger.info(" Driver JDBC chargé avec succès : " + driver);

            } catch (ClassNotFoundException e) {
                ConsoleLogger.error(" Driver JDBC introuvable " , e);

            }catch (IOException e) {
                ConsoleLogger.error(" Problème lors du chargement du fichier properties " , e);
            }

    }

    public static SessionFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (SessionFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SessionFactory();
                }
            }
        }
        return INSTANCE;
    }
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }




}
