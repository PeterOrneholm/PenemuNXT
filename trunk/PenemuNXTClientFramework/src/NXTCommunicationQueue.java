import java.util.ArrayList;

public class NXTCommunicationQueue<CommDataT extends NXTCommunicationData> {
	boolean Locked;
	ArrayList<CommDataT> DataQueue;

	public boolean isLocked() {
		return Locked;
	}

	public void setLocked(boolean locked) {
		Locked = locked;
	}

	public ArrayList<CommDataT> getDataQueue() {
		return DataQueue;
	}

	public void setDataQueue(ArrayList<CommDataT> dataQueue) {
		DataQueue = dataQueue;
	}

	public void add(CommDataT DataItem){
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
	
	public boolean StartUse() {
		if (this.isLocked()) {
			return false;
		} else {
			this.setLocked(true);
			return true;
		}
	}

	public void EndUse() {
		this.setLocked(false);
	}

	public NXTCommunicationQueue() {
		this.setLocked(false);
		this.setDataQueue(new ArrayList<CommDataT>());
	}
}
