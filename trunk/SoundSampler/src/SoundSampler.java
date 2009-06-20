import java.util.ArrayList;

import lejos.nxt.*;
import lejos.util.*;

public class SoundSampler implements Runnable {
	public enum Mode {
		Waiting, Sampling, Playing
	}

	Mode BotMode = Mode.Waiting;
	private ArrayList<Integer> Notes = new ArrayList<Integer>();

	public static void main(String args[]) throws InterruptedException {
		SoundSampler mainSS = new SoundSampler();
		mainSS.run();
	}
	
	public static void ShowStatus(String Status){
		LCD.clearDisplay();
		LCD.drawString(Status, 0, 0);
		LCD.refresh();
	}
	
	@Override
	public void run() {
		SoundSensor SoundS2 = new SoundSensor(SensorPort.S2);
		Stopwatch SW = new Stopwatch();
		int LastSSV = 0;

		Timer TM;
		
		ShowStatus("Wait for beats");
		
		while(!Button.ESCAPE.isPressed()){
			if (BotMode != Mode.Playing) {
				int SSV = SoundS2.readValue();
				if (SSV >= 90 && !(LastSSV >= 90)) {
					if (BotMode == Mode.Waiting) {
						BotMode = Mode.Sampling;
						ShowStatus("Collecting beats!");
						SW.reset();
						Notes.clear();
						
						PlayTimerListener TL =new PlayTimerListener(Notes, this); 
						TM = new Timer(3000, TL);
						TL.TM = TM;
						TM.start();
					}
					Notes.add(SW.elapsed());
				}

				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}

				LastSSV = SSV;
			}
		}
		
		ShowStatus("Goodbye!");
		System.exit(0);
	}

	public void Play(ArrayList<Integer> Notes) {
		SoundSampler.ShowStatus("Play beats!");
		
		for (Integer time : Notes) {
			BeepTimerListener TL =new BeepTimerListener(); 
			Timer TM = new Timer(time, TL);
			TL.TM = TM;
			TM.start();
		}

		StopPlayTimerListener TL =new StopPlayTimerListener(this); 
		Timer TM = new Timer(Notes.get(Notes.size() - 1), TL);
		TL.TM = TM;
		TM.start();
	}
}

class StopPlayTimerListener implements TimerListener{
	public Timer TM;
	SoundSampler SS;

	public StopPlayTimerListener(SoundSampler SS){
		this.SS = SS;
	}
	
	@Override
	public void timedOut() {
		TM.stop();
		SS.BotMode = SoundSampler.Mode.Waiting;
		SoundSampler.ShowStatus("Wait for beats");
	}
	
}

class PlayTimerListener implements TimerListener{
	public Timer TM;
	ArrayList<Integer> Notes;
	SoundSampler SS;

	public PlayTimerListener(ArrayList<Integer> Notes, SoundSampler SS){
		this.Notes = Notes;
		this.SS = SS;
	}
	
	@Override
	public void timedOut() {
		TM.stop();
		SS.BotMode = SoundSampler.Mode.Playing;
		SS.Play(Notes);
	}
	
}

class BeepTimerListener implements TimerListener{
	public Timer TM;
	public BeepTimerListener(){
	}
	
	@Override
	public void timedOut() {
		TM.stop();
		Sound.beep();

		Motor.A.forward();
		try {
			Thread.sleep(75);
		} catch (InterruptedException e) {
		}

		Motor.A.backward();
		try {
			Thread.sleep(75);
		} catch (InterruptedException e) {
		}
		
		Motor.A.stop();
	}
	
}
