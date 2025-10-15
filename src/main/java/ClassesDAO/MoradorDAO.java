package java.ClassesDAO;

import java.ClassesPuras.Morador;
import java.sql.*;

public class MoradorDAO  {

    // Gravar
    public int incluirMorador(Morador morador) throws SQLException {

        String sql = "INSERT INTO MORADORES (morador_id) VALUES (?)";

        // Abre e fecha o conector
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, morador.getId());

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
    public boolean atualizarMorador(Morador morador) throws SQLException {

        String sql = "UPDATE MORADORES SET morador_id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, morador.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    // Deletar - Retorna boolean
    public boolean deletarMorador(Morador morador) throws SQLException {
        String sql = "DELETE * FROM MORADORES WHERE pessoa_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, morador.getId());

            return stmt.executeUpdate() > 0;
        }
    }
}