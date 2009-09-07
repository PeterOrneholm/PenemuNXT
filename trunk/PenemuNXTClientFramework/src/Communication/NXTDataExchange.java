package Communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Debug.NXTDebug;


class NXTDataExchange<CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData> extends Thread {
	boolean Active;

	boolean WriteBeforeRead = true;
	
	INXTCommunicationDataFactory CommDataInFactory = null;
	INXTCommunicationDataFactory CommDataOutFactory = null;

	DataOutputStream DataOut = null;
	DataInputStream DataIn = null;

	NXTCommunicationQueue<CommDataInT> DataRetrievedQueue;
	NXTCommunicationQueue<CommDataOutT> DataSendQueue;

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

	public void setDataSendQueue(NXTCommunicationQueue<CommDataOutT> dataSendQueue) {
		DataSendQueue = dataSendQueue;
	}

	public NXTDataExchange(boolean WriteBeforeRead, DataOutputStream DataOut, DataInputStream DataIn,
			NXTCommunicationQueue<CommDataInT> DataRetrievedQueue,
			NXTCommunicationQueue<CommDataOutT> DataSendQueue,
			INXTCommunicationDataFactory CommDataInFactory,
			INXTCommunicationDataFactory CommDataOutFactory) {
		this.WriteBeforeRead = WriteBeforeRead;
		this.DataOut = DataOut;
		this.DataIn = DataIn;
		this.DataRetrievedQueue = DataRetrievedQueue;
		this.DataSendQueue = DataSendQueue;
		this.Active = true;
		this.CommDataInFactory = CommDataInFactory;
		this.CommDataOutFactory = CommDataOutFactory;
	}

	public void End() {
		this.Active = false;
	}

	public void run() {
		while (this.Active) {
			if(this.WriteBeforeRead){
				this.Write();
				this.Read();
			} else {
				this.Read();
				this.Write();
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private void Write(){
		CommDataOutT DataItemOut = (CommDataOutT) CommDataOutFactory.getEmptyInstance();

		// Write
		if (this.getDataSendQueue().getQueueSize() > 0) {
			DataItemOut = this.getDataSendQueue().getAndDeleteNextItem();
		}
		
		try {
			DataItemOut.WriteData(DataOut);
			this.DataOut.flush();
		} catch (IOException ioe) {
			//System.err.println("Data write error");
		}
		
		DataItemOut = null;
	}
	
	private void Read(){
		CommDataInT DataItemIn = (CommDataInT) CommDataInFactory.getEmptyInstance();
		
		// Read
		try {
			DataItemIn.ReadData(DataIn);
		} catch (IOException ioe) {
			//System.err.println("Data read error");
		}
		
		if (DataItemIn != null
				&& DataItemIn.getDataStatus() != NXTCommunicationData.DATA_STATUS_EMPTY) {
			this.DataRetrievedQueue.add(DataItemIn);
		}
		
		DataItemIn = null;
	}
}
