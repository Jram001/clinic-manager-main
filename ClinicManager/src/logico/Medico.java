package logico;

import java.util.ArrayList;

public class Medico extends Persona {
    private static final long serialVersionUID = 8332914665293201379L;
    private String especialidad;
    private ArrayList<Cita> citas;
    private int maxCitas;
    private Departamento departamento;

    public Medico(String id, String nombre, String apellido, int edad, String cedula, String sexo, String especialidad,
            int maxCitas) {
        super(id, nombre, apellido, edad, cedula, sexo);
        this.especialidad = especialidad;
        this.maxCitas = maxCitas;
        this.citas = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public ArrayList<Cita> getCitas() {
        return citas;
    }

    public int getMaxCitas() {
        return maxCitas;
    }

    public void setMaxCitas(int maxCitas) {
        this.maxCitas = maxCitas;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

}
