import websocket.IssueAnalysisEndpoint;

import static spark.Spark.*;

public class Service {


    public static void main(String[] args) {
        staticFiles.location("/public"); //index.html is served at localhost:4567 (default port)
        staticFiles.expireTime(600);
        webSocket("/service", IssueAnalysisEndpoint.class);
        init();
    }

}
