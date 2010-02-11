package org.penemunxt.projects.penemunxtexplorer.nxt.behaviours;

import org.penemunxt.communication.NXTCommunication;
import org.penemunxt.projects.penemunxtexplorer.RobotData;
import org.penemunxt.projects.penemunxtexplorer.nxt.connection.DataShare;

import lejos.robotics.navigation.SimpleNavigator;
import lejos.robotics.subsumption.Behavior;

public class TargetReached implements Behavior {
	
	final int TARGET_DISTANCE_THRESHOLD = 10;
	SimpleNavigator simnav;
	DataShare DS;
	NXTCommunication NXTC;
	
	public TargetReached(SimpleNavigator simnav, DataShare ds,
			NXTCommunication nxtc) {
		this.simnav = simnav;
		this.DS = ds;
		this.NXTC = nxtc;
		
	}

	@Override
	public void action() {
		DS.clearTargetPos();
		NXTC.sendData(new RobotData(RobotData.POSITION_TYPE_TARGET_REACHED,
				(int) simnav.getX(), (int) simnav.getY(), (int) simnav
				.getHeading(), 0, 0, 0, 0, 0, 0, 0));

	}

	@Override
	public void suppress() {
	}

	@Override
	public boolean takeControl() {
		return (DS.movestowardstargetPoint() && DS.distancetraveled((int)simnav.getX(),(int) DS.TargetPos.x,(int) simnav.getY(),(int)DS.TargetPos.y) < TARGET_DISTANCE_THRESHOLD);
	}

}
