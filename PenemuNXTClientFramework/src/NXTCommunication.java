import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lejos.nxt.LCD;
import lejos.nxt.comm.*;

public class NXTCommunication {
	boolean m_isConnected = false;

	NXTDataExchange NXTDE = null;
	NXTConnection m_Connection = null;

	DataOutputStream DataOut = null;
	DataInputStream DataIn = null;

	ArrayList<NXTCommunicationData> DataRetrievedQueue = new ArrayList<NXTCommunicationData>();
	ArrayList<NXTCommunicationData> DataSendQueue = new ArrayList<NXTCommunicationData>();

	public ArrayList<NXTCommunicationData> getDataRetrievedQueue() {
		return DataRetrievedQueue;
	}

	public void setDataRetrievedQueue(
			ArrayList<NXTCommunicationData> dataRetrievedQueue) {
		DataRetrievedQueue = dataRetrievedQueue;
	}

	public ArrayList<NXTCommunicationData> getDataSendQueue() {
		return DataSendQueue;
	}

	public void setDataSendQueue(ArrayList<NXTCommunicationData> dataSendQueue) {
		DataSendQueue = dataSendQueue;
	}

	private void setConnected(boolean isConnected) {
		m_isConnected = isConnected;
	}

	public boolean isConnected() {
		if (this.getConnection() == null) {
			return false;
		} else {
			return m_isConnected;
		}
	}

	private void setConnection(NXTConnection Connection) {
		m_Connection = Connection;
	}

	public NXTConnection getConnection() {
		return m_Connection;
	}

	public void Disconnect() {
		try {
			this.getConnection().close();
		} catch (Exception ex) {
		}
		this.setConnection(null);
		this.setConnected(false);
	}

	public void OpenStreams() {
		if (this.isConnected()) {
			this.DataIn = this.getConnection().openDataInputStream();
			this.DataOut = this.getConnection().openDataOutputStream();
		}
	}

	public void CloseStreams() {
		if (this.isConnected()) {
			try {
				this.DataIn.close();
				this.DataOut.close();
			} catch (Exception ex) {
			}
		}
	}

	public void Connect(NXTConnectionModes ConnMode) {
		if (this.isConnected() || this.getConnection() != null) {
			this.Disconnect();
		}

		NXTConnection NewConn = null;
		if (ConnMode == NXTConnectionModes.USB) {
			LCD.drawString("USB", 1, 1);
			NewConn = USB.waitForConnection();
		} else if (ConnMode == NXTConnectionModes.Bluetooth) {
			LCD.drawString("Bluetooth", 1, 1);
			NewConn = Bluetooth.waitForConnection();
			LCD.drawString("F", 1, 1);
		}

		if (NewConn != null) {
			this.setConnected(true);
			this.setConnection(NewConn);
		}
	}

	public void StartExchangeData() {
		if(this.isConnected()){
			NXTDE = new NXTDataExchange(DataOut, DataIn,
					DataRetrievedQueue, DataSendQueue);
		}
	}

	public void StopExchangeData() {
		 NXTDE.End();
		 NXTDE = null;
	}
	
	public void ConnectAndStartAll(NXTConnectionModes ConnMode) {
		this.Connect(ConnMode);
		this.OpenStreams();
		this.StartExchangeData();
	}
	
	public void StopAll() {
		this.Disconnect();
		this.CloseStreams();
		this.StopExchangeData();
	}
	
	public NXTCommunication() {
		DataRetrievedQueue = new ArrayList<NXTCommunicationData>();
		DataSendQueue = new ArrayList<NXTCommunicationData>();
	}
}

class NXTDataExchange extends Thread {
	boolean Active;

	DataOutputStream DataOut = null;
	DataInputStream DataIn = null;

	ArrayList<NXTCommunicationData> DataRetrievedQueue;
	ArrayList<NXTCommunicationData> DataSendQueue;

	public ArrayList<NXTCommunicationData> getDataRetrievedQueue() {
		return DataRetrievedQueue;
	}

	public void setDataRetrievedQueue(
			ArrayList<NXTCommunicationData> dataRetrievedQueue) {
		DataRetrievedQueue = dataRetrievedQueue;
	}

	public ArrayList<NXTCommunicationData> getDataSendQueue() {
		return DataSendQueue;
	}

	public void setDataSendQueue(ArrayList<NXTCommunicationData> dataSendQueue) {
		DataSendQueue = dataSendQueue;
	}

	public NXTDataExchange(DataOutputStream DataOut, DataInputStream DataIn,
			ArrayList<NXTCommunicationData> DataRetrievedQueue,
			ArrayList<NXTCommunicationData> DataSendQueue) {
		this.DataOut = DataOut;
		this.DataIn = DataIn;
		this.DataRetrievedQueue = DataRetrievedQueue;
		this.DataSendQueue = DataSendQueue;
		this.Active = true;
	}

	public void End() {
		this.Active = false;
	}

	public void run() {
		while (this.Active) {
			NXTCommunicationData DataItem;

			// Write
			if (this.getDataSendQueue().size() > 0) {
				DataItem = this.getDataSendQueue().get(0);
			} else {
				DataItem = new NXTCommunicationData(
						NXTCommunicationData.STATUS_NORMAL,
						NXTCommunicationData.DATA_EMPTY_DATA);
			}
			try {
				DataItem.WriteData(DataOut);
				this.DataOut.flush();
			} catch (IOException ioe) {
				System.err.println("Data write error");
			}
			if (this.getDataSendQueue().size() > 0) {
				this.getDataSendQueue().remove(DataItem);
			}
			DataItem = null;

			// Read
			DataItem = new NXTCommunicationData();
			try {
				DataItem.ReadData(DataIn);
			} catch (IOException ioe) {
				System.err.println("Data read error");
			}
			if (DataItem != null
					&& DataItem.DataStatus != NXTCommunicationData.DATA_EMPTY_DATA) {
				this.DataRetrievedQueue.add(DataItem);
			}
			
			Thread.yield();
		}
	}
}
