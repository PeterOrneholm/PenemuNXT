package Communication;
public class NXTCommunicationDataFactory implements INXTCommunicationDataFactory {
	@Override
	public NXTCommunicationData getEmptyInstance() {
		return new NXTCommunicationData();
	}
}