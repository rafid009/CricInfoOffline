package server;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sharedResources.Match;
import sharedResources.Student;
import util.NetworkUtil;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by ASUS on 3/16/2017.
 */
public class ServerMainThread implements Runnable {
    private Thread thread;
    private ServerMain serverMain;
    private ServerSocket serverSocket;
    private NetworkUtil nc;
    private ObservableList<Student> clientList = FXCollections.observableArrayList();
    private ObservableList<Match> matches = FXCollections.observableArrayList();
    private Hashtable<String, MatchThread> matchThreadTable = new Hashtable<>();
    private Hashtable<String, Student> studentsTable = new Hashtable<String, Student>();
    private Hashtable<InetAddress, Student> ipVsClientMap = new Hashtable<>();
    private int maxPerClient;
    private int lowerLimit;
    private int upperLimit;
    private int matchId=0;
    private int matchSocketNo = 5000;
    private ArrayList<Integer> allowableSId = new ArrayList<>();

    public Hashtable<InetAddress, Student> getIpVsClientMap() {
        return ipVsClientMap;
    }

    public void setIpVsClientMap(Hashtable<InetAddress, Student> ipVsClientMap) {
        this.ipVsClientMap = ipVsClientMap;
    }

    public ArrayList<Integer> getAllowableSId() {
        return allowableSId;
    }

    public void setAllowableSId(ArrayList<Integer> allowableSId) {
        this.allowableSId = allowableSId;
    }

    public ServerMainThread(ServerMain serverMain) {
        this.serverMain = serverMain;
        this.thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(33333);
            System.out.println("server running...");
            //new WriteThreadServer(table, "Server");
            while (true) {
                Socket clientSock = serverSocket.accept();

                nc=new NetworkUtil(clientSock);
                //InetAddress ip = clientSock.getInetAddress();
                InetAddress ip = clientSock.getInetAddress();
                //System.out.println(socket);
                System.out.println(ip);
                int id = (int)nc.read();
                System.out.println("found : "+id);
                Student student = new Student(ip,id,nc);
                if(allowableSId.contains(id) && !clientList.contains(student)){
                    if(!studentsTable.containsKey(Integer.toString(id))){

                        studentsTable.put(Integer.toString(id), student);
                        ipVsClientMap.put(ip, student);
                        clientList.add(student);

                        new ReadThreadServer(this,nc,student);
                        nc.write("ready");
                        System.out.println("connected");
                        //thread.sleep(1000);
                    }
                    else{
                        Student student1 = studentsTable.get(Integer.toString(id));
                        if(ipVsClientMap.containsKey(ip) && ipVsClientMap.get(ip).getId() == id){
                            clientList.add(student1);
                            new ReadThreadServer(this,nc,student1);
                            nc.write("ready");
                            System.out.println("connected");


                        }
                        else{
                            Platform.runLater(()->serverMain.alertAboutIp(this,nc,student1));
                        }
                    }



                }
                else {

                    nc.write("put a valid student id");
                    try {

                        thread.sleep(1000); // 1 second
                    } catch (InterruptedException ex) {
                        // handle error
                        ex.printStackTrace();
                    }
                    nc.closeConnection();

                }


            }





        }catch(Exception e) {
            e.printStackTrace();
        }


    }

    public int getMatchSocketNo() {
        return matchSocketNo;
    }

    public void setMatchSocketNo(int matchSocketNo) {
        this.matchSocketNo = matchSocketNo;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public ObservableList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ObservableList<Match> matches) {
        this.matches = matches;
    }

    public ServerMain getServerMain() {
        return serverMain;
    }

    public void setServerMain(ServerMain serverMain) {
        this.serverMain = serverMain;
    }

    public Hashtable<String, MatchThread> getMatchThreadTable() {
        return matchThreadTable;
    }

    public void setMatchThreadTable(Hashtable<String, MatchThread> matchThreadTable) {
        this.matchThreadTable = matchThreadTable;
    }

    public Hashtable<String, Student> getStudentsTable() {
        return studentsTable;
    }

    public void setStudentsTable(Hashtable<String, Student> studentsTable) {
        this.studentsTable = studentsTable;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public NetworkUtil getNc() {
        return nc;
    }

    public void setNc(NetworkUtil nc) {
        this.nc = nc;
    }

    public ObservableList<Student> getClientList() {
        return clientList;
    }

    public void setClientList(ObservableList<Student> clientList) {
        this.clientList = clientList;
    }

    public int getMaxPerClient() {
        return maxPerClient;
    }

    public void setMaxPerClient(int maxPerClient) {
        this.maxPerClient = maxPerClient;
    }
}
