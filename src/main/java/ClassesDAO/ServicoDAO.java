package java.ClassesDAO;
import java.ClassesPuras.Servico;
import java.sql.*;

public class ServicoDAO {

    // Gravar
    public int incluirServico(Servico servico) throws SQLException {

        String sql = "INSERT INTO SERVICOS (morador_Id, prestador_id, serv_tipo, data_inicio, data_fim) VALUES (?,?,?,?,?)";

        // Abre e fecha o conector
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, servico.getMorId());
            stmt.setInt(2, servico.getPrestId());
            stmt.setString(3, servico.getServTipo());
            stmt.setDate(4, (Date) servico.getDataInicio());
            stmt.setDate(5, (Date) servico.getDataFim());

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

    // Consultar
    public Servico consultarServico(Servico servico) throws SQLException {

        String sql = "SELECT * FROM SERVICOS WHERE serv_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, servico.getId());

            try (ResultSet rs = stmt.executeQuery()) {

                // cursor mostra a linha n-1
                if (rs.next()) {
                    Servico s = new Servico();
                    s.setId(rs.getInt("serv_id"));
                    s.setMorId(rs.getInt("morador_id"));
                    s.setPrestId(rs.getInt("prestador_id"));
                    s.setServTipo(rs.getString("serv_tipo"));
                    s.setDataInicio(rs.getDate("data_inicio"));
                    s.setDataFim(rs.getDate("data_fim"));
                    return s;
                }
                return null;
            }
        }
    }

    // Atualizar - Retorna boolean
    public boolean atualizarServico(Servico servico) throws SQLException {

        String sql = "UPDATE SERVICOS SET morador_id=?, prestador_id=?, serv_tipo=?, data_inicio=?,data_fim=? WHERE serv_id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, servico.getMorId());
            stmt.setInt(2, servico.getPrestId());
            stmt.setString(3, servico.getServTipo());
            stmt.setDate(4, (Date) servico.getDataInicio());
            stmt.setDate(4, (Date) servico.getDataFim());
            stmt.setInt(5, servico.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    // Deletar - Retorna boolean
    public boolean deletarServico(Servico servico) throws SQLException {
        String sql = "DELETE * FROM SERVICOS WHERE serv_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, servico.getId());

            return stmt.executeUpdate() > 0;
        }
    }
}
