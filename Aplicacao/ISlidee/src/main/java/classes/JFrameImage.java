/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author u18325
 */
public class JFrameImage {
    
    JFrame jf;
    JLabel jl;
    
    public JFrameImage(JLabel l){
        jf = new JFrame();
        jl = l;
    }

    public JLabel getJl() {
        return jl;
    }

    public JLabel abrirImage()
    {        
        //jf = new JFrame();
        //JLabel jl = new JLabel();
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
    public void fecharImage(){
        jf.dispose();
    }
    public void setarImageDoLabel(BufferedImage img){
        jl.setIcon(new ImageIcon(img));
    }
}
