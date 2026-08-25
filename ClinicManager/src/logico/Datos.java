package logico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class Datos {

	private Datos() {}
	
	public static void guardar() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("datos.dat"))) {
            oos.writeObject(Clinica.getInstancia());
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static String hacerBackupLocal(String rutaCompleta) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaCompleta));
            oos.writeObject(Clinica.getInstancia());
            oos.flush();
            oos.close();
            System.out.println("Backup local creado: " + rutaCompleta);
            return rutaCompleta;
        } catch (IOException e) {
            System.err.println("Error creando backup local: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
	
    public static void cargar() {
        File f = new File("datos.dat");
        if (!f.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = ois.readObject();
            if (obj instanceof Clinica) {
                Clinica loaded = (Clinica) obj;
                Clinica.setInstancia(loaded);
            } else {
                System.err.println("El archivo no contiene un objeto Clinica valido.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	
	

}

