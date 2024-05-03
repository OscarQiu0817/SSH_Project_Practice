package tool;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory factory = buildSessionFactory();
    private static SessionFactory buildSessionFactory() {
        try {
    		Configuration config = new Configuration();
    		config = config.configure();
    		
        	return config.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static SessionFactory getSessionFactory() {
        return factory;
    }
    public static void closeSessionFactory() {
    	if(factory!=null) {
    		factory.close();
    	}
    }
}
