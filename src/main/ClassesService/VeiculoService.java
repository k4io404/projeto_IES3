package ClassesService;

import ClassesDAO.VeiculoDAO;
import ClassesPuras.Veiculo;

import java.sql.SQLException;

public class VeiculoService {

    private final VeiculoDAO veiculoDAO;

    public VeiculoService(VeiculoDAO veiculoDAO) {
        this.veiculoDAO = veiculoDAO;
    }

    public int incluirVeiculo(Veiculo veiculo) throws SQLException, IllegalArgumentException {

        // Validar objeto veículo
        if (veiculo == null){
            throw new IllegalArgumentException("O objeto veículo não pode ser nulo.");
        }

        // Validar placa do veículo
        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()){
            throw new IllegalArgumentException("A placa do veículo não pode ser vazia.");
        }

        // Validar id do veículo
        if (veiculo.getPesId() <= 0){
            throw new IllegalArgumentException("O ID da pessoa associada ao veículo deve ser um número positivo.");
        }

        try {
            // Validar se já existe um veículo com a mesma placa
            if (veiculoDAO.consultarVeiculo(veiculo) != null){
                throw new IllegalArgumentException("Já existe um veículo cadastrado com a placa: " + veiculo.getPlaca());
            }
            return veiculoDAO.incluirVeiculo(veiculo);
        } catch (SQLException e){
            System.err.println("Erro ao incluir veículo: " + e.getMessage());
            throw new SQLException("Erro ao incluir veículo no banco de dados.", e);
        }

    }

    public boolean atualizarVeiculo(Veiculo veiculo) throws SQLException, IllegalArgumentException {

        // Validar objeto veículo
        if (veiculo == null) {
            throw new IllegalArgumentException("O objeto veículo não pode ser nulo.");
        }

        // Validar placa do veículo
        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("A placa do veículo não pode ser vazia.");
        }

        // Validar id do veículo
        if (veiculo.getPesId() <= 0) {
            throw new IllegalArgumentException("O ID da pessoa associada ao veículo deve ser um número positivo.");
        }

        try {
            // Validar se já existe um veículo com a mesma placa
            if (veiculoDAO.consultarVeiculo(veiculo) == null) {
                throw new IllegalArgumentException("Não existe um veículo cadastrado com a placa: " + veiculo.getPlaca());
            }
            return veiculoDAO.atualizarVeiculo(veiculo);
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar veículo: " + e.getMessage());
            throw new SQLException("Erro ao atualizar veículo no banco de dados.", e);
        }

    }

    public boolean deletarVeiculo(Veiculo veiculo) throws SQLException, IllegalArgumentException {

        // Validar objeto veículo
        if (veiculo == null) {
            throw new IllegalArgumentException("O objeto veículo não pode ser nulo.");
        }

        // Validar placa do veículo
        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("A placa do veículo não pode ser vazia.");
        }

        // Validar id do veículo
        if (veiculo.getPesId() <= 0) {
            throw new IllegalArgumentException("O ID da pessoa associada ao veículo deve ser um número positivo.");
        }

        try {
            // Validar se já existe um veículo com a mesma placa
            if (veiculoDAO.consultarVeiculo(veiculo) == null) {
                throw new IllegalArgumentException("Não existe um veículo cadastrado com a placa: " + veiculo.getPlaca());
            }
            return veiculoDAO.deletarVeiculo(veiculo);
        } catch (SQLException e){
            System.err.println("Erro ao deletar veículo: " + veiculo.getPlaca() + " - " + e.getMessage());
            throw new SQLException("Erro ao deletar veículo do banco de dados.", e);
        }

    }

    public Veiculo consultarVeiculo(Veiculo veiculo) throws SQLException, IllegalArgumentException {

        // Validar objeto veículo
        if (veiculo == null){
            throw new IllegalArgumentException("O objeto veículo não pode ser nulo.");
        }

        // Validar placa do veículo
        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()){
            throw new IllegalArgumentException("A placa do veículo não pode ser vazia.");
        }

        // Validar id do veículo
        if (veiculo.getPesId() <= 0){
            throw new IllegalArgumentException("O ID da pessoa associada ao veículo deve ser um número positivo.");
        }

        try {
            return veiculoDAO.consultarVeiculo(veiculo);
        } catch (SQLException e) {
            System.err.println("Erro ao consultar veículo pela placa: " + veiculo.getPlaca() + " - " + e.getMessage());
            throw new SQLException("Erro ao consultar veículo no banco de dados.", e);
        }

    }

    public Veiculo[] consultarVeiculosGeral() throws SQLException {
        try {
            return veiculoDAO.consultarVeiculosGeral();
        } catch (SQLException e){
            System.err.println("Erro ao consultar todos os veículos: " + e.getMessage());
            throw new SQLException("Erro ao consultar todos os veículos no banco de dados.", e);
        }
    }

    public Veiculo[] consultarVeiculosPorTipoPessoa(String tipo) throws SQLException, IllegalArgumentException {

        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo de pessoa não pode ser vazio.");
        }

        String tipoFormatado = tipo.toUpperCase();
        if (!tipoFormatado.equals("MORADOR") && !tipoFormatado.equals("PRESTADOR") && !tipoFormatado.equals("VISITANTE")) {
            throw new IllegalArgumentException("Tipo de pessoa inválido. Use MORADOR, PRESTADOR ou VISITANTE.");
        }

        try {
            return veiculoDAO.consultarVeiculosPorTipoPessoa(tipo);
        } catch (SQLException e){
            System.err.println("Erro ao consultar veículos por tipo de pessoa: " + tipo + " - " + e.getMessage());
            throw new SQLException("Erro ao consultar veículos por tipo de pessoa no banco de dados.", e);
        }


    }

}
