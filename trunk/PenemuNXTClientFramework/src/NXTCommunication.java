import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lejos.nxt.comm.*;

public class NXTCommunication {
	boolean m_isConnected = false;

	NXTDataExchange NXTDE = null;
	NXTConnection m_Connection = null;

	DataOutputStream DataOut = null;
	DataInputStream DataIn = null;

	ArrayList<NXTCommunicationData> DataRetrievedQue = new ArrayList<NXTCommunicationData>();
	ArrayList<NXTCommunicationData> DataSendQue = new ArrayList<NXTCommunicationData>();

	public ArrayList<NXTCommunicationData> getDataRetrievedQue() {
		return DataRetrievedQue;
	}

	public void setDataRetrievedQue(
			ArrayList<NXTCommunicationData> dataRetrievedQue) {
		DataRetrievedQue = dataRetrievedQue;
	}

	public ArrayList<NXTCommunicationData> getDataSendQue() {
		return DataSendQue;
	}

	public void setDataSendQue(ArrayList<NXTCommunicationData> dataSendQue) {
		DataSendQue = dataSendQue;
	}

	private void setConnected(boolean isConnected) {
		m_isConnected = isConnected;
	}

	public boolean isConnected() {
		if (this.getConnection() == null) {
			return false;
		} else {
			return m_isConnected;
		}
	}

	private void setConnection(NXTConnection Connection) {
		m_Connection = Connection;
	}

	public NXTConnection getConnection() {
		return m_Connection;
	}

	public void Disconnect() {
		try {
			this.getConnection().close();
		} catch (Exception ex) {
		}
		this.setConnection(null);
		this.setConnected(false);
	}

	public void OpenStreams() {
		if (this.isConnected()) {
			this.DataIn = this.getConnection().openDataInputStream();
			this.DataOut = this.getConnection().openDataOutputStream();
		}
	}

	public void CloseStreams() {
		if (this.isConnected()) {
			try {
				this.DataIn.close();
				this.DataOut.close();
			} catch (Exception ex) {
			}
		}
	}

	public void Connect(NXTConnectionModes ConnMode) {
		if (this.isConnected() || this.getConnection() != null) {
			this.Disconnect();
		}

		NXTConnection NewConn = null;
		if (ConnMode == NXTConnectionModes.USB) {
			NewConn = USB.waitForConnection();
		} else if (ConnMode == NXTConnectionModes.Bluetooth) {
			NewConn = Bluetooth.waitForConnection();
		}

		if (NewConn != null) {
			this.setConnected(true);
			this.setConnection(NewConn);
		}
	}

	public void StartExchangeData() {
		if(this.isConnected()){
			NXTDE = new NXTDataExchange(DataOut, DataIn,
					DataRetrievedQue, DataSendQue);
		}
	}

	public void StopExchangeData() {
		 NXTDE.End();
		 NXTDE = null;
	}
	
	public void ConnectAndStartAll(NXTConnectionModes ConnMode) {
		this.Connect(ConnMode);
		this.OpenStreams();
		this.StartExchangeData();
	}
	
	public void StopAll() {
		this.Disconnect();
		this.CloseStreams();
		this.StopExchangeData();
	}
	
	public NXTCommunication() {
		DataRetrievedQue = new ArrayList<NXTCommunicationData>();
		DataSendQue = new ArrayList<NXTCommunicationData>();
	}
}

class NXTDataExchange extends Thread {
	boolean Active;

	DataOutputStream DataOut = null;
	DataInputStream DataIn = null;

	ArrayList<NXTCommunicationData> DataRetrievedQue;
	ArrayList<NXTCommunicationData> DataSendQue;

	public ArrayList<NXTCommunicationData> getDataRetrievedQue() {
		return DataRetrievedQue;
	}

	public void setDataRetrievedQue(
			ArrayList<NXTCommunicationData> dataRetrievedQue) {
		DataRetrievedQue = dataRetrievedQue;
	}

	public ArrayList<NXTCommunicationData> getDataSendQue() {
		return DataSendQue;
	}

	public void setDataSendQue(ArrayList<NXTCommunicationData> dataSendQue) {
		DataSendQue = dataSendQue;
	}

	public NXTDataExchange(DataOutputStream DataOut, DataInputStream DataIn,
			ArrayList<NXTCommunicationData> DataRetrievedQue,
			ArrayList<NXTCommunicationData> DataSendQue) {
		this.DataOut = DataOut;
		this.DataIn = DataIn;
		this.DataRetrievedQue = DataRetrievedQue;
		this.DataSendQue = DataSendQue;
		this.Active = true;
	}

	public void End() {
		this.Active = false;
	}

	public void run() {
		while (this.Active) {
			NXTCommunicationData DataItem;

			// Write
			if (this.getDataSendQue().size() > 0) {
				DataItem = this.getDataSendQue().get(0);
			} else {
				DataItem = new NXTCommunicationData(
						NXTCommunicationData.STATUS_NORMAL,
						NXTCommunicationData.DATA_EMPTY_DATA);
			}
			try {

				this.DataOut.flush();
			} catch (IOException ioe) {
				System.err.println("Data write error");
			}
			if (this.getDataSendQue().size() > 0) {
				this.getDataSendQue().remove(DataItem);
			}
			DataItem = null;

			// Read
			DataItem = new NXTCommunicationData();
			try {
				DataItem.OverallStatus = this.DataIn.readInt();
				DataItem.DataStatus = this.DataIn.readInt();
				DataItem.Param1 = this.DataIn.readInt();
				DataItem.Param2 = this.DataIn.readInt();
			} catch (IOException ioe) {
				System.err.println("Data write error");
			}
			if (DataItem != null
					&& DataItem.DataStatus != NXTCommunicationData.DATA_EMPTY_DATA) {
				this.DataRetrievedQue.add(DataItem);
			}
			
			Thread.yield();
		}
	}
}
