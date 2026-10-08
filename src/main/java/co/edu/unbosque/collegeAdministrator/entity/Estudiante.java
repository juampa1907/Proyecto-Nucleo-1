package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ESTUDIANTE")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
public class Estudiante extends Usuario{


    private List<Materia>  materiasPlanDeEstudio;

    private Integer numeroSemestre;

    private Integer numeroCreditosCursados;

}
