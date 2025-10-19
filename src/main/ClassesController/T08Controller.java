package ClassesController;

import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class T08Controller {
    private TextField pesquisaField;
    private TableView<Object> locaisTableView; // Usar LocalDTO aqui
    private Pagination paginacao;
    private Button cadastrarButton;
    
    public void initialize() {
        // similar à página anterior
    }
    
    private void handleCadastrarButtonAction() {
        // ao clicar no botão "cadastrar", deve seguir para a página T08_1
    }
}