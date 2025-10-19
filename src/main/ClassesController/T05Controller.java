package ClassesController;

import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
// import com.example.portaria.dto.PessoaDTO;
// import com.example.portaria.service.PessoaService;


public class T05Controller {
    private TextField pesquisaField;
    private TableView<Object> pessoasTableView; // Usar PessoaDTO aqui
    private Pagination paginacao;
    private Button cadastrarButton;

    public void initialize() {
        // similar à página anterior, com paginação, barra de pesquisa e filtros de dados
    }

    private void handleCadastrarButtonAction() {
        // ao clicar no botão cadastrar, deve ir para a página seguinte T05_1
    }
}