package server;

import sharedResources.Match;
import sharedResources.Student;
import util.NetworkUtil;

import java.util.Hashtable;

/**
 * Created by MdRafid on 11/18/2015.
 */

public class ReadThreadServer implements Runnable {
    private Thread thr;
    private NetworkUtil nc;
    private Student student;
    private ServerMainThread serverMainThread;

    public ReadThreadServer(ServerMainThread serverMainThread, NetworkUtil nc, Student student) {
        this.nc = nc;
        this.serverMainThread = serverMainThread;
        this.student = student;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                String receivedMsg = (String)nc.read();
                if (receivedMsg.equals("1")){
                    String msg = "1::"+Integer.toString(serverMainThread.getMaxPerClient());
                    int length = serverMainThread.getMatches().size();
                    msg += "::"+Integer.toString(length);
                    for(Match m : serverMainThread.getMatches()){
                        String matchData = m.getMatchId()+"::"+m.getTeam1()+"::"+m.getTeam2()+"::"+m.getSocketNo();
                        msg += "::" + matchData;
                    }
                    nc.write(msg);
                }



            }
        } catch (Exception e) {
            serverMainThread.getClientList().remove(student);
            //serverMainThread.getStudentsTable().remove(student.getId());
            System.out.println(e);
            e.printStackTrace();
        }
        nc.closeConnection();
    }
}