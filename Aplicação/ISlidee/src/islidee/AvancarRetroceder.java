/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avancareretroceder;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 *
 * @author u18325
 */
public class AvancarRetroceder {
    
    Robot robot;

    public AvancarRetroceder() throws AWTException {
        robot = new Robot();
    }
    
    public void avancar(){        
        robot.keyPress(KeyEvent.VK_RIGHT);
    }
    public void retroceder(){        
        robot.keyPress(KeyEvent.VK_LEFT);        
    }
}
