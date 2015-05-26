#README for Luminis MAP-Works Single Sign-on Portlets

When the login link is clicked, the portlets utilize the MAP-Works webservice to
generate a login URL in MAP-Works to redirect a user to. There are two portlets
because the URL parameters are slightly different for Faculty/Staff and
Students.

At present, the webservice URLs are hard-coded because the presumption is that
if the URLs change, the functionality is likely changing as well.

##Prerequisites

Luminis 5.x (Luminis 4 is not supported)

Grails 2.2.5 (the portlets plugins have not been ported to a newer version)

Check BuildConfig.groovy for Grails dependencies/plugins. It definitely needs
Ellucian Luminis libraries to build properly. You will need to install these in
your local maven or other Grails-compatible repository.

NO ELLUCIAN, SKYFACTOR, OR OTHER PROPRIETARY CODE IS INCLUDED WITH THIS 
DISTRIBUTION.

Notate your Institution ID given to you by MAP-Works. It is utilized as a shared
secret by their webservice.

##Clone upstream

    git clone https://github.com/StEdwardsTeam/Mapworks-Luminis-SSO.git
    cd MAP-Works/[StudentMAPWorks|FacultyMAPWorks]

##Build

    grails war

##Deploy

Copy the generated war to your Luminis 5 deployment directory.

##Add

Add the new portlet to a page. It will be under the category "SEU Custom".

##Configure

As an administrator, click the configure link in the portlet. Add your 
site-specific information. Enter the Institution ID. Save.

##Test

As a valid MAP-Works user (not "lumadmin"), click on the portlet URL to attempt
to sign on to MAP-Works.

##Debugging Tips

Check the catalina and luminis logs.

You may need to import the MAP-Works SSL certificates into the Java keystore.

You may need to patch Luminis' Java to have unlimited encryption.
