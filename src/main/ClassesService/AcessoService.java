package ClassesService;

import ClassesDAO.AcessoDAO;
import ClassesPuras.Acesso;
import ClassesPuras.LocalControlado;
import ClassesPuras.Pessoa;

import java.sql.SQLException;

public class AcessoService {

    private final AcessoDAO acessoDAO;

    public AcessoService(AcessoDAO acessoDAO) {
        this.acessoDAO = acessoDAO;
    }

    public int incluirAcesso(Acesso acesso) throws SQLException, IllegalArgumentException {

        if (acesso == null){
            throw new IllegalArgumentException("O objeto acesso não pode ser nulo.");
        }

        if (acesso.getId() <= 0){
            throw new IllegalArgumentException("O ID da pessoa associada ao acesso deve ser um número positivo.");
        }

        if (acesso.getData() == null){
            throw new IllegalArgumentException("A data do acesso não pode ser nulo.");
        }

        if (!acesso.getTipoAcesso().equals("SAIDA") && !acesso.getTipoAcesso().equals("ENTRADA")) {
            throw new IllegalArgumentException("O tipo de acesso deve ser 'SAIDA' ou 'ENTRADA'.");
        }

        try {
            if (acessoDAO.consultarAcesso(acesso) != null){
                throw new IllegalArgumentException("Já existe um acesso cadastrado com esse id.");
            }
            return acessoDAO.incluirAcesso(acesso);

        } catch (SQLException e){
            System.err.println("Erro ao incluir acesso: " + e.getMessage());
            throw new SQLException("Erro ao incluir acesso no banco de dados.", e);
        }

    }

    public Acesso consultarAcesso(Acesso acesso) throws SQLException {

        try {
            return acessoDAO.consultarAcesso(acesso);
        } catch (SQLException e){
            System.err.println("Erro ao consultar o acessos: " + e.getMessage());
            throw new SQLException("Erro ao consultar o acesso no banco de dados.", e);
        }

    }

    public Acesso[] consultarAcessosPorLocal(LocalControlado local) throws SQLException {
        try {
            return acessoDAO.consultarAcessosPorLocal(local);
        } catch (SQLException e){
            System.err.println("Erro ao consultar o acessos por local: " + e.getMessage());
            throw new SQLException("Erro ao consultar o acesso por local no banco de dados.", e);
        }

    }

    public Acesso[] consultarAcessosPorPessoa(Pessoa pessoa) throws SQLException {
        try {
            return acessoDAO.consultarAcessosPorPessoa(pessoa);
        } catch (SQLException e){
            System.err.println("Erro ao consultar o acessos por pessoa: " + e.getMessage());
            throw new SQLException("Erro ao consultar o acesso por pessoa no banco de dados.", e);
        }
    }

    public Acesso[] consultarAcessosOrderData( ) throws SQLException {
        try {
            return acessoDAO.consultarAcessosOrderData();
        } catch (SQLException e){
            System.err.println("Erro ao consultar o acessos por data: " + e.getMessage());
            throw new SQLException("Erro ao consultar o acesso por data no banco de dados.", e);
        }
    }

}
