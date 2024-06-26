package server;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.*;

import simulation.Simulation;

public class Server extends Thread{
static HttpServer server;
	
	public void run() {
	   try {
		server = HttpServer.create(new InetSocketAddress(8000), 50);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   server.createContext("/parameter", new RequestHandler());
	   server.createContext("/image", new ImageHandler());
	   server.setExecutor(null); // creates a default executor
	   server.start();
	   Simulation.makeSimulation(25000);
	}
	
	public static void main(String args[]){  
		Server server = new Server();  
		server.start();  
	} 
}
