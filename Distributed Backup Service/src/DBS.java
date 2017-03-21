//Distributed Backup Service

import java.io.IOException;

public class DBS {

    private static int repDegree, recStorage;
    private static String peerAP, oper, filePath;

    public static void main(String[] args) throws IOException {

        if(!procArgs(args))
            return;

    }

    private static boolean procArgs(String[] args){

        if(args.length == 2){

            if(args[1].equals("STATE")){

            }

            else{
                System.out.println("Usage: TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>\n");
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
                System.out.println("Usage: TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>\n");
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

                System.out.println("File Path: " + filePath + " Replication Degree: " + repDegree + "\n");

            }

            else{
                System.out.println("Usage: TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>\n");
                return false;
            }

        }

        else{
            System.out.println("Usages:\nTestApp <peer_ap> BACKUP <file_path> <rep_deg>\n" +
                                        "TestApp <peer_ap> RESTORE <file_path>\n" +
                                        "TestApp <peer_ap> DELETE <file_path>\n" +
                                        "TestApp <peer_ap> RECLAIM <amount_KB>\n" +
                                        "TestApp <peer_ap> STATE");
            return false;
        }

        peerAP = args[0];
        oper = args[1];
        System.out.println("Peer AP: " + peerAP + " Sub Protocol: " + oper + "\n");

        return true;
    }

}
