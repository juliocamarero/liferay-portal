<%@ page import="com.liferay.portal.dao.jdbc.util.DataSourceSwapUtil" %>
<%@ page import="com.liferay.portal.events.StartupAction" %>
<%@ page import="com.liferay.portal.kernel.cache.CacheRegistryUtil" %>
<%@ page import="com.liferay.portal.kernel.cache.MultiVMPoolUtil" %>
<%@ page import="com.liferay.portal.kernel.util.CentralizedThreadLocal" %>
<%@ page import="com.liferay.portal.kernel.webcache.WebCachePoolUtil" %>
<%@ page import="com.liferay.portal.service.QuartzLocalServiceUtil" %>
<%@ page import="com.liferay.portal.util.PortalInstances" %>

<%@ page import="java.io.File" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.OutputStream" %>

<%@ page import="java.util.Properties" %>

<%
String parameter = request.getParameter("post");

if (parameter == null) {
%>

<form name="wizard" action="wizard.jsp" method="POST">
	<input type="hidden" name="post" value="post" />
	<input type="text" name="jdbc.default.driverClassName" value="com.mysql.jdbc.Driver" />
	<input type="text" name="jdbc.default.url" value="jdbc:mysql://localhost:3306/lportal?useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false&maxAllowedPacket=16777216&jdbcCompliantTruncation=false" />
	<input type="text" name="jdbc.default.username" value="root" />
	<input type="text" name="jdbc.default.password" value="root" />
	<input type="submit" value="Submit" />
</form>

<%
}
else {
	String driverClassName = request.getParameter("jdbc.default.driverClassName");
	String url = request.getParameter("jdbc.default.url");
	String username = request.getParameter("jdbc.default.username");
	String password = request.getParameter("jdbc.default.password");

	// Write properties to portal-ext.properites

	File portalExtFile = new File(application.getRealPath("/WEB-INF/classes/portal-ext.properties"));

	Properties portalExtProperties = new Properties();

	if (portalExtFile.exists()) {
		InputStream is = new FileInputStream(portalExtFile);

		portalExtProperties.load(is);

		is.close();
	}

	portalExtProperties.put("jdbc.default.driverClassName", driverClassName);
	portalExtProperties.put("jdbc.default.url", url);
	portalExtProperties.put("jdbc.default.username", username);
	portalExtProperties.put("jdbc.default.password", password);

	OutputStream os = new FileOutputStream(portalExtFile);

	portalExtProperties.store(os, "Wizard generated properties");

	os.close();

	// Swap datasource

	Properties jdbcProperties = new Properties();

	jdbcProperties.put("driverClassName", driverClassName);
	jdbcProperties.put("url", url);
	jdbcProperties.put("username", username);
	jdbcProperties.put("password", password);

	DataSourceSwapUtil.swapCounterDataSource(jdbcProperties);
	DataSourceSwapUtil.swapPortalDataSource(jdbcProperties);

	// Clear caches

	CacheRegistryUtil.clear();
	MultiVMPoolUtil.clear();
	WebCachePoolUtil.clear();
	CentralizedThreadLocal.clearShortLivedThreadLocals();

	// Rebuild database and loading data

	QuartzLocalServiceUtil.checkQuartzTables();
	new StartupAction().run(null);
	PortalInstances.reload(application);

	// Kill current session to remove company id saved in cookies
	session.invalidate();
%>

<script type="text/javascript">
	location.href = '/';
</script>

<%
}
%>
