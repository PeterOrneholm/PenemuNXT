package org.penemunxt.communication;

public class NXTCommunication {
	boolean isConnected;
	boolean WriteBeforeRead;

	INXTCommunicationDataFactories DataFactories;

	NXTDataExchanger NXTDE;

	INXTDataStreamConnection NXTDSC;

	NXTCommunicationQueue DataRetrievedQueue;
	NXTCommunicationQueue DataSendQueue;

	boolean DataRetrievedQueueIsLocked;
	boolean DataSendQueueIsLocked;

	public NXTCommunicationQueue getDataRetrievedQueue() {
		return DataRetrievedQueue;
	}

	public void setDataRetrievedQueue(NXTCommunicationQueue dataRetrievedQueue) {
		DataRetrievedQueue = dataRetrievedQueue;
	}

	public NXTCommunicationQueue getDataSendQueue() {
		return DataSendQueue;
	}

	public void setDataSendQueue(NXTCommunicationQueue dataSendQueue) {
		DataSendQueue = dataSendQueue;
	}

	private void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public boolean isConnected() {
		if (this.NXTDSC == null) {
			return false;
		} else {
			return isConnected;
		}
	}

	public void Disconnect() {
		try {
			this.NXTDSC.close();
		} catch (Exception ex) {
		}
		this.setConnected(false);
	}

	public void CloseStreams() {
		if (this.isConnected()) {
			try {
				this.NXTDSC.getDataIn().close();
				this.NXTDSC.getDataOut().close();
			} catch (Exception ex) {
			}
		}
	}

	public void Connect(NXTConnectionModes ConnMode, String Name, String Address) {
		if (NXTDSC != null) {
			if (this.isConnected()) {
				this.Disconnect();
			}

			if (NXTDSC.Connect(ConnMode, Name, Address)) {
				this.setConnected(true);
			}
		}
	}

	public void StartExchangeData() {
		if (this.isConnected()) {
			NXTDE = new NXTDataExchanger(WriteBeforeRead, this.NXTDSC
					.getDataOut(), this.NXTDSC.getDataIn(), DataRetrievedQueue,
					DataSendQueue, DataFactories);
			NXTDE.start();
		}
	}

	public void StopExchangeData() {
		NXTDE.End();
		NXTDE = null;
	}

	public void ConnectAndStartAll(NXTConnectionModes ConnMode, String Name,
			String Address) {
		if (NXTDSC != null) {
			this.Connect(ConnMode, Name, Address);
			this.StartExchangeData();
		}
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
			INXTCommunicationDataFactories DataFactories,
			INXTDataStreamConnection NXTDSC) {
		super();

		this.setConnected(false);
		this.WriteBeforeRead = WriteBeforeRead;
		this.setDataRetrievedQueue(new NXTCommunicationQueue());
		this.setDataSendQueue(new NXTCommunicationQueue());
		this.DataFactories = DataFactories;
		this.NXTDSC = NXTDSC;
	}

	public void sendShutDown() {
		this.getDataSendQueue().add(
				new NXTCommunicationData(
						NXTCommunicationData.MAIN_STATUS_SHUT_DOWN,
						NXTCommunicationData.DATA_STATUS_ONLY_STATUS, true));
	}

	public void sendData(INXTCommunicationData data) {
		this.getDataSendQueue().add(data);
	}
}