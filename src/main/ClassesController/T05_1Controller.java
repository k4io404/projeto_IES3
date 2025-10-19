package ClassesController;

import java.util.Date;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class T05_1Controller {
    private ChoiceBox<String> tipoChoiceBox;
    private TextField nomeField;
    private TextField cpfField;
    private DatePicker dataNascimentoPicker;
    private TextField telefoneField;
    private TextField emailField;
    private TextField moradorAssociadoField;
    private TextField empresaField;
    private TextField cnpjField;
    private TabPane tabPane;
    private Tab autorizacoesTab;
    private Tab veiculosTab;
    private Tab casasTab;
    private Tab servicosTab;
    private ChoiceBox<String> localChoiceBox;
    private Button adicionarLocalButton;
    private TableView<Object> autorizacoesTableView;
    private TextField placaField;
    private TextField modeloField;
    private TextField corField;
    private Button adicionarVeiculoButton;
    private TableView<Object> veiculosTableView;
    private TextField numeroCasaField;
    private ChoiceBox<String> relacionamentoChoiceBox;
    private Button adicionarCasaButton;
    private TableView<Object> casasTableView;
    private ChoiceBox<String> moradorServicoChoiceBox;
    private Button adicionarServicoButton;
    private TableView<Object> servicosTableView;
    private Button salvarTudoButton;
    private Button cancelarButton;

    public void initialize() {
    }

    private void onTipoPessoaChanged() {
    }

    private void handleSalvarTudoButtonAction() {
        // Validação dos campos obrigatórios e inserção no banco de dados
    }

    private void handleCancelarButtonAction() {
        // Limpar todos os campos e voltar para a tela T05
    }
}