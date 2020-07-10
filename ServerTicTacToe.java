import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTicTacToe extends JFrame {
    ServerSocket serverSocket;
    Socket socket1,socket2;
    JTextArea textArea;
    DataInputStream dataInputStream1,dataInputStream2;
    DataOutputStream dataOutputStream1,dataOutputStream2;
    int count = 0;
    public ServerTicTacToe(String title) throws Exception{
       super(title);
       textArea = new JTextArea();
       textArea.setEditable(false);
       textArea.setBounds(10,10,400,300);
       add(textArea);
       setLayout(null);
       setVisible(true);
       setSize(400,300);
       setResizable(false);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       serverSocket = new ServerSocket(2634);
       textArea.append("waiting for connections");
       socket1 = serverSocket.accept();
       dataInputStream1 = new DataInputStream(socket1.getInputStream());
       dataOutputStream1 = new DataOutputStream(socket1.getOutputStream());
       textArea.append("\nPlayer X connected");
       dataOutputStream1.writeUTF("X");
       socket2 = serverSocket.accept();
       dataInputStream2 = new DataInputStream(socket2.getInputStream());
       dataOutputStream2 = new DataOutputStream(socket2.getOutputStream());
       textArea.append("\nPlayer O connected");
       dataOutputStream2.writeUTF("O");
       dataOutputStream1.writeUTF("connected");



        textArea.append("\n");
       while(true) {
           String string1 = dataInputStream1.readUTF();
           String string2 = dataInputStream1.readUTF();
           count++;
           textArea.append("\nlocation: "+string1);
           dataOutputStream2.writeUTF(string1);
           dataOutputStream2.writeUTF(string2);
           if(string2.equals("true")) {
               textArea.append("\nPlayer X Wins");
               break;
           }
           if((count==9)&&(string2.equals("false"))) {
               textArea.append("\nMATCH TIE");
               break;
           }


           string1 = dataInputStream2.readUTF();
           string2 = dataInputStream2.readUTF();
           count++;
           textArea.append("\nlocation: "+string1);
           dataOutputStream1.writeUTF(string1);
           dataOutputStream1.writeUTF(string2);
           if(string2.equals("true")) {
               textArea.append("\nPlayer O Wins");
               break;
           }

       }

       //socket1.close();
       //socket2.close();
       //serverSocket.close();
       //dataInputStream1.close();
       //dataOutputStream1.close();
       //dataInputStream2.close();
       //dataOutputStream2.close();

    }
    public static void main(String[] args) {

        try {
            new ServerTicTacToe("Server");
        }
        catch (Exception exception) {

        }
    }
}
