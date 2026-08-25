package logico;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Manejador de cliente para el servidor
 * Gestiona la comunicacion con un cliente conectado
 * Sigue el patron Command para procesar comandos
 */
public class ManejadorCliente implements Runnable {
    private Socket clienteSocket;
    private Clinica clinica;
    private Servidor servidor;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String direccionCliente;
    private boolean conectado;

    public ManejadorCliente(Socket socket, Clinica clinica, Servidor servidor) {
        if (socket == null || clinica == null || servidor == null) {
            throw new IllegalArgumentException("Los parametros no pueden ser nulos");
        }
        this.clienteSocket = socket;
        this.clinica = clinica;
        this.servidor = servidor;
        this.direccionCliente = socket.getInetAddress().getHostAddress();
        this.conectado = true;
    }

    @Override
    public void run() {
        System.out.println("Hilo iniciado para cliente: " + direccionCliente);
        
        try {
            inicializarStreams();
            enviarMensajeBienvenida();
            procesarComandos();
            
        } catch (EOFException e) {
            System.out.println("Cliente desconectado (EOF): " + direccionCliente);
        } catch (SocketException e) {
            System.out.println("Cliente desconectado: " + direccionCliente);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error con cliente " + direccionCliente + ": " + e.getMessage());
        } finally {
            desconectar();
        }
    }
    
    private void inicializarStreams() throws IOException {
        output = new ObjectOutputStream(clienteSocket.getOutputStream());
        output.flush();
        input = new ObjectInputStream(clienteSocket.getInputStream());
    }
    
    private void enviarMensajeBienvenida() throws IOException {
        output.writeObject("CONEXION_EXITOSA:Bienvenido al Sistema Doctor+");
        output.flush();
    }
    
    private void procesarComandos() throws IOException, ClassNotFoundException {
        while (conectado && !clienteSocket.isClosed()) {
            Object comandoObj = input.readObject();
            
            if (comandoObj instanceof String) {
                String comando = (String) comandoObj;
                System.out.println("Comando de " + direccionCliente + ": " + comando);
                procesarComando(comando);
            }
        }
    }
    
