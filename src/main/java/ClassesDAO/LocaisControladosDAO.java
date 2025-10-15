package java.ClassesDAO;
import java.ClassesPuras.LocaisControlados;
import java.ClassesPuras.Pessoa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocaisControladosDAO {

    // Gravar
    public int incluirLocaisControlados(LocaisControlados locaisControlados) throws SQLException {

        String sql = "INSERT INTO LOCAIS_CONTROLADOS (local_nome) VALUES (?)";

        // Abre e fecha o conector
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, locaisControlados.getLocalNome());

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
    public boolean atualizarLocaisControlados(LocaisControlados locaisControlados) throws SQLException {

        String sql = "UPDATE LOCAIS_CONTROLADOS SET autorizacao=? WHERE local_nome=? ";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, locaisControlados.getLocalNome());


            return stmt.executeUpdate() > 0;
        }
    }

    // Consultar
    public LocaisControlados consultarLocaisControladosEspecificos(LocaisControlados locaisControlados) throws SQLException {

        String sql = "SELECT * FROM LOCAIS_CONTROLADOS WHERE local_nome = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, locaisControlados.getLocalNome());

            try (ResultSet rs = stmt.executeQuery()) {

                // cursor mostra a linha n-1
                if (rs.next()) {
                    LocaisControlados l = new LocaisControlados();
                    l.setLocalId(rs.getInt("local_id"));
                    l.setLocalNome(rs.getString("local_id"));
                    return l;
                }
                return null;
            }
        }
    }

    // Consultar
    public LocaisControlados[] consultarLocaisControladosGeral(LocaisControlados locaisControlados) throws SQLException {

        String sql = "SELECT * FROM LOCAIS_CONTROLADOS WHERE local_nome = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, locaisControlados.getLocalNome());

            try (ResultSet rs = stmt.executeQuery()) {

                List<LocaisControlados> listaLocaisControlados = new ArrayList<>();

                // cursor mostra a linha n-1
                while (rs.next()) {
                    LocaisControlados l = new LocaisControlados();
                    l.setLocalId(rs.getInt("local_id"));
                    l.setLocalNome(rs.getString("local_id"));
                    listaLocaisControlados.add(l);
                }
                return listaLocaisControlados.toArray(new LocaisControlados[0]);
            }
        }
    }

    // Deletar - Retorna boolean
    public boolean deletarLocaisControlados(LocaisControlados locaisControlados) throws SQLException {

        String sql = "DELETE * FROM LOCAIS_CONTROLADOS WHERE local_nome = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, locaisControlados.getLocalNome());

            return stmt.executeUpdate() > 0;
        }
    }

}
