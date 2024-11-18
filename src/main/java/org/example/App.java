package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Set;

public class App {
    // JPA Configuration (USE SESSION)
    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    static EntityManager entityManager = entityManagerFactory.createEntityManager();
    // Start the transaction to input data
    public static void main( String[] args ) {
        entityManager.getTransaction().begin();

        final int EMPLOYEE_AMOUNT = 3;
        System.out.println("What is going on?");

        // Using setters
        DepartmentClass dept = new DepartmentClass();
        dept.setId(50);
        dept.setUbicacion("Palma");
        dept.setNombre("I+D");
        // Persist
        entityManager.persist(dept);

        // Using constructor
        DepartmentClass dept2 = new DepartmentClass("Research", "Manacor", 51);
        entityManager.persist(dept2);

        // Employees creation
        String[] jobPositions = {"Jefe", "Analista", "Programador"};
        createEmployees(EMPLOYEE_AMOUNT, jobPositions, dept);

        changeDepartment(dept);

        // End the transaction. Consider ending it every time an employee/department is added
        // Free up resources
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();
    }

    static void createEmployees(int amount, String[] jobPositions, DepartmentClass dept) {
        for (int i = 0; i < amount; i++) {
            EmployeeClass employee = new EmployeeClass();
            employee.setNombre("Employee" + i);
            employee.setPuesto(jobPositions[i]);
            employee.setDepno(dept);
            entityManager.persist(employee);
        }
    }

    static void changeDepartment(DepartmentClass dept) {
        Set<EmployeeClass> employeeGroup = dept.getEmpleadosGroup();
        for (EmployeeClass employee : employeeGroup) {
            System.out.println(employee.getNombre());
        }
    }
}
