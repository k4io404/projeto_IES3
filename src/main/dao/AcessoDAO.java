package java.ClassesDAO;
import java.ClassesPuras.Acesso;
import java.ClassesPuras.StatusAcesso;
import java.ClassesPuras.TipoAcesso;
import java.sql.*;

public class AcessoDAO {

    // Gravar
    public int incluirAcesso(Acesso acesso) throws SQLException {

        String sql = "INSERT INTO ACESSOS (local_id, pessoa_id, acesso_data, acesso_tipo, acesso_status) VALUES (?,?,?,?,?)";

        // Abre e fecha o conector
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, acesso.getLocId());
            stmt.setInt(2, acesso.getPesId());
            stmt.setTimestamp(3, Timestamp.valueOf(acesso.getData()));
            stmt.setString(4, acesso.getTipoAcesso().name());

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
    // Atualizar - Retorna boolean
    public void atualizarAcesso(Acesso acesso) throws SQLException {
        // Não faz sentido poder atualizar um acesso...
        // Temos que discutir, não quero o FBI me acusando de abrigar o PocketNaro ou Mrs. Squid...
    }

    // Consultar
    public Acesso consultarVeiculo(Acesso acesso) throws SQLException {

        String sql = "SELECT * FROM ACESSOS WHERE acesso_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, acesso.getId());

            try (ResultSet rs = stmt.executeQuery()) {

                //ACESSOS
                // acesso_id INT
                // local_id INT
                // pessoa_id INT
                // acesso_data DATETIME
                // acesso_tipo ENUM(...)
                // acesso_status ENUM(...)

                // cursor mostra a linha n-1
                if (rs.next()) {
                    Acesso a = new Acesso();
                    a.setId(rs.getInt("acesso_id"));
                    a.setLocId(rs.getInt("local_id"));
                    a.setPesId(rs.getInt("pessoa_id"));
                    a.setData(rs.getDate("acesso_data"));
                    a.setTipoAcesso(TipoAcesso.valueOf(rs.getString("acesso_tipo")));
                    a.setStatusAcesso(StatusAcesso.valueOf(rs.getString("acesso_status")));
                    return a;
                }
                return null;
            }
        }
    }

    @Deprecated
    // Deletar - Retorna boolean
    public void deletarAcesso(Acesso acesso) throws SQLException {
        // Não faz sentido poder deletar um acesso...
        // Temos que discutir, não quero o FBI me acusando de abrigar o PocketNaro ou Mrs. Squid...
    }



}
