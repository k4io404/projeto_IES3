package java.ClassesDAO;

import java.ClassesPuras.Visitante;
import java.ClassesPuras.Morador;
import java.sql.*;
import java.time.LocalDate;

public class VisitanteDAO {

    public String getTabela() {
        return "VISITANTES";
    }

    // Gravar
    public int incluirVisitante(Visitante visitante, Morador morador) throws SQLException {

        String sql = "INSERT INTO " + getTabela() + " (pessoa_id, morador_id, visit_data_autorizacao) VALUES (?, ?, ?)";

        // Abre e fecha o conector
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, visitante.getId());
            stmt.setInt(2, morador.getId());
            stmt.setDate(3, Date.valueOf(LocalDate.now()));

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

    // Consultar com base no Visitante
    public Visitante consultarVisitante(Visitante visitante) throws SQLException {

        String sql = "SELECT * FROM " + getTabela() + " WHERE pessoa_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, visitante.getId());

            try (ResultSet rs = stmt.executeQuery()) {

                // cursor mostra a linha n-1
                if (rs.next()) {
                    Visitante v = new Visitante();
                    v.setId(rs.getInt("pessoa_id"));
                    return v;
                }
                return null;
            }
        }
    }

    // Consultar com base no morador vinculado
    public Morador consultarVisitanteComBaseMorador(Morador morador) throws SQLException {

        String sql = "SELECT * FROM " + getTabela() + " WHERE morador_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, morador.getId());

            try (ResultSet rs = stmt.executeQuery()) {

                // cursor mostra a linha n-1
                if (rs.next()) {
                    Morador m = new Morador();
                    m.setId(rs.getInt("morador_id"));
                    return m;
                }
                return null;
            }
        }
    }

    // Consultar visitante com base no morador vinculado e visitante
    public Visitante consultarVisitanteMorador(int morador_id, int visitane_id ) throws SQLException {

        String sql = "SELECT * FROM " + getTabela() + " WHERE morador_id = ? and visitane_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, morador_id);
            stmt.setInt(2, visitane_id);

            try (ResultSet rs = stmt.executeQuery()) {
                // cursor mostra a linha n-1
                if (rs.next()) {
                    Visitante v = new Visitante();
                    v.setId(rs.getInt("visitante_id"));
                    return v;
                }
                return null;
            }
        }
    }


    // Atualizar
    public boolean atualizarVisitante(Visitante visitante, Morador morador) throws SQLException {

        String sql = "UPDATE " + getTabela() + " SET morador_id=? WHERE pessoa_id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, morador.getId());
            stmt.setInt(2, visitante.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    // Deletar - Retorna boolean
    public boolean deletarMorador(Visitante visitante) throws SQLException {
        String sql = "DELETE * FROM " + getTabela() + " WHERE pessoa_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, visitante.getId());

            return stmt.executeUpdate() > 0;
        }
    }

}