    private void procesarComando(String comando) {
        try {
            switch (comando) {
                case "AGREGAR_MEDICO":
                    agregarEntidad((Medico) input.readObject(), "Medico", "ACTUALIZAR_MEDICOS");
                    break;
                    
                case "BUSCAR_MEDICO_POR_CEDULA":
                    buscarMedicoPorCedula();
                    break;
                    
                case "AGREGAR_PACIENTE":
                    agregarEntidad((Paciente) input.readObject(), "Paciente", "ACTUALIZAR_PACIENTES");
                    break;
                    
                case "BUSCAR_PACIENTE_POR_CEDULA":
                    buscarPacientePorCedula();
                    break;
                    
                case "AGENDAR_CITA":
                    agregarEntidad((Cita) input.readObject(), "Cita", "ACTUALIZAR_CITAS");
                    break;
                    
                case "OBTENER_MEDICOS":
                    output.writeObject(clinica.getMedicos());
                    break;
                    
                case "OBTENER_PACIENTES":
                    output.writeObject(clinica.getPacientes());
                    break;
                    
                case "OBTENER_CITAS_ACTIVAS":
                	ArrayList<Cita> citasActivas = new ArrayList<>();
                    for (Cita c : clinica.getCitas()) {
                        if (c != null && c.isEsActivo()) {
                            citasActivas.add(c);
                        }
                    }
                    output.writeObject(citasActivas);
                    break;
                    
                case "OBTENER_MEDICOS_DISPONIBLES":
                    LocalDate fecha = (LocalDate) input.readObject();
                    output.writeObject(clinica.getMedicosDisponibles(fecha));
                    break;
                    
                case "AGREGAR_CONSULTA":
                    agregarConsulta();
                    break;
                    
                case "AGREGAR_ENFERMEDAD_VIGILADA":
                    agregarEntidad((EnfermedadBajoVigilancia) input.readObject(), 
                                  "Enfermedad vigilada", "ACTUALIZAR_ENFERMEDADES");
                    break;
                    
                case "OBTENER_ENFERMEDADES_VIGILADAS":
                    output.writeObject(clinica.getEnfermedadesVigiladas());
                    break;
                    
                case "OBTENER_CATALOGO_VACUNAS":
                    output.writeObject(clinica.getCatalogoVacunas());
                    break;
                    
                case "EXISTE_PACIENTE":
                    String cedula = (String) input.readObject();
                    boolean existe = clinica.verificarSiPacienteExiste(cedula);
                    output.writeObject(existe);
                    break;
                    
                case "BROADCAST_MENSAJE":
                    String mensaje = (String) input.readObject();
                    System.out.println("Broadcast de " + direccionCliente + ": " + mensaje);
                    servidor.broadcast("MENSAJE:" + direccionCliente + ": " + mensaje, this);
                    break;
                    
                case "HACER_BACKUP":
                    boolean backupExitoso = servidor.guardarBackup();
                    output.writeObject(backupExitoso);
                    if (backupExitoso) {
                        System.out.println("Backup solicitado por: " + direccionCliente);
                    }
                    break;
                    
                case "PING":
                    output.writeObject("PONG");
                    break;
                    
                case "DESCONECTAR":
                    output.writeObject("DESCONEXION_EXITOSA");
                    desconectar();
                    break;
                    
                default:
                    output.writeObject("ERROR:COMANDO_DESCONOCIDO");
                    break;
            }
            output.flush();
        } catch (IOException e) {
            System.err.println("Error de I/O procesando comando: " + e.getMessage());
            try {
                output.writeObject("ERROR:Error de comunicacion");
                output.flush();
            } catch (IOException ex) {
                System.err.println("No se pudo enviar mensaje de error al cliente");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error de deserializacion: " + e.getMessage());
            try {
                output.writeObject("ERROR:Formato de datos invalido");
                output.flush();
            } catch (IOException ex) {
                System.err.println("No se pudo enviar mensaje de error al cliente");
            }
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            try {
                output.writeObject("ERROR:" + e.getMessage());
                output.flush();
            } catch (IOException ex) {
                System.err.println("No se pudo enviar mensaje de error al cliente");
            }
        }
    }
    
    public void enviarMensajeBroadcast(String mensaje) {
        try {
            if (output != null && conectado) {
                output.writeObject("BROADCAST:" + mensaje);
                output.flush();
            }
        } catch (IOException e) {
            System.err.println("Error enviando broadcast a " + direccionCliente);
        }
    }
    
    private void desconectar() {
        if (!conectado) {
            return; // Ya desconectado
        }
        
        conectado = false;
        
        cerrarRecursos();
        servidor.removerCliente(this);
        System.out.println("Cliente desconectado: " + direccionCliente);
    }
    
    private void cerrarRecursos() {
        try {
            if (input != null) input.close();
        } catch (IOException e) {
            System.err.println("Error cerrando input: " + e.getMessage());
        }
        
        try {
            if (output != null) output.close();
        } catch (IOException e) {
            System.err.println("Error cerrando output: " + e.getMessage());
        }
        
        try {
            if (clienteSocket != null && !clienteSocket.isClosed()) {
                clienteSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error cerrando socket: " + e.getMessage());
        }
    }
    
    public String getDireccionCliente() {
        return direccionCliente;
    }
    
    // ============ METODOS HELPER PARA ELIMINAR REDUNDANCIA ============
    
    /**
     * Metodo generico para agregar entidades (Medico, Paciente, Cita, etc.)
     */
    private void agregarEntidad(Object entidad, String tipoEntidad, String mensajeBroadcast) 
            throws IOException {
        if (entidad == null) {
            output.writeObject(false);
            return;
        }
        
        // Agregar segun el tipo
        if (entidad instanceof Medico) {
            clinica.getMedicos().add((Medico) entidad);
        } else if (entidad instanceof Paciente) {
            clinica.getPacientes().add((Paciente) entidad);
        } else if (entidad instanceof Cita) {
            clinica.getCitas().add((Cita) entidad);
        } else if (entidad instanceof EnfermedadBajoVigilancia) {
            clinica.getEnfermedadesVigiladas().add((EnfermedadBajoVigilancia) entidad);
        }
        
        output.writeObject(true);
        System.out.println(tipoEntidad + " agregado por: " + direccionCliente);
        servidor.guardarBackup();
        servidor.broadcast(mensajeBroadcast, this);
    }
    
    private void buscarMedicoPorCedula() throws IOException, ClassNotFoundException {
        String cedula = (String) input.readObject();
        Medico encontrado = null;
        
        for (Medico m : clinica.getMedicos()) {
            if (m.getCedula().equals(cedula) && m.isActivo()) {
                encontrado = m;
                break;
            }
        }
        
        output.writeObject(encontrado);
    }
    
    private void buscarPacientePorCedula() throws IOException, ClassNotFoundException {
        String cedula = (String) input.readObject();
        Paciente encontrado = null;
        
        for (Paciente p : clinica.getPacientes()) {
            if (p.getCedula().equals(cedula) && p.isActivo()) {
                encontrado = p;
                break;
            }
        }
        
        output.writeObject(encontrado);
    }
    
    private void agregarConsulta() throws IOException, ClassNotFoundException {
        Consulta consulta = (Consulta) input.readObject();
        
        if (consulta != null) {
            clinica.getConsultas().add(consulta);
            
            if (consulta.getPaciente() != null) {
                consulta.getPaciente().agregarConsulta(consulta);
            }
            
            output.writeObject(true);
            System.out.println("Consulta agregada por: " + direccionCliente);
            servidor.guardarBackup();
            servidor.broadcast("ACTUALIZAR_CONSULTAS", this);
        } else {
            output.writeObject(false);
        }
    }
}