package com.mycompany.maventest;

import classes.Server;
import classes.Utils;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author u18300
 */
public class Tray {
    
    Server servidor = null;
    MenuItem mostramsg3;
    MenuItem mostramsg4;
    MenuItem mostramsg5;
    public Tray(JFrame tela) throws IOException
    {
        final TrayIcon trayIcon;
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File("E:\\github repository\\TCC-2019\\Aplicacao\\ISlidee\\target\\classes\\logo2.jpeg");
            Image image = ImageIO.read(file);
            MouseListener mouseListener = new MouseListener() {
            public void mouseClicked(MouseEvent e) {
        
            }
            public void mouseEntered(MouseEvent e) {

            }
            public void mouseExited(MouseEvent e) {  

            }
            public void mousePressed(MouseEvent e) {           
                System.out.println("Tray Icon - O Mouse foi pressionado!");                       
            }
            public void mouseReleased(MouseEvent e) {
            
            }};

            ActionListener exitListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }};
            
            ActionListener mostramsglistener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"<html> <center> ISlidee ®<br> <i>'Tornando seu dia mais fácil'</i> <br> Aplicativo de slides <br> TACNOLOGY  </center> </html>","About",JOptionPane.PLAIN_MESSAGE);            }};
            
            PopupMenu popup = new PopupMenu("Menu de Opções");
            
            MenuItem max1 = new MenuItem("Minimizar");
            ActionListener maximizar = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    MenuItem it = (MenuItem)max1;
                if(!tela.isVisible())
                {
                    it.setLabel("Minimizar");
                    tela.setVisible(true);
                }
                else
                {
                    tela.setVisible(false);
                    it.setLabel("Maximizar");
                }
            }};
            max1.addActionListener(maximizar);
            popup.add(max1);
            
            popup.addSeparator();
            
            PopupMenu popup2 = new PopupMenu("Gerenciamento");
            MenuItem mostramsg2 = new MenuItem("Reenviar Slide");
            ActionListener reenviarSlides = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if(servidor!=null)
                        servidor.enviarSlides();
                    else JOptionPane.showMessageDialog(null, "Servidor nulo ou IP não enviado", "Alerta", JOptionPane.ERROR_MESSAGE);
                }
            };
            mostramsg2.addActionListener(reenviarSlides);
            mostramsg2.setEnabled(false);
            mostramsg3 = new MenuItem("Visualizar IP");
            ActionListener visualiarIP = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JOptionPane.showMessageDialog(null, "Seu endereço é : " + Utils.getIPAddress(true) , "Endereço de IP", JOptionPane.INFORMATION_MESSAGE);
                }
            };
            mostramsg4 = new MenuItem("Restaurar recebimento mensagem");
            ActionListener restaurarMensagem = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JOptionPane.showMessageDialog(null, "Iniciando recebimento na porta : " + servidor.getSERVERPORT_MSG(), "Processo de restaurar recebimento", JOptionPane.INFORMATION_MESSAGE);
                    if(servidor!=null)
                        servidor.receberMensagem();
                }
            };
            mostramsg4.addActionListener(restaurarMensagem);
            mostramsg4.setEnabled(false);
            mostramsg5 = new MenuItem("Restaurar recebimento de imagem");
            ActionListener restaurarImagem = new ActionListener()
            {
                 public void actionPerformed(ActionEvent e)
                {
                    JOptionPane.showMessageDialog(null, "Iniciando recebimento na porta : " + servidor.getSERVERPORT_IMG(), "Processo de restaurar recebimento", JOptionPane.INFORMATION_MESSAGE);
                    if(servidor!=null)
                        servidor.receberImagens();
                }
            };
            mostramsg5.addActionListener(restaurarImagem);
            mostramsg5.setEnabled(false);
            popup2.add(mostramsg5);
            popup2.add(mostramsg4);
            popup2.add(mostramsg2);
            popup2.add(mostramsg3);
            popup.add(popup2);
            
            popup.addSeparator();
             
            MenuItem about = new MenuItem("About");
            MenuItem exit = new MenuItem("Sair");
            about.addActionListener(mostramsglistener);
            exit.addActionListener(exitListener);
            popup.add(about);
            popup.add(exit);
            trayIcon = new TrayIcon(image, "TrayIcon Demonstração", popup);
            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    trayIcon.displayMessage("Action Event",
                    "Um Evento foi disparado",
                    TrayIcon.MessageType.INFO);
                }
            };
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);
            trayIcon.addMouseListener(mouseListener);
            try {
               tray.add(trayIcon);
            } catch (AWTException e) {
               System.err.println("Erro, TrayIcon não sera adicionado.");}
            }
        else
        {
            JOptionPane.showMessageDialog(null,"recurso ainda não esta disponível pra o seu sistema");
        }
    }
    
    public void setServer(Server servidor)
    {
       this.servidor = servidor;
       mostramsg3.setEnabled(true);
       mostramsg4.setEnabled(true);
       mostramsg5.setEnabled(true);
    }
    
}
