/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testeprint;

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
import javax.swing.JOptionPane;

public class TestTray {
    public static void main(String[] args) {
        
    final TrayIcon trayIcon; // declarando uma constante do tipo TrayIcon
    // Aqui vamos testar se o recurso é suportado
    if (SystemTray.isSupported()) {
    //declarando uma variavel  do tipo SystemTray
    SystemTray tray = SystemTray.getSystemTray();
    //declarando uma variavel  do tipo Image que contera a imagem tray.gif
    Image image = Toolkit.getDefaultToolkit().getImage("tray.gif");
     //Criamos um listener para escutar os eventos de mouse
    MouseListener mouseListener = new MouseListener() {
        public void mouseClicked(MouseEvent e) {
        
        }
        public void mouseEntered(MouseEvent e) {

        }
        public void mouseExited(MouseEvent e) {  

        }
        public void mousePressed(MouseEvent e) {           
        //Toda vez que for clicado imprime uma mensagem na tela
            System.out.println("Tray Icon - O Mouse foi pressionado!");                       
        }
        public void mouseReleased(MouseEvent e) {
            
        }
    };
    // Criamos um ActionListener para a ação de encerramento do programa.
    ActionListener exitListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            //Imprime uma mensagem de despedida na tela
            System.out.println("Saindo...");
            JOptionPane.showMessageDialog(null,"Saindo...");
            System.exit(0);
        }
    };
    // Criamos um ActionListener para a exibir uma mensagem na tela ao clicarmos
    //em um item do menu.
    ActionListener mostramsglistener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null,"Java 6 é uma revolução.");
        }
    };
    //Criando um objeto PopupMenu
    PopupMenu popup = new PopupMenu("Menu de Opções");
    //criando itens do menu
    MenuItem mostramsg = new MenuItem("Exibir Mensagem");
    MenuItem defaultItem = new MenuItem("Sair");
    //na linha a seguir associamos os objetos aos eventos
    mostramsg.addActionListener(mostramsglistener);
    defaultItem.addActionListener(exitListener);
    //Adicionando itens ao PopupMenu
    popup.add(mostramsg);
    //adiconando um separador
    popup.addSeparator();
    //Criando objetos do tipo Checkbox
    CheckboxMenuItem cheque1 = new CheckboxMenuItem("Ativar Plugins");
    popup.add(cheque1);
    CheckboxMenuItem cheque2 = new CheckboxMenuItem("Iniciar Minimizado");
    popup.add(cheque2);
    popup.addSeparator();
    //Criando um submenu
    PopupMenu popup2 = new PopupMenu("SubMenu de Opções");
    MenuItem mostramsg2 = new MenuItem("Item1");
    MenuItem mostramsg3 = new MenuItem("Item2");
    MenuItem mostramsg4 = new MenuItem("Item3");
    popup2.add(mostramsg2);
    popup2.add(mostramsg3);
    popup2.add(mostramsg4);
    popup.add(popup2);
    popup.addSeparator();
    popup.add(defaultItem);
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
    else {
        //Caso o item  System Tray não for suportado
        JOptionPane.showMessageDialog(null,"recurso ainda não esta disponível pra o seu sistema");
    }
}

}// Fim do código.
