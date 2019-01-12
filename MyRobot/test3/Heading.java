/**
  using static weighted sum 

  moving obstacle/harassing robot
*/

import	EDU.gatech.cc.is.util.Vec2;
import	EDU.gatech.cc.is.abstractrobot.*;
import	EDU.gatech.cc.is.clay.*;
import  java.util.Random;

public class Heading extends ControlSystemMFN150 {
  public final static boolean DEBUG = true;
  private NodeVec2 turret_configuration;
  private NodeVec2 steering_configuration;
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
  

  double moveToHome = 1.0;
  double noise = 0.3;
  double avoid = 0.8;

  public void configure() {
    //======
		// Set some initial hardware configurations.
		//======
		abstract_robot.setObstacleMaxRange(2.0); // don't consider 
	 						 // things further away
		abstract_robot.setBaseSpeed(0.4*abstract_robot.MAX_TRANSLATION);


    // robot's pos
    NodeVec2 PS_GLOBAL_POS = new v_GlobalPosition_r(abstract_robot);

    // target
    NodeVec2 PS_HOMEBASE_GLOBAL = new v_FixedPoint_ (10.0, 0);
    // robot as center, target's pos
    NodeVec2 PS_HOMEBASE = new v_GlobalToEgo_rv(abstract_robot, PS_HOMEBASE_GLOBAL);
    
    // obstacles
    NodeVec2Array PS_OBS = new va_Obstacles_r(abstract_robot);
    

    // motor schemas
    // vector move to target
    NodeVec2 MS_MOVE_TO_HOMEBASE = new v_LinearAttraction_v(0.4, 0.1, PS_HOMEBASE);
    // Vector to leave detected objects
    NodeVec2 MS_AVOID_OBSTACLES = new v_Avoid_va(2.0, abstract_robot.RADIUS + 0.1, PS_OBS);

    // noise vector
    
    // NodeVec2 MS_NOISE_VECTOR = new v_Noise_(10, 5);
    // double random1 = Math.random();
    // double random2 = Math.random();
    // long randX = (long)Math.random() * 10;
    // long randY = (long)Math.random() * 5;
    // if(random1 <= 0.5 && random2 <= 0.5){
    //   randX = -randX;
    //   randY = -randY;
    // } else if(random1 <= 0.5 && random2 > 0.5){
    //   randX = -randX;
    //   // randY = randY;
    // } else if(random1 > 0.5 && random2 <= 0.5){
    //   // randX = -randX;
    //   randY = -randY;
    // } 
    // else if(random1 > 0.5 && random2 > 0.5){
    //   randX = -randX;
    //   randY = -randY;
    // }
    NodeVec2 MS_NOISE_VECTOR = new v_Noise_(10, getRand());
    // swirl
    // NodeVec2 swirl_obstacles  = new v_Swirl_vav(3.0, abstract_robot.RADIUS + 0.1, PS_OBS, PS_HOMEBASE);
    
    //  AS_GO_HOME
    v_StaticWeightedSum_va AS_GO_HOME = new v_StaticWeightedSum_va();

    AS_GO_HOME.weights[0] = avoid;
    AS_GO_HOME.embedded[0] = MS_AVOID_OBSTACLES;

    AS_GO_HOME.weights[2] = noise;
    AS_GO_HOME.embedded[2] = MS_NOISE_VECTOR;

    // AS_GO_HOME.weights[2] = 0.8;
    // AS_GO_HOME.embedded[2] = swirl_obstacles;

    AS_GO_HOME.weights[1] = moveToHome;
    AS_GO_HOME.embedded[1] = MS_MOVE_TO_HOMEBASE;

    turret_configuration = AS_GO_HOME;
    // vector for steer-configuration
    steering_configuration = AS_GO_HOME;
  }
  public int takeStep() {
    Vec2 result;
    double dresult;
    long curr_time = abstract_robot.getTime();
    Vec2 p;
    double change;
    
    // use configure() to function
    /**
      mtggain   = pb.getmtggain();
			if (mtggain != oldmtggain) configure();
			oldmtggain = mtggain;
     */
    
    

    // SETTER
    result = steering_configuration.Value(curr_time);
    abstract_robot.setSteerHeading(curr_time, result.t);
    abstract_robot.setSpeed(curr_time, result.r);

    
    // if stick in somewhere, set noise larger to escape
    if(oldSteering != 0 ) {
      change = oldSteering - result.t;
      if((change >= 0 && change >= 0.9) || (change < 0 && change <= -0.9)){
        System.out.println("old noise:" + noise);
        noise = 2.0;
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

    // if(oldTurret != 0 ) {
    //   change = oldTurret - result.t;
    //   if((change >= 0 && change >= 0.9) || (change < 0 && change <= -0.9)){
    //     System.out.println("Turret: " + result.t + " noise:" + noise);
    //     noise = 2.0;
    //     configure();
    //   } else {
    //     noise = 0.2;
    //     configure();
    //   }
    // }
    // oldTurret = result.t;

    // STATE DISPLAY
		abstract_robot.setDisplayString("trying to go to the goal");

    // abstract_robot.setSteerHeading(curr_time, heading);
    // abstract_robot.setSpeed(curr_time, speed);

    return(CSSTAT_OK);

  }

}