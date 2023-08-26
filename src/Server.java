import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Server{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MarcoServidor());
    }
}

class MarcoServidor extends JFrame implements Runnable {
    private JTextArea areatexto;

    public MarcoServidor() {
        setBounds(1200, 300, 280, 350);
        JPanel milamina = new JPanel();
        milamina.setLayout(new BorderLayout());
        areatexto = new JTextArea();
        milamina.add(new JScrollPane(areatexto), BorderLayout.CENTER);
        add(milamina);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Thread mihilo = new Thread(this);
        mihilo.start();
    }

    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(9999);
            String nick, ip, mensaje;
            DatosEnviados datos_recibidos;

            while (true) {
                Socket misocket = servidor.accept();
                ObjectInputStream paquete_datos = new ObjectInputStream(misocket.getInputStream());
                datos_recibidos = (DatosEnviados) paquete_datos.readObject();
                nick = datos_recibidos.getNick();
                ip = datos_recibidos.getIp();
                mensaje = datos_recibidos.getMensaje();

                // Enviar mensaje a todos los clientes conectados (broadcast)
                areatexto.append("\n" + nick + " : " + mensaje + " para " + ip);

                misocket.close();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class DatosEnviados implements Serializable {
    private String nick, ip, mensaje;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
