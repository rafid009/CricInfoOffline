package client;

import sharedResources.Match;
import util.FileDownloader;
import util.NetworkUtil;

import java.util.StringTokenizer;

/**
 * Created by ASUS on 3/17/2017.
 */
public class MatchCommunicationThread implements Runnable {
    private Thread thread;
    private Match match;
    private ClientMain clientMain;
    private String serverAddress = "127.0.0.1";
    private int matchPort;
    private NetworkUtil nc;


    public MatchCommunicationThread(Match match, ClientMain clientMain) {
        this.match = match;
        this.clientMain = clientMain;
        this.thread = new Thread(this);
        thread.start();
    }

    public void initConnection(){
        try{
            matchPort = match.getSocketNo();
            nc = new NetworkUtil(serverAddress, matchPort);
            nc.write(Integer.toString(clientMain.getClientId()));
            System.out.println("established connection with match");
            clientMain.getSubscribedMatches().put(Integer.toString(match.getMatchId()), nc);
            clientMain.getSubscribed().add(match.getMatchId());
            clientMain.setCurrNumOfMatch(clientMain.getCurrNumOfMatch()+1);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public ClientMain getClientMain() {
        return clientMain;
    }

    public void setClientMain(ClientMain clientMain) {
        this.clientMain = clientMain;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getMatchPort() {
        return matchPort;
    }

    public void setMatchPort(int matchPort) {
        this.matchPort = matchPort;
    }

    public NetworkUtil getNc() {
        return nc;
    }

    public void setNc(NetworkUtil nc) {
        this.nc = nc;
    }

    @Override
    public void run() {
        try{
            initConnection();
            while(true){
                String msg = (String)nc.read();

                if (msg == null || msg.equals("")) continue;

                StringTokenizer tok = new StringTokenizer(msg, "::");
                if(!tok.hasMoreTokens()) continue;
                String flag = tok.nextToken();
                if(flag.equals("2")){

                    String fileName = tok.nextToken();
                    System.out.println("got file "+fileName);
                    System.out.println();
                    int fileSize = Integer.parseInt(tok.nextToken());
                    FileDownloader fileDownloader=new FileDownloader(clientMain,fileName,fileSize,nc);
                    fileDownloader.receive();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
