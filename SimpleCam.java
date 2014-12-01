//-----------------------------------------------------------------------------
//  File:         SimpleCam.java (to be used in a Webots java controllers)
//  Date:         March 28, 2012
//  Description:  Camera with simple blob localization capability
//  Project:      Robotstadium, the online robot soccer competition
//  Code Based on original provider :Yvan Bourquin - www.cyberbotics.com   
//  
//-----------------------------------------------------------------------------

import java.lang.Math;
import com.cyberbotics.webots.controller.Camera;

public class SimpleCam extends Camera {


  
  public static final double UNKNOWN = -999.9;
  public enum Goal { SKY_BLUE, YELLOW, UNKNOWN_COLOR };
  public enum Foot {RED, BLUE, UNKNOWN_COLOR};
  private Goal goalColor;
  private Foot footColor;
  private double fov;
  private int width, height;
  private int[] image;
  private int[] binaryImage;
  private double blobDirectionAngle = UNKNOWN;
  private double blobElevationAngle = UNKNOWN;
  private double ballDirectionAngle = UNKNOWN;
  private double ballElevationAngle = UNKNOWN;
  private double goalDirectionAngle = UNKNOWN;
  private double goalElevationAngle = UNKNOWN;
  private double lineDirectionAngle = UNKNOWN;
  private double lineElevationAngle = UNKNOWN;
  private double ourGoalDirectionAngle = UNKNOWN;
  private double ourGoalElevationAngle = UNKNOWN;
  private double coPlayerDirectionAngle = UNKNOWN;
  private double coPlayerElevationAngle = UNKNOWN;
  private double opPlayerDirectionAngle = UNKNOWN;
  private double opPlayerElevationAngle = UNKNOWN;

  protected EdgeDetector edge;
  protected LineBuilder line;
  double r = 2.3;
  public SimpleCam(String name) {

      super(name);
      goalColor = Goal.UNKNOWN_COLOR;
      footColor = Foot.UNKNOWN_COLOR;
      fov = getFov();
      width = getWidth();
      height = getHeight();
     }
  
  public void setGoalColor(Goal goal) {
      this.goalColor = goal;
  }

  public void setFootColor(Foot foot){
      this.footColor = foot;
  }



