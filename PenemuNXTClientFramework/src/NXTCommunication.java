import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.*;

public class NXTCommunication<CommDataT extends NXTCommunicationData> {
	boolean m_isConnected = false;

	NXTDataExchange<CommDataT> NXTDE = null;
	NXTConnection m_Connection = null;

	DataOutputStream DataOut = null;
	DataInputStream DataIn = null;

	NXTCommunicationQueue<CommDataT> DataRetrievedQueue;
	NXTCommunicationQueue<CommDataT> DataSendQueue;

	boolean DataRetrievedQueueIsLocked = false;
	boolean DataSendQueueIsLocked = false;

	public NXTCommunicationQueue<CommDataT> getDataRetrievedQueue() {
		return DataRetrievedQueue;
	}

	public void setDataRetrievedQueue(NXTCommunicationQueue<CommDataT> dataRetrievedQueue) {
		DataRetrievedQueue = dataRetrievedQueue;
	}

	public NXTCommunicationQueue<CommDataT> getDataSendQueue() {
		return DataSendQueue;
	}

	public void setDataSendQueue(NXTCommunicationQueue<CommDataT> dataSendQueue) {
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
		if (this.isConnected()) {
			NXTDebug.WriteMessageAndWait("NXTDE");
			NXTDE = new NXTDataExchange<CommDataT>(DataOut, DataIn, DataRetrievedQueue,
					DataSendQueue);
			NXTDE.start();
		}
	}

	public void StopExchangeData() {
		NXTDE.End();
		NXTDE = null;
	}

	public void ConnectAndStartAll(NXTConnectionModes ConnMode) {
		this.Connect(ConnMode);
		// NXTDebug.WriteMessageAndWait("Con F");
		this.OpenStreams();
		NXTDebug.WriteMessageAndWait("Streams O");
		this.StartExchangeData();
		NXTDebug.WriteMessageAndWait("Data ex");
	}

	public void StopAll() {
		this.Disconnect();
		this.CloseStreams();
		this.StopExchangeData();
	}

	public NXTCommunication() {
		DataRetrievedQueue = new NXTCommunicationQueue<CommDataT>();
		DataSendQueue = new NXTCommunicationQueue<CommDataT>();
	}
}

class NXTDataExchange<CommDataT extends NXTCommunicationData> extends Thread {
	boolean Active;

	DataOutputStream DataOut = null;
	DataInputStream DataIn = null;

	NXTCommunicationQueue<CommDataT> DataRetrievedQueue;
	NXTCommunicationQueue<CommDataT> DataSendQueue;

	public NXTCommunicationQueue<CommDataT> getDataRetrievedQueue() {
		return DataRetrievedQueue;
	}

	public void setDataRetrievedQueue(NXTCommunicationQueue<CommDataT> dataRetrievedQueue) {
		DataRetrievedQueue = dataRetrievedQueue;
	}

	public NXTCommunicationQueue<CommDataT> getDataSendQueue() {
		return DataSendQueue;
	}

	public void setDataSendQueue(NXTCommunicationQueue<CommDataT> dataSendQueue) {
		DataSendQueue = dataSendQueue;
	}

	public NXTDataExchange(DataOutputStream DataOut, DataInputStream DataIn,
			NXTCommunicationQueue<CommDataT> DataRetrievedQueue,
			NXTCommunicationQueue<CommDataT> DataSendQueue) {
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
			CommDataT DataItem;

			NXTDebug.WriteMessageAndWait("Write section");
			NXTDebug.WriteMessageAndWait("Listsize "
					+ String.valueOf(this.getDataSendQueue().getQueueSize()));

			// Write
			if (this.getDataSendQueue().getQueueSize() > 0) {
				NXTDebug.WriteMessageAndWait("From List");
				DataItem = this.getDataSendQueue().getAndDeleteNextItem();
			} else {
				NXTDebug.WriteMessageAndWait("From Empty");
				DataItem = (CommDataT) new NXTCommunicationData(
						NXTCommunicationData.STATUS_NORMAL,
						NXTCommunicationData.DATA_EMPTY_DATA);
			}
			try {
				NXTDebug.WriteMessageAndWait("Try write");
				DataItem.WriteData(DataOut);
				NXTDebug.WriteMessageAndWait("Flush");
				this.DataOut.flush();
				NXTDebug.WriteMessageAndWait("Flushed");
			} catch (IOException ioe) {
				System.err.println("Data write error");
			}
			DataItem = null;

			// Read
			DataItem = new NXTCommunicationTestData();
			try {
				NXTDebug.WriteMessageAndWait("Try read");
				DataItem.ReadData(DataIn);
			} catch (IOException ioe) {
				System.err.println("Data read error");
			}
			if (DataItem != null
					&& DataItem.DataStatus != NXTCommunicationData.DATA_EMPTY_DATA) {
				this.DataRetrievedQueue.add(DataItem);

				NXTDebug.WriteMessageAndWait("Try writeretreaved");
				
				NXTDebug.WriteMessageAndWait("MainStatus");
				LCD.drawInt(DataItem.MainStatus, 1, 3);
				NXTDebug.WriteMessageAndWait("DataStatus");
				LCD.drawInt(DataItem.DataStatus, 1, 4);
				NXTDebug.WriteMessageAndWait("Param1");
				LCD.drawInt(((NXTCommunicationTestData) DataItem).Param1, 1, 5);
				NXTDebug.WriteMessageAndWait("Param2");
				LCD.drawInt(((NXTCommunicationTestData) DataItem).Param2, 1, 6);
				
				Button.ENTER.waitForPressAndRelease();
			}
			Thread.yield();
		}
	}
}
