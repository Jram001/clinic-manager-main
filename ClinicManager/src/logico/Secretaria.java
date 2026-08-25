package logico;

public class Secretaria extends Persona {
    private static final long serialVersionUID = 1L;

    // Atributos asumidos por la instrucción ya que Persona no los tiene

    // Atributos específicos del rol
    private String turno;
    private double salario;
    private String extensionTelefonica;

    public Secretaria(String id, String cedula, String nombre, String apellido, String telefono, String direccion,
            String sexo, int edad, String turno, double salario, String extensionTelefonica) {
        super(id, nombre, apellido, edad, cedula, sexo);
        this.turno = turno;
        this.salario = salario;
        this.extensionTelefonica = extensionTelefonica;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getExtensionTelefonica() {
        return extensionTelefonica;
    }

    public void setExtensionTelefonica(String extensionTelefonica) {
        this.extensionTelefonica = extensionTelefonica;
    }

}
