/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.List;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    public static BufferedImage zoom (int mult,BufferedImage imagem)
    {
        int x1=getResolution().width/mult*mult;
        int y1=getResolution().height/mult*mult;
        int x2=x1*(mult-1);
        int y2=y1*(mult-1);
        
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
        for (int x = 1 ; x < imgs.length ; x++)
        {
            File outputfile = new File(PATH_SALVAR+"\\slide" + x + ".jpg");
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
}
