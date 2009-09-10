package org.penemunxt.nxtclient.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;

class NXTDataExchanger<CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData>
		extends Thread {
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

	public void setDataSendQueue(
			NXTCommunicationQueue<CommDataOutT> dataSendQueue) {
		DataSendQueue = dataSendQueue;
	}

	public NXTDataExchanger(boolean WriteBeforeRead, DataOutputStream DataOut,
			DataInputStream DataIn,
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
		final int WaitMilliseconds[] = { 0, 100, 250, 500, 1000, 1500, 2000,
				2500, 3000, 5000, 7000 };
		int WaitPos = -1;
		boolean ResetWaitPos;

		while (this.Active) {
			ResetWaitPos = false;

			if (this.WriteBeforeRead) {
				if (this.Write()) {
					ResetWaitPos = true;
				}
				if (this.Read()) {
					ResetWaitPos = true;
				}
			} else {
				if (this.Read()) {
					ResetWaitPos = true;
				}
				if (this.Write()) {
					ResetWaitPos = true;
				}
			}

			if (ResetWaitPos) {
				WaitPos = -1;
			}
			if (WaitPos < (WaitMilliseconds.length - 1)) {
				WaitPos += 1;
			}

			if (WaitMilliseconds[WaitPos] == 0) {
				Thread.yield();
			} else {
				try {
					Thread.sleep(WaitMilliseconds[WaitPos]);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	private boolean Write() {
		boolean RealData;
		CommDataOutT DataItemOut = (CommDataOutT) CommDataOutFactory
				.getEmptyInstance();

		// Write
		if (this.getDataSendQueue().getQueueSize() > 0) {
			DataItemOut = this.getDataSendQueue().getAndDeleteNextItem();
			RealData = true;
		} else {
			RealData = false;
		}

		try {
			DataItemOut.WriteData(DataOut);
			this.DataOut.flush();
		} catch (IOException ioe) {
			// System.err.println("Data write error");
		}

		DataItemOut = null;

		return RealData;
	}

	private boolean Read() {
		boolean RealData;
		CommDataInT DataItemIn = (CommDataInT) CommDataInFactory
				.getEmptyInstance();

		// Read
		try {
			DataItemIn.ReadData(DataIn);
		} catch (IOException ioe) {
			// System.err.println("Data read error");
		}

		if (DataItemIn != null
				&& DataItemIn.getDataStatus() != NXTCommunicationData.DATA_STATUS_EMPTY) {
			this.DataRetrievedQueue.add(DataItemIn);
			RealData = true;
		} else {
			RealData = false;
		}

		LCD.drawInt(DataItemIn.getMainStatus(), 1, 5);
		
		DataItemIn = null;

		return RealData;
	}
}
