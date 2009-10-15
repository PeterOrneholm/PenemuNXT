package org.penemunxt.projects.communicationtest.pc;
import java.util.ArrayList;

import org.penemunxt.projects.communicationtest.AccelerationValues;

public class DataShare {
	public ArrayList<Integer> Sound;
	public ArrayList<Integer> IRDistance;
	public ArrayList<AccelerationValues> Acceleration;
	
	public DataShare(){
		super();
		Sound = new ArrayList<Integer>();
		IRDistance = new ArrayList<Integer>();
		Acceleration = new ArrayList<AccelerationValues>();
	}
}
