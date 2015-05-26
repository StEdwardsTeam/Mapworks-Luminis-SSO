<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<div>
    ${header}
    ${text}
    <g:if test="${flash.error}">
        <div class="portlet-msg-error" style="margin-bottom:1em">${flash.error}</div>
    </g:if>
    <a ${urlStyle} href="${portletResponse.createActionURL()}" target="_blank">${urlText}</a>
</div>
