package server;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.sun.net.httpserver.*;

import simulation.Simulation;

class RequestHandler implements HttpHandler {
	@Override
       public void handle(HttpExchange t) throws IOException {
		   t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
           InputStream is = t.getRequestBody();
           Scanner scanner = new Scanner(is).useDelimiter("\\A");
           String body = scanner.hasNext() ? scanner.next() : "";
           System.out.println("Body of inconing Parameter-request: " + body);
           String parameter = body.substring(14).split("\"")[0];
           String value = body.split("value\":")[1].split("}")[0];
           
           switch(parameter) {
           case "Sensor Length":
        	   Simulation.sensorLength = Integer.parseInt(value);
             break;
           case "Turn Left":
        	   Simulation.turnLeft = Double.parseDouble(value);
             break;
           case "Turn Right":
        	   Simulation.turnRight = Double.parseDouble(value);
             break;
           case "Sensor Angle":
        	   Simulation.angle = Double.parseDouble(value);
             break;
           case "Fade Red":
        	   Simulation.fadeRed = Double.parseDouble(value);
             break;
           case "Fade Green":
        	   Simulation.fadeGreen = Double.parseDouble(value);
             break;
           case "Fade Blue":
        	   Simulation.fadeBlue = Double.parseDouble(value);
             break;
           case "Toggle":
        	   Simulation.run = Integer.parseInt(value) == 0 ? false : true;
//        	   System.out.println(Integer.parseInt(value) == 0 ? false : true);
//        	   System.out.println(Simulation.run);
             break;
         }

           String response = "All Good!";
           t.sendResponseHeaders(200, response.length());
           OutputStream os = t.getResponseBody();
           os.write(response.getBytes());
           os.close();
	}
}