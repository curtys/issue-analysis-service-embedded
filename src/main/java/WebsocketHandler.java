import ch.unibe.scg.curtys.vectorization.issue.Issue;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebSocket
public class WebsocketHandler {

    private final static Logger LOG = LoggerFactory.getLogger(WebsocketHandler.class);

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        ObjectMapper mapper = new ObjectMapper();
        Protocol in;
        Protocol out = new Protocol(Protocol.Event.DOES_NOT_UNDERSTAND);
        Issue issue = null;
        try {
			in = mapper.readValue(message, Protocol.class);
			if (in != null && in.event == Protocol.Event.REQUEST &&
					!StringUtils.isBlank(in.payload)) {
				issue = mapper.readValue(in.payload, Issue.class);
				Result res = Controller.instance().analyse(issue);
				out = new Protocol(res);
			}
		} catch (IOException e) {
			LOG.info("Did not understand message: " + message);
			out = new Protocol(Protocol.Event.DOES_NOT_UNDERSTAND);
        } finally {
			try {
				Service.send(user, out);
			} catch (IOException e) {
				LOG.error("Could not send message to remote.");
				e.printStackTrace();
			} finally {
				user.close();
			}
        }
    }

}
