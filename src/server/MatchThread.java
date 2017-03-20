package server;

import com.sun.security.ntlm.Server;
import sharedResources.Match;
import util.FileUploader;
import util.NetworkUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created by ASUS on 3/12/2017.
 */
public class MatchThread implements Runnable {

    private Thread thread;
    private Match match;
    private ServerMain serverMain;
    private ServerMainThread serverMainThread;
    private Hashtable<String, String> currOverTable = new Hashtable<>();
    private Hashtable<String, NetworkUtil> matchSubscriberPortMap = new Hashtable<>();
    private Hashtable<NetworkUtil, String> clintIdMap = new Hashtable<>();
    private Hashtable<NetworkUtil, FileUploader> matchNcFUMap = new Hashtable<>();
    private int team1Wickets=0;
    private int team2Wickets=0;
    private int currOver=0;

    public Hashtable<NetworkUtil, String> getClintIdMap() {
        return clintIdMap;
    }

    public void setClintIdMap(Hashtable<NetworkUtil, String> clintIdMap) {
        this.clintIdMap = clintIdMap;
    }

    public Hashtable<NetworkUtil, FileUploader> getMatchNcFUMap() {
        return matchNcFUMap;
    }

    public void setMatchNcFUMap(Hashtable<NetworkUtil, FileUploader> matchNcFUMap) {
        this.matchNcFUMap = matchNcFUMap;
    }

    public int getCurrOver() {
        return currOver;
    }

    public void setCurrOver(int currOver) {
        this.currOver = currOver;
    }

    public MatchThread(ServerMainThread serverMainThread, ServerMain serverMain, Match match) {
        this.serverMain = serverMain;
        this.serverMainThread = serverMainThread;
        this.match = match;
        this.thread = new Thread(this);
        thread.start();
        new MatchPortThread(this, match);
    }




