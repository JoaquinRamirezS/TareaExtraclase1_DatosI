import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Cliente {

    public static void main(String[] args) {
        MarcoCliente mimarco = new MarcoCliente();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class MarcoCliente extends JFrame {

    public MarcoCliente() {
        setBounds(600, 300, 280, 350);
        LaminaMarcoCliente milamina = new LaminaMarcoCliente();
        add(milamina);
        setVisible(true);
    }
}

class LaminaMarcoCliente extends JPanel {

    private JTextField campo1, nick;
    private JButton miboton;
    private JTextArea campochat;
    private int port;

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

    private int findAvailablePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
