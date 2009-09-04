public class NXTCommunicationData {
	final static int STATUS_NORMAL = 100;
	final static int STATUS_ERROR = 200;
	
	final static int DATA_EMPTY_DATA = 0;
	final static int DATA_NORMAL_DATA = 1;
	
	int OverallStatus;
	int DataStatus;
	int Param1;
	int Param2;
	public Integer getOverallStatus() {
		return OverallStatus;
	}
	public void setOverallStatus(int overallStatus) {
		OverallStatus = overallStatus;
	}
	public Integer getParam1() {
		return Param1;
	}
	public void setParam1(int param1) {
		Param1 = param1;
	}
	public Integer getParam2() {
		return Param2;
	}
	public void setParam2(int param2) {
		Param2 = param2;
	}
	
	public NXTCommunicationData(int OverallStatus, int DataStatus, int Param1, int Param2) {
		super();
		this.OverallStatus = OverallStatus;
		this.DataStatus = DataStatus;
		this.Param1 = Param1;
		this.Param2 = Param2;
	}

	public NXTCommunicationData(int OverallStatus, int DataStatus) {
		this(OverallStatus, DataStatus, 0, 0);
	}

	public NXTCommunicationData() {
		this(STATUS_NORMAL, DATA_EMPTY_DATA);
	}
}
