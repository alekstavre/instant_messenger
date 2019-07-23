package client_server;

import javax.swing.JFrame;

public class Main_server {

	public static void main(String[] args) {
		Server ServerObj = new Server();
		ServerObj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ServerObj.runServer();
	}

}
