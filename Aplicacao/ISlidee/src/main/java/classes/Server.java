    package classes;

import com.mycompany.maventest.TelaInicial;
    import java.awt.AWTException;
    import java.awt.image.BufferedImage;
    import java.io.BufferedInputStream;
    import java.io.BufferedReader;
    import java.io.BufferedWriter;
    import java.io.ByteArrayInputStream;
    import java.io.File;
    import java.io.FileInputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.io.OutputStream;
    import java.io.OutputStreamWriter;
    import java.io.PrintWriter;
    import java.net.InetAddress;
    import java.net.ServerSocket;
    import java.net.Socket;
    import java.util.logging.Level;
    import java.util.logging.Logger;
    import javax.imageio.ImageIO;
    import javax.swing.ImageIcon;
    import javax.swing.JLabel;

    public class Server {
        // CONFIG
        private Socket connectedSocketMSG;
        private Socket connectedSocketIMG;
        private static final int SERVERPORT_MSG = 32500;
        private static final int SERVERPORT_IMG = 32499;
        private static String SERVER_IP = "";
        // ENVIAR
        private PrintWriter out = null;
        // RECEBER
        private ServerSocket serverSocket;
        private BufferedReader input = null;

        public Server()
        {
        }
        
        public void receberMensagem() {                                           
            new Thread(() -> {
                Socket socket = null;
                try {
                    serverSocket = new ServerSocket(SERVERPORT_MSG);
                    connectedSocketMSG = serverSocket.accept();
                    input = new BufferedReader(new InputStreamReader(connectedSocketMSG.getInputStream()));
                    final String message = input.readLine();
                    System.out.println("Mensagem recebida: "+message);
                    System.out.println("TESTE : IP DO ANDROID - "+connectedSocketMSG.getInetAddress());
                    switch(message)
                        {
                        case "IP":
                            String msg = input.readLine();
                            if(msg.length()>0)
                            {
                                SERVER_IP = msg;
                                enviarMensagem("OK");
                            }
                            System.out.println("TESTE : IP DO ANDROID - "+msg);
                            break;
                        case "CURSOR":
                            String X = input.readLine();
                            String Y = input.readLine();
                            Utils.mover(new Integer(X), new Integer(Y));
                            enviarMensagem("OK");
                            break;
                        /*case "ZOOM":
                            String X = input.readLine();
                            String Y = input.readLine();
                            Utils.mover(new Integer(X), new Integer(Y));
                            enviarMensagem("OK");
                            break;*/
                        case "AVANCAR":
                            Utils.avancar();
                            enviarMensagem("OK");
                            break;
                        case "RECUAR":
                            Utils.retroceder();
                            enviarMensagem("OK");
                            break;
                        case "STOP":
                            socket.close();
                            break;
                        }        
                    } catch (IOException | AWTException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }).start();
        }           
                      
        public void enviarMensagem(String txtMessage) {                                          
            try {
                Thread enviar = new Thread(() -> {
                    try {
                        if(connectedSocketMSG==null)
                        {
                            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                            connectedSocketMSG = new Socket(serverAddr, SERVERPORT_MSG);
                        }
                        if(connectedSocketMSG !=null){
                            if (out == null)
                                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connectedSocketMSG.getOutputStream())), true);
                            out.println(txtMessage);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                enviar.start();
                enviar.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
       public void receberImagens(){
        new Thread(() -> {
            try {
                JLabel jl = null;
                serverSocket = new ServerSocket(SERVERPORT_IMG);
                do {
                    Socket connectedSocket = serverSocket.accept();
                    int filesize = 6022386;
                    int bytesRead;
                    int current = 0;
                    byte[] mybytearray  = new byte [filesize];
                    InputStream is = connectedSocketIMG.getInputStream();
                    bytesRead = is.read(mybytearray,0,mybytearray.length);
                    current = bytesRead;
                    do {
                        bytesRead =is.read(mybytearray, current, (mybytearray.length-current));
                        if(bytesRead >= 0) current += bytesRead;
                    } while(bytesRead > -1);
                    ByteArrayInputStream bis = new ByteArrayInputStream(mybytearray);
                    BufferedImage imageSobrePor = ImageIO.read(bis);
                    
                    if (jl == null) jl = Utils.abrirImage();
                    else{
                        jl.setIcon(new ImageIcon(imageSobrePor));
                    }
                } while (connectedSocketIMG != null);
            }catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
        }

        public void enviarSlides(String pathImg ,int qtdImg) {                                           
            Thread sendImg = new Thread(() -> {
                    try {
                        if(SERVER_IP.length()>0)
                        {
                            if (connectedSocketIMG == null){
                                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                                connectedSocketIMG = new Socket(serverAddr, SERVERPORT_IMG);
                            }
                            if (connectedSocketIMG !=null) {
                                for (int x1 = 1; x1 <= qtdImg; x1++) {
                                    File input_file = new File(pathImg+"\\slide" + x1 + ".jpg");
                                    byte [] byteArray  = new byte [(int)input_file.length()];
                                    FileInputStream fis = new FileInputStream(input_file);
                                    BufferedInputStream bis = new BufferedInputStream(fis);
                                    bis.read(byteArray,0,byteArray.length);
                                    OutputStream os = connectedSocketIMG.getOutputStream();
                                    os.write(byteArray,0,byteArray.length);
                                    os.flush();
                                    os.close();
                                    Thread.sleep(200);
                                }
                                connectedSocketIMG.close();
                            }
                        }
                    }catch (IOException | InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            sendImg.start();
            sendImg.interrupt();
            } 
        }                                          
