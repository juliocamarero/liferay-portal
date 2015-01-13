/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.assetcategoryadmin;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

/**
 * @author Eudaldo Alonso
 */
public class AssetCategoryAdminPortlet extends MVCPortlet {

	public void deleteVocabulary(ActionRequest actionRequest)
		throws PortalException {

		long[] deleteVocabularyIds = null;

		long vocabularyId = ParamUtil.getLong(actionRequest, "vocabularyId");

		if (vocabularyId > 0) {
			deleteVocabularyIds = new long[] {vocabularyId};
		}
		else {
			deleteVocabularyIds = StringUtil.split(
				ParamUtil.getString(actionRequest, "deleteVocabularyIds"), 0L);
		}

		for (long deleteVocabularyId : deleteVocabularyIds) {
			AssetVocabularyServiceUtil.deleteVocabulary(deleteVocabularyId);
		}
	}

	@Override
	public void processAction(
			ActionMapping actionMapping, ActionForm actionForm,
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				updateVocabulary(actionRequest);
			}
			else if (cmd.equals(Constants.DELETE)) {
				deleteVocabulary(actionRequest);
			}

			sendRedirect(actionRequest, actionResponse);
		}
		catch (Exception e) {
			if (e instanceof NoSuchVocabularyException ||
				e instanceof PrincipalException) {

				SessionErrors.add(actionRequest, e.getClass());

				setForward(actionRequest, "portlet.asset_category_admin.error");
			}
			else if (e instanceof DuplicateVocabularyException ||
					 e instanceof VocabularyNameException) {

				SessionErrors.add(actionRequest, e.getClass());
			}
			else {
				throw e;
			}
		}
	}

	@Override
	public ActionForward render(
			ActionMapping actionMapping, ActionForm actionForm,
			PortletConfig portletConfig, RenderRequest renderRequest,
			RenderResponse renderResponse)
		throws Exception {

		ActionUtil.getVocabulary(renderRequest);

		return actionMapping.findForward(
			getForward(
				renderRequest, "portlet.asset_category_admin.edit_vocabulary"));
	}

	public void updateVocabulary(ActionRequest actionRequest) throws Exception {
		long vocabularyId = ParamUtil.getLong(actionRequest, "vocabularyId");

		Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(
			actionRequest, "title");
		Map<Locale, String> descriptionMap =
			LocalizationUtil.getLocalizationMap(actionRequest, "description");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			AssetVocabulary.class.getName(), actionRequest);

		if (vocabularyId <= 0) {

			// Add vocabulary

			AssetVocabularyServiceUtil.addVocabulary(
				StringPool.BLANK, titleMap, descriptionMap,
				getSettings(actionRequest), serviceContext);
		}
		else {

			// Update vocabulary

			AssetVocabularyServiceUtil.updateVocabulary(
				vocabularyId, StringPool.BLANK, titleMap, descriptionMap,
				getSettings(actionRequest), serviceContext);
		}
	}

	protected String getSettings(ActionRequest actionRequest) {
		AssetVocabularySettingsHelper vocabularySettingsHelper =
			new AssetVocabularySettingsHelper();

		int[] indexes = StringUtil.split(
			ParamUtil.getString(actionRequest, "indexes"), 0);

		long[] classNameIds = new long[indexes.length];
		long[] classTypePKs = new long[indexes.length];
		boolean[] requireds = new boolean[indexes.length];

		for (int i = 0; i < indexes.length; i++) {
			int index = indexes[i];

			classNameIds[i] = ParamUtil.getLong(
				actionRequest, "classNameId" + index);

			classTypePKs[i] = ParamUtil.getLong(
				actionRequest,
				"subtype" + classNameIds[i] + "-classNameId" + index,
				AssetCategoryConstants.ALL_CLASS_TYPE_PK);

			requireds[i] = ParamUtil.getBoolean(
				actionRequest, "required" + index);
		}

		vocabularySettingsHelper.setClassNameIdsAndClassTypePKs(
			classNameIds, classTypePKs, requireds);

		boolean multiValued = ParamUtil.getBoolean(
			actionRequest, "multiValued");

		vocabularySettingsHelper.setMultiValued(multiValued);

		return vocabularySettingsHelper.toString();
	}

}