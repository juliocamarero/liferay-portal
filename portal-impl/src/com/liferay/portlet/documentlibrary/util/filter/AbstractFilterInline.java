package com.liferay.portlet.documentlibrary.util.filter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.documentlibrary.action.EditInlineImageEntryAction;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.util.EditInlineFilter;
import com.liferay.portlet.documentlibrary.util.EditInlineUtil;

public abstract class AbstractFilterInline implements EditInlineFilter {
	class CropData {
		private int posX;
		private int posY;
		private int width;
		private int height;
		private int withInBrowser;
		private int heightInBrowser;
		
		private void updateByProportions(BufferedImage bi) {
			float withImg = (float)bi.getWidth();
			float heightImg = (float)bi.getHeight();
			float proporcionX = (float)(withImg / withInBrowser);
			float proporcionY = (float)(heightImg / heightInBrowser);
			
			posX *= proporcionX;
			width *= proporcionX;
			posY *= proporcionY;
			height *= proporcionY;
		}
	}

	private final static String getExtension(String fileName) {
		String resul = "";
		
		if(fileName != null) {
			int lastDot = fileName.lastIndexOf(".");
			if(lastDot > -1) {
				resul = fileName.substring(lastDot +1);
			}
		}
		
		return resul;
	}
	
	public abstract BufferedImage process(BufferedImage biSrc, BufferedImage biDst, ActionRequest actionRequest,
			ActionResponse actionResponse);
	
	public String process(ActionRequest actionRequest,
			ActionResponse actionResponse) throws PortletException {
		String resul = null;
		try {
			String editinlineVersionFile = actionRequest.getParameter(EditInlineImageEntryAction.EDITINLINE_VERSION_FILE);
			float flEditinlineVersionFile = Float.parseFloat(editinlineVersionFile);
			String editinlineMaxVersionFile = actionRequest.getParameter(EditInlineImageEntryAction.EDITINLINE_MAX_VERSION_FILE);

			String strBoolCrop = actionRequest.getParameter(EditInlineImageEntryAction.CROP_ENABLED);
			boolean boolCrop = Boolean.parseBoolean(strBoolCrop);
			ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			long fileEntryId = ParamUtil.getLong(actionRequest, "fileEntryId");
			DLFileEntry fileEntry = DLFileEntryLocalServiceUtil
					.getDLFileEntry(fileEntryId);

			EditInlineUtil.deleteTempVersionesSuperiores(fileEntry, editinlineVersionFile, editinlineMaxVersionFile);

			String fileName = fileEntry.getTitle();
			String extension = getExtension(fileName);
			InputStream is = DLAppServiceUtil.getTempFileEntryAsStream(themeDisplay.getUserId(),
					fileName, EditInlineUtil.TEMP_FOLDER_EDITINLINE,
					editinlineVersionFile);

			BufferedImage bi = ImageIO.read(is);
			BufferedImage biSrc = bi;
			CropData cropData = null;
			if(boolCrop) {
				cropData = getCropData(actionRequest);
				cropData.updateByProportions(bi);
				biSrc = bi.getSubimage(cropData.posX, cropData.posY, cropData.width, cropData.height);
			}

			BufferedImage biDst = createCompatibleDestImage(biSrc, null);

			BufferedImage bi2 = process(biSrc, biDst, actionRequest, actionResponse);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if(boolCrop) {
				Graphics2D g = bi.createGraphics();
				g.drawImage(bi2, null, cropData.posX, cropData.posY);
				bi2 = bi;
			}

			float flEditinlineNewVersionFile = flEditinlineVersionFile + 1;
			String strEditinlineNewVersionFile = Float.toString(flEditinlineNewVersionFile);

			ImageIO.write(bi2, extension, baos);
			baos.flush();
			InputStream newIs = new ByteArrayInputStream(baos.toByteArray());
			DLAppServiceUtil.updateTempFile(fileName, EditInlineUtil.TEMP_FOLDER_EDITINLINE,
					strEditinlineNewVersionFile, newIs);
			resul = strEditinlineNewVersionFile;
			baos.close();
	
		} catch (Exception e) {
			throw new PortletException(e);
		}
		return resul;
	}
	
	private CropData getCropData(ActionRequest actionRequest) {
		CropData resul = new CropData();
		resul.posX = (int)ParamUtil.getFloat(actionRequest, EditInlineImageEntryAction.CROP_X1);
		resul.posY = (int)ParamUtil.getFloat(actionRequest, EditInlineImageEntryAction.CROP_Y1);
		resul.height = (int)ParamUtil.getFloat(actionRequest, EditInlineImageEntryAction.CROP_H);
		resul.width = (int)ParamUtil.getFloat(actionRequest, EditInlineImageEntryAction.CROP_W);
		resul.heightInBrowser = (int)ParamUtil.getFloat(actionRequest, EditInlineImageEntryAction.IMAGE_HEIGHT);
		resul.withInBrowser = (int)ParamUtil.getFloat(actionRequest, EditInlineImageEntryAction.IMAGE_WIDTH);
		
		return resul; 
	}
	
	/* METODOS COPIADOS */
    protected BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel dstCM) {
        if ( dstCM == null )
            dstCM = src.getColorModel();
        return new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), dstCM.isAlphaPremultiplied(), null);
    }
	/**
	 * A convenience method for getting ARGB pixels from an image. This tries to avoid the performance
	 * penalty of BufferedImage.getRGB unmanaging the image.
	 */
	public int[] getRGB( BufferedImage image, int x, int y, int width, int height, int[] pixels ) {
		int type = image.getType();
		if ( type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB )
			return (int [])image.getRaster().getDataElements( x, y, width, height, pixels );
		return image.getRGB( x, y, width, height, pixels, 0, width );
    }

	/**
	 * A convenience method for setting ARGB pixels in an image. This tries to avoid the performance
	 * penalty of BufferedImage.setRGB unmanaging the image.
	 */
	public void setRGB( BufferedImage image, int x, int y, int width, int height, int[] pixels ) {
		int type = image.getType();
		if ( type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB )
			image.getRaster().setDataElements( x, y, width, height, pixels );
		else
			image.setRGB( x, y, width, height, pixels, 0, width );
    }


}
