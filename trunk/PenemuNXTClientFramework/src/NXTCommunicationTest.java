import lejos.nxt.Button;
import lejos.nxt.LCD;

public class NXTCommunicationTest implements Runnable {
	public static void main(String[] args) {
		NXTCommunicationTest NXTCT = new NXTCommunicationTest();
		NXTCT.run();
	}

	@Override
	public void run() {
		NXTCommunication NXTComm = new NXTCommunication();
		NXTComm.ConnectAndStartAll(NXTConnectionModes.Bluetooth);
		
		NXTComm.DataSendQue.add(new NXTCommunicationData(
				NXTCommunicationData.STATUS_NORMAL,
				NXTCommunicationData.DATA_NORMAL_DATA, 1, 5));
		NXTComm.DataSendQue.add(new NXTCommunicationData(
				NXTCommunicationData.STATUS_NORMAL,
				NXTCommunicationData.DATA_NORMAL_DATA, 3, 512));
		NXTComm.DataSendQue.add(new NXTCommunicationData(
				NXTCommunicationData.STATUS_NORMAL,
				NXTCommunicationData.DATA_NORMAL_DATA, 78, 489));
		
		while(!Button.ESCAPE.isPressed()){
			LCD.clear();
			
			LCD.drawInt(NXTComm.DataRetrievedQue.size(), 1, 1);
			if(NXTComm.DataRetrievedQue.size() > 0){
				LCD.drawInt(NXTComm.DataRetrievedQue.get(0).OverallStatus, 1, 3);
				LCD.drawInt(NXTComm.DataRetrievedQue.get(0).DataStatus, 1, 4);
				LCD.drawInt(NXTComm.DataRetrievedQue.get(0).Param1, 1, 5);
				LCD.drawInt(NXTComm.DataRetrievedQue.get(0).Param2, 1, 6);
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}
	}
}
