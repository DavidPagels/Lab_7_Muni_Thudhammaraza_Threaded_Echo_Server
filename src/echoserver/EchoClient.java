package echoserver;

import java.net.*;
import java.io.*;

public class EchoClient implements Runnable {
	public static Socket socket;
	private static OutputStream output;
	private static InputStream input;
	private int flag;

	private EchoClient(int flag){
		this.flag = flag;
	}

	public static void main(String [] args) throws InterruptedException {
		try{
			String host = "localhost";
			if(args.length != 0)
				host = args[0];

			socket = new Socket(host, 6013);
			output = socket.getOutputStream();
			input = socket.getInputStream();
			Thread [] servT = new Thread[2];
			
			for(int i = 0; i < 2; i++){
				servT[i] = new Thread(new EchoClient(i));
				servT[i].start();
			}

			for(int i = 0; i < 2; i++)
				servT[i].join();

			socket.close();
		}catch(IOException ioe){
			System.out.println("IOException");
			System.err.println(ioe);
		}
	}

	@Override
	public void run() {
		if(flag == 0)
			threadedInput();
		else 
			threadedOutput();

	}

	public void threadedInput(){
		int byt;
		try {
			while((byt = System.in.read()) != -1){
				output.write(byt);
			}
			socket.shutdownOutput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void threadedOutput(){
		try {
			int byt;
			while((byt = input.read()) != -1)
				System.out.write(byt);
			System.out.flush();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}