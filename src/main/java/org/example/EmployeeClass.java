package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "empleado")
public class EmployeeClass {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empleado_id_gen")
    @SequenceGenerator(name = "empleado_id_gen", sequenceName = "empleado_empno_seq", allocationSize = 1)
    @Column(name = "empno", nullable = false)
    private Integer id;

    @Column(name = "nombre", length = 10)
    private String nombre;

    @Column(name = "puesto", length = 15)
    private String puesto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depno")
    private DepartmentClass depno;

    // Print information inside the Object
    @Override
    public String toString() {
        return "ID: " + id + " | Nombre: " + nombre + " | Puesto: " + puesto;
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

    public String getPuesto() {
        return puesto;
    }
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public DepartmentClass getDepno() {
        return depno;
    }
    public void setDepno(DepartmentClass depno) {
        this.depno = depno;
    }
}