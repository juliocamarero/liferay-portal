package com.liferay.portlet.documentlibrary.util.filter;

import com.liferay.portlet.documentlibrary.util.EditInlineFilter;

public class EditInlineFilterFactory {
	public static EditInlineFilter getFilter(String filter) {
		EditInlineFilter resul = null;

		if(filter.equals("blur")) {
			resul = new BlurFilterEditInline();
		} else if(filter.equals("gaussian")) {
			resul = new GaussianFilterInline();
		} else if(filter.equals("greassescale")) {
			resul = new GreasseScaleFilter();
		} else if(filter.equals("rotate")) {
			resul = new RotateFilter();
		}
		
		return resul;
	}
}
