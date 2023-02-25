package servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class TCPServer {
	public static void main(String argv[]) throws Exception{

		ServerSocket server = new ServerSocket(6789);
		Connections con;
		
		while(true) {
			Socket connectionSocket = server.accept();
			System.out.println("Cliente " + connectionSocket.getInetAddress());
			//estancia e inicia uma thread para cada conexão
			con = new Connections(connectionSocket);
			con.start();
		}
	}
}
