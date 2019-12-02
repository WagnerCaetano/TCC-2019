/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.util.List;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author u18300
 */
public class Utils {
    
    public static BufferedImage printar() throws AWTException
    {
        Robot robot = new Robot(); 
        BufferedImage bi = robot.createScreenCapture(new Rectangle(0, 0, getResolution().width, getResolution().height)); // aqui vc configura as posições xy e o tam da área que quer capturar 
        return bi;
    }
    public static BufferedImage zoom (float mult,BufferedImage imagem)
    {
        int x1=(int) (getResolution().width*(1+mult));
        int y1=(int) (getResolution().height*(1+mult));
        int x2=(int) (x1*(mult-1));
        int y2=(int) (y1*(mult-1));
        
        imagem = imagem.getSubimage(x1,y1, x2, y2);
        return imagem;
    }
    public static BufferedImage zoom (int x1 , int y1 , int x2 , int y2,BufferedImage imagem)
    {
        imagem = imagem.getSubimage(x1,y1, x2, y2);
        return imagem;
    }
    
    private static Dimension getResolution()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        return d;
    }
    public static void init(String Path)throws Exception {
        File diretorio = new File(Path);
        diretorio.mkdir();       
    }
    public static void telaCheia() throws AWTException{  
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_F5);
    }
    public static void avancar() throws AWTException{  
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_RIGHT);
    }
    public static void retroceder() throws AWTException{   
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_LEFT);        
    }
    public static void mover(int X, int Y) throws AWTException{
        Robot robot = new Robot();
        robot.mouseMove(X, Y);
    }
    public static int listaSlides(String PATH_SLIDE,String PATH_SALVAR) throws IOException{
        new File(PATH_SALVAR).mkdir();
        limparPasta(PATH_SALVAR);
        List lista = new ArrayList();
        PowerPointHelper pph = new PowerPointHelper();
        pph.setPowerPoint(PATH_SLIDE);
        BufferedImage[] imgs = pph.getSlides();  
        for (int x = 0 ; x < imgs.length ; x++)
        {
            File outputfile = new File(PATH_SALVAR+"\\slide" + (x+1) + ".jpg");
            ImageIO.write(imgs[x], "jpg", outputfile);
        }
        return imgs.length;
    }
    private static void limparPasta(String pathDeletar){
        File pasta = new File(pathDeletar);    
        File[] arquivos = pasta.listFiles();
        if(arquivos!=null)
            for(File arquivo : arquivos)
                arquivo.delete();
    }
    public static JLabel abrirImage()
    {
        JFrame jf;
        jf = new JFrame();
        JLabel jl = new JLabel();
        jf.add(jl);
        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jf.setUndecorated(true);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        device.setFullScreenWindow(jf);
        return jl;
    }
    public static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
    
    public static BufferedImage IndiceToBufferedImage(int indice) throws IOException
    {
        File file = new File("C:\\Temp\\SLIDES\\slide"+(indice+1)+".jpg");
        BufferedImage img = ImageIO.read(file);
        return img;
    }
    
    public static int getProporcaoX(int device)
    {
        return (int)(device * getResolution().getWidth())/1100;
    }
    public static int getProporcaoY(int device)
    {
        return (int)(device * getResolution().getHeight())/700;
    }
    
    public static String getIPAddress(boolean useIPv4) {
            try {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface intf : interfaces) {
                    List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                    for (InetAddress addr : addrs) {
                        if (!addr.isLoopbackAddress()) {
                            String sAddr = addr.getHostAddress();
                            //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                            boolean isIPv4 = sAddr.indexOf(':')<0;

                            if (useIPv4) {
                                if (isIPv4)
                                    return sAddr;
                            } else {
                                if (!isIPv4) {
                                    int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                    return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                                }
                            }
                        }
                    }
                }
            } catch (Exception ignored) { } // for now eat exceptions
            return null;
    }
}
