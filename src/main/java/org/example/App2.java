package org.example;

import jakarta.persistence.PersistenceException;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

public class App2 {
    public static void main(String[] args) {
        // Open the SessionFactory and Session using a try-with-resources
        // They are opened from a utility class that returns sessions
        try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
             Session session = HibernateUtil.openSession(sessionFactory)) {

            Transaction transaction = session.beginTransaction();

            try {
                // Setter and Constructor initialization of departments
                DepartmentClass dept1 = createDepartment(50, "I+D", "Palma");
                DepartmentClass dept2 = new DepartmentClass("Research", "Manacor", 51);

                //System.out.println(dept1);
                //System.out.println(dept2);

                // Create employees and assign to department
                EmployeeClass emp1 = createEmployee("Jose", "Jefe", dept1);
                EmployeeClass emp2 = createEmployee("Pepe", "Analista", dept1);
                EmployeeClass emp3 = createEmployee("Luis", "Programador", dept1);

                // Reassign emp3 to a different department without persisting the new department
                DepartmentClass dept3 = new DepartmentClass();
                dept3.setId(10);
                emp3.setDepno(dept3);

                // Persist entities
                session.persist(dept1);
                session.persist(dept2);
                session.persist(emp1);
                session.persist(emp2);
                session.persist(emp3);
                // Commit the changes to the DB (if possible)
                transaction.commit();
                // No need to manually close the Objects because they are inside a try-with-resources
            } catch (IllegalStateException | PersistenceException e) {
                System.err.println("Error during transaction. Rolling back.");
                e.printStackTrace();
                transaction.rollback();
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("SessionFactory initialization failed.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static DepartmentClass createDepartment(int id, String name, String location) {
        DepartmentClass department = new DepartmentClass();
        department.setId(id);
        department.setNombre(name);
        department.setUbicacion(location);
        return department;
    }

    private static EmployeeClass createEmployee(String name, String position, DepartmentClass department) {
        EmployeeClass employee = new EmployeeClass();
        employee.setNombre(name);
        employee.setPuesto(position);
        employee.setDepno(department);
        return employee;
    }

    // Utility class for Hibernate session and transaction management.
    public static class HibernateUtil {
        private static final SessionFactory SESSION_FACTORY;

        static {
            try {
                SESSION_FACTORY = new Configuration().configure().buildSessionFactory();
            } catch (Throwable ex) {
                System.err.println("Initial SessionFactory creation failed: " + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }

        public static SessionFactory getSessionFactory() {
            return SESSION_FACTORY;
        }

        public static Session openSession(SessionFactory sessionFactory) {
            if (sessionFactory == null) {
                throw new IllegalStateException("SessionFactory is not initialized.");
            }
            return sessionFactory.openSession();
        }
    }
}


/*
https://www.digitalocean.com/community/tutorials/jpa-hibernate-annotations
https://medium.com/javarevisited/jpa-annotations-overview-cfdcfdb913f4

Java annotations are a form of metadata that is added to Java code. They give additional information to what
they are attached to.

    - @Entity. Specifies that a class is an entity extracted from a DB. It can be applied to Java classes,
    Enums or Interfaces.
    - @Table. It specifies what table from the DB is mapped to in the entity. Therefore, in order to map any
    relation (or table) from a DB to Java using Hibernate, we need both the @Entity and @Table annotations.
    - @Column. Maps the column from the DB to a variable inside the entity. The name of the column must match
    the name of the column inside the table.
        * nullable. Indicates if a variable can be null (true) or not (false)
        * length. In the case the datatype in the DB is a VARCHAR or TEXT, we need to specify what length it
        accepts given a String in Java.
    - @Id.  This annotation specifies that the specified @Column should be treated as a primary key.
    - @OneToMany. Indicates that a relationship is 1:N. From the class it is specified, to the one it is related
    (in that order).
    - @GeneratedValue. It will specify how the primary key (@Id) will be generated.
        * strategy. Optional value. It sets the key generation strategy that persistence Object will use to
        generate the primary key. Default value is AUTO.
        * generator. Optional value. It is the tag or name of that generator.
    - @SequenceGenerator. Allows us to specify/customize how the DB will generate the values indicated with the
    @GeneratedValue annotation.
        * sequenceName. Just as the generator value, it is optional and is used to name the sequence.
        * allocationSize. The amount of increment the generator will increase the value by. By default it is 50.
    - @ManyToOne. Indicates that a relationship is N:1. From the class it is specified to the one it is related,
    in that exact order.
        * fetch. There are 2 types of fetch. LAZY and EAGER.
            > LAZY. It will only retrieve data from the relationships (tables) when it is needed. In terms of efficiency,
            lazy is better since we do not use as much memory as with the eager type. Furthermore, in the vast majority
            of occasions, LAZY is the type of fetch by DEFAULT. The only exception to this rule is with the @ManyToOne
            relation (such as in our case). So when we have a @ManyToOne type of relation between two tables, the default
            type is EAGER.
            > EAGER. It will retrieve all the data and store it in memory. Although it is true that access to data is faster
            like this, it is not the most efficient way of fetching. Especially if we are dealing with huge amounts of
            information stored inside the DB.
    - @JoinColumn. It will specify the relation / column in the DB that is used for the @OneToMany or @ManyToOne
    annotations.

*/