/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portal.upgrade;

import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.upgrade.v5_3_0.UpgradeDocumentLibrary;
import com.liferay.portal.upgrade.v5_3_0.UpgradeShopping;

/**
 * <a href="UpgradeProcess_5_3_0.java.html"><b><i>View Source</i></b></a>
 *
 * @author Jorge Ferrer
 */
public class UpgradeProcess_5_3_0 extends UpgradeProcess {

	public int getThreshold() {
		return ReleaseInfo.RELEASE_5_3_0_BUILD_NUMBER;
	}

	protected void doUpgrade() throws Exception {
//		upgrade(UpgradeSchema.class);
//		upgrade(UpgradeAsset.class);
//		upgrade(UpgradeAssetPublisher.class);
//		upgrade(UpgradeBlogs.class);
		upgrade(UpgradeDocumentLibrary.class);
//		upgrade(UpgradeExpando.class);
//		upgrade(UpgradeGroup.class);
//		upgrade(UpgradePolls.class);
//		upgrade(UpgradePortletId.class);
		upgrade(UpgradeShopping.class);
//		upgrade(UpgradeSocial.class);
//		upgrade(UpgradeWiki.class);
	}

}