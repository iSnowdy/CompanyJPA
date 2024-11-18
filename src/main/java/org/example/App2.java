package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Set;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

public class App2 {
    // Start the transaction to input data
    public static void main( String[] args ) {
        SessionFactory sessionFactory = null;
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        final int EMPLOYEE_AMOUNT = 3;
        System.out.println("Session open? " + session.isOpen());
        // Using setters
        DepartmentClass dept = new DepartmentClass();
        dept.setId(50);
        dept.setUbicacion("Palma");
        dept.setNombre("I+D");

        System.out.println(dept);

        // Using constructor
        DepartmentClass dept2 = new DepartmentClass("Research", "Manacor", 51);
        System.out.println(dept2);

        for (EmployeeClass employeeClass : dept.getEmpleadosGroup()) {
            System.out.println(employeeClass);
        }
        // Employees creation
        String[] jobPositions = {"Jefe", "Analista", "Programador"};
        //createEmployees(EMPLOYEE_AMOUNT, jobPositions, dept, session);

        EmployeeClass e1 = new EmployeeClass();
        e1.setNombre("Jose");
        e1.setPuesto(jobPositions[0]);
        e1.setDepno(dept);

        EmployeeClass e2 = new EmployeeClass();
        e2.setNombre("Pepe");
        e2.setPuesto(jobPositions[1]);
        e2.setDepno(dept);

        EmployeeClass e3 = new EmployeeClass();
        e3.setNombre("Luis");
        e3.setPuesto(jobPositions[2]);
        e3.setDepno(dept);

        // Change the department number
        // In order to do this, since we can't modify the ID of the Department without passing a Department
        // Class, I create a whole new Department which is empty and only has the department ID that we want
        // to change to employee to. However, it is important to not persist this "new" department
        DepartmentClass dept3 = new DepartmentClass();
        dept3.setId(10);
        e3.setDepno(dept3);

        // Persist
        session.persist(dept);
        session.persist(dept2);
        session.persist(e1);
        session.persist(e2);
        session.persist(e3);
        transaction.commit();

        // Free resources
        session.close();
        sessionFactory.close();
        System.exit(0);
    }

    static void createEmployees(int amount, String[] jobPositions, DepartmentClass dept, Session session) {

        System.out.println("Inside createEmployees method");
        System.out.println(dept);

        for (int i = 0; i < amount; i++) {
            EmployeeClass employee = new EmployeeClass();
            employee.setId(15 + i);
            employee.setNombre("Employee" + (i + 1));
            employee.setPuesto(jobPositions[i]);
            employee.setDepno(dept);
            session.persist(employee);
            System.out.println(employee);
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