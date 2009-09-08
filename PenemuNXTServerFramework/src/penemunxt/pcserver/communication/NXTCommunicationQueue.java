package penemunxt.pcserver.communication;
import java.util.ArrayList;

public class NXTCommunicationQueue<CommDataT extends INXTCommunicationData> {
	ArrayList<CommDataT> DataQueue;
	
	public ArrayList<CommDataT> getDataQueue() {
		return DataQueue;
	}

	public void setDataQueue(ArrayList<CommDataT> dataQueue) {
		DataQueue = dataQueue;
	}

	public void add(CommDataT DataItem){
		if(DataItem!=null){
			if(DataItem.isPrioritated()){
				this.addPrioritated(DataItem);
			}else{
				this.addNormal(DataItem);
			}
		}
	}

	public void addNormal(CommDataT DataItem){
		this.DataQueue.add(DataItem);
	}
	
	public void addPrioritated(CommDataT DataItem){
		this.DataQueue.add(0, DataItem);
	}
	
	public CommDataT getNextItem() {
		if (this.DataQueue.size() > 0) {
			return this.DataQueue.get(0);
		} else {
			return null;
		}
	}

	public CommDataT getAndDeleteNextItem() {
		CommDataT DataItem = this.getNextItem();
		if (DataItem != null) {
			this.DataQueue.remove(DataItem);
		}
		return DataItem;
	}

	public int getQueueSize(){
		return this.DataQueue.size();
	}

	public NXTCommunicationQueue() {
		this.setDataQueue(new ArrayList<CommDataT>());
	}
}
