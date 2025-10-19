package ClassesController;

import java.util.Date;
import java.util.List;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class T04Controller {
    private TextField pesquisaField;
    private TableView<Object> acessosTableView; // Usar AcessoDTO aqui
    private TableColumn<Object, String> localColumn; // Usar AcessoDTO aqui
    private TableColumn<Object, String> pessoaColumn; // Usar AcessoDTO aqui
    private TableColumn<Object, String> tipoPessoaColumn; // Usar AcessoDTO aqui
    private TableColumn<Object, Date> dataColumn; // Usar AcessoDTO aqui
    private Pagination paginacao;
    
    // private AcessoService acessoService = new AcessoService();

    public void initialize() {
        // necessário requisitar ao banco de dados informações referentes ao acessos para mostrar nas tabela
    }

    private void handlePesquisarButtonAction() {
        // ao clicar no botão pesquisar precisa realizar requisição ao banco de dados para consultar os acessos pesquisados, por meio do nome do local, nome do indivíduo ou data
    }
}