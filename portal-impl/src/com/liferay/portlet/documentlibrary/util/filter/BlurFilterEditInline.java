package com.liferay.portlet.documentlibrary.util.filter;

import java.awt.image.BufferedImage;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

public class BlurFilterEditInline extends AbstractFilterInline {

	private int hRadius = 10;
	private int vRadius = 10;
	private int iterations = 1;

	@Override
	public BufferedImage process(BufferedImage biSrc, BufferedImage biDst,
			ActionRequest actionRequest, ActionResponse actionResponse) {
		int width = biSrc.getWidth();
		int height = biSrc.getHeight();

		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		getRGB(biSrc, 0, 0, width, height, inPixels);

		for (int i = 0; i < iterations; i++) {
			blur(inPixels, outPixels, width, height, hRadius);
			blur(outPixels, inPixels, height, width, vRadius);
		}

		setRGB(biDst, 0, 0, width, height, inPixels);
		return biDst;
	}
	
	public BufferedImage filter(BufferedImage src, BufferedImage dst) {

		int width = src.getWidth();
		int height = src.getHeight();

		if (dst == null)
			dst = createCompatibleDestImage(src, null);

		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		getRGB(src, 0, 0, width, height, inPixels);

		for (int i = 0; i < iterations; i++) {
			blur(inPixels, outPixels, width, height, hRadius);
			blur(outPixels, inPixels, height, width, vRadius);
		}

		setRGB(dst, 0, 0, width, height, inPixels);
		return dst;
	}

	private static void blur(int[] in, int[] out, int width, int height,
			int radius) {
		int widthMinus1 = width - 1;
		int tableSize = 2 * radius + 1;
		int divide[] = new int[256 * tableSize];

		for (int i = 0; i < 256 * tableSize; i++)
			divide[i] = i / tableSize;

		int inIndex = 0;

		for (int y = 0; y < height; y++) {
			int outIndex = y;
			int ta = 0, tr = 0, tg = 0, tb = 0;

			for (int i = -radius; i <= radius; i++) {
				int rgb = in[inIndex + ImageMath.clamp(i, 0, width - 1)];
				ta += (rgb >> 24) & 0xff;
				tr += (rgb >> 16) & 0xff;
				tg += (rgb >> 8) & 0xff;
				tb += rgb & 0xff;
			}

			for (int x = 0; x < width; x++) {
				out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16)
						| (divide[tg] << 8) | divide[tb];

				int i1 = x + radius + 1;
				if (i1 > widthMinus1)
					i1 = widthMinus1;
				int i2 = x - radius;
				if (i2 < 0)
					i2 = 0;
				int rgb1 = in[inIndex + i1];
				int rgb2 = in[inIndex + i2];

				ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
				tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
				tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
				tb += (rgb1 & 0xff) - (rgb2 & 0xff);
				outIndex += height;
			}
			inIndex += width;
		}
	}

}
