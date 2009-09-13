package org.penemunxt.communication;
import java.util.ArrayList;

public class NXTCommunicationQueue {
	ArrayList<INXTCommunicationData> DataQueue;
	
	public ArrayList<INXTCommunicationData> getDataQueue() {
		return DataQueue;
	}

	public void setDataQueue(ArrayList<INXTCommunicationData> dataQueue) {
		DataQueue = dataQueue;
	}

	public void add(INXTCommunicationData DataItem){
		if(DataItem!=null){
			if(DataItem.isPrioritated()){
				this.addPrioritated(DataItem);
			}else{
				this.addNormal(DataItem);
			}
		}
	}

	public void addNormal(INXTCommunicationData DataItem){
		this.DataQueue.add(DataItem);
	}
	
	public void addPrioritated(INXTCommunicationData DataItem){
		this.DataQueue.add(0, DataItem);
	}
	
	public INXTCommunicationData getNextItem() {
		if (this.DataQueue.size() > 0) {
			return this.DataQueue.get(0);
		} else {
			return null;
		}
	}

	public INXTCommunicationData getAndDeleteNextItem() {
		INXTCommunicationData DataItem = this.getNextItem();
		if (DataItem != null) {
			this.DataQueue.remove(DataItem);
		}
		return DataItem;
	}

	public int getQueueSize(){
		return this.DataQueue.size();
	}

	public NXTCommunicationQueue() {
		super();
		this.setDataQueue(new ArrayList<INXTCommunicationData>());
	}
}
