/**
  global path programming
  using A* algorithm
  moving obstacle/harassing robot
*/

import	EDU.gatech.cc.is.util.Vec2;
import	EDU.gatech.cc.is.abstractrobot.*;
import	EDU.gatech.cc.is.clay.*;
import  java.lang.*;
import  java.util.ArrayList;
import  java.util.Stack;


public class Heading extends ControlSystemMFN150 {
  public final static boolean DEBUG = true;
  private NodeVec2 turret_configuration;
  private NodeVec2 steering_configuration;
  private NodeVec2 avoidVec;

  private NodeVec2 robotPos;
  private NodeVec2 movingHomebase;
  private NodeVec2 moveToHomebase; // to homebase vector
  private NodeVec2Array obstaclesPos;// input the obstacles position(x,y) array
  // getPath from Astar
  Obstacles obstacles = new Obstacles();
  // default target's homebase
  double homeX = 10.0;
  double homeY = 0.0;
  int counter = 0;
  // polar view ji zuobiao
  double heading = 0;
  double speed = 1.0;
  double noise = 0.2;
  
  double oldSteering = 0;
  double oldTurret = 0;

  public long getRand() {
    long randY = 5;
    double random = Math.random();
    if(random <= 0.5) {
      randY = -5;
    }
    return randY;
  }
  public void configure() {
    //======
		// Set some initial hardware configurations.
		//======
		abstract_robot.setObstacleMaxRange(3.0); // don't consider 
	 						 // things further away
		abstract_robot.setBaseSpeed(0.4*abstract_robot.MAX_TRANSLATION);

    // robot's pos 
    NodeVec2 PS_GLOBAL_POS = new v_GlobalPosition_r(abstract_robot);
    robotPos = PS_GLOBAL_POS;

    // target homebase position
    NodeVec2 PS_HOMEBASE_GLOBAL = movingHomebase;
    // robot as center, target's pos
    NodeVec2 PS_HOMEBASE = new v_GlobalToEgo_rv(abstract_robot, PS_HOMEBASE_GLOBAL);
    // vector move to target
    NodeVec2 MS_MOVE_TO_HOMEBASE = new v_LinearAttraction_v(0.4, 0, PS_HOMEBASE);

    /**
    *   use position to avoid instead of force
        avoid moving obstacles/robots
    */
    // obstacles position array
    
    NodeVec2Array PS_OBS = new va_Obstacles_r(abstract_robot);
    NodeVec2 MS_NOISE_VECTOR = new v_Noise_(10, getRand());

    // Vector to leave detected objects
    NodeVec2 MS_AVOID_OBSTACLES = new v_Avoid_va(2.0, abstract_robot.RADIUS + 0.1, PS_OBS);
    avoidVec = MS_AVOID_OBSTACLES;
    //  AS_GO_HOME
    v_StaticWeightedSum_va AS_GO_HOME = new v_StaticWeightedSum_va();

    AS_GO_HOME.weights[0] = 1.0;
    AS_GO_HOME.embedded[0] = MS_AVOID_OBSTACLES;
    AS_GO_HOME.weights[1] = 1.0;
    AS_GO_HOME.embedded[1] = MS_MOVE_TO_HOMEBASE;
    AS_GO_HOME.weights[2] = noise;
    AS_GO_HOME.embedded[2] = MS_NOISE_VECTOR;
    
    turret_configuration = AS_GO_HOME;
    // vector for steer-configuration
    steering_configuration = AS_GO_HOME;
  }
  public int takeStep() {

    long curr_time = abstract_robot.getTime();
    Vec2 result;
    double change;// for noise changing

    Stack<Node> stack = new Stack<>();
    stack = obstacles.path;

    abstract_robot.setSpeed(curr_time, speed);
    
    // set heading
    result = steering_configuration.Value(curr_time);
    abstract_robot.setSteerHeading(curr_time, result.t);
    // if stick in somewhere, set noise larger to escape
    if(oldSteering != 0 ) {
      change = oldSteering - result.t;
      if((change >= 0 && change >= 0.9) || (change < 0 && change <= -0.9)){
        System.out.println("old noise:" + noise);
        noise = 0.5;
        System.out.println("Steering: " + result.t + " noise:" + noise);
        configure();
      } else if((change >= 0 && change <= 0.1) || (change < 0 && change >= -0.1)){
        noise = 0.2;
        configure();
      }
    }
    oldSteering = result.t;  

    // TURRET
		result = turret_configuration.Value(curr_time);
		abstract_robot.setTurretHeading(curr_time, result.t);
    Vec2 pos = abstract_robot.getPosition(curr_time);
    

    // 
    // {
    //   // default is the target's position
    //   movingHomebase =  new v_FixedPoint_ (homeX, homeY);
    //   configure();
    // }
    
    // get path (x,y) from Obstacles.java
    Node tmp = new Node();
    if(!stack.empty()) {
      if (curr_time == 0) {
        tmp = stack.pop();
        tmp = exchangeBack(tmp.intX, tmp.intY);
        homeX = tmp.x;
        homeY = tmp.y;
        System.out.println("first homeX: " + homeX + "homeY: " + homeY);
        movingHomebase =  new v_FixedPoint_ (homeX, homeY);
        configure();
      }
      //distance can't be too small,otherwise it will stuck in the old Homebase
      double distance = Math.sqrt((homeX - pos.x)*(homeX - pos.x) + (homeY - pos.y)*(homeY - pos.y));
      if(distance <= 0.5 ) {
        tmp = stack.pop();
        tmp = exchangeBack(tmp.intX, tmp.intY);
        homeX = tmp.x;
        homeY = tmp.y;
        System.out.println("after homeX: " + homeX + "homeY: " + homeY);
        movingHomebase =  new v_FixedPoint_ (homeX, homeY);
        configure();
      }
    }else {
      movingHomebase =  new v_FixedPoint_ (10.0, 0);
      configure();
    }

    
   // STATE DISPLAY
		abstract_robot.setDisplayString(" homeX, " + homeX + " HomeY:"+ homeY);

    return(CSSTAT_OK);

  }
  // isObstacle's indexX,indexY back to (double, double) in Sim
  public static Node exchangeBack(int indexX, int indexY) {
      Node result = new Node();
      // old node

      result.x = (double)indexX / 2.0 - 5.0;
      result.y = (double)indexY / 2.0 - 5.0;

      System.out.println("result: " + result.x + result.y);
      return result;
  }
}