package ClassesDAO;

import java.sql.SQLException;


public class TestePessoaDAO {
    public static void main(String[] args) {

        // Leitura CPF
        PessoaDAO pessoaDAOLeituraCpf = new PrestadorDAO();
        try {
            System.out.println("----------------------------");
            System.out.println("Teste leitura por CPF");
            System.out.println(pessoaDAOLeituraCpf.consultarPessoaCpf("11111111111").toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Leitura Nome
        PessoaDAO pessoaDAOLeituraNome = new PrestadorDAO();
        try {
            System.out.println("----------------------------");
            System.out.println("Teste leitura por Nome");
            System.out.println(pessoaDAOLeituraNome.consultarPessoaNome("Ana Souza").toString());
            System.out.println("----------------------------");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}