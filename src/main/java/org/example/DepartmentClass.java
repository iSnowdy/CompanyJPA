package org.example;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "departamento")
public class DepartmentClass {
    @Id
    @Column(name = "depno", nullable = false)
    private Integer id;

    public DepartmentClass() {}

    public DepartmentClass(String name, String location, Integer number) {
        setNombre(name);
        setUbicacion(location);
        setId(number);
    }

    @Column(name = "nombre", length = 14)
    private String nombre;

    @Column(name = "ubicacion", length = 13)
    private String ubicacion;

    @OneToMany(mappedBy = "depno")
    private Set<org.example.EmployeeClass> empleadosGroup = new LinkedHashSet<>();

    // Print information inside the Object
    @Override
    public String toString() {
        return "ID: " +getId() + " | Name: " + getNombre() + " | Ubicacion: " + getUbicacion();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Set<org.example.EmployeeClass> getEmpleadosGroup() {
        return empleadosGroup;
    }
    public void setEmpleadosGroup(Set<org.example.EmployeeClass> empleadosGroup) {
        this.empleadosGroup = empleadosGroup;
    }
}