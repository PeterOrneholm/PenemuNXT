package org.penemunxt.nxtclient.communication;

public interface INXTDataItemProcessor {
	<CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData> void SendIsShuttingDownMessage(NXTCommunication<CommDataInT, CommDataOutT> NXTComm);
	<CommDataInT extends INXTCommunicationData, CommDataOutT extends INXTCommunicationData> void ProcessItem(CommDataInT dataItem, NXTCommunication<CommDataInT, CommDataOutT> NXTComm);
}
