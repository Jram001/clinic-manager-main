package logico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;

public class Control implements Serializable {

    private static final long serialVersionUID = 8332914665293201379L;

    private static Control control; // Instancia
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private static Usuario loggedUsuario;

    private Control() {
        // Se inizializa arriba
    }

    public static Control getInstance() {
        if (control == null) {
            if (!cargarDelDisco() || control == null) {
                control = new Control();
            }
            if (control.usuarios == null) {
                control.usuarios = new ArrayList<>();
            }
            if (control.usuarios.isEmpty()) {
                Usuario aux = new Usuario("Administrador", md5("123456"), "administrador", "0");
                Usuario auxSec = new Usuario("Secretaria", md5("123456"), "secretaria", "1");
                control.regUser(aux);
                control.regUser(auxSec);
                control.guardarAlDisco();
            }
        }
        if (!control.userNameExists("Medico")) {
            Usuario auxMed = new Usuario("Medico", md5("123456"), "medico", "MED1");
            control.regUser(auxMed);
            control.guardarAlDisco();
        }
        return control;
    }

    public static void setInstancia(Control c) {
        control = c;
    }

    public ArrayList<Usuario> getMisUsuarios() {
        return usuarios;
    }

    public void setMisUsers(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public static Control getControl() {
        return control;
    }

    public static void setControl(Control control) {
        Control.control = control;
    }

    public static Usuario getLoggedUsuario() {
        return loggedUsuario;
    }

    public static void setLoginUser(Usuario loggedUsuario) {
        Control.loggedUsuario = loggedUsuario;
    }

    public void regUser(Usuario usuario) {
        usuarios.add(usuario);
    }

    public boolean userNameExists(String username) {
        if (username == null)
            return false;
        for (Usuario u : usuarios) {
            if (username.equalsIgnoreCase(u.getNombreUsuario())) {
                return true;
            }
        }
        return false;
    }

    public boolean confirmLogin(String nombreUsuario, String clave) {
        if (nombreUsuario == null || clave == null)
            return false;
        String userNorm = nombreUsuario.trim();
        for (Usuario u : usuarios) {
            if (u == null || u.getNombreUsuario() == null)
                continue;
            if (u.getNombreUsuario().trim().equalsIgnoreCase(userNorm)) {
                String claveStored = u.getClave();
                String hashedClave = md5(clave);

                if (hashedClave.equals(claveStored)) {
                    loggedUsuario = u;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean guardarAlDisco() {
        File file = new File("usuarios.dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(getInstance());
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public String buscarUsuarioId(String nombreUsuario) {
        if (nombreUsuario == null)
            return null;
        String normalized = nombreUsuario.trim().toLowerCase();
        for (Usuario u : usuarios) {
            if (u == null || u.getNombreUsuario() == null)
                continue;
            if (u.getNombreUsuario().trim().equalsIgnoreCase(normalized)) {
                return u.getLinkId();
            }
        }
        return null;
    }

    public Usuario buscarUsuario(String nombreUsuario) {
        if (nombreUsuario == null)
            return null;
        String normalized = nombreUsuario.trim();
        for (Usuario u : usuarios) {
            if (u == null || u.getNombreUsuario() == null)
                continue;
            if (u.getNombreUsuario().trim().equalsIgnoreCase(normalized)) {
                return u;
            }
        }
        return null;
    }

    public static boolean cargarDelDisco() {
        File file = new File("usuarios.dat");
        if (!file.exists())
            return false;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Control c = (Control) ois.readObject();
            setControl(c);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static String md5(String clave) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(clave.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest)
                sb.append(String.format("%02x", b & 0xff));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error calculando MD5", e);
        }

    }

    public void borrarUsuarioPorLinkId(String linkId) {
        for (Usuario u : usuarios) {
            if (u.getLinkId().equalsIgnoreCase(linkId)) {
                usuarios.remove(u);
            }
        }
    }

    public void borrarUsuarioPorNombreUsuario(String nombreUsuario) {
        for (Usuario u : usuarios) {
            if (u.getNombreUsuario().equalsIgnoreCase(nombreUsuario)) {
                usuarios.remove(u);
            }
        }
    }

    public static void logout() {
        loggedUsuario = null;
    }

}