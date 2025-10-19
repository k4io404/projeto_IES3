package ClassesController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class T01Controller {

    @FXML private TextField loginField;
    @FXML private PasswordField senhaField;
    @FXML private Button entrarButton;

    @FXML private void handleEntrarButtonAction() {
        String login = loginField.getText();
        String senha = senhaField.getText();



        System.out.println(login.equals("") ? "vazio" : login);
        System.out.println(senha.equals("") ? "vazio" : senha);
    }
}