  // find a blob whose rgb components match
  private void findColorBlob(int R, int G, int B, int threshold) {

      int x = 0, y = 0;
      int npixels = 0;

      for (int i = 0; i < image.length; i++) {
          int r = Camera.pixelGetRed(image[i]);
          int g = Camera.pixelGetGreen(image[i]);
          int b = Camera.pixelGetBlue(image[i]);

      if (Math.abs(r - R) + Math.abs(g - G) + Math.abs(b - B) < threshold) {
           x += i % width;
           y += i / width;
           npixels++;
      }
    }

    if (npixels > 0) {
      blobDirectionAngle = ((double)x / npixels / width - 0.5) * fov;
      blobElevationAngle = -((double)y / npixels / height - 0.5) * fov;
    }
    else {
      blobDirectionAngle = UNKNOWN;
      blobElevationAngle = UNKNOWN;
    }
  }

// analyse image and find field line which is white
  public int[] getBinaryImage(int[] img){
	  binaryImage = new int[img.length]; 
      for(int i = 0; i < img.length; i++){
          int r = Camera.pixelGetRed(img[i]);
          int g = Camera.pixelGetGreen(img[i]);
          int b = Camera.pixelGetBlue(img[i]);
          if( (r+g+b)/3 > 250){
              binaryImage[i] = 255;
          }
          if ((r+g+b)/3 <= 250){
              binaryImage[i] = 0;
          } 
      }
      return binaryImage;   
  }
  public void findLine(int[] biImage){
      int x = 1; 
      int y = 1;
      int npixels = 0;
      double rate = 0;
      for(int i = 0; i < biImage.length; i++){
          if(biImage[i] == 255){
              x += i % width;
              y += i / width;
              npixels++;
              rate = x / y;
          }
          if(npixels > 20 && rate > 5){
              lineDirectionAngle = ((double)x / npixels / width - 0.5) * fov;
              lineElevationAngle = -((double)y / npixels / height - 0.5) * fov;
          } 
          else{
              lineDirectionAngle = UNKNOWN;
              lineElevationAngle = UNKNOWN;
          }
     } 
  
  }
  // analyse image and find blobs
  public void processImage() {

    image = getImage();
    int[] biImg = getBinaryImage(image);
    findLine(biImg);
    // find orange ball
    findColorBlob(240, 140, 50, 60);
    ballDirectionAngle = blobDirectionAngle;
    ballElevationAngle = blobElevationAngle;
    //System.out.println("camera: ball: dir: " + ballDirectionAngle + " elev: " + ballElevationAngle);

    // find goal
    switch (goalColor) {
        case SKY_BLUE:
        findColorBlob(30, 200, 200, 60);
        goalDirectionAngle = blobDirectionAngle;
        goalElevationAngle = blobElevationAngle;
        findColorBlob(140, 140, 15, 60);
        ourGoalDirectionAngle  = blobDirectionAngle;
        ourGoalElevationAngle = blobElevationAngle;
        break;
        case YELLOW:
        findColorBlob(140, 140, 15, 60);
        goalDirectionAngle = blobDirectionAngle;
        goalElevationAngle = blobElevationAngle;
        findColorBlob(30, 200, 200, 60);
        ourGoalDirectionAngle = blobDirectionAngle;
        ourGoalElevationAngle = blobElevationAngle;
        break;
        default:
        goalDirectionAngle = UNKNOWN;
        goalElevationAngle = UNKNOWN;
        ourGoalDirectionAngle = UNKNOWN;
        ourGoalElevationAngle = UNKNOWN;
    }
    
    //System.out.println("camera: goal: dir: " + goalDirectionAngle + " elev: " + goalElevationAngle);
    switch(footColor){
        case BLUE:
        findColorBlob(34, 34, 255, 60);
        coPlayerDirectionAngle = blobDirectionAngle;
        coPlayerElevationAngle = blobElevationAngle;
        findColorBlob(255,34, 34, 60);
        opPlayerDirectionAngle = blobDirectionAngle;
        opPlayerElevationAngle = blobElevationAngle;
        break;
        case RED:
        findColorBlob(255, 34, 34, 60);
        coPlayerDirectionAngle = blobDirectionAngle;
        coPlayerElevationAngle = blobElevationAngle;
        findColorBlob(34, 34, 255, 60);
        opPlayerDirectionAngle = blobDirectionAngle;
        opPlayerElevationAngle = blobElevationAngle;
        break;
        default:
        coPlayerDirectionAngle = UNKNOWN;
        coPlayerElevationAngle = UNKNOWN;
        opPlayerDirectionAngle = UNKNOWN;
        opPlayerElevationAngle = UNKNOWN;
    }
    
  }

  // all direction and elevation angles are indicated in radians
  // with respect to the camera focal (or normal) line
  // a direction angle will not exceed +/- half the field of view
  // a positive direction is towards the right of the camera image
  // a positive elevation is towards the top of the camera image
  public double getBallDirectionAngle() {
      return ballDirectionAngle;
  }

  public double getBallElevationAngle() {
      return ballElevationAngle;
  }

  public double getGoalDirectionAngle() {

      return goalDirectionAngle;
  }

  public double getGoalElvationAngle(){
      return goalElevationAngle;
  
  }

  public double getOurGoalDirectionAngle(){
      return ourGoalDirectionAngle;
  }

  public double getOurGoalElevationAngle(){
      return ourGoalElevationAngle;
  }
  public double getLineDirectionAngle(){
      return lineDirectionAngle;
  
  }
  public double getLineElevationAngle(){
      return lineElevationAngle;
  }

  public double getCoPlayerDirectionAngle(){
      return coPlayerDirectionAngle;
  
  }

  public double getCoPlayerElevationAngle(){
      return coPlayerElevationAngle;
  }

  public double getOpPlayerDirectionAngle(){
      return opPlayerDirectionAngle;
  }

  public double getOpPlayerElevationAngle(){
      return opPlayerElevationAngle;
  }

     
  public void setFieldEdge(){
      edge = new EdgeDetector(image, width, height);
      edge.setThreshold(250);
      edge.setWidGaussianKernel(10);
          edge.process();
      
       double r = (double)((width+height)/2);
      line = new LineBuilder(edge.getEdgeImage(), width, height, r);
      int[][] aline = line.getHoughArrayImage();
      int numpix = 0;
      for(int i = 0; i < width; i++){
          for(int j = 0; j < height; j++){
              if(aline[i][j] > 255){
                 numpix++;
              }
          }
      }
  }
}