    @Override
    public void run() {

            //for team1;
            int scores = 0;
        Random random = new Random();
            for (int i=0;i<20;i++){
                ArrayList<Integer>over=new ArrayList<>();
                if(team1Wickets == 10) break;
                for(int j=0;j<6;j++){
                    if(team1Wickets == 10) break;

                    int ball =  random.nextInt()%8;
                    ball = ball<0? -1*ball:ball;
                    if(ball == 7){
                        team1Wickets++;
                        System.out.println(i+" wicket "+team1Wickets);
                    }
                    else scores = scores+ball;
                    System.out.println(ball);

                    over.add(ball);


                }
                match.getScores1().add(over);
                currOver++;
                String filePath = "./src/serverFiles/";
                String fileName = match.getMatchId() +"_"+match.getTeam1()+"_"+Integer.toString(i)+".txt";
                try {
                    FileWriter fw = new FileWriter(filePath+fileName);
                    for(int k=0;k<over.size();k++){
                        if(over.get(k).equals(7)) {
                            fw.write("out");
                        }
                        else if(over.get(k).equals(0)) fw.write("dot");
                        else {

                            fw.write(Integer.toString(over.get(k)));
                        }
                        fw.write(" ");
                    }
                    fw.write("\nscore : "+scores+" wickets : "+team1Wickets);
                    fw.flush();
                    fw.close();
                    File file = new File(filePath+fileName);
                    int size =(int) file.length();

                    for(NetworkUtil nc: matchSubscriberPortMap.values()){
                        currOverTable.remove(clintIdMap.get(nc));
                        currOverTable.put(clintIdMap.get(nc), Integer.toString(currOver));
                        new Thread(){
                            public void run(){
                                matchNcFUMap.get(nc).send(filePath, fileName, size);
                            }

                        }.start();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            match.setTeam1Score(scores);
            team2Wickets=0;
            scores = 0;
            for (int i=0;i<20;i++){
                ArrayList<Integer>over=new ArrayList<>();
                if(team2Wickets == 10 || scores>match.getTeam1Score()) break;
                for(int j=0;j<6;j++){
                    if(team2Wickets == 10 || scores>match.getTeam1Score()) break;

                    int ball =  random.nextInt()%8;
                    ball = ball<0?-1*ball:ball;
                    if(ball == 7){
                        team2Wickets++;
                        System.out.println(i+" wicket "+team2Wickets);
                    }
                    else {
                        scores += ball;
                    }
                    over.add(ball);
                }
                match.getScores2().add(over);
                currOver++;
                String filePath = "./src/serverFiles/";
                String fileName = match.getMatchId() +"_"+match.getTeam2()+"_"+Integer.toString(i)+".txt";
                try {
                    FileWriter fw2 = new FileWriter(filePath+fileName);
                    for(int k=0;k<over.size();k++){
                        if(over.get(k).equals(7)){
                            fw2.write("out");
                        }
                        else if(over.get(k).equals(0)) fw2.write("dot");
                        else fw2.write(Integer.toString(over.get(k)));
                        fw2.write(" ");
                    }
                    fw2.write("\nscore : "+scores+" wickets : "+team2Wickets);
                    fw2.flush();
                    System.out.println("");
                    fw2.close();
                    File file = new File(filePath+fileName);
                    int size =(int) file.length();

                    for(NetworkUtil nc: matchSubscriberPortMap.values()){
                        currOverTable.remove(clintIdMap.get(nc));
                        currOverTable.put(clintIdMap.get(nc), Integer.toString(currOver));
                        new Thread(){
                            public void run(){
                                matchNcFUMap.get(nc).send(filePath, fileName, size);
                            }

                        }.start();
                        System.out.println();
                    }
                }
                catch (Exception e){
                    System.out.println("");;
                    e.printStackTrace();
                }

                try {
                    thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }
            match.setTeam2Score(scores);

            if(match.getTeam1Score()>match.getTeam2Score()){
                match.setWinner(match.getTeam1());
            }
            else if(match.getTeam2Score()>match.getTeam1Score()){
                match.setWinner(match.getTeam2());
            }else {
                match.setWinner("draw");
            }
            try {
                thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String filePath = "./src/serverFiles/";
            String fileName = match.getMatchId() +"_winner_is_"+match.getWinner()+".txt";
            try{
                FileWriter fw3 = new FileWriter(filePath+fileName);
                fw3.write("Winner of match "+match.getTeam1()+" vs "+match.getTeam2()+" is "+match.getWinner()+"\n"+match.getTeam1()+": "+match.getTeam1Score()+" "+match.getTeam2()+": "+match.getTeam2Score());
                fw3.flush();
                System.out.println("");
                fw3.close();
                File file = new File(filePath+fileName);
                int size =(int) file.length();

                for(NetworkUtil nc: matchSubscriberPortMap.values()){
                    currOverTable.remove(clintIdMap.get(nc));
                    System.out.println();
                    currOverTable.put(clintIdMap.get(nc), Integer.toString(currOver));
                    new Thread(){
                        public void run(){
                            matchNcFUMap.get(nc).send(filePath, fileName, size);
                        }

                    }.start();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }






    }

    public int getTeam1Wickets() {
        return team1Wickets;
    }

    public void setTeam1Wickets(int team1Wickets) {
        this.team1Wickets = team1Wickets;
    }

    public int getTeam2Wickets() {
        return team2Wickets;
    }

    public void setTeam2Wickets(int team2Wickets) {
        this.team2Wickets = team2Wickets;
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

    public ServerMain getServerMain() {
        return serverMain;
    }

    public void setServerMain(ServerMain serverMain) {
        this.serverMain = serverMain;
    }

    public ServerMainThread getServerMainThread() {
        return serverMainThread;
    }

    public void setServerMainThread(ServerMainThread serverMainThread) {
        this.serverMainThread = serverMainThread;
    }

    public Hashtable<String, String> getCurrOverTable() {
        return currOverTable;
    }

    public void setCurrOverTable(Hashtable<String, String> currOverTable) {
        this.currOverTable = currOverTable;
    }

    public Hashtable<String, NetworkUtil> getMatchSubscriberPortMap() {
        return matchSubscriberPortMap;
    }

    public void setMatchSubscriberPortMap(Hashtable<String, NetworkUtil> matchSubscriberPortMap) {
        this.matchSubscriberPortMap = matchSubscriberPortMap;
    }
}
