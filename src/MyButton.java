/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jesus
 */
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.Serializable;

public class MyButton extends JButton implements Serializable{

    int x;
    int y;
    boolean start = false;
    boolean end = false;
    boolean block = false;
    boolean pathed = false;

    public MyButton(int x, int y) {
        this.x = x;
        this.y = y;
        this.setText(String.valueOf(x) + " " + String.valueOf(y));
    }

    @Override
    public String toString() {
        return "MyButton{" + "x=" + x + ", y=" + y + ", start=" + start + ", end=" + end + ", block=" + block + ", pathed=" + pathed + '}';
    }
    
    

}
