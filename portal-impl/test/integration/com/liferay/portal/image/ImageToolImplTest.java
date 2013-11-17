/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.image;

import com.liferay.portal.kernel.image.ImageBag;
import com.liferay.portal.kernel.image.ImageTool;
import com.liferay.portal.kernel.image.ImageToolUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;

import java.io.File;
import java.io.RandomAccessFile;

import java.util.Arrays;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Shuyang Zhou
 * @author Sampsa Sohlman
 */
@ExecutionTestListeners(listeners = {EnvironmentExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class ImageToolImplTest {

	public ImageToolImplTest() throws Exception {
		LIFERAY_GIF = getRenderedImage("liferay.gif");
	}

	@Test
	public void testCropBottomRight() throws Exception {

		testCrop(
			LIFERAY_GIF, LIFERAY_GIF.getHeight() / 2,
			LIFERAY_GIF.getWidth() / 2, LIFERAY_GIF.getWidth() / 2,
			LIFERAY_GIF.getHeight() / 2);
	}

	@Test
	public void testCropCenter() throws Exception {
		testCrop(
			LIFERAY_GIF, LIFERAY_GIF.getHeight() -
				LIFERAY_GIF.getHeight() / 2,
			LIFERAY_GIF.getWidth() - LIFERAY_GIF.getWidth() / 2,
			LIFERAY_GIF.getWidth() / 4, LIFERAY_GIF.getHeight() / 4);
	}

	@Test
	public void testCropMoveUpperCornerDownAndRight() throws Exception {
		testCrop(
			LIFERAY_GIF, LIFERAY_GIF.getHeight(), LIFERAY_GIF.getWidth(),
			(LIFERAY_GIF.getWidth() / 4), (LIFERAY_GIF.getHeight() / 4));
	}

	@Test
	public void testCropMoveUpperCornerUpAndLeft() throws Exception {
		testCrop(
			LIFERAY_GIF, LIFERAY_GIF.getHeight(), LIFERAY_GIF.getWidth(),
			-(LIFERAY_GIF.getWidth() / 4), -(LIFERAY_GIF.getHeight() / 4));
	}

	@Test
	public void testCropSame() throws Exception {
		testCrop(
			LIFERAY_GIF, LIFERAY_GIF.getHeight(), LIFERAY_GIF.getWidth(), 0, 0);
	}

	@Test
	public void testCropTopLeft() throws Exception {
		testCrop(
			LIFERAY_GIF, (LIFERAY_GIF.getHeight() -
				(LIFERAY_GIF.getHeight() / 2)),
			(LIFERAY_GIF.getWidth() - (LIFERAY_GIF.getWidth() / 2)), 0, 0);
	}

	@Test
	public void testReadBMP() throws Exception {
		read("liferay.bmp");
	}

	@Test
	public void testReadGIF() throws Exception {
		read("liferay.gif");
	}

	@Test
	public void testReadJPEG() throws Exception {
		read("liferay.jpeg");
	}

	@Test
	public void testReadJPG() throws Exception {
		read("liferay.jpg");
	}

	@Test
	public void testReadPNG() throws Exception {
		read("liferay.png");
	}

	protected File getFile(String fileName) {
		fileName =
			"portal-impl/test/integration/com/liferay/portal/image/" +
				"dependencies/" + fileName;

		return new File(fileName);
	}

	protected RenderedImage getRenderedImage(String fileName) throws Exception {
		File file = getFile(fileName);
		ImageBag imageBag = ImageToolUtil.read(file);

		return imageBag.getRenderedImage();
	}

	protected void read(String fileName) throws Exception {
		File file = getFile(fileName);

		BufferedImage expectedImage = ImageIO.read(file);

		Assert.assertNotNull(expectedImage);

		DataBufferByte expectedDataBufferByte =
			(DataBufferByte)expectedImage.getData().getDataBuffer();

		byte[][] expectedData = expectedDataBufferByte.getBankData();

		String expectedType = FileUtil.getExtension(fileName);

		if (expectedType.equals("jpeg")) {
			expectedType = ImageTool.TYPE_JPEG;
		}

		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");

		byte[] bytes = new byte[(int)randomAccessFile.length()];

		randomAccessFile.readFully(bytes);

		ImageBag imageBag = ImageToolUtil.read(bytes);

		RenderedImage resultImage = imageBag.getRenderedImage();

		Assert.assertNotNull(resultImage);

		DataBufferByte resultDataBufferByte =
			(DataBufferByte)resultImage.getData().getDataBuffer();

		byte[][] resultData = resultDataBufferByte.getBankData();

		String resultType = imageBag.getType();

		Assert.assertTrue(
			StringUtil.equalsIgnoreCase(expectedType, resultType));
		Assert.assertTrue(Arrays.deepEquals(expectedData, resultData));
	}

	protected void testCrop(
				RenderedImage image, int height, int width, int x, int y)
		throws Exception {

		RenderedImage croppedImage = ImageToolUtil.crop(
			image, height, width, x, y);

		int maxHeight = image.getHeight() - (y < 0 ? -y : y);
		int maxWidth = image.getWidth() - (x < 0 ? -x : x);

		Assert.assertEquals(
			croppedImage.getHeight(), Math.min(maxHeight, height));

		Assert.assertEquals(croppedImage.getWidth(), Math.min(maxWidth, width));
	}

	protected RenderedImage LIFERAY_GIF;

}