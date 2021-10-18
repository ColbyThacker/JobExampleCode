import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.lang.Math; 

import javax.swing.*;

public class TSP extends JFrame{

	private static final long serialVersionUID = 1L;

// This is defining the point class along with its' basic constructor giving it an X and a Y coordinate
private static class Point{
    final double x1; 
    final double y1;
    public Point(double x1, double y1, Color color) {
        this.x1 = x1;
        this.y1 = y1;
    }               
}

//This is a basic linked list of the points that are added
private final static LinkedList<Point> points = new LinkedList<Point>();
//This is a basic linked list of the points but this time they are ordered to form the shortest possible path
private final static LinkedList<Point> orderedCircuit = new LinkedList<Point>();

//Simple function to add a point to the linked list "points"
public void addPoint(double x1, double y1) {
    points.add(new Point(x1, y1, Color.red));        
    repaint();
}

//Simple function to calculate the distance between two points
public double distBtwPts(double x1, double y1, double x2, double y2)
{
	return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
}

//Find the distance between a line and a point
public double findLinePtDist(int ptIndex, int ocIndex)
{
	double x0 = points.get(ptIndex).x1;
	double y0 = points.get(ptIndex).y1;
	double x1 = orderedCircuit.get(ocIndex).x1;
	double y1 = orderedCircuit.get(ocIndex).y1;
	double x2, y2;
	if(ocIndex+1 == orderedCircuit.size())
	{
		x2 = orderedCircuit.get(0).x1;
		y2 = orderedCircuit.get(0).y1;
	} else {
		x2 = orderedCircuit.get(ocIndex+1).x1;
		y2 = orderedCircuit.get(ocIndex+1).y1;
	}
	double px=x2-x1;
    double py=y2-y1;
    double temp=(px*px)+(py*py);
    double u=((x0 - x1) * px + (y0 - y1) * py) / (temp);
    if(u>1){
        u=1;
    }
    else if(u<0){
        u=0;
    }
    double x = x1 + u * px;
    double y = y1 + u * py;

    double dx = x - x0;
    double dy = y - y0;
    double dist = Math.sqrt(dx*dx + dy*dy);
    return dist;

}

//Algorithm which when inputted a list of points organizes a linked list of the points in such a way that every point is connected to the closest line created by two other points 
public void runGreedyAlg(TSP ts)
{
	orderedCircuit.add(new Point(points.get(0).x1, points.get(0).y1, Color.black));
	orderedCircuit.add(new Point(points.get(1).x1, points.get(1).y1, Color.black));
	points.remove(1);
	points.remove(0);
	double shortestDist = 100000;
	double tempDist = 100000;
	int shortestPtInd = 0;
	int shortestOCInd = 0;
	while(points.size() != 0)
	{
		for(int i=0; i < points.size(); i++)
		{
			for (int j = 0; j < orderedCircuit.size(); j++)
			{
				tempDist = findLinePtDist(i, j);
				if(tempDist <= shortestDist)
				{
					shortestDist = tempDist;
					shortestPtInd = i;
					shortestOCInd = j+1;
				}
			}
		}
		orderedCircuit.add(shortestOCInd, new Point(points.get(shortestPtInd).x1, points.get(shortestPtInd).y1, Color.black));
		points.remove(shortestPtInd);
		shortestDist = 100000;
		tempDist = 100000;
		shortestPtInd = 0;
		shortestOCInd = 0;
	}
}

//Reads the points from a preset file then sends the results to my greedy algorithm, the rest of the main function is just for printing the results to the console and drawing the results
public static void main(String[] args) {
	TSP t = new TSP();
	String[] splitstr = new String[3];
	try {
	      File myObj = new File("C:\\Users\\colby\\Documents\\Project4\\Random100.tsp");
	      boolean isCoords = false;
	      Scanner myReader = new Scanner(myObj);
	      while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	        if(isCoords) {
	        	splitstr = data.trim().split("\\s+");
	        	t.addPoint(Double.parseDouble(splitstr[1]), Double.parseDouble(splitstr[2]));
	        	Arrays.fill(splitstr, null);
	        }
	        if(data.contains("NODE_COORD_SECTION")) {
	        	isCoords = true;
	        }
	      }
	      myReader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
    t.runGreedyAlg(t);
    System.out.println("ORDER OF TOUR");
    for (int j = 0; j < orderedCircuit.size(); j++)
	{
    	System.out.println("X : "+orderedCircuit.get(j).x1+" ; Y : "+orderedCircuit.get(j).y1);
	}
    t.add(new JComponent() {

		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            repaint();
            for (int i = 0; i < orderedCircuit.size()-1; i++) 
    		{
            	Shape l = new Line2D.Double(orderedCircuit.get(i).x1 * 10, orderedCircuit.get(i).y1 * 10, orderedCircuit.get(i+1).x1 * 10, orderedCircuit.get(i+1).y1 * 10);
            	g2.draw(l);
    		}
            Shape l = new Line2D.Double(orderedCircuit.get(0).x1 * 10, orderedCircuit.get(0).y1 * 10, orderedCircuit.get(orderedCircuit.size()-1).x1 * 10, orderedCircuit.get(orderedCircuit.size()-1).y1 * 10);
            g2.draw(l);
		}
    });

    t.setDefaultCloseOperation(EXIT_ON_CLOSE);
    t.setSize(1000, 1000);
    t.setVisible(true);
	
}

}