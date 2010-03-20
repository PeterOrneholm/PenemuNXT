package org.penemunxt.communication;

public class NXTDataProcessor implements INewItemListener {
	public boolean Active;
	NXTCommunication NXTC;
	INXTCommunicationDataFactories DataFactories;

	public boolean isActive() {
		return Active;
	}

	public void setActive(boolean active) {
		Active = active;
	}

	public NXTCommunication getNXTC() {
		return NXTC;
	}

	public void setNXTC(NXTCommunication comm) {
		NXTC = comm;
	}

	public NXTDataProcessor(NXTCommunication NXTComm,
			INXTCommunicationDataFactories DataFactories) {
		super();
		this.setNXTC(NXTComm);
		this.setActive(true);
		this.DataFactories = DataFactories;
	}

	public void ProcessItem(INXTCommunicationData dataItem,
			NXTCommunication NXTComm) {
		// Override by sub
	}

	@Override
	public void NewItemInList() {
		INXTCommunicationData DataItem = NXTC.getDataRetrievedQueue()
		.getAndDeleteNextItem();

		if (DataItem != null && NXTC != null) {
			if (DataItem.getMainStatus() == NXTCommunicationData.MAIN_STATUS_SHUT_DOWN) {
				this.NXTC.getDataSendQueue().add(
						DataFactories.getDataOutFactory()
								.getEmptyIsShuttingDownInstance());
				this.setActive(false);
			} else if (DataItem.getMainStatus() == NXTCommunicationData.MAIN_STATUS_SHUTTING_DOWN) {
				this.setActive(false);
			} else if (DataItem.getDataStatus() == NXTCommunicationData.DATA_STATUS_NORMAL) {
				this.ProcessItem(DataItem, NXTC);
			}
		}
	}
}