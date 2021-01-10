package client2;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientFXMLController {
    public TextArea txtMessageArea;
    public TextField txtMessage;

    static Socket socket;
    static DataInputStream dataInputStream;
    static DataOutputStream dataOutputStream;


    public void initialize(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket=new Socket("localhost",5050);
                    dataInputStream=new DataInputStream(socket.getInputStream());
                    dataOutputStream=new DataOutputStream(socket.getOutputStream());

                    String message="";

                    while (!message.equals("end")){
                        message=dataInputStream.readUTF();
                        txtMessageArea.appendText("\n"+message.trim());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
    public void messageOnAction(ActionEvent actionEvent) throws IOException {
        dataOutputStream.writeUTF(txtMessage.getText());
        txtMessage.setText(null);
    }
}
