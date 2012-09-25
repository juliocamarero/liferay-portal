package com.liferay.portlet.documentlibrary.util.filter;

import java.awt.image.BufferedImage;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;


public class GaussianFilterInline extends AbstractFilterInline {
	public final static String GAUSSIAN_RADIUS="gaussianRadius";
	
    private float radius = 10;
	private float bloom = 2;
	private float bloomThreshold = 192;
    private float angle = 0;
	private int sides = 5;

	public final static String GAUSSIAN_BLOOM="gaussianBloom";

	public final static String GAUSSIAN_BLOOM_THRESHOLD="gaussianBloomThreshold";

	public final static String GAUSSIAN_ANGLE="gaussianAngle";

	public final static String GAUSSIAN_SIDES="gaussianSides";
	
	private boolean isParseableFloat(String str) {
		try {
			Float.parseFloat(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean isParseableInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void readParameter(ActionRequest actionRequest) {
		String strRadius = actionRequest.getParameter(GaussianFilterInline.GAUSSIAN_RADIUS);
		String strBloom = actionRequest.getParameter(GaussianFilterInline.GAUSSIAN_BLOOM);
		String strBloomThreshold = actionRequest.getParameter(GaussianFilterInline.GAUSSIAN_BLOOM_THRESHOLD);
		String strAngle = actionRequest.getParameter(GaussianFilterInline.GAUSSIAN_ANGLE);
		String strSides = actionRequest.getParameter(GaussianFilterInline.GAUSSIAN_SIDES);
		
		if(isParseableFloat(strRadius)) {
			radius = Float.parseFloat(strRadius);
		}
		if(isParseableFloat(strBloom)) {
			bloom = Float.parseFloat(strBloom);
		}
		if(isParseableFloat(strBloomThreshold)) {
			bloomThreshold = Float.parseFloat(strBloomThreshold);
		}
		if(isParseableFloat(strAngle)) {
			angle = Float.parseFloat(strAngle);
		}
		if(isParseableInteger(strSides)) {
			sides = Integer.parseInt(strSides);
		}
	}

	@Override
	public BufferedImage process(BufferedImage biSrc, BufferedImage biDst,
			ActionRequest actionRequest, ActionResponse actionResponse) {
		readParameter(actionRequest);
        int width = biSrc.getWidth();
        int height = biSrc.getHeight();
        int rows = 1, cols = 1;
        int log2rows = 0, log2cols = 0;
        int iradius = (int)Math.ceil(radius);
        int tileWidth = 128;
        int tileHeight = tileWidth;

        int adjustedWidth = (int)(width + iradius*2);
        int adjustedHeight = (int)(height + iradius*2);

		tileWidth = iradius < 32 ? Math.min(128, width+2*iradius) : Math.min(256, width+2*iradius);
		tileHeight = iradius < 32 ? Math.min(128, height+2*iradius) : Math.min(256, height+2*iradius);

        if ( biDst == null ) {
        	biDst = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        }

        while (rows < tileHeight) {
            rows *= 2;
            log2rows++;
        }
        while (cols < tileWidth) {
            cols *= 2;
            log2cols++;
        }
        int w = cols;
        int h = rows;

		tileWidth = w;
		tileHeight = h;//FIXME-tileWidth, w, and cols are always all the same

        FFT fft = new FFT( Math.max(log2rows, log2cols) );

        int[] rgb = new int[w*h];
        float[][] mask = new float[2][w*h];
        float[][] gb = new float[2][w*h];
        float[][] ar = new float[2][w*h];

        // Create the kernel
		double polyAngle = Math.PI/sides;
		double polyScale = 1.0f / Math.cos(polyAngle);
		double r2 = radius*radius;
		double rangle = Math.toRadians(angle);
		float total = 0;
        int i = 0;
        for ( int y = 0; y < h; y++ ) {
            for ( int x = 0; x < w; x++ ) {
                double dx = x-w/2f;
                double dy = y-h/2f;
				double r = dx*dx+dy*dy;
				double f = r < r2 ? 1 : 0;
				if (f != 0) {
					r = Math.sqrt(r);
					if ( sides != 0 ) {
						double a = Math.atan2(dy, dx)+rangle;
						a = ImageMath.mod(a, polyAngle*2)-polyAngle;
						f = Math.cos(a) * polyScale;
					} else
						f = 1;
					f = f*r < radius ? 1 : 0;
				}
				total += (float)f;

				mask[0][i] = (float)f;
                mask[1][i] = 0;
                i++;
            }
        }
		
        // Normalize the kernel
        i = 0;
        for ( int y = 0; y < h; y++ ) {
            for ( int x = 0; x < w; x++ ) {
                mask[0][i] /= total;
                i++;
            }
        }

        fft.transform2D( mask[0], mask[1], w, h, true );

        for ( int tileY = -iradius; tileY < height; tileY += tileHeight-2*iradius ) {
            for ( int tileX = -iradius; tileX < width; tileX += tileWidth-2*iradius ) {

                // Clip the tile to the image bounds
                int tx = tileX, ty = tileY, tw = tileWidth, th = tileHeight;
                int fx = 0, fy = 0;
                if ( tx < 0 ) {
                    tw += tx;
                    fx -= tx;
                    tx = 0;
                }
                if ( ty < 0 ) {
                    th += ty;
                    fy -= ty;
                    ty = 0;
                }
                if ( tx+tw > width )
                    tw = width-tx;
                if ( ty+th > height )
                    th = height-ty;
                biSrc.getRGB( tx, ty, tw, th, rgb, fy*w+fx, w );

                // Create a float array from the pixels. Any pixels off the edge of the source image get duplicated from the edge.
                i = 0;
                for ( int y = 0; y < h; y++ ) {
                    int imageY = y+tileY;
                    int j;
                    if ( imageY < 0 )
                        j = fy;
                    else if ( imageY > height )
                        j = fy+th-1;
                    else
                        j = y;
                    j *= w;
                    for ( int x = 0; x < w; x++ ) {
                        int imageX = x+tileX;
                        int k;
                        if ( imageX < 0 )
                            k = fx;
                        else if ( imageX > width )
                            k = fx+tw-1;
                        else
                            k = x;
                        k += j;

                        ar[0][i] = ((rgb[k] >> 24) & 0xff);
                        float r = ((rgb[k] >> 16) & 0xff);
                        float g = ((rgb[k] >> 8) & 0xff);
                        float b = (rgb[k] & 0xff);

						// Bloom...
                        if ( r > bloomThreshold )
							r *= bloom;
//							r = bloomThreshold + (r-bloomThreshold) * bloom;
                        if ( g > bloomThreshold )
							g *= bloom;
//							g = bloomThreshold + (g-bloomThreshold) * bloom;
                        if ( b > bloomThreshold )
							b *= bloom;
//							b = bloomThreshold + (b-bloomThreshold) * bloom;

						ar[1][i] = r;
						gb[0][i] = g;
						gb[1][i] = b;

                        i++;
                        k++;
                    }
                }

                // Transform into frequency space
                fft.transform2D( ar[0], ar[1], cols, rows, true );
                fft.transform2D( gb[0], gb[1], cols, rows, true );

                // Multiply the transformed pixels by the transformed kernel
                i = 0;
                for ( int y = 0; y < h; y++ ) {
                    for ( int x = 0; x < w; x++ ) {
                        float re = ar[0][i];
                        float im = ar[1][i];
                        float rem = mask[0][i];
                        float imm = mask[1][i];
                        ar[0][i] = re*rem-im*imm;
                        ar[1][i] = re*imm+im*rem;
                        
                        re = gb[0][i];
                        im = gb[1][i];
                        gb[0][i] = re*rem-im*imm;
                        gb[1][i] = re*imm+im*rem;
                        i++;
                    }
                }

                // Transform back
                fft.transform2D( ar[0], ar[1], cols, rows, false );
                fft.transform2D( gb[0], gb[1], cols, rows, false );

                // Convert back to RGB pixels, with quadrant remapping
                int row_flip = w >> 1;
                int col_flip = h >> 1;
                int index = 0;

                //FIXME-don't bother converting pixels off image edges
                for ( int y = 0; y < w; y++ ) {
                    int ym = y ^ row_flip;
                    int yi = ym*cols;
                    for ( int x = 0; x < w; x++ ) {
                        int xm = yi + (x ^ col_flip);
                        int a = (int)ar[0][xm];
                        int r = (int)ar[1][xm];
                        int g = (int)gb[0][xm];
                        int b = (int)gb[1][xm];

						// Clamp high pixels due to blooming
						if ( r > 255 )
							r = 255;
						if ( g > 255 )
							g = 255;
						if ( b > 255 )
							b = 255;
                        int argb = (a << 24) | (r << 16) | (g << 8) | b;
                        rgb[index++] = argb;
                    }
                }

                // Clip to the output image
                tx = tileX+iradius;
                ty = tileY+iradius;
                tw = tileWidth-2*iradius;
                th = tileHeight-2*iradius;
                if ( tx+tw > width )
                    tw = width-tx;
                if ( ty+th > height )
                    th = height-ty;
                biDst.setRGB( tx, ty, tw, th, rgb, iradius*w+iradius, w );
            }
        }
        return biDst;
 	}

}
