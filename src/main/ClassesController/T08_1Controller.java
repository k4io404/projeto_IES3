package ClassesController;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class T08_1Controller {
    private TextField nomeLocalField;
    private TextArea descricaoArea;
    private Button salvarButton;
    private Button cancelarButton;

    private void handleSalvarButtonAction() {
        String nome = nomeLocalField.getText();
        String descricao = descricaoArea.getText();
        // ao clicar no botão "Salvar", os dados devem ser inseridos no banco de dados
    }

    private void handleCancelarButtonAction() {
        // ao clicar no botão "Cancelar", deve voltar à página T08
    }
}