package server;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.sun.net.httpserver.*;

import simulation.Painter;

class ImageHandler implements HttpHandler {
	@Override
       public void handle(HttpExchange t) throws IOException {
		   t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		   //System.out.println("A Request has been recieved!");
           InputStream is = t.getRequestBody();
           Scanner scanner = new Scanner(is).useDelimiter("\\A");
           String body = scanner.hasNext() ? scanner.next() : "";
           ByteArrayOutputStream output = new ByteArrayOutputStream();
           String response = "";
           try
           {
               ImageIO.write(Painter.image, "png", output); //Painter.imageQueue.poll()
        	   response = "{\n"
      					+ "\"image\": " + "\"" + Base64.getEncoder().encodeToString(output.toByteArray()) + "\"\n"
      					+ "}";
           }
           catch(Exception e)
           {
        	   response = "There is no image ready yet!";
           }
           t.sendResponseHeaders(200, response.length());
           OutputStream os = t.getResponseBody();
           os.write(response.getBytes());
           os.close();
	}
}