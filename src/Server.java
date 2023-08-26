/**
 * Instituto Tecnológico de Costa Rica
 * Área de Ingenieria en Computadores
 * CE-1103 Estructuras y Algoritmos de Datos I
 * Prof:Luis Barboza Artavia
 * Estudiante:Joaquín Ramírez Sequeira
 * II Semestre,2023
 */



import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

/**
 * Clase principal que inicia el servidor de chat.
 * El objetivo es que funcione como un intermediario entre los clientes y
 * muestre sus msjs
 */

public class Server{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MarcoServidor());
    }
}

/**
 * Clase que representa la ventana del servidor de chat.
 */

class MarcoServidor extends JFrame implements Runnable {
    private JTextArea areatexto;


    /**
     * Constructor que inicializa la ventana del servidor.
     */
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

    /**
     * Método que inicia la ejecución del servidor en un hilo separado.
     */
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

                setBackground(new Color(173, 216, 230)); 


                areatexto.append("\n" + nick + " : " + mensaje );

                misocket.close();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Clase para representar los datos enviados por los clientes.
 */
class DatosEnviados implements Serializable {
    private String nick, ip, mensaje;

    /**
     * Obtiene el apodo del remitente.
     * @return Apodo del remitente.
     */
    public String getNick() {
        return nick;
    }

    /**
     * Establece el apodo del remitente.
     * @param nick Apodo del remitente.
     */
    public void setNick(String nick) {
        this.nick = nick;
    }


    /**
     * Obtiene la dirección IP del remitente.
     * @return Dirección IP del remitente.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Establece la dirección IP del remitente.
     * @param ip Dirección IP del remitente.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Obtiene el mensaje enviado por el remitente.
     * @return Mensaje del remitente.
     */
    public String getMensaje() {
        return mensaje;
    }


    /**
     * Establece el mensaje enviado por el remitente.
     * @param mensaje Mensaje del remitente.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
