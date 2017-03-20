package server;

import sharedResources.Match;
import util.FileUploader;
import util.NetworkUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by ASUS on 3/17/2017.
 */
public class MatchPortThread implements Runnable {

    //private NetworkUtil nc;
    private Match match;
    private ServerSocket serverSocket;
    private MatchThread matchThread;
    private Thread thread;

    public MatchPortThread(MatchThread matchThread, Match match) {
        this.matchThread = matchThread;

        this.match = match;
        this.thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try{
            serverSocket = new ServerSocket(match.getSocketNo());
            while(true){
                Socket clientSocket = serverSocket.accept();
                NetworkUtil nc = new NetworkUtil(clientSocket);
                String clientId = (String) nc.read();
                System.out.println(clientId+" got this match id "+match.getMatchId());
                matchThread.getMatchSubscriberPortMap().put(clientId, nc);
                matchThread.getClintIdMap().put(nc,clientId);
                matchThread.getMatchNcFUMap().put(nc, new FileUploader(nc,matchThread));
                if(matchThread.getCurrOverTable().containsKey(clientId)){
                    new Thread(){
                        public  void run(){
                            int offset = Integer.parseInt(matchThread.getCurrOverTable().get(clientId));
                            int flag = 0;
                            int team1Length = match.getScores1().size();
                            int team2Length = match.getScores2().size();
                            int curr = matchThread.getCurrOver();
                            String filePath = "./src/serverFiles/";
                            String filename=match.getMatchId()+"_"+match.getTeam1()+"_"+match.getTeam2()+"_toto_"+clientId+".txt";
                            try {
                                FileWriter fw = new FileWriter(filePath+filename);
                                ArrayList<ArrayList<Integer>> team1Arr = match.getScores1();
                                for(int i=offset; i<team1Length;i++){
                                    ArrayList<Integer> over = team1Arr.get(i);
                                    for (int j=0;j<over.size();j++){
                                        fw.write(Integer.toString(over.get(j))+" ");

                                    }
                                    fw.write("\n");
                                }
                                ArrayList<ArrayList<Integer>> team2Arr = match.getScores2();

                                int curr1 = offset-team1Length<=0?0:offset-team1Length;
                                System.out.println("Offset : "+offset+" curr1 : "+curr1);
                                for(int i=curr1; i<curr-team1Length;i++){
                                    ArrayList<Integer> over = team2Arr.get(i);
                                    for (int j=0;j<over.size();j++){
                                        fw.write(Integer.toString(over.get(j))+" ");

                                    }
                                    System.out.println();
                                    fw.write("\n");
                                }
                                fw.close();
                                File file = new File(filePath+filename);
                                int size = (int) file.length();
                                matchThread.getMatchNcFUMap().get(nc).send(filePath,filename,size);
                                matchThread.getCurrOverTable().put(clientId, Integer.toString(curr));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }.start();



                }
                else if(matchThread.getCurrOver() > 0){

                    new Thread(){
                        public void run(){
                            int team1 = match.getScores1().size();
                            int curr = matchThread.getCurrOver();

                            int rest;
                            rest= curr-team1<=0? 0: curr-team1;
                            String filePath = "./src/serverFiles/";
                            String filename=match.getMatchId()+"_"+match.getTeam1()+"_"+match.getTeam2()+"_"+clientId+".txt";

                            try {
                                FileWriter fw = new FileWriter(filePath+filename);
                                ArrayList<ArrayList<Integer>> team1Arr = match.getScores1();
                                for(int i=0; i<team1;i++){
                                    ArrayList<Integer> over = team1Arr.get(i);
                                    for (int j=0;j<over.size();j++){
                                        fw.write(Integer.toString(over.get(j))+" ");

                                    }
                                    fw.write("\n");
                                }
                                ArrayList<ArrayList<Integer>> team2Arr = match.getScores2();
                                for(int i=0; i<rest;i++){
                                    ArrayList<Integer> over = team2Arr.get(i);
                                    for (int j=0;j<over.size();j++){
                                        fw.write(Integer.toString(over.get(j))+" ");

                                    }
                                    System.out.println();
                                    fw.write("\n");
                                }
                                fw.close();
                                File file = new File(filePath+filename);
                                int size = (int) file.length();
                                System.out.println(filePath+filename+" writing this for "+clientId);
                                matchThread.getMatchNcFUMap().get(nc).send(filePath,filename,size);
                                matchThread.getCurrOverTable().put(clientId, Integer.toString(curr));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }.start();
                }
                else{
                    matchThread.getCurrOverTable().put(clientId,"0");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
}
