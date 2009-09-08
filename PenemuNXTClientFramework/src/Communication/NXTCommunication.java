package Communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.LCD;
import lejos.nxt.comm.*;

public class NXTCommunication<CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData> {
	boolean m_isConnected = false;

	boolean WriteBeforeRead = true;
	
	INXTCommunicationDataFactory CommDataInFactory = null;
	INXTCommunicationDataFactory CommDataOutFactory = null;
	
	NXTDataExchanger<CommDataInT, CommDataOutT> NXTDE = null;
	NXTConnection m_Connection = null;

	DataOutputStream DataOut = null;
	DataInputStream DataIn = null;

	NXTCommunicationQueue<CommDataInT> DataRetrievedQueue;
	NXTCommunicationQueue<CommDataOutT> DataSendQueue;

	boolean DataRetrievedQueueIsLocked = false;
	boolean DataSendQueueIsLocked = false;

	public NXTCommunicationQueue<CommDataInT> getDataRetrievedQueue() {
		return DataRetrievedQueue;
	}

	public void setDataRetrievedQueue(NXTCommunicationQueue<CommDataInT> dataRetrievedQueue) {
		DataRetrievedQueue = dataRetrievedQueue;
	}

	public NXTCommunicationQueue<CommDataOutT> getDataSendQueue() {
		return DataSendQueue;
	}

	public void setDataSendQueue(NXTCommunicationQueue<CommDataOutT> dataSendQueue) {
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
			NXTDE = new NXTDataExchanger<CommDataInT, CommDataOutT>(WriteBeforeRead, DataOut, DataIn, DataRetrievedQueue,
					DataSendQueue, CommDataInFactory, CommDataOutFactory);
			NXTDE.start();
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

	public NXTCommunication(boolean WriteBeforeRead, INXTCommunicationDataFactory CommDataInFactory, INXTCommunicationDataFactory CommDataOutFactory) {
		this.WriteBeforeRead = WriteBeforeRead;
		
		this.setDataRetrievedQueue(new NXTCommunicationQueue<CommDataInT>());
		this.setDataSendQueue(new NXTCommunicationQueue<CommDataOutT>());

		this.CommDataInFactory = CommDataInFactory;
		this.CommDataOutFactory = CommDataOutFactory;
	}
}