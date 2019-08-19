/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testeprint;

import java.awt.event.*;
import javax.swing.*;
import org.jdesktop.jdic.tray.*;

public class TestTray {
    public static JMenuItem quit;
    
    public TestTray() {
        JPopupMenu menu = new JPopupMenu("Tray Icon Menu");
        menu.add(new JMenuItem("Test Item"));
        menu.addSeparator();
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }});
        menu.add(quitItem);
        
        // O arquivo “devmedia.gif” deve existir no mesmo diretório

        // onde estiver esta classe.
        ImageIcon icon = new ImageIcon("duke.gif");
        TrayIcon ti = new TrayIcon(icon, "JDIC Tray Icon API Test", menu);

        // Ação para clique com botão esquerdo.
        ti.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, 
                    "JDIC Tray Icon API Test!", "About",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
               
        SystemTray tray = SystemTray.getDefaultSystemTray();
        tray.addTrayIcon(ti);
    }

    public static void main(String[] args) {
        new TestTray();
    }   
}
