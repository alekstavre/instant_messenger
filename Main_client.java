package client_server;

import javax.swing.JFrame;

public class Main_client {
    public static void main(String args[]){
        Client clientObject = new Client("127.0.0.1");
        clientObject.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientObject.startRunning();
    }
}