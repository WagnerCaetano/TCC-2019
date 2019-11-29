    package classes;

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
    import javax.swing.JTextField;

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
        private ServerSocket serverSocketMSG;
        private ServerSocket serverSocketIMG;
        private BufferedReader input = null;
        
        private final int qtdImg;
        private final JTextField txtConexao;

        public Server(int qtdImg,JTextField txtConexao)
        {
            this.qtdImg = qtdImg;
            this.txtConexao = txtConexao;
            receberMensagem();
            txtConexao.setText("SERVIDOR INICIADO...");
        }

    
        public void receberMensagem() {                                           
            new Thread(() -> {
                try {
                    if(serverSocketMSG == null || (!serverSocketMSG.isBound()))
                        serverSocketMSG = new ServerSocket(SERVERPORT_MSG);
                    if( connectedSocketMSG == null || connectedSocketMSG.isClosed() )
                        connectedSocketMSG = serverSocketMSG.accept();
                    SERVER_IP = connectedSocketMSG.getInetAddress().toString().substring(1);
                    enviarSlides();
                    do  {
                    input = new BufferedReader(new InputStreamReader(connectedSocketMSG.getInputStream()));
                    final String message = input.readLine();
                    System.out.println("Mensagem recebida: "+message);
                    switch(message)
                        {
                        case "CURSOR":
                            String X = input.readLine();
                            String Y = input.readLine();
                            if(X.contains("."))
                                X = X.substring(0,X.indexOf(".")-1);
                            if(Y.contains("."))
                                Y = Y.substring(0,Y.indexOf(".")-1);
                            Utils.mover(Integer.parseInt(X), Integer.parseInt(Y));
                            break;
                        case "ZOOM-S":
                            int indice = Integer.parseInt(input.readLine());
                            Float scale = Float.parseFloat(input.readLine());
                            JLabel jl = Utils.abrirImage();
                            jl.setIcon(new ImageIcon(Utils.zoom(scale, Utils.IndiceToBufferedImage(indice))));
                            break;
                        case "AVANCAR":
                            Utils.avancar();
                            break;
                        case "RECUAR":
                            Utils.retroceder();
                            break;
                        case "STOP":
                            connectedSocketMSG.close();
                            break;
                        default:break;
                        } 
                    }while(connectedSocketMSG!=null || (!connectedSocketMSG.isClosed()) );
                    } 
                    catch (IOException | AWTException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }).start();
            
        }      
        
        public void enviarSlides()
        {
            if(SERVER_IP.length()>0)
                    {
                        enviarSlides("C:\\Temp\\SLIDES", qtdImg);
                        txtConexao.setText("ENVIANDO IMAGENS PARA O CELULAR...");
                        receberImagens();
                        txtConexao.setText("PRONTO PARA RECEBER IMAGENS");        
                    }
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
                if( serverSocketIMG == null || (!serverSocketIMG.isBound()) )
                    serverSocketIMG = new ServerSocket(SERVERPORT_IMG);
                do {
                    Socket connectedSocket = serverSocketIMG.accept();
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
                        for (int x1 = 1; x1 <= qtdImg; x1++) {
                        if (connectedSocketIMG == null || connectedSocketIMG.isClosed()){
                                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                                connectedSocketIMG = new Socket(serverAddr, SERVERPORT_IMG);
                            }
                        if (connectedSocketIMG !=null) {
                                
                                    File input_file = new File(pathImg+"\\slide" + x1 + ".jpg");
                                    byte [] byteArray  = new byte [(int)input_file.length()];
                                    FileInputStream fis = new FileInputStream(input_file);
                                    BufferedInputStream bis = new BufferedInputStream(fis);
                                    bis.read(byteArray,0,byteArray.length);
                                    OutputStream os = connectedSocketIMG.getOutputStream();
                                    os.write(byteArray,0,byteArray.length);
                                    os.flush();
                                    os.close();
                                    System.out.println("ENVIOU " + x1);
                                }
                        }
                    }catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            sendImg.start();
            sendImg.interrupt();
            } 
        
        public int getSERVERPORT_MSG() {
        return SERVERPORT_MSG;
        }

        public int getSERVERPORT_IMG() {
            return SERVERPORT_IMG;
        }

        public String getSERVER_IP() {
            return SERVER_IP;
        }
        
        }      
        


