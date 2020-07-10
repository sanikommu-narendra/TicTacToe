import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientTicTacToe extends  JFrame implements ActionListener {
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    JPanel jPanel;
    JButton jButton[];
    TextArea textArea;
    String string,stringrev;
    int count;
    boolean flag;
    boolean[] booleans;

    public ClientTicTacToe() throws  Exception{

        setLayout(null);
        textArea = new TextArea("",3,6,TextArea.SCROLLBARS_VERTICAL_ONLY);
        textArea.setBounds(40,230,240,50);
        add(textArea);
        jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(3,3,2,2));
        jPanel.setBounds(60,20,200,200);
        jPanel.setBackground(Color.GRAY);
        add(jPanel);
        jButton = new JButton[9];
        booleans = new boolean[9];
        for(int i=0;i<9;i++) {
            jButton[i] = new JButton();
            jPanel.add(jButton[i]);
            jButton[i].setActionCommand(""+i);
            jButton[i].addActionListener(this);
            booleans[i]=false;
        }
        setVisible(true);
        setSize(350,330);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        socket = new Socket("localhost",2634);
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        string = dataInputStream.readUTF();
        setTitle("You Are Player "+string);
        if(string.equals("X")) {
            stringrev="O";
            flag = false;
            textArea.append("You Are Player X\nWaitng For Opponent");
            if(dataInputStream.readUTF().equals("connected"))
                textArea.append("\nOpponent connected\nFirst Move Is Yours");
                flag = true;

        }
        else {
            stringrev="X";
            flag = true;
            textArea.append("You Are Player O\n");
            textArea.append("Opponent connected\nFirst Move Is Opponent Move");
        }

        ClientTicTacToe clientTicTacToe = this;
        new Thread(){
            public void run() {
                try {
                    while (true) {
                        String string1 = dataInputStream.readUTF();
                        String string2 = dataInputStream.readUTF();
                        int num = Integer.parseInt(string1);
                        boolean bool = Boolean.parseBoolean(string2);
                        count++;
                        //booleans[num] = true;
                        jPanel.remove(num);
                        jPanel.add(new JLabel(stringrev, JLabel.CENTER), num);
                        jPanel.validate();
                        if(string2.equals("true")) {
                            textArea.append("\n*******DEFEAT*******");
                            for(int i=0;i<9;i++) {
                                jButton[i].removeActionListener(clientTicTacToe);
                            }
                            break;
                        }
                        if((count==9)&&(!isVictory())) {
                            textArea.append(("\n*******MATCH TIE*******"));
                            break;
                        }
                    }
                    //socket.close();
                    //dataInputStream.close();
                    //dataOutputStream.close();
                }
                catch (Exception exception) {

                }
            }
        }.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (!flag) {
                textArea.append("\nWait For Opponent Connection");
                return;
            }
            String str = e.getActionCommand();
            int num = Integer.parseInt(str);
            if (string.equals("X")) {
                if (count % 2 == 0) {
                    count++;
                    booleans[num] = true;
                    jPanel.remove(num);
                    jPanel.add(new JLabel("X", JLabel.CENTER), num);
                    jPanel.validate();
                    Boolean victory = isVictory();
                    dataOutputStream.writeUTF(str);
                    dataOutputStream.writeUTF(victory.toString());
                    if(victory) {
                        textArea.append("\n*******VICTORY*******");
                        for(int i=0;i<9;i++) {
                            jButton[i].removeActionListener(this);
                        }
                        //socket.close();
                        //dataInputStream.close();
                        //dataOutputStream.close();
                    }
                    if((count==9)&&(!isVictory())) {
                        textArea.append("\n*******MATCH TIE*******");
                    }
                } else {
                    textArea.append("\nWait For Opponets Move");
                }
            }
            if (string.equals("O")) {
                if (count % 2 != 0) {
                    count++;
                    booleans[num] = true;
                    jPanel.remove(num);
                    jPanel.add(new JLabel("O", JLabel.CENTER), num);
                    jPanel.validate();
                    Boolean victory = isVictory();
                    dataOutputStream.writeUTF(str);
                    dataOutputStream.writeUTF(victory.toString());
                    if(victory) {
                        textArea.append("\n*******VICTORY*******");
                        for(int i=0;i<9;i++) {
                            jButton[i].removeActionListener(this);
                        }
                        //socket.close();
                        //dataInputStream.close();
                        //dataOutputStream.close();
                    }
                } else {
                    textArea.append("\nWait For Opponets Move");
                }
            }
        }
        catch (Exception exception) {

        }

    }

    public  boolean isVictory() {
        if((booleans[0]==true&&booleans[1]==true&&booleans[2]==true)||
           (booleans[3]==true&&booleans[4]==true&&booleans[5]==true)||
           (booleans[6]==true&&booleans[7]==true&&booleans[8]==true)||
           (booleans[0]==true&&booleans[3]==true&&booleans[6]==true)||
           (booleans[1]==true&&booleans[4]==true&&booleans[7]==true)||
           (booleans[2]==true&&booleans[5]==true&&booleans[8]==true)||
           (booleans[0]==true&&booleans[4]==true&&booleans[8]==true)||
           (booleans[2]==true&&booleans[4]==true&&booleans[6]==true)) {
            return true;
        }
        else return false;
    }

    public static void main(String[] args) throws  Exception {
        try {
            new ClientTicTacToe();
        }
        catch (Exception exception) {
            //System.out.println(exception);
        }
    }
}
