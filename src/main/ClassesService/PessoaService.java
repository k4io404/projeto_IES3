package ClassesService;

import ClassesDAO.PessoaDAO;
import ClassesPuras.Pessoa;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PessoaService {

    private final PessoaDAO pessoaDAO;

    public PessoaService() {
        this.pessoaDAO = new PessoaDAO();
    }

    // Incluir pessoa com validação
    public int incluirPessoa(Pessoa pessoa, String tipoPessoa) throws SQLException, IllegalArgumentException{

        if (pessoa == null) {
            throw new IllegalArgumentException("O objeto Pessoa não pode ser nulo.");
        }
        if (pessoa.getNome() == null || pessoa.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da pessoa não pode ser vazio.");
        }
        if (pessoa.getCpf() == null || !isValidCPF(pessoa.getCpf())) {
            throw new IllegalArgumentException("CPF inválido ou vazio.");
        }
        if (pessoa.getEmail() == null || !isValidEmail(pessoa.getEmail())) {
            throw new IllegalArgumentException("E-mail inválido ou vazio.");
        }
        if (pessoa.getTelefone() == null || pessoa.getTelefone().trim().isEmpty()) {
            throw new IllegalArgumentException("O telefone da pessoa não pode ser vazio.");
        }
        if (pessoa.getDataNasc() == null) {
            throw new IllegalArgumentException("A data de nascimento da pessoa não pode ser nula.");
        }

        try {

            if (pessoaDAO.consultarPessoaCpf(pessoa.getCpf()) != null){
                throw new IllegalArgumentException("Já existe uma pessoa cadastrada com o CPF: " + pessoa.getCpf());
            }

            Pessoa[] todasPessoas = pessoaDAO.consultarPessoasGeral();
            for (Pessoa p : todasPessoas) {
                if (p.getEmail() != null && p.getEmail().equalsIgnoreCase(pessoa.getEmail())) {
                    throw new IllegalArgumentException("Já existe uma pessoa cadastrada com o e-mail: " + pessoa.getEmail());
                }
            }

           // return pessoaDAO.incluirPessoa(pessoa);
            return 1;
        } catch (SQLException e){
            System.err.println("Erro ao incluir pessoa: " + e.getMessage());
            throw new SQLException("Erro ao incluir pessoa no banco de dados.", e);
        }


    }

    public boolean atualizarPessoa(Pessoa pessoa) throws SQLException {
        return true;
    }



    private boolean isValidCPF(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}