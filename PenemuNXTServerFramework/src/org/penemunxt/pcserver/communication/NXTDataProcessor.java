package org.penemunxt.pcserver.communication;

import org.penemunxt.pcserver.communication.INXTCommunicationData;
import org.penemunxt.pcserver.communication.NXTCommunication;
import org.penemunxt.pcserver.communication.NXTCommunicationData;

public class NXTDataProcessor<CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData>
		extends Thread  {
	public boolean Active;
	NXTCommunication<CommDataInT, CommDataOutT> NXTComm;
	INXTDataItemProcessor DataItemProcessor;
	
	
	public boolean isActive() {
		return Active;
	}

	public void setActive(boolean active) {
		Active = active;
	}
	
	public NXTCommunication<CommDataInT, CommDataOutT> getNXTComm() {
		return NXTComm;
	}

	public void setNXTComm(NXTCommunication<CommDataInT, CommDataOutT> comm) {
		NXTComm = comm;
	}
	
	public INXTDataItemProcessor getDataItemProcessor() {
		return DataItemProcessor;
	}

	public void setDataItemProcessor(INXTDataItemProcessor dataItemProcessor) {
		DataItemProcessor = dataItemProcessor;
	}

	public NXTDataProcessor(NXTCommunication<CommDataInT, CommDataOutT> NXTComm, INXTDataItemProcessor dataItemProcessor) {
		this.setNXTComm(NXTComm);
		this.setActive(true);
		this.setDataItemProcessor(dataItemProcessor);
	}

	@Override
	public void run() {
		// Handle retrieved data
		while (this.isActive()) {
			CommDataInT DataItem = (CommDataInT) NXTComm
					.getDataRetrievedQueue().getAndDeleteNextItem();

			if (DataItem != null) {
				if (DataItem.getMainStatus() == NXTCommunicationData.MAIN_STATUS_SHUT_DOWN) {
					this.getDataItemProcessor().<CommDataInT, CommDataOutT>SendIsShuttingDownMessage(NXTComm);
					this.setActive(false);
				} else if (DataItem.getMainStatus() == NXTCommunicationData.MAIN_STATUS_SHUTTING_DOWN) {
					this.setActive(false);
				} else if (DataItem.getDataStatus() == NXTCommunicationData.DATA_STATUS_NORMAL) {
					this.getDataItemProcessor().<CommDataInT, CommDataOutT>ProcessItem(DataItem, NXTComm);
				}
			}

			Thread.yield();
		}
	}
}