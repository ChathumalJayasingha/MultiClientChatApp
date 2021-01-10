package server;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class ServerFXMLController {
    public TextArea txtArea;
    public TextField txtMessage;

    static ServerSocket serverSocket;
    static Socket socket;
    static DataInputStream dataInputStream;
    static DataOutputStream dataOutputStream;
    public TextArea areaUserList;
    HashSet<String>names=new HashSet<>();
    HashSet<DataOutputStream>outputStreamHashSet=new HashSet<>();

    public void initialize(){
        new Thread(()->{
            try {
                serverSocket=new ServerSocket(5050);
                System.out.println("Server Is Online");
                while (true){

                    socket = serverSocket.accept();
                    System.out.println("Client Accepted "+socket);
                    txtArea.appendText("\nSocket Accepted -:> "+socket);

                    dataInputStream=new DataInputStream(socket.getInputStream());
                    dataOutputStream=new DataOutputStream(socket.getOutputStream());

                    dataOutputStream.writeUTF("Enter Unique Name Please");
                    new ServerThread(this,socket);

                }



            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }
    public void massageOnAction(ActionEvent actionEvent) throws IOException {
        txtArea.appendText("\nServer : "+txtMessage.getText());
        for (DataOutputStream out:outputStreamHashSet){
            out.writeUTF("\nServer : "+txtMessage.getText());
        }
        txtMessage.setText(null);
    }

    class ServerThread{
            ServerFXMLController serverFXMLController;
            Socket socket;
            String userName;
            boolean userJoined;

        public ServerThread(ServerFXMLController serverFXMLController, Socket socket) {
            this.serverFXMLController = serverFXMLController;
            this.socket = socket;

            new Thread(() -> {
                for (String nm:names) {
                    areaUserList.appendText("\n"+nm);
                }
                try {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    while (true){

                        if (!userJoined){
                            userName=dataInputStream.readUTF();
                            System.out.println("read Name "+userName);
                            if (names.contains(userName)){
                                dataOutputStream.writeUTF("Already Exit");
                                txtArea.appendText("\n Already Exit");
                                System.out.println("already Exit");
                            }else {
                                names.add(userName);
                                dataOutputStream.writeUTF("Accepted Now You Can Chat Enjoy");
                                txtArea.appendText("\n"+userName+" Join ");
                                System.out.println("Accepted");
                                areaUserList.clear();
                                for (String nm:names) {
                                    areaUserList.appendText("\n"+nm);
                                }
                                boolean add = outputStreamHashSet.add(dataOutputStream);
                                if (add){
                                    System.out.println("added Hash Set" );
                                }else {
                                    System.out.println("Added fail hashset");
                                }while (true){
                                    String message=dataInputStream.readUTF();
                                    System.out.println(userName+"  : "+message);
                                    for (DataOutputStream stream : outputStreamHashSet){
                                        stream.writeUTF(userName+" : "+message);
                                    }
                                    txtArea.appendText("\n"+userName+"  : "+message);
                                }
                            }
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

}
