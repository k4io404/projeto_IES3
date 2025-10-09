package java.ClassesDAO;

import java.ClassesPuras.*;
import java.sql.*;

public class MoradorCasaDAO {

    // Gravar
    public int incluirMoradorCasa(MoradorCasa moradorCasa) throws SQLException {

        String sql = "INSERT INTO MORADOR_CASA (morador_Id, casa_id, tipo_vinculo ) VALUES (?,?,?)";

        // Abre e fecha o conector
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, moradorCasa.getMoradorId());
            stmt.setInt(2, moradorCasa.getCasaId());
            stmt.setString(3,  moradorCasa.getTipoVinculo().name());

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
    public MoradorCasa consultarMoradorCasa(MoradorCasa moradorCasa) throws SQLException {

        String sql = "SELECT * FROM MORADOR_CASA WHERE casa_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, moradorCasa.getCasaId());

            try (ResultSet rs = stmt.executeQuery()) {

                // cursor mostra a linha n-1
                if (rs.next()) {
                    MoradorCasa mc = new MoradorCasa();
                    mc.setMoradorId(rs.getInt("morador_Id"));
                    mc.setCasaId(rs.getInt("casa_id"));
                    mc.setTipoVinculo(TipoVinculo.valueOf(rs.getString("tipo_vinculo")));
                    return mc;
                }
                return null;
            }
        }
    }



    // Atualizar - Retorna boolean
    public boolean atualizarMoradorCasa(MoradorCasa moradorCasa) throws SQLException {

        String sql = "UPDATE MORADOR_CASA SET morador_Id=?, casa_id=?, tipo_vinculo=? WHERE casa_id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, moradorCasa.getMoradorId());
            stmt.setInt(2, moradorCasa.getCasaId());
            stmt.setString(3, moradorCasa.getTipoVinculo().name());
            stmt.setInt(4, moradorCasa.getCasaId());

            return stmt.executeUpdate() > 0;
        }
    }

    // Deletar - Retorna boolean
    public boolean deletarMoradorCasa(MoradorCasa moradorCasa) throws SQLException {
        String sql = "DELETE * FROM MORADOR_CASA WHERE morador_Id = ? and casa_id=? ";
        try (Connection conn = ConnectionFactory.getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, moradorCasa.getMoradorId());
            stmt.setInt(2, moradorCasa.getCasaId());

            return stmt.executeUpdate() > 0;
        }
    }

}
