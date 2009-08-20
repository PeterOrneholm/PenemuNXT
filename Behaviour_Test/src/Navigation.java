import lejos.localization.Point;
import lejos.nxt.Motor;


public class Navigation {
	
	static int wheeldiameter;
	static int wheeldistance;
	double leftwheeloldangle = 0;
	double rightwheeloldangle = 0;
	double robotangle = 0;
	double robotnewangle;
	double leftwheelangle;
	double rightwheelangle;
	double leftdist = 0;
	double rightdist = 0;
	Point robotpos = new Point(0,0);

	public static void Navigation ( int wheeldia, int wheeldist){
		wheeldiameter = wheeldia;
		wheeldistance = wheeldist;
		
	}
	
	public double getRobotangle () {
				
		leftwheelangle = (((Motor.A.getTachoCount()/180)*Math.PI) - leftwheeloldangle);
		rightwheelangle = (((Motor.B.getTachoCount()/180)*Math.PI) - rightwheeloldangle);
		
		leftdist = ((wheeldiameter*Math.PI)*leftwheelangle/(2*Math.PI));
		rightdist = ((wheeldiameter*Math.PI)*rightwheelangle/(2*Math.PI));
		
		robotnewangle = (leftdist-rightdist)/wheeldistance;
		
		leftwheeloldangle += leftwheelangle;
		rightwheeloldangle += rightwheelangle;
		robotangle += robotnewangle;
		
		return robotangle;
				
	}
	
	public double getRobotAverageangle(){
		return (2*robotangle+robotnewangle)/2;
	}
	
	public double getRobotAverageDist(){
		return (leftdist+rightdist)/2;
		
	}
	
	public Point getRobotpos() {
		double x, y, hypotenuse;
		
		hypotenuse = Math.sqrt((2*Math.pow((getRobotAverageDist()/robotnewangle),2))
				-((2*Math.pow((getRobotAverageDist()/robotnewangle),2))*Math.cos(robotnewangle)));
		
		x = robotpos.x + (Math.cos(getRobotAverageangle()*hypotenuse));
		y = robotpos.y + (Math.cos(getRobotAverageangle()*hypotenuse));
		
		robotpos.x = (float) x;
		robotpos.y = (float) y;
		
		return robotpos;
	}
	
	
}
