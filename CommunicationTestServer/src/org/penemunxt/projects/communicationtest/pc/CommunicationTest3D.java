package org.penemunxt.projects.communicationtest.pc;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.media.j3d.*;

import org.penemunxt.communication.*;
import org.penemunxt.communication.pc.*;
import org.penemunxt.graphics.pc.Graph;
import org.penemunxt.projects.communicationtest.*;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class CommunicationTest3D implements Runnable {
	boolean Active;
	DataShare DS;
	NXTCommunication NXTC;
	TransformGroup xgroup;
	TransformGroup zgroup;
	TransformGroup ygroup;

	public static void main(String[] args) {
		CommunicationTest3D PCCT = new CommunicationTest3D();
		PCCT.start();
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	private void setup3D() {
		SimpleUniverse universe = new SimpleUniverse();
		BranchGroup group = new BranchGroup();

		Appearance A = new Appearance();
		Box NXTBox = new Box(0.3f,0.2f,0.5f, A);
		// ColorCube NXTBox = new ColorCube(0.3);

		xgroup = new TransformGroup();
		ygroup = new TransformGroup();
		zgroup = new TransformGroup();

		xgroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ygroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		zgroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		xgroup.addChild(NXTBox);
		ygroup.addChild(xgroup);
		zgroup.addChild(ygroup);

		group.addChild(zgroup);

		universe.getViewingPlatform().setNominalViewingTransform();
		universe.addBranchGraph(group);
	}

	@Override
	public void run() {
		Active = true;

		// Object to share data internal
		DS = new DataShare();

		// Setup data factories
		NXTCommunicationDataFactories DataFactories = new NXTCommunicationDataFactories(
				new SensorDataFactory(), new ServerMessageDataFactory());

		// Setup and start the communication
		NXTC = new NXTCommunication(false, DataFactories,
				new PCDataStreamConnection());
		NXTC.ConnectAndStartAll(NXTConnectionModes.USB, "NXT",
				"");

		// Setup a data processor
		SensorDataProcessor SDP = new SensorDataProcessor(DS, NXTC,
				DataFactories);
		SDP.start();

		this.setup3D();
		Transform3D form = new Transform3D();

		while (Active) {
			this.Active = SDP.Active;

			AccelerationValues AV = DS.Acceleration
					.get(DS.Acceleration.size() - 1);

			float tx = (float) (2 * Math.PI * (AV.getX() / 360f));
			float ty = (float) (2 * Math.PI * (AV.getY() / 360f));
			float tz = (float) (2 * Math.PI * (AV.getZ() / 360f));

			form.rotX(tx);
			xgroup.setTransform(form);
			form.rotY(ty);
			ygroup.setTransform(form);
			form.rotZ(tz);
			zgroup.setTransform(form);
			
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}

		NXTC.Disconnect();
		System.exit(0);
	}
}
