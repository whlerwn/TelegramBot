package router;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface RouterService {

    /**
     * Sends notification to TEAMLEAD or TEACHER
     *
     * @param untrackedUsers - string of untracked users
     * @return "Success" when received
     */
    @WebMethod
    String sendNotification(String untrackedUsers);

    /**
     * @param stream - stream of bytes
     * @return "Success" when received
     */
    @WebMethod
    String sendReport(String stream);
}