package server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ServerController {

    @FXML
    private AnchorPane firstActionServer;

    @FXML
    private TextField numMatchesPerClient;



    @FXML
    private TextField studentList;

    private ServerMain serverMain;


    public ServerMain getServerMain() {
        return serverMain;
    }

    public void setServerMain(ServerMain serverMain) {
        this.serverMain = serverMain;
    }





    @FXML
    void onStartOfServer(ActionEvent event) {
        serverMain.setServerMainThread(new ServerMainThread(serverMain));
        serverMain.getServerMainThread().setMaxPerClient(Integer.parseInt(numMatchesPerClient.getText()));
        String students = studentList.getText();
        ArrayList<Integer> studentArr = new ArrayList<>();
        if(students.contains("-")){
            String[] strings = students.split("-");
            //serverMain.getServerMainThread().setLowerLimit(Integer.parseInt(lowerLimit));
            //serverMain.getServerMainThread().setUpperLimit(Integer.parseInt(upperLimit));
            int lowerLimit=Integer.parseInt(strings[0]);
            int upperLimit=Integer.parseInt(strings[1]);
            for (int i=lowerLimit;i<=upperLimit;i++){
                studentArr.add(i);
            }
        }
        else if(students.contains(",")){
            String[] strings = students.split(",");
            for (String s: strings){
                studentArr.add(Integer.parseInt(s));
            }
        }
        serverMain.getServerMainThread().setAllowableSId(studentArr);
        serverMain.showCreateMatchPage();
    }

}
