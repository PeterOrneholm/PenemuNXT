package org.penemunxt.sensors;

public final class SensorRanges {
	public final static int OPTICAL_DISTANCE_MIN_LENGTH_MM = 150;
	public final static int OPTICAL_DISTANCE_MAX_LENGTH_MM = 800;
	public final static int ULTRASONIC_DISTANCE_MIN_LENGTH_CM = 20;
	public final static int ULTRASONIC_DISTANCE_MAX_LENGTH_CM = 100;
	public final static int ULTRASONIC_DISTANCE_ERROR_CODE = 255;

	public static boolean checkOpticalDistanceRange(int value) {
		return (value > OPTICAL_DISTANCE_MIN_LENGTH_MM && value < OPTICAL_DISTANCE_MAX_LENGTH_MM);
	}
	
	public static boolean checkUltrasonicDistanceRange(int value) {
		return (value != ULTRASONIC_DISTANCE_ERROR_CODE
				&& value > ULTRASONIC_DISTANCE_MIN_LENGTH_CM && value < ULTRASONIC_DISTANCE_MAX_LENGTH_CM);
	}
}
