/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testeprint;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author u18300
 */
public class TestePrint {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //Loop infinito 
        try{ Robot robot = new Robot(); 
        BufferedImage bi = robot.createScreenCapture(new Rectangle(0, 0, 1600, 900)); // aqui vc configura as posições xy e o tam da área que quer capturar 
        BufferedImage bi2 = bi.getSubimage(200,200, 400, 400);
        ImageIO.write(bi, "png", new File("E://tela.png"));// Salva a imagem 
        ImageIO.write(bi2, "png", new File("E://tela2.png"));// Salva a imagem 
        
        Frame frame = new Frame("Test");
        frame.setUndecorated(true);

        frame.add(new Component() {
        //BufferedImage img = ImageIO.read(new URL("http://upload.wikimedia.org/"+
                                                 //"wikipedia/en/2/24/Lenna.png"));
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(bi2, 0, 0, 1600, 900,     this);
            }
        });

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        gs.setFullScreenWindow(frame);
        frame.validate();
        
        } 
        catch(AWTException e)
        { e.printStackTrace(); } 
        catch(IOException e){ e.printStackTrace();} 
        try { //pode mudar aqui o tempo em milessegundos 
            Thread.currentThread().sleep(10000); } 
        catch(Exception e) { } 
    }
    
}
