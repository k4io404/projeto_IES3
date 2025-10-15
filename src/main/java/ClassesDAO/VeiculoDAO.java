package java.ClassesDAO;
import java.ClassesPuras.Acesso;
import java.ClassesPuras.Pessoa;
import java.ClassesPuras.Veiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    // Gravar
    public int incluirVeiculo(Veiculo veiculo) throws SQLException {

        String sql = "INSERT INTO VEICULOS (vei_placa, pessoa_id, vei_cor, vei_modelo) VALUES (?,?,?,?)";

        // Abre e fecha o conector
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, veiculo.getPlaca());
            stmt.setInt(2, veiculo.getPesId());
            stmt.setString(3, veiculo.getCor());
            stmt.setString(4, veiculo.getModelo());

            // Linha afetada pela ação
            int affectedRows = stmt.executeUpdate();

            // Cursos sempre fica um antes
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // id gerado
                }
            }

            // Retornar a linha afetada pela ação
            return affectedRows;
        }
    }

    @Deprecated
    // Cuidado, a discutir implementação. Alterar um ID único de morador pode causar prejuízos a coeerência do BD
    // Atualizar - Retorna boolean
    public boolean atualizarVeiculo(Veiculo veiculo) throws SQLException {

        String sql = "UPDATE VEICULOS SET vei_placa=?, pessoa_id=?, vei_cor=?, vei_modelo=? ";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, veiculo.getPlaca());
            stmt.setInt(2, veiculo.getPesId());
            stmt.setString(3, veiculo.getCor());
            stmt.setString(4,veiculo.getModelo());

            return stmt.executeUpdate() > 0;
        }
    }

    // Consultar
    public Veiculo consultarVeiculoEspecifico(Veiculo veiculo) throws SQLException {

        String sql = "SELECT * FROM VEICULOS WHERE vei_placa = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, veiculo.getPlaca());

            try (ResultSet rs = stmt.executeQuery()) {

                // VEICULOS
                // vei_placa VARCHAR(8)
                // pessoa_id INT
                // vei_cor VARCHAR(20)
                // vei_modelo VARCHAR(40


                // cursor mostra a linha n-1
                if (rs.next()) {
                    Veiculo v = new Veiculo();
                    v.setPlaca(rs.getString("vei_placa"));
                    v.setPesId(rs.getInt("pessoa_id"));
                    v.setCor(rs.getString("vei_cor"));
                    v.setModelo(rs.getString("vei_modelo"));
                    return v;
                }
                return null;
            }
        }
    }

    // Consultar Veiculo Geral
    public Veiculo[] consultarVeiculoGeral(Veiculo veiculo) throws SQLException {

        String sql = "SELECT * FROM VEICULOS";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, veiculo.getPlaca());

            try (ResultSet rs = stmt.executeQuery()) {

                List<Veiculo> listaVeiculos = new ArrayList<>();

                // cursor mostra a linha n-1
                while (rs.next()) {
                    Veiculo v = new Veiculo();
                    v.setPlaca(rs.getString("vei_placa"));
                    v.setPesId(rs.getInt("pessoa_id"));
                    v.setCor(rs.getString("vei_cor"));
                    v.setModelo(rs.getString("vei_modelo"));
                    listaVeiculos.add(v);
                }
                return listaVeiculos.toArray(new Veiculo[0]);
            }
        }
    }

    // Consultar Veiculo por Tipo Responsavel
    public Veiculo[] consultarVeiculoResponsavel(Pessoa pessoa, String tipo) throws SQLException {

        String sql;

        if(tipo.equalsIgnoreCase("MORADOR")){
            sql = "SELECT A.* FROM PESSOAS A LEFT JOIN MORADOR B ON A.pessoa_id = B.pessoa_id";
        } else if (tipo.equalsIgnoreCase("PRESTADOR")){
            sql = "SELECT A.* FROM PESSOAS A LEFT JOIN PRESTADOR B ON A.pessoa_id = B.pessoa_id";
        } else if(tipo.equalsIgnoreCase("VISITANTE")){
            sql = "SELECT A.* FROM PESSOAS A LEFT JOIN VISITANTE B ON A.pessoa_id = B.pessoa_id";
        } else {
            throw new IllegalArgumentException("Tipo de pessoa inválido: " + tipo);
        }

        Veiculo veiculo = new Veiculo() ;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, veiculo.getPlaca());

            try (ResultSet rs = stmt.executeQuery()) {

                List<Veiculo> listaVeiculos = new ArrayList<>();

                // cursor mostra a linha n-1
                while (rs.next()) {
                    Veiculo v = new Veiculo();
                    v.setPlaca(rs.getString("vei_placa"));
                    v.setPesId(rs.getInt("pessoa_id"));
                    v.setCor(rs.getString("vei_cor"));
                    v.setModelo(rs.getString("vei_modelo"));
                    listaVeiculos.add(v);
                }
                return listaVeiculos.toArray(new Veiculo[0]);
            }
        }
    }

    // Deletar
    public boolean deletarServico(Veiculo veiculo) throws SQLException {
        String sql = "DELETE * FROM VEICULOS WHERE vei_placa = ?";
        try (Connection conn = ConnectionFactory.getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, veiculo.getPlaca());

            return stmt.executeUpdate() > 0;
        }
    }


}
