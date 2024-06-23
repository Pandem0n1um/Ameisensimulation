package simulation;

import java.util.Random;

public

class Bot {
	public double xPos;
	public double yPos;
	public double direction;
	Random rand1 = new Random();

	public Bot(double xPos, double yPos, double direction) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.direction = direction;
	}

	public
	void
	updatePosition(int sensorLength)
	{
		// move
		this.xPos = this.xPos + Math.cos(this.direction);
		this.yPos = this.yPos + Math.sin(this.direction);
		// rebound from edge
		if (xPos < 0)
		{
			xPos = Painter.imageWidth;
			direction = rand1.nextFloat() * Math.PI * 2;
		}
		else if ( xPos > Painter.imageWidth)
		{
			xPos = 0;
			direction = rand1.nextFloat() * Math.PI * 2;

		}
		else if (yPos > Painter.imageHeight)
		{
			yPos = 0;
			direction = rand1.nextFloat() * Math.PI * 2;

		}
		else if ( yPos < 0 )
		{
			yPos = Painter.imageHeight;
			direction = rand1.nextFloat() * Math.PI * 2;

		}

		// move to others
		if (this.xPos > 1 && this.xPos < Painter.imageWidth - 1 && this.yPos > 1 && this.yPos < Painter.imageHeight - 1)
		{
			double left;
			double straight;
			double right;
			double x;
			double y;
			// left
			left = direction - Math.PI * Simulation.angle;
			x = (xPos + sensorLength * Math.cos(left)) % Painter.imageWidth;
			y = (yPos + sensorLength * Math.sin(left)) % Painter.imageHeight;
			left = Painter.determineDirectionToTurn(x, y);
			// straight
			straight = direction;
			x = (xPos + sensorLength * Math.cos(straight)) % Painter.imageWidth;
			y = (yPos + sensorLength * Math.sin(straight)) % Painter.imageHeight;
			straight = Painter.determineDirectionToTurn(x, y);
			// right
			right = direction + Math.PI * Simulation.angle;
			x = (xPos + sensorLength * Math.cos(right)) % Painter.imageWidth;
			y = (yPos + sensorLength * Math.sin(right)) % Painter.imageHeight;
			right = Painter.determineDirectionToTurn(x, y);
			if (left > straight && left > right)
			{
				direction = direction - Simulation.turnLeft;
			}
			else if (straight > left && straight > right)
			{
				// ahead is biggest concentration => there is no turn needed
			}
			else if (right > left && right > straight)
			{
				direction = direction + Simulation.turnRight;
			}
		}
	}
}
