package com.liferay.portal.language;

import junit.framework.TestCase;

public class LanguageResourcesTest extends TestCase {

	public void testAutomaticCopyFixValue() {
		assertEquals("test",LanguageResources.fixValue("test (Automatic Copy)"));
	}
	
	public void testMistypedAutomaticCopyFixValue() {
		assertUnchanged("Missing Space(Automatic Copy)");
		assertUnchanged("Space in annotation ( Automatic Copy)");
		assertUnchanged("Space in annotation (Automatic Copy )");
		assertUnchanged("Spaces in annotation ( Automatic Copy)");
		assertUnchanged("Trailing Space (Automatic Copy) ");
		assertUnchanged("(Automatic Copy) has to be at end");
	}
	
	public void testAutomaticTranslationFixValue() {
		assertEquals("test",LanguageResources.fixValue("test (Automatic Translation)"));
	}
	
	
	public void testMistypedAutomaticTranslationFixValue() {
		assertUnchanged("Missing Space(Automatic Translation)");
		assertUnchanged("Space in annotation ( Automatic Translation)");
		assertUnchanged("Space in annotation (Automatic Translation )");
		assertUnchanged("Spaces in annotation ( Automatic Translation )");
		assertUnchanged("Trailing Space (Automatic Translation) ");
		assertUnchanged("(Automatic Translation) has to be at end");
	}

	public void testNopFixValue() {
		assertUnchanged("test");
		assertUnchanged("test (Unknown annotation)");
		assertUnchanged("test");
	}
	
	public void testFixValueForQuestionableTranslations() {
		assertEquals("test",LanguageResources.fixValue("test (Questionable Translation)"));
		assertEquals("test",LanguageResources.fixValue("test (Questionable Translation: can be annotated)"));
		assertEquals("test",LanguageResources.fixValue("test (Questionable Translation also works without colon)"));
		assertEquals("does not require closing brace",
				LanguageResources.fixValue("does not require closing brace (Questionable Translation"));
		assertEquals("does not require closing brace",
				LanguageResources.fixValue("does not require closing brace (Questionable Translation   "));
		assertEquals("Will ignore everything after the annotation",
				LanguageResources.fixValue("Will ignore everything after the annotation (Questionable Translation) " +
						"because we don't expect it to be in the middle of a translated value"));

		assertUnchanged("Requires Leading Space(Questionable Translation)");
	}

	private void assertUnchanged(String value) {
		assertEquals(value, LanguageResources.fixValue(value));
	}
}
