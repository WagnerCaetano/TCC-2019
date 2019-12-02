package classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class JFrameImage {
    
    JFrame jf;
    JLabel jl;
    Image cursorImage ;
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    public JFrameImage(){
        jf = new JFrame();
        cursorImage = new ImageIcon(getClass().getClassLoader().getResource("seta-do-mouse-8.png").getFile().substring(1)).getImage();
        jf.setCursor(toolkit.createCustomCursor(cursorImage, new Point(0,0), "SetaMaior"));   
    }

    public JLabel getJl() {
        return jl;
    }

    public JLabel abrirImage()
    {        
        jl = new JLabel();
        jl.setBackground(Color.BLACK);
        jf.setBackground(Color.BLACK);
        jf.add(jl,BorderLayout.CENTER);
        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jf.setUndecorated(true);
        jf.setVisible(true);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        device.setFullScreenWindow(jf);
        return jl;
    }
    public void fecharImage(){
        if(jf.isActive())
            jf.dispose();
    }
    public boolean telaAberta()
    {
        return jf.isVisible();
    }
    public void setarImageDoLabel(BufferedImage img){

        BufferedImage resized = Utils.resize(img, 1440, 1080);

        jl.setIcon(new ImageIcon(resized));
    }
}
