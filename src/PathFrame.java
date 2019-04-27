/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jesus
 */
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class PathFrame extends JFrame {

    JPanel p = new JPanel();
    ArrayList<MyButton> buttons = new ArrayList<MyButton>();

    private boolean start = false;
    private boolean end = false;
    private boolean block = false;

    JMenuBar menuBar;
    JMenu menu;
    JMenu menuSaveLoad;
    JMenuItem menuSave;
    JMenuItem menuLoad;
    JMenuItem menuStart;
    JMenuItem menuReset;
    JMenuItem menuToggleBlock;
    JMenuItem menuDeletePath;

    public static void main(String args[]) {
        new PathFrame();
    }

    public PathFrame() {
        setSize(1920, 1080);
        setResizable(false);
        setLocationRelativeTo(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int gridLength = 40;
        int gridWidth = 40;

        p.setLayout(new GridLayout(gridLength, gridWidth));

        menuBar = new JMenuBar();
        menu = new JMenu("Edit");
        menuSaveLoad = new JMenu("File");
        menuSave = new JMenuItem("Save",
                KeyEvent.VK_T);
        menuLoad = new JMenuItem("Load",
                KeyEvent.VK_T);
        menuStart = new JMenuItem("Start",
                KeyEvent.VK_T);
        menuReset = new JMenuItem("Reset",
                KeyEvent.VK_T);
        menuToggleBlock = new JMenuItem("Toggle Block",
                KeyEvent.VK_T);
        menuDeletePath = new JMenuItem("Delete Path",
                KeyEvent.VK_T);
        //DELETE PATH
        menuDeletePath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyButton endBtn = null;
                for (MyButton b : buttons) {
                    if (b.start) {
                        b.pathed = false;
                    }
                    if (b.pathed) {
                        b.setBackground(Color.white);
                        b.pathed = false;
                    }
                    if (b.end) {
                        endBtn = b;
                    }
                }
                for (MyButton b : buttons) {
                    if (b.x == endBtn.x && b.y == (endBtn.y + 1) && !b.block && !b.start) {
                        b.setBackground(Color.white);
                    } else if (b.x == endBtn.x && b.y == (endBtn.y - 1) && !b.block && !b.start) {
                        b.setBackground(Color.white);
                    } else if (b.y == endBtn.y && b.x == (endBtn.x + 1) && !b.block && !b.start) {
                        b.setBackground(Color.white);
                    } else if (b.y == endBtn.y && b.x == (endBtn.x - 1) && !b.block && !b.start) {
                        b.setBackground(Color.white);
                    }
                }
            }
        });

        //SAVE GRID
        menuSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveGrid();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //LOAD GRID
        menuLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadGrid();
                } catch (IOException ex) {
                    Logger.getLogger(PathFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        //TOGGLE BLOCK
        menuToggleBlock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (block) {
                    block = false;
                } else {
                    block = true;
                }
            }
        });

        //RESET
        menuReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (MyButton b : buttons) {
                    b.setBackground(Color.white);
                    b.block = false;
                    b.start = false;
                    b.end = false;
                    b.pathed = false;
                    start = false;
                    end = false;
                }
            }
        });

        //START
        menuStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyButton startBtn = null;
                MyButton endBtn = null;

                for (MyButton b : buttons) {
                    if (b.start) {
                        startBtn = b;
                    }
                    if (b.end) {
                        endBtn = b;
                    }
                }

                MyButton currentBtn = startBtn;
                double distance = distance(currentBtn, endBtn);

                while (distance != 1) {
                    currentBtn.pathed = true;
                    closer(currentBtn).setBackground(Color.green);
                    currentBtn = closer(currentBtn);
                    distance = distance(currentBtn, endBtn);
                }
            }
        });

        menu.add(menuStart);
        menu.add(menuReset);
        menu.add(menuToggleBlock);
        menu.add(menuDeletePath);
        menuSaveLoad.add(menuSave);
        menuSaveLoad.add(menuLoad);
        menuBar.add(menuSaveLoad);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        int x = 0;
        int y = -1;
        for (int i = 0; i < gridLength * gridWidth; i++) {
            if (i % gridWidth == 0) {
                x = 0;
                y++;
            } else {
                x++;
            }
            MyButton b = new MyButton(x, y);
            b.setBackground(Color.white);

            b.addActionListener(new ActionListener() {

                //ASSIGNING COLORS
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!start && !end && !block) {
                        b.setBackground(Color.blue);
                        b.start = true;
                        start = true;
                    } else if (start && !end && b.start && !block) {
                        b.setBackground(Color.white);
                        b.start = false;
                        start = false;
                    } else if (start && !end && !b.start && !block) {
                        b.setBackground(Color.red);
                        b.end = true;
                        end = true;
                    } else if (start && end && b.end && !block) {
                        b.setBackground(Color.white);
                        b.end = false;
                        end = false;
                    } else if (block && !b.block) {
                        b.setBackground(Color.black);
                        b.block = true;
                    } else if (block && b.block) {
                        b.setBackground(Color.white);
                        b.block = false;
                    }
                }
            });
            b.setText("");
            buttons.add(b);
            p.add(b);
        }

        add(p);
        setVisible(true);
    }

    public double distance(MyButton current, MyButton end) {
        double distance = Math.sqrt(Math.pow((end.x - current.x), 2) + Math.pow((end.y - current.y), 2));
        return distance;
    }

    public MyButton closer(MyButton current) {
        MyButton top = null;
        MyButton bottom = null;
        MyButton left = null;
        MyButton right = null;
        MyButton endBtn = null;

        for (MyButton b : buttons) {
            if (b.x == current.x && b.y == (current.y + 1) && !b.block && !b.start && !b.pathed) {
                top = b;
            }
            if (b.x == current.x && b.y == (current.y - 1) && !b.block && !b.start && !b.pathed) {
                bottom = b;
            }
            if (b.y == current.y && b.x == (current.x + 1) && !b.block && !b.start && !b.pathed) {
                right = b;
            }
            if (b.y == current.y && b.x == (current.x - 1) && !b.block && !b.start && !b.pathed) {
                left = b;
            }
            if (b.end) {
                endBtn = b;
            }

        }

        double closerDistance = 999999999;
        MyButton closerBtn = null;

        if (top != null) {
            if (distance(top, endBtn) < closerDistance) {
                closerDistance = distance(top, endBtn);
                closerBtn = top;
            }
        }
        if (bottom != null) {
            if (distance(bottom, endBtn) < closerDistance) {
                closerDistance = distance(bottom, endBtn);
                closerBtn = bottom;
            }
        }
        if (right != null) {
            if (distance(right, endBtn) < closerDistance) {
                closerDistance = distance(right, endBtn);
                closerBtn = right;
            }
        }
        if (left != null) {
            if (distance(left, endBtn) < closerDistance) {
                closerDistance = distance(left, endBtn);
                closerBtn = left;
            }
        }

        return closerBtn;

    }

    public void saveGrid() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new FileOutputStream(new File(path)));

                for (MyButton b : buttons) {
                    oos.writeObject(b);
                }
            } catch (Exception e) {

            } finally {
                if (oos != null) {
                    oos.close();
                }
            }

        }
    }

    public void loadGrid() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        ArrayList<MyButton> cargados = new ArrayList<MyButton>();

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(fileToSave));
                while (true) {
                    cargados.add((MyButton) ois.readObject());
                }
            } catch (Exception e) {

            } finally {
                for (int i = 0; i < buttons.size(); i++) {
                    buttons.get(i).block = cargados.get(i).block;
                    buttons.get(i).pathed = cargados.get(i).pathed;
                    buttons.get(i).start = cargados.get(i).start;
                    buttons.get(i).end = cargados.get(i).end;

                    if (buttons.get(i).start) {
                        buttons.get(i).setBackground(Color.blue);
                    } else if (buttons.get(i).end) {
                        buttons.get(i).setBackground(Color.red);
                    } else if (buttons.get(i).block) {
                        buttons.get(i).setBackground(Color.black);
                    } else if (buttons.get(i).pathed) {
                        buttons.get(i).setBackground(Color.green);
                    }
                }

                if (ois != null) {
                    ois.close();
                }
            }

        }
    }
}
