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
	 * Returns the next boolean value, with skewed bias
	 * towards true. As bias approaches 1.0, the result will 
	 * be more likely to be true.
	 * @param bias Must be between 0 and 1 inclusive
	 * @return
	 */
	private static final double PRECISION = 10000000.0;			//precision level of the bias
	public static boolean randBool(double bias){
		if ((bias < 0.0 )|| (bias > 1.0)){
			throw new IllegalArgumentException("Invalid bias value: " + bias);
		}

		int threshold =  (int) (bias * PRECISION);
		return ((rand.nextInt((int)PRECISION)) <= threshold);
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
	public static Point randPoint(Rectangle rect){
		int xPt = randInt(rect.x, rect.x + rect.width-1);
		int yPt = randInt(rect.y, rect.y + rect.height-1);

		return new Point(xPt, yPt);
	}


	/**
	 * Returns a randomly generated rectangle completely contained
	 * within the specified rectangle.
	 * The returned rectangle will never have a dimension of zero
	 * @param rect
	 * @return
	 */
	public static Rectangle randRect(Rectangle rect){

		int x = randInt(rect.x, rect.x + rect.width -1);		
		int y = randInt(rect.y, rect.y + rect.height -1);

		return new Rectangle(x, y, randInt(1, Math.abs(x - (rect.x + rect.width))), randInt(1, Math.abs(y - (rect.y + rect.height))));
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
			return new Rectangle(p1.x, p1.y, Math.abs(p2.x-p1.x)+1, Math.abs(p2.y- p1.y)+1);
		}
		else if (p1.y > p2.y) {
			//above or on same horizontal plane as p1. p1 is bottom left, p2 is top right.
			return new Rectangle(p1.x, p2.y, Math.abs(p2.x-p1.x)+1, Math.abs(p1.y- p2.y)+1);
		}



		return null;
	}

	/**
	 * Returns a random point along the perimeter of the
	 * given rectangle. The point chosen is uniformly
	 * distributed.
	 * @param r
	 * @return
	 */
	public static Point randPerimeter(Rectangle r){

		//height is 1, special case
		if (r.height <= 1){
			return randPoint(r);
		}

		//otherwise height is at least 2, choose along perimeter
		int perimeter = (2* r.width)+(2*(r.height-2));					//discrete rectangle

		int index = randInt(perimeter-1);		

		//piecewise function
		if(index < r.width){
			//top row of rectangle
			return new Point(r.x + index, r.y);
		}
		else if(index < (r.width + r.height-2)){
			//left edge of rectangle
			return new Point(r.x, r.y+1 + (index-r.width));
		}
		else if(index < (r.width + 2*(r.height-2))){
			//right edge of rectangle
			return new Point(r.x + r.width -1, r.y+1 + (index-(r.width + (r.height-2))));
		}
		else if(index < perimeter){
			//bottom row of rectangle
			return new Point(r.x + (index - (r.width + 2*(r.height-2))), r.y + r.height-1);
		}

		return null;
	}

	protected enum RectangleSide{
		TOP,
		BOTTOM,
		LEFT,
		RIGHT
	}
	/**
	 * Randomly picks a point on a rectangle given the specified side.
	 * @param r
	 * @param side
	 * @return
	 */
	public static Point randRectEdge(Rectangle r, RectangleSide side){
		Point result = null;
		if(side == RectangleSide.TOP){
			result = new Point(randInt(r.x, r.x+r.width-1),r.y);
		}
		else if(side == RectangleSide.BOTTOM){
			result = new Point(randInt(r.x, r.x+r.width-1),r.y + r.height-1);
		}
		else if(side == RectangleSide.LEFT){
			result = new Point(r.x, randInt(r.y, r.y + r.height -1));
		}
		else if(side == RectangleSide.RIGHT){
			result = new Point(r.x+ r.width-1, randInt(r.y, r.y + r.height -1));
		}
		return result;
	}


	private static final int ACCURACY = 10000000;
	/**
	 * Randomly picks an index according to the array of probabilities given.
	 * Eg: A = {0.25, 0.50, 0.25}
	 * The expected returns should be:
	 * 0 => ~25%
	 * 1 => ~50%
	 * 2 => ~25%
	 * 
	 * @param probArray An array in which the sum of all elements add up to 1.
	 * @return An index of the array.
	 */
	public static int randArray(double[] probArray){

		int threshold = 0;
		int randNum = rand.nextInt(ACCURACY-1);

		for (int i =0; i < probArray.length ;i++){
			threshold += probArray[i] * ACCURACY;

			if(randNum < threshold){
				return i;
			}

		}

		//probabilites likely did not add to 1.
		System.err.println("Warning! Probability array did not add to 1.");
		return -1;
	}


	//=======================================================================
	//Perlin noise generator, invented in 1985 by Ken Perlin according to wikipedia
	//Code based on:
	//http://devmag.org.za/2009/04/25/perlin-noise/
	
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public static double[][] randPerlin(int width, int height, int octaveCount){
		double[][] baseNoise = genWhiteNoise(width, height);

		double[][][] smoothNoise = new double[octaveCount][][]; //an array of 2D arrays containing

		double persistance = 0.5f;

		//generate smooth noise
		for (int i = 0; i < octaveCount; i++)
		{
			smoothNoise[i] = GenerateSmoothNoise(baseNoise, i);
		}

		double[][] perlinNoise = new double[width][height];
		double amplitude = 1.0f;
		double totalAmplitude = 0.0f;

		//blend noise together
		for (int octave = octaveCount - 1; octave >= 0; octave--)
		{
			amplitude *= persistance;
			totalAmplitude += amplitude;

			for (int i = 0; i < width; i++)
			{
				for (int j = 0; j < height; j++)
				{
					perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
				}
			}
		}

		//normalisation
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				perlinNoise[i][j] /= totalAmplitude;
			}
		}

		return perlinNoise;

	}


	private static double[][] genWhiteNoise(int width, int height){
		double[][] noise = new double[width][height];

		for (int i=0; i < width; i++){
			for (int j=0; j < height; j++){
				noise[i][j] = rand.nextDouble() % 1;
			}
		}


		return noise;
	}


	private static double[][] GenerateSmoothNoise(double[][] baseNoise, int octave)
	{
		int width = baseNoise.length;
		int height = baseNoise[0].length;

		double[][] smoothNoise = new double[width][height];

		int samplePeriod = 1 << octave; // calculates 2 ^ k
		double sampleFrequency = 1.0f / samplePeriod;

		for (int i = 0; i < width; i++)
		{
			//calculate the horizontal sampling indices
			int sample_i0 = (i / samplePeriod) * samplePeriod;
			int sample_i1 = (sample_i0 + samplePeriod) % width; //wrap around
			double horizontal_blend = (i - sample_i0) * sampleFrequency;

			for (int j = 0; j < height; j++)
			{
				//calculate the vertical sampling indices
				int sample_j0 = (j / samplePeriod) * samplePeriod;
				int sample_j1 = (sample_j0 + samplePeriod) % height; //wrap around
				double vertical_blend = (j - sample_j0) * sampleFrequency;

				//blend the top two corners
				double top = Interpolate(baseNoise[sample_i0][sample_j0],
						baseNoise[sample_i1][sample_j0], horizontal_blend);

				//blend the bottom two corners
				double bottom = Interpolate(baseNoise[sample_i0][sample_j1],
						baseNoise[sample_i1][sample_j1], horizontal_blend);

				//final blend
				smoothNoise[i][j] = Interpolate(top, bottom, vertical_blend);
			}
		}

		return smoothNoise;
	}


	/**
	 * 
	 * @param x0
	 * @param x1
	 * @param alpha
	 * @return
	 */
	private static double Interpolate(double x0, double x1, double alpha){
		return x0 * (1 - alpha) + alpha * x1;
	}








	//======================================================================
	//Rectangle utilities.






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
	 * @return A rectangle that represents the inner rectangle.
	 * Note that any dimension is less than 3 will be given their original
	 * dimensions.
	 */
	public static Rectangle innerRectangle(Rectangle rect){

		int height = rect.height;
		int width = rect.width;

		//shrink width if possible.
		if (height > 2){
			height -= 2;
		}
		if (width > 2){
			width -=2;
		}


		return new Rectangle(rect.x + 1, rect.y+1, width, height);
	}




	/**
	 * Returns a rectangle that represents the original rectangle
	 * with one layer of border added
	 * Eg:
	 * 
	 *  					#######
	 *  #####         =>    #######
	 *  #####				#######
	 * 						#######
	 * @param rect
	 * @return
	 */
	public static Rectangle outerRectangle(Rectangle rect){
		return new Rectangle(rect.x -1, rect.y -1, rect.width+2, rect.height+2);
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

		//check if r1's verticies are contained in r2
		if (r2.contains(r1.getLocation())){
			return true;
		}
		else if(r2.contains(new Point(r1.x + r1.width, r1.y))){
			return true;
		}
		else if(r2.contains(new Point(r1.x, r1.y + r1.height))){
			return true;
		}
		else if(r2.contains(new Point(r1.x + r1.width, r1.y + r1.height))){
			return true;
		}

		return false;
	}




	/**
	 * Checks if a rectangle has negative coordinates
	 * or width or height
	 * @param r
	 * @return
	 */
	public static boolean validRect(Rectangle r){
		if (r.x < 0){
			return false;
		}
		else if (r.y < 0){
			return false;
		}
		else if(r.width < 0){
			return false;
		}
		else if (r.height < 0){
			return false;
		}


		return true;
	}


