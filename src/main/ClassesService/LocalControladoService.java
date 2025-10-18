package ClassesService;

import ClassesDAO.LocalControladoDAO;
import ClassesPuras.LocalControlado;

import java.sql.SQLException;

public class LocalControladoService {

    private LocalControladoDAO localControladoDAO;

    public LocalControladoService() {
        this.localControladoDAO = new LocalControladoDAO();
    }

    public int incluirLocalControlado(LocalControlado localControlado) throws SQLException, IllegalArgumentException {

        if (localControlado == null) {
            throw new IllegalArgumentException("O objeto LocalControlado não pode ser nulo.");
        }
        if (localControlado.getNome() == null || localControlado.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do local controlado não pode ser vazio.");
        }

        if (localControlado.getId() <= 0){
            throw new IllegalArgumentException("O ID da pessoa associada ao veículo deve ser um número positivo.");
        }

        try {
            if (localControladoDAO.consultarLocalControlado(localControlado) != null) {
                throw new IllegalArgumentException("Já existe um local controlado com o nome: " + localControlado.getNome());
            }
            return localControladoDAO.incluirLocalControlado(localControlado);
        } catch (SQLException e){
            System.err.println("Erro ao incluir local controlado: " + e.getMessage());
            throw new SQLException("Erro ao incluir local controlado no banco de dados.", e);
        }

    }

    public boolean atualizarLocalControlado(LocalControlado localControlado) throws SQLException, IllegalArgumentException {

        if (localControlado == null) {
            throw new IllegalArgumentException("O objeto LocalControlado não pode ser nulo.");
        }
        if (localControlado.getNome() == null || localControlado.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do local controlado não pode ser vazio.");
        }

        if (localControlado.getId() <= 0){
            throw new IllegalArgumentException("O ID da pessoa associada ao veículo deve ser um número positivo.");
        }

        try {
            if (localControladoDAO.consultarLocalControlado(localControlado) == null) {
                throw new IllegalArgumentException("Não existe um local controlado com o nome: " + localControlado.getNome());
            }
            return localControladoDAO.atualizarLocalControlado(localControlado);

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar local controlado: " + e.getMessage());
            throw new SQLException("Erro ao atualizar local controlado no banco de dados.", e);
        }
    }

    public boolean deletarLocalControlado(LocalControlado localControlado) throws SQLException, IllegalArgumentException {

        if (localControlado == null) {
            throw new IllegalArgumentException("O objeto LocalControlado não pode ser nulo.");
        }
        if (localControlado.getNome() == null || localControlado.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do local controlado não pode ser vazio.");
        }

        if (localControlado.getId() <= 0){
            throw new IllegalArgumentException("O ID da pessoa associada ao veículo deve ser um número positivo.");
        }

        try {
            if (localControladoDAO.consultarLocalControlado(localControlado) == null){
                throw new IllegalArgumentException("Não existe um local controlado com o nome: " + localControlado.getNome());
            }
            return localControladoDAO.deletarLocalControlado(localControlado);

        } catch (SQLException e){
            System.err.println("Erro ao deletar local controlado: " + localControlado.getNome() + " - " + e.getMessage());
            throw new SQLException("Erro ao deletar local controlado do banco de dados.", e);
        }

    }

    public LocalControlado consultarLocalControlado(LocalControlado localControlado) throws SQLException {

        try {
            return localControladoDAO.consultarLocalControlado(localControlado);
        } catch (SQLException e){
            System.err.println("Erro ao consultar todos os locais controlados: " + e.getMessage());
            throw new SQLException("Erro ao consultar todos os locais controlados no banco de dados.", e);
        }
    }

    public LocalControlado[] consultarLocaisControladosGeral(LocalControlado localControlado) throws SQLException, IllegalArgumentException {

        try {
            return localControladoDAO.consultarLocaisControladosGeral(localControlado);
        } catch (SQLException e){
            System.err.println("Erro ao consultar todos os locais controlados: " + e.getMessage());
            throw new SQLException("Erro ao consultar todos os locais controlados no banco de dados.", e);
        }

    }

}
