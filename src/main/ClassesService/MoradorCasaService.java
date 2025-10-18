package ClassesService;

import ClassesDAO.MoradorCasaDAO;
import ClassesPuras.Casa;
import ClassesPuras.Morador;
import ClassesPuras.MoradorCasa;
import ClassesPuras.Pessoa;

import java.sql.SQLException;

public class MoradorCasaService {

    private final MoradorCasaDAO moradorCasaDAO;

    public MoradorCasaService(MoradorCasaDAO moradorCasaDAO) {
        this.moradorCasaDAO = moradorCasaDAO;
    }

    public int incluirMoradorCasa(MoradorCasa moradorCasa) throws SQLException, IllegalArgumentException {

        if (moradorCasa == null) {
            throw new IllegalArgumentException("O objeto moradorCasa não pode ser nulo.");
        }

        if (moradorCasa.getId() <= 0){
            throw new IllegalArgumentException("O ID da casa deve ser um número positivo.");
        }

        if (!moradorCasa.getTipoVinculo().equals("PROPRIETARIO") && !moradorCasa.getTipoVinculo().equals("INQUILINO") && !moradorCasa.getTipoVinculo().equals("DEPENDENTE")) {
            throw new IllegalArgumentException("O tipo de acesso deve ser 'PROPRIETARIO', 'INQUILINO' ou 'DEPENDENTE'.");
        }

        try {
            if (moradorCasaDAO.consultarMoradorCasa(moradorCasa) != null){
                throw new IllegalArgumentException("Já existe um moradorCasa com esse id: " + moradorCasa.getMoradorId());
            }

            return moradorCasaDAO.incluirMoradorCasa(moradorCasa);
        } catch (SQLException e){
            System.err.println("Erro ao incluir moradorCasa: " + e.getMessage());
            throw new SQLException("Erro ao incluir o moradorCasa no banco de dados.", e);
        }

    }

    public boolean atualizarMoradorCasa(MoradorCasa moradorCasa) throws SQLException, IllegalArgumentException {

        if (moradorCasa == null) {
            throw new IllegalArgumentException("O objeto moradorCasa não pode ser nulo.");
        }

        if (moradorCasa.getId() <= 0){
            throw new IllegalArgumentException("O ID da casa deve ser um número positivo.");
        }

        if (!moradorCasa.getTipoVinculo().equals("PROPRIETARIO") && !moradorCasa.getTipoVinculo().equals("INQUILINO") && !moradorCasa.getTipoVinculo().equals("DEPENDENTE")) {
            throw new IllegalArgumentException("O tipo de acesso deve ser 'PROPRIETARIO', 'INQUILINO' ou 'DEPENDENTE'.");
        }

        try {
            if (moradorCasaDAO.consultarMoradorCasa(moradorCasa) == null){
                throw new IllegalArgumentException("Não existe um moradorCasa com esse id: " + moradorCasa.getMoradorId());
            }

            return moradorCasaDAO.atualizarMoradorCasa(moradorCasa);
        } catch (SQLException e){
            System.err.println("Erro ao atualizar moradorCasa: " + e.getMessage());
            throw new SQLException("Erro ao atualizar o moradorCasa no banco de dados.", e);
        }

    }

    public boolean deletarMoradorCasa(MoradorCasa moradorCasa) throws SQLException, IllegalArgumentException {

        if (moradorCasa == null) {
            throw new IllegalArgumentException("O objeto moradorCasa não pode ser nulo.");
        }

        if (moradorCasa.getId() <= 0){
            throw new IllegalArgumentException("O ID da casa deve ser um número positivo.");
        }

        if (!moradorCasa.getTipoVinculo().equals("PROPRIETARIO") && !moradorCasa.getTipoVinculo().equals("INQUILINO") && !moradorCasa.getTipoVinculo().equals("DEPENDENTE")) {
            throw new IllegalArgumentException("O tipo de acesso deve ser 'PROPRIETARIO', 'INQUILINO' ou 'DEPENDENTE'.");
        }

        try {
            if (moradorCasaDAO.consultarMoradorCasa(moradorCasa) == null){
                throw new IllegalArgumentException("Não existe um moradorCasa com esse id: " + moradorCasa.getMoradorId());
            }

            return moradorCasaDAO.deletarMoradorCasa(moradorCasa);
        } catch (SQLException e){
            System.err.println("Erro ao deletar moradorCasa: " + e.getMessage());
            throw new SQLException("Erro ao deletar o moradorCasa no banco de dados.", e);
        }
    }

    public MoradorCasa consultarMoradorCasa(MoradorCasa moradorCasa) throws SQLException {

        try {
            return moradorCasaDAO.consultarMoradorCasa(moradorCasa);
        } catch (SQLException e){
            System.err.println("Erro ao consultar moradorCasa por ID: " + moradorCasa.getId() + " - " + e.getMessage());
            throw new SQLException("Erro ao consultar moradorCasa no banco de dados.", e);
        }

    }

    public Casa[] consultarCasasMorador(Morador morador) throws SQLException {

        try {
            return moradorCasaDAO.consultarCasasMorador(morador);
        } catch (SQLException e){
            System.err.println("Erro ao consultar moradorCasa por morador: " + morador.getId() + " - " + e.getMessage());
            throw new SQLException("Erro ao consultar moradorCasa por morador no banco de dados.", e);
        }

    }

    public Pessoa[] consultarMoradoresCasa(Casa casa) throws SQLException {

        try {
            return moradorCasaDAO.consultarMoradoresCasa(casa);
        } catch (SQLException e){
            System.err.println("Erro ao consultar moradorCasa por casa: " + casa.getId() + " - " + e.getMessage());
            throw new SQLException("Erro ao consultar moradorCasa por casa no banco de dados.", e);
        }
    }


}



