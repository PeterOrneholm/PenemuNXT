package org.penemunxt.communication;

import java.util.ArrayList;

public class NXTCommunicationQueue {
	ArrayList<INXTCommunicationData> DataQueue;
	ArrayList<INewItemListener> NewItemListeners = new ArrayList<INewItemListener>();

	public void addNewItemListeners(INewItemListener listener) {
		NewItemListeners.add(listener);
	}

	public void removeNewItemListeners(INewItemListener listener) {
		NewItemListeners.remove(listener);
	}

	private void callNewItemListeners() {
		for (INewItemListener listener : NewItemListeners) {
			listener.NewItemInList();
		}
	}

	public ArrayList<INXTCommunicationData> getDataQueue() {
		return DataQueue;
	}

	public void setDataQueue(ArrayList<INXTCommunicationData> dataQueue) {
		DataQueue = dataQueue;
	}

	public void add(INXTCommunicationData DataItem) {
		if (DataItem != null) {
			if (DataItem.isPrioritated()) {
				this.addPrioritated(DataItem);
			} else {
				this.addNormal(DataItem);
			}
		}
	}

	public void addNormal(INXTCommunicationData DataItem) {
		synchronized (this.DataQueue) {
			this.DataQueue.add(DataItem);
			callNewItemListeners();
		}
	}

	public void addPrioritated(INXTCommunicationData DataItem) {
		synchronized (this.DataQueue) {
			this.DataQueue.add(0, DataItem);
			callNewItemListeners();
		}
	}

	public INXTCommunicationData getNextItem() {
		if (this.DataQueue.size() > 0) {
			return this.DataQueue.get(0);
		} else {
			return null;
		}
	}

	public INXTCommunicationData getAndDeleteNextItem() {
		synchronized (this.DataQueue) {
			INXTCommunicationData DataItem = this.getNextItem();
			if (DataItem != null) {
				this.DataQueue.remove(DataItem);
			}
			return DataItem;
		}
	}

	public int getQueueSize() {
		synchronized (this.DataQueue) {
			return this.DataQueue.size();
		}
	}

	public NXTCommunicationQueue() {
		super();
		this.setDataQueue(new ArrayList<INXTCommunicationData>());
	}
}
