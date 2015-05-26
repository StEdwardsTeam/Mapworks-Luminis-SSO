<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>

<%@ page import="com.liferay.portal.kernel.util.Constants" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="com.liferay.portlet.PortletPreferencesFactoryUtil" %>
<%@ page import="com.sghe.luminis.person.services.DelegatingPersonService" %>
<%@ page import="com.sghe.luminis.person.entity.Person" %>
<%@ page import="com.sghe.luminis.person.entity.Role" %>
<%@ page import="java.util.List" %>

<portlet:defineObjects />
<liferay-theme:defineObjects />

<liferay-portlet:actionURL portletConfiguration="true" var="configurationURL" />

<%
    Boolean isAdmin = false;
    String exceptionError = " ";

    //If this is any Liferay administrator, allow setting the institutionId
    DelegatingPersonService delegatingPersonService = new DelegatingPersonService();
    Person person = delegatingPersonService.getCurrentPerson();
    List<Role> roles = person.getListOfRoles();

    for (Role role : roles) {
        if (role.toString().equalsIgnoreCase("Administrator"))
            isAdmin = true;
    }

    //This is a recipe that works for Liferay 5.2.3. Most of the examples online are 6+ only.
    String portletResource = ParamUtil.getString(renderRequest, "portletResource");
    String institutionId_cfg = "************";
    String header_cfg = "";
    String text_cfg = "";
    String urlText_cfg = "";
    String urlStyle_cfg = "";
    try {
        PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(renderRequest, portletResource);
        header_cfg = prefs.getValue("header", "");
        text_cfg = prefs.getValue("text", "");
        urlText_cfg = prefs.getValue("urlText", "");
        urlStyle_cfg = prefs.getValue("urlStyle", "");

    } catch (Exception e) {
        exceptionError += "Could not retrieve portlet preferences: " + e.toString() + "<br />";
    }
%>

<liferay-ui:success key="success" message="Configuration saved" />
<p>
    <%=exceptionError%>
</p>
<% if (!isAdmin) { %>
<p>
    You must be logged in as an administrator to make changes.
</p>
<% } else { %>
<form action="<%= configurationURL %>" method="post" name="<portlet:namespace />fm">
    <input name="<portlet:namespace /><%=Constants.CMD%>" type="hidden" value="<%=Constants.UPDATE%>" />
    <br />
    Institution ID:<br />
    <input name="<portlet:namespace />institutionId" type="text" value="<%=institutionId_cfg%>" /><br />
    <br />
    Header:<br />
    <textarea rows="1" cols="80" name="<portlet:namespace />header"><%=header_cfg%></textarea><br />
    <br />
    Text:<br />
    <textarea rows="4" cols="80" name="<portlet:namespace />text"><%=text_cfg%></textarea><br />
    <br />
    URL Text:<br />
    <textarea rows="1" cols="80" name="<portlet:namespace />urlText"><%=urlText_cfg%></textarea><br />
    <br />
    URL Style:<br />
    <textarea rows="1" cols="80" name="<portlet:namespace />urlStyle"><%=urlStyle_cfg%></textarea><br />
    <br />
    <input type="submit" value="Submit changes" />
</form>
<% } %>
