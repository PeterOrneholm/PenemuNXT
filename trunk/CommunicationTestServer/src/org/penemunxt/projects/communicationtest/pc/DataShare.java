package org.penemunxt.projects.communicationtest.pc;
import java.util.ArrayList;

public class DataShare {
	public ArrayList<Integer> Sound;
	public ArrayList<Integer> IRDistance;
	public ArrayList<Integer> USDistance;
	
	public DataShare(){
		super();
		Sound = new ArrayList<Integer>();
		IRDistance = new ArrayList<Integer>();
		USDistance = new ArrayList<Integer>();
	}
}
