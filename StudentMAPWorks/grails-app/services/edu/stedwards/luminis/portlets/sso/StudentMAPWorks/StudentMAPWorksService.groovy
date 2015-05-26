package edu.stedwards.luminis.portlets.sso.StudentMAPWorks

import groovyx.net.http.*

class StudentMAPWorksService {

    // The URL could probably be configuration. However, it is likely that if the
    // webservice changes that the methodology would likewise change.
    def getLoginURL(studentId, institutionId) {
        def http = new HTTPBuilder('https://ws.map-works.com')
        def result

        try {
            def resp = http.get( path: '/wsaccess.asmx/WSGetStudentURL',
                                query: [institution : institutionId, studentID : studentId]
                                )
            result = resp.toString()

            //log problem URL here but pass error message up to portlet for handling
            if (result.contains('ERROR')) {
                log.error("URL attempted: " + 'https://ws.map-works.com'
                        + '/wsaccess.asmx/WSGetStudentURL?institution='
                        + institutionId + '&studentID=' + studentId)
            }

        } catch (Exception e) {
            log.error('Could not access MAP-Works authentication webservice: ' + e.toString()
                        + "\nURL attempted: " + 'https://ws.map-works.com'
                        + '/wsaccess.asmx/WSGetStudentURL?institution='
                        + institutionId + '&studentID=' + studentId)
        }

        return result
    }
}
