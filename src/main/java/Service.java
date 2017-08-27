import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

import static spark.Spark.*;

public class Service {


    public static void main(String[] args) {
        staticFiles.location("/public"); //index.html is served at localhost:4567 (default port)
        staticFiles.expireTime(600);
        webSocket("/service", WebsocketHandler.class);
        init();
    }

    public static void send(Session session, Protocol msg)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        session.getRemote().sendString(mapper.writeValueAsString(msg));
    }

}
