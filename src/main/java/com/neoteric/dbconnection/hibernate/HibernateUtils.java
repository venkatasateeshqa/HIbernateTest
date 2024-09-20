package com.neoteric.dbconnection.hibernate;
import com.neoteric.dbconnection.entity.AadharEntity;
import com.neoteric.dbconnection.entity.AccountAddressEntity;
import com.neoteric.dbconnection.entity.AccountEntity;
import com.neoteric.dbconnection.entity.AddressEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;


import java.util.Properties;

public class HibernateUtils {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPersistence");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }

    public static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if (sessionFactory==null){
            Configuration configuration=new Configuration();
            Properties properties=new Properties();
            properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
            properties.put(Environment.URL, "jdbc:mysql://@localhost:3306/bank");
            properties.put(Environment.USER, "root");
            properties.put(Environment.PASS, "root");
            properties.put(Environment.DIALECT,"org.hibernate.dialect.MySQLDialect");

            properties.put(Environment.SHOW_SQL,true);
            configuration.setProperties(properties);
            configuration.addAnnotatedClass(AccountEntity.class)
                    .addAnnotatedClass(AccountAddressEntity.class)
                    .addAnnotatedClass(AddressEntity.class)
                    .addAnnotatedClass(AadharEntity.class);
            ServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            sessionFactory=configuration.buildSessionFactory(registry);
            System.out.println("Session created");
            }
        else {
            System.out.println("Session not created");
        }

        return sessionFactory;
    }
}
