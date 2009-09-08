package penemunxt.pcserver.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;

public class NXTCommunication<CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData> {
	boolean isConnected = false;

	boolean WriteBeforeRead;

	INXTCommunicationDataFactory CommDataInFactory = null;
	INXTCommunicationDataFactory CommDataOutFactory = null;

	NXTDataExchanger<CommDataInT, CommDataOutT> NXTDE = null;
	NXTConnector dataConnection = null;

	DataOutputStream DataOut = null;
	DataInputStream DataIn = null;

	NXTCommunicationQueue<CommDataInT> DataRetrievedQueue;
	NXTCommunicationQueue<CommDataOutT> DataSendQueue;

	boolean DataRetrievedQueueIsLocked = false;
	boolean DataSendQueueIsLocked = false;

	public NXTCommunicationQueue<CommDataInT> getDataRetrievedQueue() {
		return DataRetrievedQueue;
	}

	public void setDataRetrievedQueue(
			NXTCommunicationQueue<CommDataInT> dataRetrievedQueue) {
		DataRetrievedQueue = dataRetrievedQueue;
	}

	public NXTCommunicationQueue<CommDataOutT> getDataSendQueue() {
		return DataSendQueue;
	}

	public void setDataSendQueue(
			NXTCommunicationQueue<CommDataOutT> dataSendQueue) {
		DataSendQueue = dataSendQueue;
	}

	private void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public boolean isConnected() {
		if (this.getConnection() == null) {
			return false;
		} else {
			return isConnected;
		}
	}

	private void setConnection(NXTConnector Connection) {
		dataConnection = Connection;
	}

	public NXTConnector getConnection() {
		return dataConnection;
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
			this.DataIn = this.getConnection().getDataIn();
			this.DataOut = this.getConnection().getDataOut();
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

	public void Connect(NXTConnectionModes ConnMode, String Name, String Address) {
		if (this.isConnected() || this.getConnection() != null) {
			this.Disconnect();
		}

		NXTConnector NewConn = new NXTConnector();
		boolean ConnectSuccess = false;
		int ConnectionMode = 0;

		if (ConnMode == NXTConnectionModes.USB) {
			ConnectionMode = NXTCommFactory.USB;
		} else if (ConnMode == NXTConnectionModes.Bluetooth) {
			ConnectionMode = NXTCommFactory.BLUETOOTH;
		}

		ConnectSuccess = NewConn.connectTo(Name, Address, ConnectionMode);

		if (NewConn != null && ConnectSuccess) {
			this.setConnected(true);
			this.setConnection(NewConn);
		}
	}

	public void StartExchangeData() {
		if (this.isConnected()) {
			NXTDE = new NXTDataExchanger<CommDataInT, CommDataOutT>(
					WriteBeforeRead, DataOut, DataIn, DataRetrievedQueue,
					DataSendQueue, CommDataInFactory, CommDataOutFactory);
			NXTDE.start();
		}
	}

	public void StopExchangeData() {
		NXTDE.End();
		NXTDE = null;
	}

	public void ConnectAndStartAll(NXTConnectionModes ConnMode, String Name,
			String Address) {
		this.Connect(ConnMode, Name, Address);
		this.OpenStreams();
		this.StartExchangeData();
	}

	
	public void ConnectAndStartAll(NXTConnectionModes ConnMode, String Name) {
		this.ConnectAndStartAll(ConnMode, Name, null);
	}
	
	public void ConnectAndStartAll(NXTConnectionModes ConnMode) {
		this.ConnectAndStartAll(ConnMode, null, null);
	}

	public void StopAll() {
		this.Disconnect();
		this.CloseStreams();
		this.StopExchangeData();
	}

	public NXTCommunication(boolean WriteBeforeRead,
			INXTCommunicationDataFactory CommDataInFactory,
			INXTCommunicationDataFactory CommDataOutFactory) {
		this.WriteBeforeRead = WriteBeforeRead;

		this.setDataRetrievedQueue(new NXTCommunicationQueue<CommDataInT>());
		this.setDataSendQueue(new NXTCommunicationQueue<CommDataOutT>());

		this.CommDataInFactory = CommDataInFactory;
		this.CommDataOutFactory = CommDataOutFactory;
	}
}