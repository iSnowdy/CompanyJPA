package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Set;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

public class App3 {
    // Session and Transaction configuration
    public static Session session;
    public static Transaction tx;
    public static SessionFactory sf = null;


    public static void main( String[] args ) {

        try {
            sf = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session = sf.openSession();
        tx = session.beginTransaction();

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
        createEmployees(EMPLOYEE_AMOUNT, jobPositions, dept);

        changeDepartment(dept);
        // Persist
        session.persist(dept);
        session.persist(dept2);
        tx.commit();
    }

    static void createEmployees(int amount, String[] jobPositions, DepartmentClass dept) {

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

    static void changeDepartment(DepartmentClass dept) {
        Set<EmployeeClass> employeeGroup = dept.getEmpleadosGroup();
        for (EmployeeClass employee : employeeGroup) {
            System.out.println(employee.getNombre());
        }
    }
}
