package java.ClassesDAO;
import java.ClassesPuras.Autorizacao;
import java.sql.*;

public class AutorizacoesDAO {

    // Gravar
    public int incluirAutorizacao(Autorizacao autorizacao) throws SQLException {

        String sql = "INSERT INTO AUTORIZACOES (pessoa_id, local_id, autorizacao) VALUES (?,?,?)";

        // Abre e fecha o conector
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, autorizacao.getPesId());
            stmt.setInt(2, autorizacao.getLocId());
            stmt.setInt(3, autorizacao.getAutorizacao());

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

    // Atualizar - Retorna boolean
    public boolean atualizarAutorizacao(Autorizacao autorizacao) throws SQLException {

        String sql = "UPDATE AUTORIZACOES SET autorizacao=? WHERE pessoa_id=? AND local_id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, autorizacao.getAutorizacao());
            stmt.setInt(2, autorizacao.getPesId());
            stmt.setInt(3, autorizacao.getLocId());

            return stmt.executeUpdate() > 0;
        }

    }

    // Consultar
    public Autorizacao consultarAutorizacao(Autorizacao autorizacao) throws SQLException {

        String sql = "SELECT * FROM AUTORIZACOES WHERE pessoa_id = ? AND local_id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, autorizacao.getPesId());
            stmt.setInt(2, autorizacao.getLocId());

            try (ResultSet rs = stmt.executeQuery()) {

                // cursor mostra a linha n-1
                if (rs.next()) {
                    Autorizacao a = new Autorizacao();
                    a.setPesId(rs.getInt("pessoa_id"));
                    a.setLocId(rs.getInt("local_id"));
                    a.setAutorizacao(rs.getInt("autorizacao"));
                    return a;
                }
                return null;
            }
        }
    }

    // Deletar - Retorna boolean
    public boolean deletarAutorizacao(Autorizacao autorizacao) throws SQLException {

        String sql = "DELETE * FROM AUTORIZACOES WHERE pessoa_id = ? AND local_id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, autorizacao.getPesId());
            stmt.setInt(2, autorizacao.getLocId());

            return stmt.executeUpdate() > 0;
        }
    }


}