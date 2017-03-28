//Distributed Backup Service Client Interface

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DBS {

    private static int repDegree, recStorage;
    private static String peerAP, address, obj, oper, filePath;

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
            System.out.println("Usages:\nDBS <host/obj> BACKUP <file_path> <rep_deg>\n" +
                                        "DBS <host/obj> RESTORE <file_path>\n" +
                                        "DBS <host/obj> DELETE <file_path>\n" +
                                        "DBS <host/obj> RECLAIM <amount_KB>\n" +
                                        "DBS <host/obj> STATE");
            return false;
        }

        peerAP = args[0];
        oper = args[1];

        parsePeerAP();

        return true;
    }

    private static void parsePeerAP(){
        String[] parts = peerAP.split("/");

        address = parts[0];
        obj = parts[1];
    }

    public static void main(String[] args) throws IOException {

        if(!procArgs(args))
            return;

        System.out.println("peerAP " + peerAP + "\nSub Protocol: " + oper);

        //locate peer in rmi register
        try {

            Registry registry = LocateRegistry.getRegistry(address);

            peer = (RMI) registry.lookup(obj);

        } catch (RemoteException | NotBoundException e) {
            System.out.println("Invalid RMI object name");
            return;
        }

        if(oper.equals("BACKUP"))
            peer.backup(filePath, repDegree);
    }

}