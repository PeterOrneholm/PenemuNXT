package org.penemunxt.nxtclient.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.penemunxt.nxtclient.debug.NXTDebug;

import lejos.nxt.comm.*;

public class NXTCommunication<CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData> {
	boolean isConnected = false;

	boolean WriteBeforeRead = true;
	
	INXTCommunicationDataFactory CommDataInFactory = null;
	INXTCommunicationDataFactory CommDataOutFactory = null;
	
	NXTDataExchanger<CommDataInT, CommDataOutT> NXTDE = null;
	NXTConnection dataConnection = null;

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
		this.isConnected = isConnected;
	}

	public boolean isConnected() {
		if (this.getConnection() == null) {
			return false;
		} else {
			return isConnected;
		}
	}

	private void setConnection(NXTConnection Connection) {
		dataConnection = Connection;
	}

	public NXTConnection getConnection() {
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
			NewConn = USB.waitForConnection();
		} else if (ConnMode == NXTConnectionModes.Bluetooth) {
			NewConn = Bluetooth.waitForConnection();
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