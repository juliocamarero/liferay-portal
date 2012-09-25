package com.liferay.portlet.documentlibrary.util.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

public class GreasseScaleFilter extends AbstractFilterInline {

	@Override
	public BufferedImage process(BufferedImage biSrc, BufferedImage biDst,
			ActionRequest actionRequest, ActionResponse actionResponse) {
		if (biDst == null) {
			biDst = new BufferedImage(biSrc.getWidth(), biSrc.getHeight(),
					biSrc.getType());
		}

		// Recorre las imagenes y obtiene el color de la imagen original y la
		// almacena en el destino
		for (int x = 0; x < biSrc.getWidth(); x++) {
			for (int y = 0; y < biSrc.getHeight(); y++) {
				// Obtiene el color
				Color c1 = new Color(biSrc.getRGB(x, y));
				// Calcula la media de tonalidades
				int med = (c1.getRed() + c1.getGreen() + c1.getBlue()) / 3;
				// Almacena el color en la imagen destino
				biDst.setRGB(x, y, new Color(med, med, med).getRGB());
			}
		}
		return biDst;
	}

}
