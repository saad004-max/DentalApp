package ma.emsi.common.utils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Properties;
import ma.emsi.config.SessionFactory;
import ma.emsi.repository.api.UserRepo;

public class FactoryUtils {

    /**
     * Crée une instance d'une implémentation définie dans un Properties, en utilisant :
     * - constructeur par défaut si args est vide
     * - sinon le constructeur compatible avec les types des args (varargs)
     *
     * @param props   properties (clé -> nom de classe impl)
     * @param key     clé de la propriété
     * @param apiType type attendu (interface/classe mère)
     * @param args    0..n arguments du constructeur
     */
    public static  <T> T buildImplInstance(Properties props,
                                    String key,
                                    Class<T> apiType,
                                    Object... args) {

        String implClassName = props.getProperty(key);
        if (implClassName == null || implClassName.isBlank()) {
            throw new IllegalArgumentException("Property manquante: " + key);
        }

        try {
            Class<?> implClass = Class.forName(implClassName);

            Object obj = newInstanceWithArgs(implClass, args);

            // Sécurité: vérifier que l'implémentation est bien du bon type
            return apiType.cast(obj);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Classe introuvable: " + implClassName, e);
        } catch (ClassCastException e) {
            throw new RuntimeException("La classe " + implClassName + " n'implémente pas " + apiType.getName(), e);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de créer " + implClassName + " avec args="
                                       + Arrays.toString(args), e);
        }
    }

    /**
     * Instancie une classe via réflexion en choisissant un constructeur compatible
     * avec les types réels des arguments (varargs).
     *
     * @param implClass classe concrète à instancier
     * @param args      0..n arguments
     */
    private static Object newInstanceWithArgs(Class<?> implClass, Object... args) throws Exception {

        // 0 argument => constructeur par défaut
        if (args == null || args.length == 0) {
            Constructor<?> ctor = implClass.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        }

        // Chercher le meilleur constructeur compatible (même arité + compatibilité de types)
        Constructor<?> best = null;
        int bestScore = Integer.MAX_VALUE;

        for (Constructor<?> ctor : implClass.getDeclaredConstructors()) {
            Class<?>[] paramTypes = ctor.getParameterTypes();
            if (paramTypes.length != args.length) continue;

            int score = compatibilityScore(paramTypes, args);
            if (score >= 0 && score < bestScore) {
                bestScore = score;
                best = ctor;
            }
        }

        if (best == null) {
            throw new NoSuchMethodException("Aucun constructeur compatible trouvé pour "
                                            + implClass.getName() + " avec args=" + Arrays.toString(args));
        }

        best.setAccessible(true);
        return best.newInstance(args);
    }

    /**
     * Retourne un score de compatibilité (plus petit = meilleur), ou -1 si incompatible.
     * - exact match => 0
     * - héritage/interface => +1/+2...
     * - wrapper/primitive => ok
     * - null => accepté si param non-primitive (petite pénalité)
     */
    private static int compatibilityScore(Class<?>[] paramTypes, Object[] args) {
        int score = 0;

        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> p = paramTypes[i];
            Object a = args[i];

            if (a == null) {
                if (p.isPrimitive()) return -1; // null impossible pour primitive
                score += 3; // pénalité: null est moins précis
                continue;
            }

            Class<?> aType = a.getClass();

            // exact
            if (p.equals(aType)) continue;

            // primitive <-> wrapper
            if (p.isPrimitive()) {
                Class<?> wrap = wrapperOf(p);
                if (wrap.equals(aType)) {
                    score += 1;
                    continue;
                }
                return -1;
            }

            // assignable (héritage / interface)
            if (p.isAssignableFrom(aType)) {
                score += inheritanceDistance(aType, p);
                continue;
            }

            return -1;
        }

        return score;
    }

    /** Distance d'héritage approximative (0 si égal, 1 si parent direct, etc.) */
    private static int inheritanceDistance(Class<?> child, Class<?> parent) {
        if (child.equals(parent)) return 0;
        int d = 0;
        Class<?> cur = child;
        while (cur != null && !cur.equals(parent)) {
            cur = cur.getSuperclass();
            d++;
        }
        // si parent est une interface, on met une pénalité fixe (approx)
        if (cur == null && parent.isInterface()) return 2;
        return d;
    }

    private static Class<?> wrapperOf(Class<?> primitive) {
        if (primitive == int.class) return Integer.class;
        if (primitive == long.class) return Long.class;
        if (primitive == double.class) return Double.class;
        if (primitive == float.class) return Float.class;
        if (primitive == boolean.class) return Boolean.class;
        if (primitive == char.class) return Character.class;
        if (primitive == byte.class) return Byte.class;
        if (primitive == short.class) return Short.class;
        throw new IllegalArgumentException("Primitive non supporté: " + primitive);
    }


    /*
    public static void main(String[] args) throws Exception {

        var propertiesFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");

      Properties properties = new Properties();
                properties.load(propertiesFile);
        var userRepo = FactoryUtils.buildImplInstance(properties,"userRepo", UserRepo.class , SessionFactory.getInstance().getConnection() );

        System.out.println(userRepo);
        
        var user = userRepo.findById(1L);
        System.out.println("user = " + user);
    }

     */
}
