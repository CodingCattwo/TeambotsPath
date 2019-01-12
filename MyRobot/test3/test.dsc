bounds -5 15 -5 5

seed 3

time 10.0

maxtimestep 100

background xFFFFFF

// target
object EDU.gatech.cc.is.simulation.BinSim  10 0 0 0.50 x0000BB x000000 4

robot EDU.gatech.cc.is.abstractrobot.MultiForageN150Sim 
	Wander 0 -1.5 0 x000000 xFF0000 2

robot EDU.gatech.cc.is.abstractrobot.MultiForageN150Sim 
	Wander 0  1.5 0 x000000 xFF0000 2 

// obstacles
object EDU.gatech.cc.is.simulation.ObstacleSim 4.0 4.0 0 0.35 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim 6 5 0 0.25 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim 7 -.8 0 0.33 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim 7.5 3 0 0.20 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim 8.5 2 0 0.10 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim 9.5 -3.5 0 0.35 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim 6.5 4.2 0 0.20 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim 1.5 4.2 0 0.20 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim 3.5 4.2 0 0.20 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim 2.5 4.2 0 0.20 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim 4.5 4.2 0 0.20 xC0C0C0 x000000 2
object EDU.gatech.cc.is.simulation.ObstacleSim 5.5 4.2 0 0.20 xC0C0C0 x000000 2

