
bounds -5 15 -5 5

seed 3

time 10.0

maxtimestep 100

background 	xE1FFFF

// target
object EDU.gatech.cc.is.simulation.BinSim  10 0 0 0.50 x0000BB x000000 4

robot  EDU.gatech.cc.is.abstractrobot.MultiForageN150Sim 
	Heading -4.5 0 0 x000000 xFF0000 2

robot EDU.gatech.cc.is.abstractrobot.MultiForageN150Sim 
	Wander 0 -1.5 0 xC0C0C0 x000000 2

robot EDU.gatech.cc.is.abstractrobot.MultiForageN150Sim 
	Wander 0  1.5 0 xC0C0C0 x000000 2 

robot EDU.gatech.cc.is.abstractrobot.MultiForageN150Sim 
	Wander 0  2.5 0 xC0C0C0 x000000 2 

robot EDU.gatech.cc.is.abstractrobot.MultiForageN150Sim 
	Wander 0  -2.5 0 xC0C0C0 x000000 2 

robot EDU.gatech.cc.is.abstractrobot.MultiForageN150Sim 
	Wander 4  1.5 0 xC0C0C0 x000000 2 

robot EDU.gatech.cc.is.abstractrobot.MultiForageN150Sim 
	Wander 2  -1.5 0 xC0C0C0 x000000 2 
robot EDU.gatech.cc.is.abstractrobot.MultiForageN150Sim 
	Wander 3  1.5 0 xC0C0C0 x000000 2 

// obstacles                                    x    y   z radius
// west-north 
object EDU.gatech.cc.is.simulation.ObstacleSim  -0.5  3.5    0 0.40 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  -1.5   0    0 0.30 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  -2.5 2    0 0.35 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  -4   4    0 0.30 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  1  1.2    0 0.30 xC0C0C0 x000000 2



//west-south 
object EDU.gatech.cc.is.simulation.ObstacleSim   0.8  -1   0 0.30 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  -0.5 -2.8   0 0.25 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  -1.5  -4   0 0.20 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  -3  -2.3   0 0.30 xC0C0C0 x000000 2

// east-north

object EDU.gatech.cc.is.simulation.ObstacleSim  3.5  2.8    0 0.40 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  5   0.8   0 0.30 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  6.5  3.5   0 0.40 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  8    1    0 0.30 xC0C0C0 x000000 2


//east-south

object EDU.gatech.cc.is.simulation.ObstacleSim  3  	0    0 0.30 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  3  -1.5    0 0.30 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  3  -2.0    0 0.30 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  3   -2.5    0 0.30 xC0C0C0 x000000 2

object EDU.gatech.cc.is.simulation.ObstacleSim  6   -3    0 0.30 xC0C0C0 x000000 2

object EDU.gatech.cc.is.simulation.ObstacleSim  7  -1.2    0 0.30 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  6.5  -0.9    0 0.30 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  7.5  -1.5    0 0.30 xC0C0C0 x000000 2


// mark the corner
object EDU.gatech.cc.is.simulation.ObstacleSim  -5   5    0 0.30 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  -5   -5   0 0.30 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  15    5    0 0.30 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim  15    -5   0 0.30 xC0C0C0 x000000 2

// object EDU.gatech.cc.is.simulation.ObstacleSim -2.0 -1.0 0 0.30 xC0C0C0 x000000 2
// object EDU.gatech.cc.is.simulation.ObstacleSim 2.5   2   0 0.30 xC0C0C0 x000000 2
// object EDU.gatech.cc.is.simulation.ObstacleSim -4    4   0 0.35 xC0C0C0 x000000 2
// object EDU.gatech.cc.is.simulation.ObstacleSim 4   -3.5  0 0.15 xC0C0C0 x000000 2
// object EDU.gatech.cc.is.simulation.ObstacleSim 3.5  -2   0 0.25 xC0C0C0 x000000 2
// object EDU.gatech.cc.is.simulation.ObstacleSim -3.5 -2   0 0.35 xC0C0C0 x000000 2
// object EDU.gatech.cc.is.simulation.ObstacleSim 3.5  -2   0 0.15 xC0C0C0 x000000 2

// object EDU.gatech.cc.is.simulation.ObstacleSim 5    0    0 0.30 xC0C0C0 x000000 2

// object EDU.gatech.cc.is.simulation.ObstacleSim 4.0  4.0   0 0.35 xC0C0C0 x000000 2
// object EDU.gatech.cc.is.simulation.ObstacleSim 6    5     0 0.25 xC0C0C0 x000000 2
// object EDU.gatech.cc.is.simulation.ObstacleSim 7   -0.8   0 0.33 xC0C0C0 x000000 2
// object EDU.gatech.cc.is.simulation.ObstacleSim 7.5  3     0 0.20 xC0C0C0 x000000 2
// object EDU.gatech.cc.is.simulation.ObstacleSim 8.5  2     0 0.10 xC0C0C0 x000000 2
// object EDU.gatech.cc.is.simulation.ObstacleSim 9.5 -3.5   0 0.35 xC0C0C0 x000000 2
// object EDU.gatech.cc.is.simulation.ObstacleSim 10.5 4.2   0 0.20 xC0C0C0 x000000 2



