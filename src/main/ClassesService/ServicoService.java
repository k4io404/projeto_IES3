package ClassesService;

import ClassesDAO.ServicoDAO;
import ClassesPuras.Morador;
import ClassesPuras.Prestador;
import ClassesPuras.Servico;

import java.sql.SQLException;

public class ServicoService {

    private final ServicoDAO servicoDAO;

    public ServicoService(ServicoDAO servicoDAO) {
        this.servicoDAO = servicoDAO;
    }

    public int incluirServico(Servico servico) throws SQLException, IllegalArgumentException {

        if (servico == null){
            throw new IllegalArgumentException("O objeto serviço não pode ser nulo.");
        }

        if (servico.getId() <= 0){
            throw new IllegalArgumentException("O ID do serviço deve ser um número positivo.");
        }

        if (servico.getServTipo() == null || servico.getServTipo().trim().isEmpty()){
            throw new IllegalArgumentException("O tipo de serviço não pode ser vazia.");
        }

        if (servico.getDataInicio() == null){
            throw new IllegalArgumentException("A data de início não pode ser vazia.");
        }

        try {

            Servico[] todosServico = servicoDAO.consultarServicos();

            for (Servico s : todosServico) {
                if (s.getId() == servico.getId()) {
                    throw new IllegalArgumentException("Já existe um servico cadastrada com o esse id: " + servico.getId());
                }
            }

            return servicoDAO.incluirServico(servico);
        } catch (SQLException e) {
            System.err.println("Erro ao incluir serviço: " + e.getMessage());
            throw new SQLException("Erro ao incluir serviço no banco de dados.", e);

        }


    }

    public boolean atualizarServico(Servico servico) throws SQLException, IllegalArgumentException {

        if (servico == null){
            throw new IllegalArgumentException("O objeto serviço não pode ser nulo.");
        }

        if (servico.getId() <= 0){
            throw new IllegalArgumentException("O ID do serviço deve ser um número positivo.");
        }

        if (servico.getServTipo() == null || servico.getServTipo().trim().isEmpty()){
            throw new IllegalArgumentException("O tipo de serviço não pode ser vazia.");
        }

        if (servico.getDataInicio() == null){
            throw new IllegalArgumentException("A data de início não pode ser vazia.");
        }

        try {
            Servico[] todosServico = servicoDAO.consultarServicos();

            for (Servico s : todosServico) {
                if (s.getId() == servico.getId()) {
                    return servicoDAO.atualizarServico(servico);
                }
            }
            throw new IllegalArgumentException("Não existe um servico cadastrada com o esse id: " + servico.getId());

        } catch (SQLException e) {
            System.err.println("Erro ao incluir serviço: " + e.getMessage());
            throw new SQLException("Erro ao incluir serviço no banco de dados.", e);

        }

    }

    public boolean deletarServico(Servico servico) throws SQLException, IllegalArgumentException {

        if (servico == null){
            throw new IllegalArgumentException("O objeto serviço não pode ser nulo.");
        }

        if (servico.getId() <= 0){
            throw new IllegalArgumentException("O ID do serviço deve ser um número positivo.");
        }

        if (servico.getServTipo() == null || servico.getServTipo().trim().isEmpty()){
            throw new IllegalArgumentException("O tipo de serviço não pode ser vazia.");
        }

        if (servico.getDataInicio() == null){
            throw new IllegalArgumentException("A data de início não pode ser vazia.");
        }

        try {
            Servico[] todosServico = servicoDAO.consultarServicos();

            for (Servico s : todosServico) {
                if (s.getId() == servico.getId()) {
                    return servicoDAO.deletarServico(servico);
                }
            }
            throw new IllegalArgumentException("Não existe um servico cadastrada com o esse id: " + servico.getId());

        } catch (SQLException e) {
            System.err.println("Erro ao incluir serviço: " + e.getMessage());
            throw new SQLException("Erro ao incluir serviço no banco de dados.", e);

        }
    }

    public Servico[] consultarServicos() throws SQLException {

        try {
            return servicoDAO.consultarServicos();
        } catch (SQLException e){
            System.err.println("Erro ao consultar os serviços: " + e.getMessage());
            throw new SQLException("Erro ao consultar os serviços no banco de dados.", e);
        }

    }

    public Servico[] consultarServicosMorador(Morador morador) throws SQLException {
        try {
            return servicoDAO.consultarServicosMorador(morador);
        } catch (SQLException e){
            System.err.println("Erro ao consultar os serviços por morador: " + e.getMessage());
            throw new SQLException("Erro ao consultar os serviços por morador no banco de dados.", e);
        }
    }

    public Servico[] consultarServicosPrestador(Prestador prestador) throws SQLException {
        try {
            return servicoDAO.consultarServicosPrestador(prestador);
        } catch (SQLException e){
            System.err.println("Erro ao consultar os serviços por prestador: " + e.getMessage());
            throw new SQLException("Erro ao consultar os serviços por prestador no banco de dados.", e);
        }
    }

}
