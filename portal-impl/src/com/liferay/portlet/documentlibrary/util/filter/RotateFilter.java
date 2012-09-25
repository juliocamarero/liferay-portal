package com.liferay.portlet.documentlibrary.util.filter;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.liferay.portlet.documentlibrary.action.EditInlineImageEntryAction;

public class RotateFilter extends AbstractFilterInline {
	public final static String SENTIDO_ROTATE = "sentido_rotate";

	@Override
	public BufferedImage process(BufferedImage biSrc, BufferedImage biDst,
			ActionRequest actionRequest, ActionResponse actionResponse) {
		
		Graphics2D g = biSrc.createGraphics();
		AffineTransform at = g.getTransform();
		double rot = Math.PI/2.0;
		int translateX = biSrc.getHeight();
		int translateY = 0;
		String sentido = actionRequest.getParameter(SENTIDO_ROTATE);
		if(sentido != null && sentido.equals("left")) {
			rot = 1.5 * Math.PI;
			translateY = biSrc.getWidth();
			translateX = 0;
		}
		at.translate(translateX, translateY);

		at.rotate(rot);
		AffineTransformOp op = new AffineTransformOp(at, null);
		return op.filter(biSrc, null);
	}

}
