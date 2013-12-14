import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;


public class CurveDrawer {
	private BufferedImage frameBuffer;
	private int width;
	private int height;

	private int white;
	private int black;
	private int green;
	private int red;
	private int blue;
	private int darkGray;
	private int lightGray;
	
	private Graphics2D g2;

	private static final int RADIUS = 7;
	private static final int HALFRADIUS = 3;

	private int xClicked;
	private int yclicked;


	ArrayList<Point> controlPoints;
	int[] binomial;
	double t;
	//double k = .01;

	public CurveDrawer(int width, int height){
		this.width = width;
		this.height = height;
		frameBuffer = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		white = Color.WHITE.getRGB();
		black = Color.BLACK.getRGB();
		green = Color.GREEN.getRGB();
		red = Color.RED.getRGB();
		blue = Color.BLUE.getRGB();
		darkGray = Color.DARK_GRAY.getRGB();
		lightGray = Color.LIGHT_GRAY.getRGB();

		g2 = frameBuffer.createGraphics();

		controlPoints = new ArrayList<Point>();
	}

	//	 private MouseAdapter mouseListener = new MouseAdapter() {
	//	        @Override
	//	        public void mousePressed(MouseEvent me) {
	//	            xClicked = me.getX();
	//	            yClicked = me.getY();
	//	            System.out.println("click");
	//	        }
	//	 }

	public void drawBackground(){
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				frameBuffer.setRGB(i, j, white);
			}
		}
	}

	public void drawPoint(int x, int y){	
		g2.setColor(Color.black);
		g2.fillOval(x,y,RADIUS,RADIUS);
		controlPoints.add(new Point(x, y));
		//int[] f = getBinomialCoef(controlPoints.size());

		//draw the control point shape
//		if(controlPoints.size() >1){
//			for(int i = 0; i < controlPoints.size()-1; i++){
//				g2.drawLine(controlPoints.get(i).x+HALFRADIUS, 
//						controlPoints.get(i).y+HALFRADIUS, 
//						controlPoints.get(i+1).x+HALFRADIUS,
//						controlPoints.get(i+1).y+HALFRADIUS);
//			}
//		}
		
		//draw the curve affected by control points
		if(controlPoints.size()>=2){
			drawCurve();
		}
		if(controlPoints.size() == 7){
			clear();
		}
	}

	//draw curve based on control points
	public void drawCurve(){
		//generate formula based on controlPoints.size()
		//binomial = getBinomialCoef(controlPoints.size());
		double x1, y1, x2 = 0, y2 = 0;
		x1 = controlPoints.get(0).x;
		y1 = controlPoints.get(0).y;
		for(t=.01;t<=1;t+=.01){
			//reset x2,y2
			x2 = 0;
			y2 = 0;
			for(int i = 0; i <= controlPoints.size()-1; i++){
				x2 += controlPoints.get(i).x * bernstein(t, controlPoints.size()-1, i);
				y2 += controlPoints.get(i).x * bernstein(t, controlPoints.size()-1, i);
			}
			//System.out.println(x2 + "," + y2);
			g2.drawLine((int)x1,(int)y1, (int)x2, (int)y2);
			x1 = x2;
			y1 = y2;
			System.out.println("From (" + (int)x1 + "," + (int)y1 + ")" + " To (" +(int) x2 + "," + (int)y2 + ")");
			//			System.out.println("To (" +(int) x2 + "," + (int)y2 + ")");
		}
	}
	// calculate bernstein polynomial at position t
	public double bernstein(double n, int exp, int i){
		return getBinomial(exp, i) * Math.pow(n,i) * Math.pow(1-n, exp-i);
	}

	//n choose k, binomial coefficients
	private int getBinomial(int n, int k){
		if (k < 0)  return 0;
		else if (k > n)  return 0;
		else return (factorial(n) / (factorial(k) * factorial(n-k)));
	}

	//old version of calculating binomials
//	private int[] getBinomialCoef(int n){
//		int[] coefs = new int[n+1];
//
//		if(n == 1){
//			coefs[0]= 1;
//		}
//		else{
//			for(int i = 0; i<=n; i++){
//				int c = factorial(n)/(factorial(i)*(factorial(n-i)));
//				coefs[i]=c;
//			}
//		}
//		return coefs;
//	}

	public void clear(){
		g2.setColor(Color.white);
		g2.fillRect(0, 0, width, height);
		controlPoints.clear();
	}

	/**
	 * Returns the rendered image
	 * @return A Image representation of the currently rendered image
	 */
	public Image getImage()
	{
		return frameBuffer;
	}

	private int factorial(int n){
		if(n == 0){
			return 1;
		}
		if(n == 1){
			return 1;
		}
		else return n * factorial(n-1);
	}

	/**
	 * takes a float array color and converts it to an int
	 * @param rgba a color represented by a float array
	 * @return the integer representation of the color
	 */
	public int colorInt(float[] rgba){

		int r = (int)((rgba[0])*(255));
		int g = (int)((rgba[1])*(255));
		int b = (int)((rgba[2])*(255));
		int a = (int)((rgba[3])*(255));
		int color = (a<<24)|(r<<16)|(g<<8)|(b);
		return color;
	}
}
