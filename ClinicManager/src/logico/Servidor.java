package logico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Servidor de la clinica que maneja conexiones de clientes mediante sockets
 * Implementa patron Singleton para asegurar una unica instancia
 * Gestiona backups automaticos y comunicacion con multiples clientes
 */
public class Servidor {
    private static final String BACKUP_FILE = "backup_clinica.dat";
    private static final int INTERVALO_BACKUP_MINUTOS = 5;

    private ServerSocket serverSocket;
    private Clinica clinica;
    private boolean servidorActivo;
    private ScheduledExecutorService scheduler;
    private CopyOnWriteArrayList<ManejadorCliente> clientesConectados;
    private static Servidor instancia;

    private Servidor(int puerto) {
        validarPuerto(puerto);

        try {
            inicializarServidor(puerto);
            cargarBackup();
            programarBackupAutomatico();

            System.out.println("Servidor listo. Esperando conexiones de clientes...");

        } catch (IOException e) {
            System.err.println("Error fatal al iniciar servidor: " + e.getMessage());
            throw new RuntimeException("No se pudo iniciar el servidor", e);
        }
    }

    private void validarPuerto(int puerto) {
        if (puerto < 1 || puerto > 65535) {
            throw new IllegalArgumentException("Puerto invalido: " + puerto);
        }
    }

    private void inicializarServidor(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        clinica = Clinica.getInstancia();
        servidorActivo = true;
        scheduler = Executors.newScheduledThreadPool(2);
        clientesConectados = new CopyOnWriteArrayList<>();

        System.out.println("========================================");
        System.out.println("Servidor de Clinica Medica iniciado");
        System.out.println("Puerto: " + puerto);
        System.out.println("IP: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("========================================");
    }

    public static Servidor getInstance(int puerto) {
        if (instancia == null) {
            instancia = new Servidor(puerto);
        }
        return instancia;
    }

    public void iniciar() {
        System.out.println("Servidor listo para aceptar conexiones...");

        while (servidorActivo) {
            try {
                Socket clienteSocket = serverSocket.accept();
                String direccionCliente = clienteSocket.getInetAddress().getHostAddress();
                int puertoCliente = clienteSocket.getPort();

                System.out.println("Nuevo cliente conectado: " + direccionCliente + ":" + puertoCliente);
                System.out.println("Total de clientes conectados: " + (clientesConectados.size() + 1));

                ManejadorCliente manejador = new ManejadorCliente(clienteSocket, clinica, this);
                clientesConectados.add(manejador);

                Executors.newSingleThreadExecutor().execute(manejador);

            } catch (IOException e) {
                if (servidorActivo) {
                    System.err.println("Error aceptando cliente: " + e.getMessage());
                }
            }
        }
    }

    private void programarBackupAutomatico() {
        scheduler.scheduleAtFixedRate(
                this::guardarBackup,
                INTERVALO_BACKUP_MINUTOS,
                INTERVALO_BACKUP_MINUTOS,
                TimeUnit.MINUTES);

        System.out.println("Backup automatico programado cada " + INTERVALO_BACKUP_MINUTOS + " minutos");
    }

    public synchronized boolean guardarBackup() {
        try {
            FileOutputStream fileOut = new FileOutputStream(BACKUP_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(clinica);
            out.close();
            fileOut.close();

            System.out.println("Backup guardado exitosamente: " + BACKUP_FILE);
            return true;

        } catch (IOException e) {
            System.err.println("Error guardando backup: " + e.getMessage());
            return false;
        }
    }

    public synchronized boolean cargarBackup() {
        try {
            File file = new File(BACKUP_FILE);
            if (!file.exists()) {
                System.out.println("No existe archivo de backup, iniciando con datos nuevos");
                return false;
            }

            FileInputStream fileIn = new FileInputStream(BACKUP_FILE);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Clinica clinicaCargada = (Clinica) in.readObject();
            in.close();
            fileIn.close();

            if (clinicaCargada != null) {
                Clinica.setInstancia(clinicaCargada);
                this.clinica = clinicaCargada;

                System.out.println("Backup cargado exitosamente: " + BACKUP_FILE);
                System.out.println("Datos cargados:");
                System.out.println("- " + clinica.getMedicos().size() + " medicos");
                System.out.println("- " + clinica.getPacientes().size() + " pacientes");
                System.out.println("- " + clinica.getCitas().size() + " citas");
                System.out.println("- " + clinica.getConsultas().size() + " consultas");
                System.out.println("- " + clinica.getEnfermedadesVigiladas().size() + " enfermedades bajo vigilancia");
                System.out.println("- " + clinica.getCatalogoVacunas().size() + " vacunas en catalogo");

                return true;
            } else {
                System.out.println("Archivo de backup corrupto, iniciando con datos nuevos");
                return false;
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error cargando backup: " + e.getMessage());
            return false;
        }
    }

    public synchronized void removerCliente(ManejadorCliente cliente) {
        if (cliente == null) {
            return;
        }

        if (clientesConectados != null) {
            clientesConectados.remove(cliente);
            System.out.println("Cliente removido. Total conectados: " + clientesConectados.size());
        }
    }

    /**
     * Envia un mensaje broadcast a todos los clientes excepto el emisor
     */
    public void broadcast(String mensaje, ManejadorCliente emisor) {
        enviarBroadcast(mensaje, emisor, false);
    }

    /**
     * Envia un mensaje broadcast a todos los clientes conectados
     */
    public void broadcastATodos(String mensaje) {
        enviarBroadcast(mensaje, null, true);
    }

    /**
     * Metodo interno para enviar broadcasts, eliminando codigo duplicado
     */
    private void enviarBroadcast(String mensaje, ManejadorCliente emisor, boolean aTodos) {
        if (clientesConectados == null || mensaje == null) {
            return;
        }

        int enviados = 0;
        for (ManejadorCliente cliente : clientesConectados) {
            if (cliente != null && (aTodos || cliente != emisor)) {
                cliente.enviarMensajeBroadcast(mensaje);
                enviados++;
            }
        }

        if (enviados > 0) {
            String tipo = aTodos ? "general" : "selectivo";
            System.out.println("Broadcast " + tipo + " enviado a " + enviados + " clientes: " + mensaje);
        }
    }

    public void detener() {
        servidorActivo = false;

        guardarBackup();

        if (clientesConectados != null && !clientesConectados.isEmpty()) {
            broadcastATodos("SERVIDOR_APAGADO:El servidor se esta apagando");
        }

        if (scheduler != null) {
            scheduler.shutdown();
        }

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error cerrando servidor: " + e.getMessage());
        }

        System.out.println("Servidor detenido correctamente");
    }

    public Clinica getClinica() {
        return clinica;
    }

    public int getClientesConectados() {
        return clientesConectados != null ? clientesConectados.size() : 0;
    }
}