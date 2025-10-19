package ClassesService;

import ClassesDAO.CasaDAO;
import ClassesPuras.Casa;

import java.sql.SQLException;

public class CasaService {

    private final CasaDAO casaDAO;

    public CasaService() {
        this.casaDAO = new CasaDAO();
    }

    public int incluirCasa(Casa casa) throws IllegalArgumentException, SQLException {
        if (casa == null) {
            throw new IllegalArgumentException("O objeto Casa não pode ser nulo.");
        }

        if (casa.getEndereco() == null || casa.getEndereco().trim().isEmpty()) {
            throw new IllegalArgumentException("O endereço da casa não pode ser vazio.");
        }

        if (casa.getId() <= 0){
            throw new IllegalArgumentException("O ID da casa deve ser um número positivo.");
        }

        try {
            if (casaDAO.consultarCasa(casa) != null){
                throw new IllegalArgumentException("Já existe uma casa com o endereço: " + casa.getEndereco());
            }
            return casaDAO.incluirCasa(casa);
        } catch (SQLException e){
            System.err.println("Erro ao incluir casa: " + e.getMessage());
            throw new SQLException("Erro ao incluir casa no banco de dados.", e);
        }


    }

    public boolean deletarCasa(Casa casa) throws SQLException {

        if (casa == null) {
            throw new IllegalArgumentException("O objeto Casa não pode ser nulo.");
        }

        if (casa.getEndereco() == null || casa.getEndereco().trim().isEmpty()) {
            throw new IllegalArgumentException("O endereço da casa não pode ser vazio.");
        }

        if (casa.getId() <= 0){
            throw new IllegalArgumentException("O ID da casa deve ser um número positivo.");
        }

        try {
            if (casaDAO.consultarCasa(casa) == null){
                throw new IllegalArgumentException("Não existe uma casa com o endereço: " + casa.getEndereco());
            }
            return casaDAO.deletarCasa(casa);
        } catch (SQLException e){
            System.err.println("Erro ao excluir casa: " + e.getMessage());
            throw new SQLException("Erro ao excluir casa no banco de dados.", e);
        }
    }

    public Casa consultarCasa(Casa casa) throws SQLException {

        try {
            return casaDAO.consultarCasa(casa);
        } catch (SQLException e){
            System.err.println("Erro ao consultar casa por ID: " + casa.getId() + " - " + e.getMessage());
            throw new SQLException("Erro ao consultar casa no banco de dados.", e);
        }

    }

    public Casa[] consultarCasaGeral(Casa casa) throws SQLException {
        try {
            return casaDAO.consultarCasaGeral(casa);

        } catch (SQLException e){
            System.err.println("Erro ao consultar todas as casas: " + casa.getId() + " - " + e.getMessage());
            throw new SQLException("Erro ao consultar todas as casas no banco de dados.", e);
        }
    }



}
