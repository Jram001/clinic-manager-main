package logico;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Cliente para comunicacion con servidor mediante sockets
 * Maneja la conexion y comandos hacia el servidor de la clinica
 */
public class Cliente {
    private static final int TIMEOUT_CONEXION = 5000; // 5 segundos

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String host;
    private int puerto;
    private boolean conectado;
    private Thread hiloEscucha;

    public Cliente(String host, int puerto) {
        validarParametros(host, puerto);
        this.host = host;
        this.puerto = puerto;
        this.conectado = false;
    }

    private void validarParametros(String host, int puerto) {
        if (host == null || host.trim().isEmpty()) {
            throw new IllegalArgumentException("El host no puede estar vacio");
        }
        if (puerto < 1 || puerto > 65535) {
            throw new IllegalArgumentException("El puerto debe estar entre 1 y 65535");
        }
    }

    public boolean conectar() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, puerto), TIMEOUT_CONEXION);

            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();

            input = new ObjectInputStream(socket.getInputStream());
            conectado = true;

            String respuesta = (String) input.readObject();
            System.out.println("Servidor: " + respuesta);

            iniciarHiloEscucha();

            System.out.println("Conectado al servidor: " + host + ":" + puerto);
            return true;

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error conectando al servidor: " + e.getMessage());
            cerrarRecursos();
            return false;
        }
    }

    private void cerrarRecursos() {
        try {
            if (input != null)
                input.close();
            if (output != null)
                output.close();
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (IOException e) {
            System.err.println("Error cerrando recursos: " + e.getMessage());
        }
    }

    private void iniciarHiloEscucha() {
        hiloEscucha = new Thread(() -> {
            while (conectado && !socket.isClosed()) {
                try {
                    Object mensajeObj = input.readObject();
                    if (mensajeObj instanceof String) {
                        String mensaje = (String) mensajeObj;
                        procesarMensajeServidor(mensaje);
                    }
                } catch (EOFException | SocketException e) {
                    System.out.println("Conexion con servidor perdida");
                    break;
                } catch (IOException | ClassNotFoundException e) {
                    if (conectado) {
                        System.err.println("Error escuchando servidor: " + e.getMessage());
                    }
                    break;
                }
            }
            desconectar();
        });
        hiloEscucha.start();
    }

    private void procesarMensajeServidor(String mensaje) {
        System.out.println("Mensaje del servidor: " + mensaje);

        if (mensaje.startsWith("BROADCAST:")) {
            String contenido = mensaje.substring(10);
            System.out.println("Broadcast recibido: " + contenido);
        } else if (mensaje.equals("SERVIDOR_APAGADO:El servidor se esta apagando")) {
            System.out.println("El servidor se esta apagando. Desconectando...");
            desconectar();
        }
    }

    public void desconectar() {
        if (!conectado) {
            return; // Ya desconectado
        }

        conectado = false;

        try {
            // Detener hilo de escucha
            if (hiloEscucha != null && hiloEscucha.isAlive()) {
                hiloEscucha.interrupt();
            }

            // Enviar comando de desconexion
            if (output != null) {
                try {
                    output.writeObject("DESCONECTAR");
                    output.flush();
                } catch (IOException e) {
                    // Ignorar errores al desconectar
                }
            }

            cerrarRecursos();
            System.out.println("Desconectado del servidor");

        } catch (Exception e) {
            System.err.println("Error desconectando: " + e.getMessage());
        }
    }

    public boolean agregarMedico(Medico medico) {
        return enviarEntidad("AGREGAR_MEDICO", medico, "agregando medico");
    }

    public Medico buscarMedicoPorCedula(String cedula) {
        return (Medico) buscarPorCedula("BUSCAR_MEDICO_POR_CEDULA", cedula, "buscando medico");
    }

    public boolean agregarPaciente(Paciente paciente) {
        return enviarEntidad("AGREGAR_PACIENTE", paciente, "agregando paciente");
    }

    public Paciente buscarPacientePorCedula(String cedula) {
        return (Paciente) buscarPorCedula("BUSCAR_PACIENTE_POR_CEDULA", cedula, "buscando paciente");
    }

    public boolean agendarCita(Cita cita) {
        return enviarEntidad("AGENDAR_CITA", cita, "agendando cita");
    }

    public ArrayList<Medico> obtenerMedicos() {
        return obtenerLista("OBTENER_MEDICOS", "obteniendo medicos");
    }

    public ArrayList<Paciente> obtenerPacientes() {
        return obtenerLista("OBTENER_PACIENTES", "obteniendo pacientes");
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Medico> obtenerMedicosDisponibles(LocalDate fecha) {
        try {
            output.writeObject("OBTENER_MEDICOS_DISPONIBLES");
            output.writeObject(fecha);
            output.flush();
            return (ArrayList<Medico>) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error obteniendo medicos disponibles: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean agregarConsulta(Consulta consulta) {
        return enviarEntidad("AGREGAR_CONSULTA", consulta, "agregando consulta");
    }

    public boolean agregarEnfermedadVigilada(EnfermedadBajoVigilancia enfermedad) {
        return enviarEntidad("AGREGAR_ENFERMEDAD_VIGILADA", enfermedad, "agregando enfermedad");
    }

    public ArrayList<EnfermedadBajoVigilancia> obtenerEnfermedadesVigiladas() {
        return obtenerLista("OBTENER_ENFERMEDADES_VIGILADAS", "obteniendo enfermedades");
    }

    public ArrayList<Vacuna> obtenerCatalogoVacunas() {
        return obtenerLista("OBTENER_CATALOGO_VACUNAS", "obteniendo vacunas");
    }

    public boolean existePaciente(String cedula) {
        try {
            output.writeObject("EXISTE_PACIENTE");
            output.writeObject(cedula);
            output.flush();
            return (boolean) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error verificando paciente: " + e.getMessage());
            return false;
        }
    }

    public boolean testConexion() {
        try {
            output.writeObject("PING");
            output.flush();
            String respuesta = (String) input.readObject();
            return "PONG".equals(respuesta);
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    public boolean solicitarBackup() {
        if (!estaConectado()) {
            System.err.println("No hay conexion activa con el servidor");
            return false;
        }

        try {
            output.writeObject("HACER_BACKUP");
            output.flush();

            Object respuesta = input.readObject();
            if (respuesta instanceof Boolean) {
                return (Boolean) respuesta;
            }

            System.err.println("Respuesta inesperada del servidor: " + respuesta);
            return false;

        } catch (IOException e) {
            System.err.println("Error de comunicacion solicitando backup: " + e.getMessage());
            desconectar();
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("Error deserializando respuesta: " + e.getMessage());
            return false;
        }
    }

    public void enviarBroadcast(String mensaje) {
        try {
            output.writeObject("BROADCAST_MENSAJE");
            output.writeObject(mensaje);
            output.flush();
        } catch (IOException e) {
            System.err.println("Error enviando broadcast: " + e.getMessage());
        }
    }

    public boolean estaConectado() {
        return conectado && socket != null && !socket.isClosed();
    }

    public ArrayList<Cita> obtenerCitas() {
        return obtenerLista("OBTENER_CITAS_ACTIVAS", "obteniendo citas");
    }

    // ============ METODOS HELPER PARA ELIMINAR REDUNDANCIA ============

    /**
     * Metodo generico para enviar entidades y obtener confirmacion boolean
     */
    private boolean enviarEntidad(String comando, Object entidad, String operacion) {
        if (!estaConectado()) {
            System.err.println("No hay conexion activa con el servidor");
            return false;
        }

        try {
            output.writeObject(comando);
            output.writeObject(entidad);
            output.flush();

            Object respuesta = input.readObject();
            return respuesta instanceof Boolean && (Boolean) respuesta;

        } catch (IOException e) {
            System.err.println("Error " + operacion + ": " + e.getMessage());
            desconectar();
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("Error deserializando respuesta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo generico para buscar por cedula
     */
    private Object buscarPorCedula(String comando, String cedula, String operacion) {
        if (!estaConectado()) {
            System.err.println("No hay conexion activa con el servidor");
            return null;
        }

        try {
            output.writeObject(comando);
            output.writeObject(cedula);
            output.flush();
            return input.readObject();

        } catch (IOException e) {
            System.err.println("Error " + operacion + ": " + e.getMessage());
            desconectar();
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("Error deserializando respuesta: " + e.getMessage());
            return null;
        }
    }

    /**
     * Metodo generico para obtener listas del servidor
     */
    @SuppressWarnings("unchecked")
    private <T> ArrayList<T> obtenerLista(String comando, String operacion) {
        if (!estaConectado()) {
            System.err.println("No hay conexion activa con el servidor");
            return new ArrayList<>();
        }

        try {
            output.writeObject(comando);
            output.flush();

            Object respuesta = input.readObject();
            if (respuesta instanceof ArrayList) {
                return (ArrayList<T>) respuesta;
            }

            System.err.println("Respuesta inesperada del servidor");
            return new ArrayList<>();

        } catch (IOException e) {
            System.err.println("Error " + operacion + ": " + e.getMessage());
            desconectar();
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            System.err.println("Error deserializando respuesta: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}