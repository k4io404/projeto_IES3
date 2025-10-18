package ClassesDAO;

import java.sql.SQLException;

public class TesteDAO {
    public static void main(String[] args) {
        PessoaDAO pessoaDAO = new PessoaDAO();
        try {
            System.out.println(pessoaDAO.consultarPessoaCpf("11111111111"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}