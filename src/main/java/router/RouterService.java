package router;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

import java.io.File;


@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface RouterService {

    /**
     * Sends notification to TEAMLEAD or TEACHER
     * @param untrackedUsers - string of untracked users
     */
    @WebMethod
    String sendNotification(String untrackedUsers);

    // TODO: javadoc
    @WebMethod
    String sendReport(String stream);
}