/*
	public static void main(String args[]){
		double[][] noise = randPerlin(30, 30, 3);
				for (int i =0; i< 30; i++ ){
					for(int j = 0; j < 30; j++){
						System.out.print(noise[i][j] + " ");

					}
					System.out.println("");
				}


		/*
		double[] prob = {0.028, 0.95, 0.022};

		//randomization test verification.
		final int MIN = 0;
		final int MAX = 1;
		final int TESTS = 10000000;
		int[] freq = new int[prob.length];


		for (int i = 0; i< TESTS ; i++){
			freq[MapRand.randArray(prob)] ++;
		}


		for (int i =0; i< freq.length; i++ ){

			System.out.println("freq[" + i  +"] = "+ freq[i]);
		}

		/*
		Rectangle r = new Rectangle(0,0,10,10);
		int freqRect[][] = new int[10][10];

		Point chosen = null;
		for(int i=0; i < TESTS; i++){
			chosen = randRectEdge(new Rectangle(1,1,3,4), RectangleSide.RIGHT);
			freqRect[chosen.y][chosen.x]++;
		}


		for (int i =0; i< freqRect.length; i++ ){

			for(int j = 0; j < freqRect[i].length; j++){
				System.out.print(freqRect[i][j] + "\t");
			}
			System.out.println("");
		}
		 
	}
*/

}
