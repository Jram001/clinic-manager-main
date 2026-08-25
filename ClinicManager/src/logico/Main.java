package logico;

import visual.Login;

public class Main {

    private static Servidor servidor;
    private static boolean esServidor = false;

    public static void main(String[] args) {

        if (args.length > 0 && args[0].equalsIgnoreCase("servidor")) {
            ejecutarComoServidor();
        } else {
            ejecutarModoStandalone();
        }
    }

    private static void ejecutarComoServidor() {
        esServidor = true;
        int puerto = 7000;

        System.out.println("=========================================");
        System.out.println("   SISTEMA DE CLINICA MEDICA - SERVIDOR");
        System.out.println("=========================================");

        servidor = Servidor.getInstance(puerto);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apagando servidor...");
            if (servidor != null) {
                servidor.detener();
            }
        }));

        servidor.iniciar();
    }

    private static void ejecutarModoStandalone() {
        System.out.println("=========================================");
        System.out.println("   SISTEMA DE CLINICA MEDICA - STANDALONE");
        System.out.println("=========================================");

        Login dialog = new Login();
        dialog.setVisible(true);
    }

    public static boolean esServidor() {
        return esServidor;
    }
}