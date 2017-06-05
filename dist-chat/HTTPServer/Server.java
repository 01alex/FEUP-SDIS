
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import java.util.ArrayList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;

public class Server implements Runnable{

  public static HttpServer server;
  private static ArrayList<String> users; //IPs

  public Server(){

  }

  public void run() {
    try {
      setup();
      server.start();
    }catch(IOException e){

    }
  }

  private static void setup() throws IOException{
    try {
      server = HttpServer.create(new InetSocketAddress(8000), 0);

      server.createContext("/getUsers", new ServerHandler("/getUsers"));
      server.createContext("/joinUser", new ServerHandler("/joinUser"));

      server.setExecutor(null);

      users = new ArrayList<String>();

      System.out.println("HTTP Server running at " + Utils.getIPv4().getHostAddress() + "\n");

    } catch (IOException e) {
      System.out.println("Exception caught: " + e.getMessage()
              + " in Server main");
    }
  }

  public static String getUsers(){

    String response = "";

    for(int i=0; i<users.size(); i++){
      response += users.get(i);
      response += "/";
    }

    return response;
  }

  //false if already exists, true if add
  public static boolean addUser(String ip){

    if(users.contains(ip))
      return false;

    users.add(ip);
    return true;
  }

  public static void main(String[] args) throws IOException{

    setup();
    server.start();

  }

}