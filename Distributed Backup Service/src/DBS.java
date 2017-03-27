//Distributed Backup Service Client Interface

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DBS {

    private static int repDegree, recStorage,  port;
    private static String peerAP, address, oper, filePath;

    private static RMI peer;

    private static boolean procArgs(String[] args){

        if(args.length == 2){

            if(args[1].equals("STATE")){

            }

            else{
                System.out.println("Usage: DBS <peer_ap> <sub_protocol> <opnd_1> <opnd_2>\n");
                return false;
            }

        }
        else if(args.length == 3){

            if(args[1].equals("RESTORE") || args[1].equals("DELETE")) {
                filePath = args[2];

                System.out.println("File Path: " + filePath + "\n");
            }

            else if(args[1].equals("RECLAIM")){

                try{
                    recStorage = Integer.parseInt(args[2]);
                }catch(NumberFormatException e){
                    System.out.println("Space reclaimed must be an integer.\n");
                    return false;
                }

                System.out.println("Reclaimed Storage: " + recStorage + "\n");

            }

            else{
                System.out.println("Usage: DBS <peer_ap> <sub_protocol> <opnd_1> <opnd_2>\n");
                return false;
            }

        }
        else if(args.length == 4){

            if(args[1].equals("BACKUP")) {

                filePath = args[2];

                try {
                    repDegree = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    System.out.println("Replication Degree must be an integer.\n");
                    return false;
                }

                System.out.println("File Path: " + filePath + "\nReplication Degree: " + repDegree + "\n");

            }

            else{
                System.out.println("Usage: DBS <peer_ap> <sub_protocol> <opnd_1> <opnd_2>\n");
                return false;
            }

        }

        else{
            System.out.println("Usages:\nDBS <peer_ap> BACKUP <file_path> <rep_deg>\n" +
                                        "DBS <peer_ap> RESTORE <file_path>\n" +
                                        "DBS <peer_ap> DELETE <file_path>\n" +
                                        "DBS <peer_ap> RECLAIM <amount_KB>\n" +
                                        "DBS <peer_ap> STATE");
            return false;
        }

        peerAP = args[0];
        oper = args[1];

        //parsePeerAP();

        return true;
    }

    private static void parsePeerAP(){
        String[] parts = peerAP.split(":");

        address = parts[0];
        port = Integer.parseInt(parts[1]);
    }

    public static void main(String[] args) throws IOException {

        if(!procArgs(args))
            return;

        System.out.println("peerAP " + peerAP + "\nSub Protocol: " + oper);

        //locate peer in rmi register
        try {

            Registry registry = LocateRegistry.getRegistry("192.168.1.146");

            peer = (RMI) registry.lookup(peerAP);

        } catch (RemoteException | NotBoundException e) {
            System.out.println("Invalid RMI object name");
            return;
        }

        if(oper.equals("BACKUP"))
            peer.backup(filePath, repDegree);
    }

}

//ex: first run Peer, then: java DBS peer BACKUP fp 3
