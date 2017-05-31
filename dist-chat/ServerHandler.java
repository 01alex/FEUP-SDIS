
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Date;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;


public class ServerHandler implements HttpHandler {

    String ctx;

    public ServerHandler(String ctx) {
        this.ctx = ctx;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        if(ctx.equals("/getUsers"))
            handleGetUsers(t);
        else if(ctx.equals("/joinUser"))
            handleJoinUser(t);

    }

    private void handleGetUsers(HttpExchange t) throws IOException{

        String response = Server.getUsers();

        sendResponse(t, 200, response);
    }

    private void handleJoinUser(HttpExchange t) throws IOException{

        String query = readPostQuery(t);
        String[] userQ = query.split("=");

        if(Server.addUser(userQ[1])) {
            System.out.println(userQ[0] + " val: " + userQ[1]);
            sendResponse(t, 200, "Success");
        }
        else sendResponse(t, 400, "Already exists");

    }

    private void sendResponse(HttpExchange t, int statusCode, String response) throws IOException {

        t.sendResponseHeaders(statusCode, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String readPostQuery(HttpExchange t) throws IOException {
        String qry;
        InputStream in = t.getRequestBody();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte buf[] = new byte[4096];
            for (int n = in.read(buf); n > 0; n = in.read(buf)) {
                out.write(buf, 0, n);
            }
            qry = new String(out.toByteArray(), "ISO-8859-1");
        } finally {
            in.close();
        }
        return qry;
    }

}