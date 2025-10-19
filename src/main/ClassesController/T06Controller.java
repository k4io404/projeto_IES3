package ClassesController;

import java.util.List;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class T06Controller {
    private TextField pesquisaField;
    private TableView<Object> veiculosTableView; // Usar VeiculoDTO aqui
    private Pagination paginacao;
    
    public void initialize() {
        // preciso realizar consulta no banco de dados referente aos veículos e apresentar nas tabelas
        // Também possui barra de pesquisa e paginação
    }
}