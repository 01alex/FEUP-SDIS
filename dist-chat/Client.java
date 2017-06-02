import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.Boolean;


public class Client extends Thread{

	String activity;
	static String myIp;
	static String serverIP;
	static Map<String, Socket> peer_Socket;
	
	public Client(String activity) throws IOException{
		
		this.activity = activity;

		this.myIp = Utils.getIPv4().getHostAddress();
	}

	
	/**************************************************
	 * runs the threads to listen to the port and talk to the peer
	 */
	public void run(){
		try{
			if(activity == "listen"){
				peerListen();
			}else{
				peerSend();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	/**************************************************
	 * Starts the program
	 * @param args
	 * @throws SocketException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws Exception {

		if(args.length != 1){
			System.out.println("Usage: Client <serverIP>\n");
			return;
		}

		serverIP = args[0];

		joinPeer();

		initNetwork();

		/*System.out.println("Initial Peers: ");
		for(String peer : peer_Socket.keySet())
		System.out.println(peer + "\n");*/

		System.out.println("======= CHAT =======\n");

		Thread listener = new Thread(new Client("listen"));
		listener.start();

		Thread.sleep(1000);

		Thread sender = new Thread(new Client("send"));
		sender.start();


		listener.join();
		sender.join();

	}

		//ioexception & socketexception
	public static void initNetwork() throws Exception{
		String connPeers = getPeers();

	  	//System.out.println("Initial connected peers: " + connPeers);

		String myIP = Utils.getIPv4().getHostAddress();

		System.out.println("Peer runing at " + myIP + "\n");

		peer_Socket = new HashMap<String, Socket>();

		String[] parts = connPeers.split("/");
		for(int i = 0; i<parts.length; i++){
			if(!parts[i].equals(myIP)){
				Socket mySoc = new Socket();
				mySoc.setReuseAddress(true);
				mySoc.connect( new InetSocketAddress(parts[i], 8008));
				peer_Socket.put(parts[i], mySoc);
			}
		}


	}

	public static String getPeers() throws IOException, MalformedURLException{

		String response = new HttpRequest(serverIP, "/getUsers").GET("UTF-8");

		return response;
	}

	public static String joinPeer() throws IOException, MalformedURLException{
		String[] paramName = { "ip" };
		String[] paramVal = { Utils.getIPv4().getHostAddress()};

		String response = new HttpRequest(serverIP, "/joinUser").POST(paramName, paramVal);

		return response;
	}


		//false if already exists, true if add
		//ioexception & socketexception
	private static Boolean addPeer(String ip) throws Exception{
		if(peer_Socket.containsKey(ip))
			return false;

		Socket mySoc = new Socket();
		mySoc.setReuseAddress(true);
		mySoc.connect( new InetSocketAddress(ip, 8008));

		peer_Socket.put(ip, mySoc);

		return true;
	}
	
	/**************************************************
	 * sends the p2p chat
	 */
	private static void peerSend() {

		try {

			Boolean done = false;
			while (!done) {
				Scanner sc = new Scanner (System.in);
				String s1 = sc.nextLine();

				if(!s1.isEmpty()){

					for(Map.Entry<String, Socket> entry : peer_Socket.entrySet()) {
						ObjectOutputStream oos = new ObjectOutputStream(entry.getValue().getOutputStream());
						oos.writeObject(new Message(s1));
					}

				}

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**************************************************
	 * The listens to the socket
	 * @throws Exception 
	 */
	private static void peerListen() throws Exception{

		//System.out.println("Listening on Socket: 8008");
		ServerSocket peerSocket = new ServerSocket();
		peerSocket.bind( new InetSocketAddress(myIp, 8008) );
		peerSocket.setReuseAddress(true);
		Socket peer = peerSocket.accept();

		
		Boolean done = false;
		while(!done){
			
			ObjectInputStream ois = new ObjectInputStream(peer.getInputStream());
			Message msg = (Message) ois.readObject();
			
			System.out.println(msg.getOriginIP() + ": " + msg.getBody());

			addPeer(msg.getOriginIP());

			/*System.out.println("Actual Peers: \n");
	  			for(String p : peer_Socket.keySet())
	  			System.out.println(p + "\n");*/

	  		}

	  		peerSocket.close();
	  	}

	/**************************************************
	 * Creates a time stamp
	 */
	private static String getTime(){
		DateFormat df = new SimpleDateFormat("hh:mm:ss");
		Date dateobj = new Date();
		return df.format(dateobj);
	}
	

}