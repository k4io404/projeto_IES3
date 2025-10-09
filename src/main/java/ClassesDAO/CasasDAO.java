package java.ClassesDAO;

import java.ClassesPuras.Casa;
import java.sql.*;

public class CasasDAO {

    // Gravar
    public int incluirCasa(Casa casa) throws SQLException {

        String sql = "INSERT INTO CASAS (casa_ender) VALUES (?)";

        // Abre e fecha o conector
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, casa.getEndereco());

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
    public Casa consultarCasa(Casa casa) throws SQLException {

        String sql = "SELECT * FROM CASAS WHERE casa_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, casa.getId());

            try (ResultSet rs = stmt.executeQuery()) {

                // cursor mostra a linha n-1
                if (rs.next()) {
                    Casa c = new Casa();
                    c.setId(rs.getInt("morador_id"));
                    return c;
                }
                return null;
            }
        }
    }


    @Deprecated
    // Cuidado, a discutir implementação. Pode causar prejuízos a coeerência do modelo
    // Atualizar - Retorna boolean
    public boolean atualizarCasa(Casa casa) throws SQLException {

        String sql = "UPDATE CASAS SET casa_ender=? WHERE casa_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, casa.getEndereco());
            stmt.setInt(2,casa.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    // Deletar - Retorna boolean
    public boolean deletarCasa(Casa casa) throws SQLException {
        String sql = "DELETE * FROM CASAS WHERE casa_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, casa.getId());

            return stmt.executeUpdate() > 0;
        }
    }

}
