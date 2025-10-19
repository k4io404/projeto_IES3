package ClassesService;

import ClassesDAO.AcessoDAO;
import ClassesDTO.AcessoDTO;
import ClassesDTO.LocalControladoDTO;
import ClassesPuras.Acesso;
import ClassesPuras.LocalControlado;

import java.sql.SQLException;

public class AcessoService {

    private final AcessoDAO acessoDAO;

    public AcessoService(AcessoDAO acessoDAO) {
        this.acessoDAO = acessoDAO;
    }

    public int incluirAcesso(AcessoDTO acessoDTO) throws SQLException, IllegalArgumentException {

        if (acessoDTO == null){
            throw new IllegalArgumentException("O objeto acesso n達o pode ser nulo.");
        }

//        if (acesso.getData() == null){
//            throw new IllegalArgumentException("A data do acesso n達o pode ser nulo.");
//        }

        Acesso acesso = new Acesso(
                acessoDTO.getPesId(),
                acessoDTO.getLocId(),
                acessoDTO.getData(),
                acessoDTO.getTipoAcesso(),
                acessoDTO.getStatusAcesso()
        );

        try {
            return acessoDAO.incluirAcesso(acesso);

        } catch (SQLException e){
            System.err.println("Erro ao incluir acesso: " + e.getMessage());
            throw new SQLException("Erro ao incluir acesso no banco de dados.", e);
        }

    }

    public Acesso consultarAcesso(AcessoDTO acessoDTO) throws SQLException {

        if (acessoDTO == null){
            throw new IllegalArgumentException("O objeto acesso n達o pode ser nulo.");
        }


        Acesso acesso = new Acesso(
                acessoDTO.getId(),
                acessoDTO.getPesId(),
                acessoDTO.getLocId(),
                acessoDTO.getData(),
                acessoDTO.getTipoAcesso(),
                acessoDTO.getStatusAcesso()
        );

        try {
            return acessoDAO.consultarAcesso(acesso);
        } catch (SQLException e){
            System.err.println("Erro ao consultar o acessos: " + e.getMessage());
            throw new SQLException("Erro ao consultar o acesso no banco de dados.", e);
        }

    }

    public Acesso[] consultarAcessosPorLocal(LocalControladoDTO localControladoDTO) throws SQLException {

        if (localControladoDTO == null){
            throw new IllegalArgumentException("O objeto local n達o pode ser nulo.");
        }

        LocalControlado  localControlado = new LocalControlado(
                localControladoDTO.getId(),
                localControladoDTO.getNome()
        );

        try {
            return acessoDAO.consultarAcessosPorLocal(localControlado);
        } catch (SQLException e){
            System.err.println("Erro ao consultar o acessos por local: " + e.getMessage());
            throw new SQLException("Erro ao consultar o acesso por local no banco de dados.", e);
        }

    }

    public Acesso[] consultarAcessosPorPessoa(int id) throws SQLException {

        try {
            return acessoDAO.consultarAcessosPorPessoa(id);
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