import lejos.localization.Point;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassSensor;

public class Navigation extends Thread implements Runnable{
	static int wheeldiameter = 56;
	static int wheeldistance = 115;
	double leftwheeloldangle = 0;
	double rightwheeloldangle = 0;
	double robotangle = 0;
	double robotnewangle;
	double leftwheelangle;
	double rightwheelangle;
	double leftdist = 0;
	double rightdist = 0;
	Point robotpos = new Point(0,0);
	
	CompassSensor CS = new CompassSensor(SensorPort.S4);
	
	public static void main ( String args[]){
		
		Motor.A.setSpeed(50);
		Motor.B.setSpeed(50);
		
		Navigation nav = new Navigation();
		
		
		nav.run();
	}

	/*public static void Navigation ( int wheeldia, int wheeldist){
		wheeldiameter = wheeldia;
		wheeldistance = wheeldist;
		
	}*/
	
	/*public double getRobotangle () {
				
		leftwheelangle = (((Motor.A.getTachoCount()/180)*Math.PI) - leftwheeloldangle);
		rightwheelangle = (((Motor.B.getTachoCount()/180)*Math.PI) - rightwheeloldangle);
		
		leftdist = ((wheeldiameter*Math.PI)*leftwheelangle/(2*Math.PI));
		rightdist = ((wheeldiameter*Math.PI)*rightwheelangle/(2*Math.PI));
		
		robotnewangle = (leftdist-rightdist)/wheeldistance;
		
		leftwheeloldangle += leftwheelangle;
		rightwheeloldangle += rightwheelangle;
		robotangle += robotnewangle;
		
		return robotangle;
				
	}*/
	
	public double getRobotangle(){
		robotnewangle = robotangle - (Math.toRadians(CS.getDegreesCartesian()));
		robotangle = (Math.toRadians(CS.getDegreesCartesian()));
		
		leftwheelangle = ((Math.toRadians(Motor.A.getTachoCount())) - leftwheeloldangle);
		rightwheelangle = ((Math.toRadians(Motor.B.getTachoCount())) - rightwheeloldangle);
		
		leftdist = ((wheeldiameter*Math.PI)*leftwheelangle/(2*Math.PI));
		rightdist = ((wheeldiameter*Math.PI)*rightwheelangle/(2*Math.PI));
		
		return robotangle;
	}
	
	public double getRobotAverageangle(){
		return (2*robotangle+robotnewangle)/2;
	}
	
	public double getRobotAverageDist(){
		return (leftdist+rightdist)/2;
		
	}
	
	public Point getRobotpos() {
		float x, y, hypotenuse;
		
		hypotenuse = (float)(Math.sqrt((2*Math.pow((getRobotAverageDist()/robotnewangle),2))
				-((2*Math.pow((getRobotAverageDist()/robotnewangle),2))*Math.cos(robotnewangle))));
		
		x = (float)(robotpos.x + (Math.cos(getRobotAverageangle()*hypotenuse)));
		y = (float)(robotpos.y + (Math.sin(getRobotAverageangle()*hypotenuse)));
		
		robotpos.x = x;
		robotpos.y = y;
		
		return robotpos;
	}

	@Override
	public void run() {
		CS.resetCartesianZero();
		LCD.clear();
		Motor.A.forward();
		Motor.B.backward();
		LCD.drawString("a",1,5);
		while(!Button.ESCAPE.isPressed()){
		LCD.drawString("b",2,5);
		int robotangle = (int) (Math.toDegrees(getRobotangle()));
		LCD.clear();
		LCD.drawInt(robotangle, 1, 1);

		LCD.drawInt((int) getRobotpos().x, 1, 2);
		LCD.drawInt((int) getRobotpos().y, 1, 3);

	
		}
	}	
}
