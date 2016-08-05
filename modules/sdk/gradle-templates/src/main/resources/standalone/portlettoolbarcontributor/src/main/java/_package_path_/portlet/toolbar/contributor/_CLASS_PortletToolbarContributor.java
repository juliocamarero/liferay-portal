package _package_.portlet.toolbar.contributor;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.journal.content.web.constants.JournalContentPortletKeys;
import com.liferay.journal.model.JournalFolderConstants;
import com.liferay.journal.service.JournalFolderService;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.toolbar.contributor.BasePortletToolbarContributor;
import com.liferay.portal.kernel.portlet.toolbar.contributor.PortletToolbarContributor;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.ResourcePermissionChecker;
import com.liferay.portal.kernel.servlet.taglib.ui.Menu;
import com.liferay.portal.kernel.servlet.taglib.ui.MenuItem;
import com.liferay.portal.kernel.servlet.taglib.ui.URLMenuItem;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + "com_liferay_hello_world_web_portlet_HelloWorldPortlet"
	},
	service = {
		PortletToolbarContributor.class
	}
)
public class _CLASS_PortletToolbarContributor
	extends BasePortletToolbarContributor {

	@Override
	public List<Menu> getPortletTitleMenus(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		List<MenuItem> portletTitleMenuItems = new ArrayList<>();

		URLMenuItem urlMenuItem = new URLMenuItem();

		urlMenuItem.setLabel("Liferay");
		urlMenuItem.setURL("http://www.liferay.com");

		menuItems.add(urlMenuItem);

		List<Menu> menus = new ArrayList<>();

		Menu menu = new Menu();

		menu.setDirection("right");
		menu.setExtended(false);
		menu.setIcon("sites");
		menu.setMarkupView("lexicon");
		menu.setMenuItems(portletTitleMenuItems);
		menu.setScroll(false);
		menu.setShowArrow(false);
		menu.setShowWhenSingleIcon(true);

		menus.add(menu);

		return menus;
	}
}