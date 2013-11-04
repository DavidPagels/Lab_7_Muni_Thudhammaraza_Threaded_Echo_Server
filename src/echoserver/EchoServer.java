package echoserver;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class EchoServer implements Runnable{
	Socket socket;
	InputStream input;
	OutputStream output;

	private EchoServer(Socket client){
		try {
			socket = client;
			input = client.getInputStream();
			output = client.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String [] args){
		int index = 0;
		try{
			ServerSocket sock = new ServerSocket(6013);
			ArrayList<Thread> threads = new ArrayList<Thread>();

			while(true){
				while(index < 40){
					Socket client = sock.accept();
					threads.add(new Thread(new EchoServer(client)));
					threads.get(index).start();
					index++;
				}	
			}
		} catch(IOException ioe) {
			System.err.println(ioe);
		}
	}

	public void run(){
		process();
	}

	public void process(){
		int byt;
		try {
			while((byt = input.read()) != -1)
				output.write(byt);
			output.flush();
			socket.shutdownOutput();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}