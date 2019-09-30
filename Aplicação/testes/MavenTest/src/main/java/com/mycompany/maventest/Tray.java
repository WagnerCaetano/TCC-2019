/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.maventest;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
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
    public Tray(JFrame tela) throws IOException
    {
        final TrayIcon trayIcon;
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = ImageIO.read(new File("C:\\Users\\00\\Documents\\imagens\\logo2.jpg"));
            //Image image = ImageIO.read(getClass().getResource("/imagens/logo2.jpeg"));
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
            
            MenuItem max1 = new MenuItem("Maximizar");
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
            MenuItem mostramsg2 = new MenuItem("Gerar código");
            MenuItem mostramsg3 = new MenuItem("Visualizar código");
            MenuItem mostramsg4 = new MenuItem("Reconectar");
            MenuItem mostramsg5 = new MenuItem("Desconectar");
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
            //criando um objeto do tipo TrayIcon
            trayIcon = new TrayIcon(image, "TrayIcon Demonstração", popup);
            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    trayIcon.displayMessage("Action Event",
                    "Um Evento foi disparado",
                    TrayIcon.MessageType.INFO);
                }
            };
            //Na linha a seguir a imagem a ser utilizada como icone sera redimensionada
            trayIcon.setImageAutoSize(true);
            //Seguida adicionamos os actions listeners
            trayIcon.addActionListener(actionListener);
            trayIcon.addMouseListener(mouseListener);
            //Tratamento de erros
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
    
}
