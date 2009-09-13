package org.penemunxt.communication;

public class NXTCommunicationDataFactories implements INXTCommunicationDataFactories {
	INXTCommunicationDataFactory DataInFactory = null;
	INXTCommunicationDataFactory DataOutFactory = null;

	public INXTCommunicationDataFactory getDataInFactory() {
		return DataInFactory;
	}

	public void setDataInFactory(INXTCommunicationDataFactory dataInFactory) {
		DataInFactory = dataInFactory;
	}

	public INXTCommunicationDataFactory getDataOutFactory() {
		return DataOutFactory;
	}

	public void setDataOutFactory(INXTCommunicationDataFactory dataOutFactory) {
		DataOutFactory = dataOutFactory;
	}

	public NXTCommunicationDataFactories(
			INXTCommunicationDataFactory dataInFactory,
			INXTCommunicationDataFactory dataOutFactory) {
		super();
		DataInFactory = dataInFactory;
		DataOutFactory = dataOutFactory;
	}

	public NXTCommunicationDataFactories() {
		this(new NXTCommunicationDataFactory(),
				new NXTCommunicationDataFactory());
	}
}
