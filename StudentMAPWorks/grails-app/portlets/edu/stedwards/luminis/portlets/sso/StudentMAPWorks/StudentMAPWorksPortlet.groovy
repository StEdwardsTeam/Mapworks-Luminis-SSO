package edu.stedwards.luminis.portlets.sso.StudentMAPWorks

import com.liferay.portal.kernel.portlet.BaseConfigurationAction
import com.liferay.portal.kernel.servlet.SessionMessages
import com.liferay.portal.kernel.util.ParamUtil
import com.liferay.portlet.PortletPreferencesFactoryUtil
import com.sghe.luminis.person.entity.Person
import com.sghe.luminis.person.entity.Role
import com.sghe.luminis.person.services.DelegatingPersonService

import javax.portlet.*

import org.apache.commons.lang3.StringUtils

class StudentMAPWorksPortlet {

	def title = 'Student MAP-Works'
	def description = '''
This portlet enables single sign-on capability to MAP-Works
'''
	def displayName = 'Student MAP-Works'
	def supports = ['text/html':['view']]

    // DEFINITIONS FOR liferay-display.xml
    def liferay_display_category = 'SEU Custom'

    // DEFINITIONS FOR liferay-portlets.xml

    //This is the method called by LifeRay to support configuration
    def liferay_portlet_configuration_action_class = 'edu.stedwards.luminis.portlets.sso.StudentMAPWorks.ConfigurationActionImpl'

    //This way you only have to change the text and InstitutionId once
    def liferay_portlet_instanceable = 'false'
    def liferay_portlet_preferences_company_wide = 'true'

    //Defaults from liferay-portlets plugin:
    def liferay_portlet_ajaxable = 'true'
    def liferay_portlet_header_portlet_css = []
    def liferay_portlet_header_portlet_javascript = [] 
    def studentMAPWorksService
    def personServiceR

	def actionView = {
        //Get SPRIDEN_ID to pass to webservice
        Person PPrsn = personServiceR.findPersonByPortalUserId(portletRequest.getRemoteUser())

        def institutionId = preferences.getValue("institutionId","")

        def loginURL = studentMAPWorksService.getLoginURL(PPrsn.getPersonId(), institutionId)

        //if loginURL contains string ERROR, send to view with error. URL itself is logged in the service.
        if (loginURL.contains('ERROR')) {
            log.error('institutionId: ' + institutionId)
            log.error('Error from webservice: ' + loginURL)
            flash.error = 'Error from MAP-Works Webservice: ' + loginURL
            portletResponse.setPortletMode(PortletMode.VIEW)
        } else {
            portletResponse.sendRedirect(loginURL)
        }
	}

	def renderView = {
        [
            header: preferences.getValue("header",'''<span style="font-weight: bold; line-height: 1.6em;">MAP-Works Student Success Portal</span>'''),
            text: preferences.getValue("text",'''<p>MAP-Works is a student success portal. Clicking the link below will open a new tab and log you into their website.</p>'''),
            urlText: preferences.getValue("urlText","Log into MAP-Works"),
            urlStyle: preferences.getValue("urlStyle",'''style="font-size:medium"''')
        ]
	}

	def renderHelp = {
		[ : ]
	}

}

//N.b., this configuration class is called directly by Liferay. If you need Grails or portlet context information,
// you'll have to get it the hard way.
// The default render() method expects $PORTLET_DIR/configuration.jsp to exist.
// In a Grails portlet, stick it under web-app.
class ConfigurationActionImpl extends BaseConfigurationAction {
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception
    {
        Boolean isAdmin = false;

        //If this is any Liferay administrator, allow setting the institutionId
        DelegatingPersonService delegatingPersonService = new DelegatingPersonService();
        Person person = delegatingPersonService.getCurrentPerson();
        List<Role> roles = person.getListOfRoles();

        for (Role role : roles) {
            if (StringUtils.equalsIgnoreCase(role.toString(),"Administrator"))
                isAdmin = true;
        }

        if (!isAdmin) {
            super.processAction(portletConfig, actionRequest, actionResponse);
            return; //should never get here.
        }

        //This is a recipe that works for Liferay 5.2.3. Most of the examples online are 6+ only.
        String portletResource = ParamUtil.getString(actionRequest, "portletResource")
        PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(actionRequest, portletResource)

        String institutionId = actionRequest.getParameter("institutionId")
        String header = actionRequest.getParameter("header")
        String text = actionRequest.getParameter("text")
        String urlText = actionRequest.getParameter("urlText")
        String urlStyle = actionRequest.getParameter("urlStyle")

        if (!StringUtils.isBlank(institutionId) && !StringUtils.containsOnly(institutionId, '*')) {
            prefs.setValue("institutionId", institutionId)
        }

        if (StringUtils.isBlank(header)) {
            prefs.reset("header")
        } else {
            prefs.setValue("header", header)
        }

        if (StringUtils.isBlank(text)) {
            prefs.reset("text")
        } else {
            prefs.setValue("text", text)
        }

        if (StringUtils.isBlank(urlText)) {
            prefs.reset("urlText")
        } else {
            prefs.setValue("urlText", urlText)
        }

        if (StringUtils.isBlank(urlStyle)) {
            prefs.reset("urlStyle")
        } else {
            prefs.setValue("urlStyle", urlStyle)
        }

        prefs.store()

        SessionMessages.add(actionRequest, portletConfig.getPortletName() + ".doConfigure")

        super.processAction(portletConfig, actionRequest, actionResponse)
    }
}
