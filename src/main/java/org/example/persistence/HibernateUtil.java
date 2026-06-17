package org.example.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Holds the single, application-wide Hibernate {@link SessionFactory}.
 * The factory is expensive to build, so it is created once and reused.
 */
public final class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        // Byte Buddy (used internally by Hibernate) only "officially" supports
        // JDKs up to its build date. Running on a newer JDK (e.g. 25) requires
        // enabling its experimental support, otherwise startup fails.
        System.setProperty("net.bytebuddy.experimental", "true");
        try {
            // Reads src/main/resources/hibernate.cfg.xml from the classpath.
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        if (!SESSION_FACTORY.isClosed()) {
            SESSION_FACTORY.close();
        }
    }
}
