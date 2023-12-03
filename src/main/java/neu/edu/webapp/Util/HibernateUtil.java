package neu.edu.webapp.Util;


import neu.edu.webapp.Model.Account;
import neu.edu.webapp.Model.Assignment;
import neu.edu.webapp.Model.Submission;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.springframework.stereotype.Component;

import java.util.Properties;


@Component
public class HibernateUtil {

    private static SessionFactory sessionFactory;

//    @Value("${spring.datasource.username}")
//    private static String dbUser;
//
//    @Value("${spring.datasource.password}")
//    private static String pass;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/cloud?createDatabaseIfNotExist=true");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "Ssangramm@12");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");

                settings.put(Environment.SHOW_SQL, "true");

                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                settings.put(Environment.HBM2DDL_AUTO, "update");

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(Account.class);
                configuration.addAnnotatedClass(Assignment.class);
                configuration.addAnnotatedClass(Submission.class);


                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}





