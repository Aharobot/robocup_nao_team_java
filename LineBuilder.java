import java.lang.Math;
import java.util.Vector;
import com.cyberbotics.webots.controller.Camera;
public class LineBuilder {
   
    public static final int neighbourhoodSize = 4;
    public static final int maxTheta  = 180;
    public static final double thetaStep = Math.PI/maxTheta;


	private int[] image;
	private int width, height;
	private double theta; 
	private double r;
	private int pixelRGB;
    private Vector<int[][]> lines;
    private double[] sinCache;
    private double[] cosCache;
    private int [][] imageOutput;
    private int [][] inputIamge;
    private int [][] houghArray;
    private int [][] lineImage;
    private int numPoints;
    private int houghHeight;
    private float centerX, centerY;
 
	public  LineBuilder(int[] image, int width, int height, double r){
		this.image = image;
		this.width = width;
		this.height = height;
        this.r = r;
 	}
    
        public void draw(){
        houghHeight = (int)(Math.sqrt(2)*Math.max(height, width))/2;
        centerX = width/2;
        centerY = height/2;
        double tsin = Math.sin(theta);
        double tcos = Math.cos(theta);
        imageOutput = new int[width][height];
        houghArray =  new int[maxTheta][2*houghHeight];
        numPoints = 0;
        sinCache = new double[maxTheta];
        cosCache = sinCache.clone();
        for(int t = 0; t < maxTheta; t++){
            double readTheta = t*thetaStep;
            sinCache[t] = Math.sin(readTheta);
            cosCache[t] = Math.cos(readTheta);

        }
        if(theta < Math.PI*0.25 || theta > Math.PI*0.75){
            for(int y = 0; y < height; y++){
                int x = (int) ((((r - houghHeight) - ((y - centerY) * tsin)) / tcos) +centerX);                
                if (x < width && x > 0){
                    imageOutput[x][y] = image[x* height+y ];  
                }
                else{
                    for(int i = 0; i < width; i++){
                          int j = (int) ((((r - houghHeight) - ((x - centerX) * tcos)) / tsin) + centerY);
                          if(j < height && j >= 0){
                              imageOutput[i][j] = image[i * height + j];
                          }
                    }
                }
            }
        }
    }

    //public getLine(){
      //  return imageOutput;
   // }
    public void imageIn2D(int[] image){
         inputIamge = new int[width][height];
              for(int x = 0; x< width; x++){
                for(int y = 0; y< height; y++){
                    inputIamge[x][y] = image[x*height+y]; 
                }
            }
             
    }

    public void addPoints(){
        imageIn2D(image);
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++ ){
                if((inputIamge[x][y])!= 0){
                    addPoint(x, y);
                }
            }
        }
    }

    public void addPoint(int x, int y){
        for(int t = 0; t < maxTheta; t++){
            int r = (int)(((x- centerX) * cosCache[t]) + ((y - centerY) * sinCache[t]));
            r += houghHeight;
            
            if(r < 0 || r >= 2*houghHeight) continue;
           
            //increment the hough line;
            houghArray[t][r]++;

        }
        numPoints++;
    }

    public Vector<int[][]> getLines(int thres){
        lines = new Vector<int[][]>(30);
        if(numPoints == 0) return lines;

        //search for local peaks above threshold draw
        for (int t = 0; t < maxTheta; t++){
loop:     
            for (int r = neighbourhoodSize; r < 2*houghHeight - neighbourhoodSize; r++){
                if(houghArray[t][r] > thres){

                    int peak = houghArray[t][r];

                    for (int dx = -neighbourhoodSize; dx <= neighbourhoodSize; dx++){


                         for(int dy = - neighbourhoodSize; dy <= neighbourhoodSize; dy++){

                             int dt = t + dx;
                             int dr = r + dy;
                             if(dt < 0) 
                             dt = dt + maxTheta;
                             else if(dt >= maxTheta)
                             dt = dt - maxTheta;
                             if(houghArray[dt][dr] > peak){
                             continue loop;
                             }
                         }   

                    }

                    //calculate the true value of theta
                    double theta = t* thetaStep;
                    //add the line to the Vector
                    draw();
                    lines.add(imageOutput);

                }

            }

        }
        return lines;
    }

    public int getHighestValue(){
        int max = 0;
        for(int t = 0; t < maxTheta; t++ ){
            for(int r = 0; r < 2*houghHeight; r++){
                if(houghArray[t][r] < max){
                    max  = houghArray[t][r];
                }
                   
            }
        }
        return max;

    }

    public int[][] getHoughArrayImage(){
        int max = getHighestValue();
        for(int t = 0; t < maxTheta; t++){
            for(int r = 0; r < 2*houghHeight; r++){
                double value = 255*((double)houghArray[t][r])/max;
                int v = 255 - (int)value;
                lineImage[t][r] = v; 
            }
        }
        return lineImage;
    }
}
