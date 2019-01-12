/*
 * forage.java
 */

import	EDU.gatech.cc.is.util.Vec2;
import	EDU.gatech.cc.is.abstractrobot.*;
import	EDU.gatech.cc.is.clay.*;

/**
 * <B>Introduction</B><BR>
 * Example of complex schema-based control system for a MultiForageN150 
 * robot.  It uses a motor schema-based configuration to search for 
 * attractor * objects and carry them to a homebase.  The configuration 
 * is built using Clay.  * It can be run in simulation or on a robot.
 * <P>
 * For detailed information on how to configure behaviors, see the
 * <A HREF="../clay/docs/index.html">Clay page</A>.
 * <P>
 * <A HREF="../COPYRIGHT.html">Copyright</A>
 * (c)1997 Georgia Tech Research Corporation
 *
 * @author Tucker Balch
 * @version $Revision: 1.2 $
 */

public class Wander  extends ControlSystemMFN150
	{
	public final static boolean DEBUG = true;
	private NodeVec2	turret_configuration;
	private NodeVec2	steering_configuration;
	private NodeDouble	gripper_fingers_configuration;
	private NodeDouble	gripper_height_configuration;
	private NodeInt 	state_monitor;
	private i_FSA_ba STATE_MACHINE;

	/**
	 * Configure the control system using Clay.
	 */
	public void configure()
		{
		//======
		// Set some initial hardware configurations.
		//======
		abstract_robot.setObstacleMaxRange(2.0); // don't consider 
	 						 // things further away
		abstract_robot.setBaseSpeed(0.3 * abstract_robot.MAX_TRANSLATION);
		abstract_robot.setGripperHeight(-1,0);   // put gripper down
		abstract_robot.setGripperFingers(-1,1);  // open gripper

		
		//======
		// perceptual schemas
		//======
		//--- robot's global position
		NodeVec2
		PS_GLOBAL_POS = new v_GlobalPosition_r(abstract_robot);

		//--- obstacles
		NodeVec2Array // the sonar readings
		PS_OBS = new va_Obstacles_r(abstract_robot);

		//--- homebase 
		NodeVec2      // the place to deliver
		PS_HOMEBASE0_GLOBAL = new v_FixedPoint_(0.0,0.0);
		NodeVec2      // make it egocentric
		PS_HOMEBASE0 = new v_GlobalToEgo_rv(abstract_robot,
				PS_HOMEBASE0_GLOBAL);

		// //--- targets of visual class 0
		NodeVec2Array 
		PS_TARGETS0_EGO = 
			new va_VisualObjects_r(0,abstract_robot);
		NodeVec2Array 
		PS_TARGETS0_GLOBAL = 
			new va_Add_vav(PS_TARGETS0_EGO, PS_GLOBAL_POS);

		// //--- filter out targets close to homebase
		NodeVec2Array 
		PS_TARGETS0_GLOBAL_FILT = new va_FilterOutClose_vva(0.75,
			PS_HOMEBASE0_GLOBAL, PS_TARGETS0_GLOBAL);

		// //--- make them egocentric
		NodeVec2Array 
		PS_TARGETS0_EGO_FILT = new va_Subtract_vav(
			PS_TARGETS0_GLOBAL_FILT, PS_GLOBAL_POS);

		// //--- get the closest one
		NodeVec2
		PS_CLOSEST0 = new v_Closest_va(PS_TARGETS0_EGO_FILT);

		// //--- type of object in the gripper
		// NodeInt
		// PS_IN_GRIPPER = new i_InGripper_r(abstract_robot);


		//======
		// Perceptual Features
		//======
		// is something visible?
		NodeBoolean
		PF_TARGET0_VISIBLE = new b_NonZero_v(PS_CLOSEST0);



		//======
		// motor schemas
		//======
		// avoid obstacles
		NodeVec2
		MS_AVOID_OBSTACLES = new v_Avoid_va(0.5,
			abstract_robot.RADIUS + 0.1,
			PS_OBS);
		
		// swirl obstacles wrt homebase0
		NodeVec2
		MS_SWIRL_OBSTACLES_HOMEBASE0 = new v_Swirl_vav(2.0,
			abstract_robot.RADIUS + 0.22,
			PS_OBS,
			PS_HOMEBASE0);

		// noise vector
		NodeVec2
		MS_NOISE_VECTOR = new v_Noise_(5,seed);

		// swirl obstacles wrt noise
		NodeVec2
		MS_SWIRL_OBSTACLES_NOISE = new v_Swirl_vav(2.0,
			abstract_robot.RADIUS + 0.1,
			PS_OBS,
			MS_NOISE_VECTOR);


		//======
		// AS_WANDER
		//======
		v_StaticWeightedSum_va 
		AS_WANDER = new v_StaticWeightedSum_va();

		AS_WANDER.weights[0]  = 1.0;
		AS_WANDER.embedded[0] = MS_AVOID_OBSTACLES;

		AS_WANDER.weights[1]  = 2.0;
		AS_WANDER.embedded[1] = MS_NOISE_VECTOR;

		AS_WANDER.weights[2]  = 1.0;
		AS_WANDER.embedded[2] = MS_SWIRL_OBSTACLES_NOISE;




		//======
		// STATE_MACHINE  switch mode among acquire, wander, deliver 
    // now stay in state-wander
		//======
		STATE_MACHINE = new i_FSA_ba();

		STATE_MACHINE.state = 0;
		
		// STATE 0 WANDER
		STATE_MACHINE.triggers[0][0]  = PF_TARGET0_VISIBLE;
		STATE_MACHINE.follow_on[0][0] = 1; // transition to ACQUIRE0

		state_monitor = STATE_MACHINE;

		//======
		// STEERING
		//======
		v_Select_vai
		STEERING = new v_Select_vai((NodeInt)STATE_MACHINE);

		STEERING.embedded[0] = AS_WANDER;
		// STEERING.embedded[1] = AS_ACQUIRE0;
		// STEERING.embedded[2] = AS_DELIVER0;


		//======
		// TURRET
		//======
		v_Select_vai
		TURRET = new v_Select_vai((NodeInt)STATE_MACHINE);

		TURRET.embedded[0] = AS_WANDER;
		// TURRET.embedded[1] = AS_ACQUIRE0;
		// TURRET.embedded[2] = AS_DELIVER0;


		turret_configuration = TURRET;
		steering_configuration = STEERING;
		// gripper_fingers_configuration = GRIPPER_FINGERS;
		}
		
	/**
	 * Called every timestep to allow the control system to
	 * run.
	 */
	public int takeStep()
		{
		Vec2	result;
		double	dresult;
		long	curr_time = abstract_robot.getTime();
		Vec2	p;

		// STEER
		result = steering_configuration.Value(curr_time);
		abstract_robot.setSteerHeading(curr_time, result.t);
		abstract_robot.setSpeed(curr_time, result.r);

		// TURRET
		result = turret_configuration.Value(curr_time);
		abstract_robot.setTurretHeading(curr_time, result.t);

		// STATE DISPLAY
		int state = STATE_MACHINE.Value(curr_time);
		if (state == 0)
			abstract_robot.setDisplayString("wander");


		return(CSSTAT_OK);
		}
	}
