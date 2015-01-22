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

package com.liferay.portal.kernel.words;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.jazzy.InvalidWord;

import java.util.List;
import java.util.Set;

/**
 * @author Shinn Lok
 */
@ProviderType
public interface Words {

	public List<InvalidWord> checkSpelling(String text);

	public List<String> getDictionaryList();

	public Set<String> getDictionarySet();

	public String getRandomWord();

	public boolean isDictionaryWord(String word);

}