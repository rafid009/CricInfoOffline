package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ClientController {

    @FXML
    private TextField ipAddress;

    @FXML
    private TextField portNumber;

    @FXML
    private TextField studentId;

    @FXML
    private Button login;

    private ClientMain clientMain;

    public ClientMain getClientMain() {
        return clientMain;
    }

    public void setClientMain(ClientMain clientMain) {
        this.clientMain = clientMain;
    }

    @FXML
    void onLoginAction(ActionEvent event) {
        clientMain.setClientId(Integer.parseInt(studentId.getText()));
        new ReadThreadClient(clientMain, ipAddress.getText(), Integer.parseInt(portNumber.getText()));
        clientMain.showMatchList();
    }

    public TextField getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(TextField ipAddress) {
        this.ipAddress = ipAddress;
    }

    public TextField getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(TextField portNumber) {
        this.portNumber = portNumber;
    }

    public TextField getStudentId() {
        return studentId;
    }

    public void setStudentId(TextField studentId) {
        this.studentId = studentId;
    }

    public Button getLogin() {
        return login;
    }

    public void setLogin(Button login) {
        this.login = login;
    }
}
