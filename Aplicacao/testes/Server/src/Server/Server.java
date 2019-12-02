package Server;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Server extends javax.swing.JFrame {
    //CONFIG
    private Socket connectedSocket;
    private static final int SERVERPORT_MSG = 32500;
    private static final int SERVERPORT_IMG = 32499;
    private static String SERVER_IP = "";
    private boolean parar = false;
    // ENVIAR
    private PrintWriter out = null;
    // RECEBER
    private ServerSocket serverSocket;
    private BufferedReader input = null;
    
    
    public Server() {
        initComponents();
        txtMeuIP.setText("192.168.15.3");
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtIP = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtMessage = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnEnviar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnReceber = new javax.swing.JButton();
        btnParar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtReceber = new javax.swing.JTextPane();
        txtMeuIP = new javax.swing.JLabel();
        btnEnviar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("ENVIAR:");

        jLabel2.setText("IP:");

        jLabel3.setText("Mensagem:");

        btnEnviar.setText("ENVIAR");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("RECEBER:");

        btnReceber.setText("RECEBER");
        btnReceber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceberActionPerformed(evt);
            }
        });

        btnParar.setText("PARAR DE RECEBER");
        btnParar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPararActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(txtReceber);

        txtMeuIP.setText("MEU IP:");

        btnEnviar1.setText("ENVIAR IMAGEM");
        btnEnviar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnEnviar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIP, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(211, 211, 211))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnReceber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnParar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEnviar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(105, 105, 105))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMeuIP)
                            .addComponent(jLabel1))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtMeuIP)
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEnviar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEnviar1)
                .addGap(42, 42, 42)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReceber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnParar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        try {
            SERVER_IP = txtIP.getText().toString();
            new Thread(new Runnable()
            {
                public void run() {
                    try {
                        InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                        connectedSocket = new Socket(serverAddr, SERVERPORT_MSG);
                        if(connectedSocket !=null){
                            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connectedSocket.getOutputStream())), true);
                            out.println(txtMessage.getText().toString());
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void btnReceberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceberActionPerformed
        new Thread(new Runnable() {
            @Override
                public void run() {
                    Socket socket = null;
                    try {
                        serverSocket = new ServerSocket(SERVERPORT_MSG);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            socket = serverSocket.accept();
                            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            final String message = input.readLine();
                            txtReceber.setText(txtReceber.getText().toString() + "Client Says: " + message + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                serverSocket = new ServerSocket(SERVERPORT_MSG);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        if(parar)
                        {
                            serverSocket=null;
                            break;
                        }
                    }
                }
        }).start();
        btnEnviar.setEnabled(false);
    }//GEN-LAST:event_btnReceberActionPerformed

    private void btnPararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPararActionPerformed
        parar = true; 
        btnEnviar.setEnabled(true);
    }//GEN-LAST:event_btnPararActionPerformed

    
    private void btnEnviar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviar1ActionPerformed
    try {
            SERVER_IP = txtIP.getText().toString();
            new Thread(new Runnable()
            {
                public void run() {
                    try {
                        int qtdImg = 21;
                        String pathImg="C:\\Temp\\SLIDES";
                        for (int x1 = 1; x1 <= qtdImg; x1++) {
                        if (connectedSocket == null || connectedSocket.isClosed()){
                                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                                connectedSocket = new Socket(serverAddr, SERVERPORT_IMG);
                            }
                        if (connectedSocket !=null) {
                                
                                    File input_file = new File(pathImg+"\\slide" + x1 + ".jpg");
                                    byte [] byteArray  = new byte [(int)input_file.length()];
                                    FileInputStream fis = new FileInputStream(input_file);
                                    BufferedInputStream bis = new BufferedInputStream(fis);
                                    bis.read(byteArray,0,byteArray.length);
                                    OutputStream os = connectedSocket.getOutputStream();
                                    os.write(byteArray,0,byteArray.length);
                                    os.flush();
                                    Thread.sleep(200);
                                    System.out.println("ENVIOU " + x1);
                                }
                                connectedSocket.close();
                            }
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnEnviar1ActionPerformed
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Server().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnEnviar1;
    private javax.swing.JButton btnParar;
    private javax.swing.JButton btnReceber;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtIP;
    private javax.swing.JTextField txtMessage;
    private javax.swing.JLabel txtMeuIP;
    private javax.swing.JTextPane txtReceber;
    // End of variables declaration//GEN-END:variables
}
