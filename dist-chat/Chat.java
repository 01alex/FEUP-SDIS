import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.Boolean;

public class Chat extends Thread{

	private String activity;
	static String ip;
	static Peer peer;
	static ArrayList<Peer> peers;
	public static final int DEFAULT_PORT = 8008;

	public Chat(String activity) throws IOException{

		this.activity = activity;

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

		if(args.length > 1){
			System.out.println("Usages:\nChat\nChat <serverIP>\n");
			return;
		}

		ip = Utils.getIPv4().getHostAddress();
		System.out.println("Peer runing at " + ip + "\n");

		peers = new ArrayList<Peer>();
		peers.add(new Peer(ip, DEFAULT_PORT));	//first peers is own

		if(args.length == 1) {
			String serverIP = args[0];
			joinPeer(serverIP);
		}

		System.out.println("======= CHAT =======\n");

		Thread listener = new Thread(new Chat("listen"));
		listener.start();

		Thread.sleep(1000);

		Thread sender = new Thread(new Chat("send"));
		sender.start();

		listener.join();
		sender.join();

	}

	public static void joinPeer(String serverIP) throws Exception{
		Socket mySoc = new Socket();
		mySoc.setReuseAddress(true);
		mySoc.connect(new InetSocketAddress(serverIP, DEFAULT_PORT));

		ObjectOutputStream oos = new ObjectOutputStream(mySoc.getOutputStream());
		oos.writeObject(new Message());
	}

	public static void sendConnectedPeers(String serverIP) throws Exception{
		Socket mySoc = new Socket();
		mySoc.setReuseAddress(true);
		mySoc.connect(new InetSocketAddress(serverIP, DEFAULT_PORT));

		ObjectOutputStream oos = new ObjectOutputStream(mySoc.getOutputStream());
		oos.writeObject(new Message(peers));
	}


	//false if already exists, true if add
	//ioexception & socketexception


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

					//System.out.println("Connected peers: \n");

					for(Peer p : peers) {
						if(p.getIP().equals(ip))
							continue;

						//System.out.println(p.getIP() + "\n");

						Socket mySoc = new Socket();
						mySoc.setReuseAddress(true);
						mySoc.connect(new InetSocketAddress(p.getIP(), DEFAULT_PORT));

						ObjectOutputStream oos = new ObjectOutputStream(mySoc.getOutputStream());
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

		ServerSocket peerSocket = new ServerSocket();
		peerSocket.bind(new InetSocketAddress(ip, DEFAULT_PORT));
		peerSocket.setReuseAddress(true);

		Boolean done = false;
        while(!done) {
        	Socket sock = peerSocket.accept();
			Thread listener = new Thread(new Listener(sock));
			listener.start();
		}

		peerSocket.close();

	}

}