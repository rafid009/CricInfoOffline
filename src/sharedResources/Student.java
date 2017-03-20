package sharedResources;

import util.NetworkUtil;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by ASUS on 3/12/2017.
 */
public class Student implements Serializable{
    private InetAddress ipAddress;
    private int id;
    private NetworkUtil nc;
    private ArrayList<Match> subscribedMatches;
    private int currMatch;

    public ArrayList<Match> getSubscribedMatches() {
        return subscribedMatches;
    }

    public void setSubscribedMatches(ArrayList<Match> subscribedMatches) {
        this.subscribedMatches = subscribedMatches;
    }

    public int getCurrMatch() {
        return currMatch;
    }

    public void setCurrMatch(int currMatch) {
        this.currMatch = currMatch;
    }

    public NetworkUtil getNc() {
        return nc;
    }

    public void setNc(NetworkUtil nc) {
        this.nc = nc;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student(InetAddress ipAddress, int id, NetworkUtil nc) {
        this.ipAddress = ipAddress;
        this.id = id;
        this.nc = nc;
    }
}
