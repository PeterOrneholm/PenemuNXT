package org.penemunxt.projects.traveltest.nxt;

import lejos.robotics.navigation.*;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.*;


public class TravelTest {
	public static void main(String[] args) {
		CompassPilot compil = new CompassPilot
		(new CompassSensor(SensorPort.S2), 49, 125, Motor.C, Motor.B);
		TachoPilot tacho = new TachoPilot(49, 125, Motor.C, Motor.B);
		SimpleNavigator simnav = new SimpleNavigator (tacho);
//		compil.calibrate();
		compil.resetCartesianZero();
		simnav.setTurnSpeed(50);
		simnav.forward();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {

		}
		simnav.stop();
		simnav.rotate(90);
		simnav.forward();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {

		}
		simnav.stop();
//		simnav.arc(300, 360);
//		simnav.setMoveSpeed(300);
//		simnav.goTo(1000, 0);
//		simnav.rotate(180);
////		simnav.goTo(0, 0);
////		simnav.updatePosition();
//		simnav.travel(2000);
		LCD.drawString(String.valueOf(simnav.getX()), 1, 2);
		LCD.drawString(String.valueOf(simnav.getY()), 1, 3);
		LCD.drawString(String.valueOf(simnav.getHeading()), 1, 4);
		Button.ESCAPE.waitForPressAndRelease();
	}
}
