package org.penemunxt.projects.communicationtest.pc;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;

import javax.media.j3d.*;

public class Hello3d {
	public Hello3d() {
		SimpleUniverse universe = new SimpleUniverse();
		BranchGroup group = new BranchGroup();

		Appearance A = new Appearance();
		// Box NXTBox = new Box(10f,20f,30f, A);
		ColorCube NXTBox = new ColorCube(0.3);

		TransformGroup xgroup = new TransformGroup();
		TransformGroup zgroup = new TransformGroup();
		TransformGroup ygroup = new TransformGroup();

		xgroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ygroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		zgroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		xgroup.addChild(NXTBox);
		ygroup.addChild(xgroup);
		zgroup.addChild(ygroup);

		group.addChild(zgroup);

		universe.getViewingPlatform().setNominalViewingTransform();
		universe.addBranchGraph(group);

		Transform3D form = new Transform3D();

		for (int i = 0; i < 7200; i++) {
			float r = (float) (2 * Math.PI * (i / 360f));
			form.rotX(r);
			xgroup.setTransform(form);
			form.rotY(r);
			ygroup.setTransform(form);
			form.rotZ(r);
			zgroup.setTransform(form);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
			}
		}
	}

	public static void main(String[] args) {
		new Hello3d();
	}
}