package client;

import javafx.application.Platform;

import sharedResources.Match;
import util.FileDownloader;
import util.NetworkUtil;

import java.util.StringTokenizer;

public class ReadThreadClient implements Runnable {
	private Thread thr;
	private NetworkUtil nc;
	private ClientMain main;
	private String serverAddress;
	private int serverPort;
	public ReadThreadClient(ClientMain main, String serverAddress, int serverPort) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.main = main;
        System.out.println("read Thread client starts "+main.getClientId());
        this.thr = new Thread(this);

		thr.start();
	}

	public void clientConnect()
	{
		try {

			nc = new NetworkUtil(serverAddress,serverPort);
			nc.write(main.getClientId());
			String isReady = (String) nc.read();
			if(isReady.equals("ready")){
			    nc.write("1");
                System.out.println("got ready");
                readMessages();
            }
            else{
			    //String msg = (String)nc.read();
                Platform.runLater(() ->
                    main.showNotValidId("Not a valid student Id")
                );

            }



		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public void readMessages(){
        try {

            while(true) {
                String msg = (String)nc.read();

                if (msg == null || msg.equals("")) continue;

                StringTokenizer tok = new StringTokenizer(msg, "::");
                if(!tok.hasMoreTokens()) continue;
                String flag = tok.nextToken();
                if(flag.equals("1")){
                    String sizeStr = tok.nextToken();
                    int maxMatches = Integer.parseInt(sizeStr);
                    main.setMaxNumOfMatch(maxMatches);
                    sizeStr = tok.nextToken();
                    int length = Integer.parseInt(sizeStr);
                    for(int i=0;i<length;i++){
                        String matchId = tok.nextToken();
                        String matchTeam1 = tok.nextToken();
                        String matchTeam2 = tok.nextToken();
                        String matchSocket = tok.nextToken();
                        Match match = new Match(matchTeam1, matchTeam2, Integer.parseInt(matchId), Integer.parseInt(matchSocket));
                        if(!main.getSubscribed().contains(matchId)) main.getMatches().add(match);

                    }
                }
                else if(flag.equals("3")){//for updating match list


                    String matchId = tok.nextToken();
                    String team1 = tok.nextToken();
                    String team2 = tok.nextToken();
                    String socket = tok.nextToken();
                    Match match = new Match(team1,team2,Integer.parseInt(matchId),Integer.parseInt(socket));
                    Platform.runLater(()->main.getMatches().add(match));
                    Platform.runLater(()->main.getMatchListController().updateMatchList(main.getMatches()));

                }

                //System.out.println(flag);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        nc.closeConnection();
    }

	public void run() {
		try {
			clientConnect();


		} catch(Exception e) {
			e.printStackTrace();
		}
		nc.closeConnection();
	}
}



