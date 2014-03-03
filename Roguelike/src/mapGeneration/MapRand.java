package mapGeneration;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

/**
 * The class that has general purpose randomization functions
 * needed for advanced map generation.
 * 
 * Also includes tools to work with the randomization.
 * 
 * Uses the MersenneTwister for high quality random numbers.
 * @author Kevin
 *
 */
public class MapRand {

	private static Random rand = new MersenneTwister();



	/** 
	 * Returns the next boolean value
	 * @return
	 */
	public static boolean randBool(){
		return rand.nextBoolean();
	}

	/**
	 * Gets the next random integer
	 * @return
	 */
	public static int randInt(){
		return rand.nextInt();
	}

	/**
	 * Gets the next random integer from 0 up to and including max
	 * @param max
	 * @return
	 */
	public static int randInt(int max){
		return rand.nextInt(Integer.MAX_VALUE) % (max+1);
	}


	/**
	 * Returns the next random integer up to and including
	 * min and min
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randInt(int min, int max){

		return (rand.nextInt((max-min)+1)+min);
	}


	/**
	 * Returns the next random Point within the bounds
	 * of the specified rectangle. 
	 * @param rect A rectangle specified from top left corner
	 * @return A random point within the specified rectangle
	 */
	public static Point randRect(Rectangle rect){
		int xPt = randInt(rect.x, rect.x + rect.width);
		int yPt = randInt(rect.y, rect.y + rect.height);

		return new Point(xPt, yPt);
	}


	/**
	 * Given two diagonally opposite points of the rectangle
	 * forms a rectangle object whose point is defined from the
	 * top left.
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static Rectangle rectFromPoints(Point p1, Point p2){


		//ensure p1 is the the left most
		if (p1.x > p2.x){
			//swap
			Point temp = p1;
			p1 = p2;
			p2 = temp;
		}

		//final two cases, either p2 is above or below p1
		if (p1.y <= p2.y){
			//below p1, can easily form rectangle now
			return new Rectangle(p1.x, p1.y, Math.abs(p2.x-p1.x), Math.abs(p2.y- p1.y));
		}
		else if (p1.y > p2.y) {
			//above or on same horizontal plane as p1. p1 is bottom left, p2 is top right.
			return new Rectangle(p1.x, p2.y, Math.abs(p2.x-p1.x), Math.abs(p1.y- p2.y));
		}



		return new Rectangle();
	}

	/**
	 * Returns a rectangle that represents the original rectangle
	 * with one layer of border removed.
	 * Eg:
	 * 
	 *  #######
	 *  #######          =>        #####
	 *  #######					   #####
	 *  #######
	 * @param rect
	 * @return
	 */
	public static Rectangle innerRectangle(Rectangle rect){
		return new Rectangle(rect.x + 1, rect.y+1, rect.width-2, rect.height-2);
	}



	/**
	 * Checks if a rectangle overlaps, even partially over another
	 * rectangle.
	 * @param r1
	 * @param r2
	 * @return
	 */
	public static boolean overlaps(Rectangle r1, Rectangle r2){

		//check if r2's verticies are contained in r1
		if (r1.contains(r2.getLocation())){
			return true;
		}
		else if(r1.contains(new Point(r2.x + r2.width, r2.y))){
			return true;
		}
		else if(r1.contains(new Point(r2.x, r2.y + r2.height))){
			return true;
		}
		else if(r1.contains(new Point(r2.x + r2.width, r2.y + r2.height))){
			return true;
		}
		return false;
	}
	/*
	public static void main(String args[]){


		//randomization test verification.
		final int MIN = 0;
		final int MAX = 30;
		final int TESTS = 100000;
		int[] freq = new int[MAX-MIN +1];

		for (int i = 0; i< TESTS ; i++){
			freq[randInt(30)] ++;
		}


		for (int i =0; i< freq.length; i++ ){

			System.out.println("freq[" + i  +"] = "+ freq[i]);
		}
	}
	 */
}
