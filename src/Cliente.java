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
import java.awt.event.*;
import java.io.*;
import java.net.*;

/**
 * Clase principal que inicia el cliente de chat.
 * Se pretende tener una comunicación entre varios clientes mediante el server
 */
public class Cliente {

    public static void main(String[] args) {
        MarcoCliente mimarco = new MarcoCliente();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

/**
 * Clase que representa la ventana del cliente de chat.
 */
class MarcoCliente extends JFrame {

    /**
     * Constructor que inicializa la ventana del cliente.
     */
    public MarcoCliente() {
        setBounds(600, 300, 280, 350);
        LaminaMarcoCliente milamina = new LaminaMarcoCliente();
        add(milamina);
        setVisible(true);
    }
}

/**
 * Clase que representa la interfaz gráfica del cliente de chat.
 */
class LaminaMarcoCliente extends JPanel {

    private JTextField campo1, nick;
    private JButton miboton;
    private JTextArea campochat;
    private int port;

    /**
     * Constructor que inicializa la interfaz gráfica del cliente.
     */
    public LaminaMarcoCliente() {
        nick = new JTextField(10);
        add(nick);

        JLabel texto = new JLabel("Chat");
        add(texto);

        campochat = new JTextArea(12, 20);
        JScrollPane scrollPane = new JScrollPane(campochat);
        add(scrollPane);

        campo1 = new JTextField(20);
        add(campo1);

        miboton = new JButton("Enviar");
        miboton.addActionListener(new EnviaTexto());
        add(miboton);

        port = findAvailablePort();
        campochat.append("Cliente escuchando en el puerto: " + port + "\n");

        setBackground(new Color(173, 216, 230));

        Thread hilo = new Thread(new RecibeMensajes());
        hilo.start();
    }

    /**
     * Clase interna que maneja el evento de enviar mensajes.
     */
    private class EnviaTexto implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Socket misocket = new Socket("127.0.0.1", 9999);
                ObjectOutputStream datos_enviados = new ObjectOutputStream(misocket.getOutputStream());

                DatosEnviados datos = new DatosEnviados();
                datos.setNick(nick.getText());
                datos.setMensaje(campo1.getText());

                datos_enviados.writeObject(datos);
                misocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Clase interna que maneja la recepción de mensajes en segundo plano.
     */
    private class RecibeMensajes implements Runnable {
        public void run() {
            try {
                ServerSocket cliente = new ServerSocket(port);

                while (true) {
                    Socket socket = cliente.accept();
                    ObjectInputStream flujoentrada = new ObjectInputStream(socket.getInputStream());
                    DatosEnviados paqueterecibido = (DatosEnviados) flujoentrada.readObject();
                    campochat.append("\n" + paqueterecibido.getNick() + " : " + paqueterecibido.getMensaje());
                    socket.close();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para encontrar un puerto disponible dinámicamente.
     * @return Puerto disponible.
     */
    private int findAvailablePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
