package org.penemunxt.media.pc;

import javax.sound.sampled.*;

public class Sound {
	public static Port getLineOut(Port.Info PortType) {
		try {
			if (AudioSystem.isLineSupported(PortType)) {
				return (Port) AudioSystem.getLine(PortType);
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public static void setSystemVolume(int volumePercentage, Port.Info PortType) {
		Port lineOut = Sound.getLineOut(PortType);
		if (lineOut != null) {
			try {
				lineOut.open();

				FloatControl controlSound = (FloatControl) lineOut
						.getControl(FloatControl.Type.VOLUME);
				controlSound.setValue((float) volumePercentage / 100);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	public static int getSystemVolume(Port.Info PortType) {
		Port lineOut = Sound.getLineOut(PortType);
		if (lineOut != null) {
			try {
				lineOut.open();

				FloatControl controlSound = (FloatControl) lineOut
						.getControl(FloatControl.Type.VOLUME);
				return (int) (100 * (controlSound.getValue() / controlSound
						.getMaximum()));
			} catch (Exception e) {
				System.out.println(e);
				return 0;
			}
		}else{
			return 0;
		}
	}
	
	

}
