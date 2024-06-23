package simulation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.stream.IntStream;

public class Painter extends Component

{
	public static BufferedImage image;
	Random rand1 = new Random();
	int counter = 0;
	public static int imageWidth = Simulation.width;
	public static int imageHeight = Simulation.height;

	public Painter(Bot[] bots) {
		image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = image.getGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, imageWidth, imageHeight);
	}

	public void paint(Bot[] bots) {
		Graphics graphics = image.getGraphics();
		for (int i = 0; i < bots.length; i++) {
			Color myColor = new Color(255, 0, 0);
			graphics.setColor(myColor);
			graphics.drawLine((int) bots[i].xPos, (int) bots[i].yPos, (int) bots[i].xPos, (int) bots[i].yPos);
		}
	}

	public void update(Bot[] bots) {
		// fade
		for(int x = 0; x < imageWidth; x ++) {
			for (int y = 0; y < imageHeight; y++) {
				int pixel = image.getRGB(x, y);
				Color color = new Color(pixel, true);
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				red *= Simulation.fadeRed;
				green *= Simulation.fadeGreen;
				blue *= Simulation.fadeBlue;
				color = new Color(red, green, blue);
				image.setRGB(x, y, color.getRGB());
			}
		};
		// blur
		if (rand1.nextDouble() < 1) {
			int[][][] rgbPixels = new int[imageWidth][imageHeight][3];
			IntStream.range(1, imageWidth - 1).parallel().forEach(x -> {
				for (int y = 1; y < imageHeight - 1; y++) {
					int pixel = image.getRGB(x, y);
					Color myDarnAwesomeColor = new Color(pixel, true);
					rgbPixels[x][y][0] += myDarnAwesomeColor.getRed();
					rgbPixels[x][y][1] += myDarnAwesomeColor.getGreen();
					rgbPixels[x][y][2] += myDarnAwesomeColor.getBlue();
				}
			});
			IntStream.range(1, imageWidth - 1).parallel().forEach(x -> {
				for (int y = 1; y < imageHeight - 1; y++) {
					int red = 0;
					int green = 0;
					int blue = 0;
					for (int xPos = -1; xPos < 2; xPos++) {
						for (int yPos = -1; yPos < 2; yPos++) {
							red += rgbPixels[x + xPos][y + yPos][0];
							green += rgbPixels[x + xPos][y + yPos][1];
							blue += rgbPixels[x + xPos][y + yPos][2];
						}
					}
					Color myDarnAwesomeColor = new Color(red / 9, green / 9, blue / 9);
					image.setRGB(x, y, myDarnAwesomeColor.getRGB());
				}
			});
		}
		// paint bots
		IntStream.range(0, bots.length).parallel().forEach(i -> {
			try {
				// has to be here, otherwise parallelism messes up the colors
				Graphics graphicsHere = image.getGraphics();
				Color myColor = Simulation.botColor;
				graphicsHere.setColor(myColor);
				graphicsHere.drawLine((int) bots[i].xPos, (int) bots[i].yPos, (int) bots[i].xPos, (int) bots[i].yPos);
			} catch (Exception e) {
				System.out.print(e);
			}
		});
	}

	public static double determineDirectionToTurn(double x, double y) {
		int red = 0;
		int green = 0;
		int blue = 0;
		if(x < 0)
		{
			x = 0;
		}
		if(y < 0)
		{
			y = 0;
		}
		Color myDarnAwesomeColor = new Color(image.getRGB((int) x, (int) y));
		red = myDarnAwesomeColor.getRed();
		return red;
	}
}