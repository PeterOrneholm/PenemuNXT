package org.penemunxt.projects.traveltest.nxt;

import lejos.robotics.navigation.*;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.*;


public class CompassTest {
	public static void main(String[] args) {
		CompassSensor CS = new CompassSensor(SensorPort.S2);
		while(!Button.ESCAPE.isPressed()){
			Sound.playTone((int) CS.getDegrees()*10, 10, 75);
		}
	}
}
