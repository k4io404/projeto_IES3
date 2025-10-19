package ClassesDAO;

import ClassesPuras.Morador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TesteMoradoDAO {

    public static void main(String[] args) {
        MoradorDAO dao = new MoradorDAO();
        int passed = 0, failed = 0;

        String uniqueCpf = gerarCpfUnico();
        String uniqueName = "TesteMorador_" + System.nanoTime();
        Date dataNasc = parseData("01/01/1990");

        Morador m = new Morador();
        m.setNome(uniqueName);
        m.setCpf(uniqueCpf);
        m.setDataNasc(dataNasc);
        m.setTelefone("99999-2222");
        m.setEmail("morador.test@example.com");

        // 1) incluirMorador
        try {
            int retorno = dao.incluirMorador(m);
            // buscar pelo cpf para recuperar id real em PESSOAS
            try {
                ClassesPuras.Pessoa pessoa = new PessoaDAO().consultarPessoaCpf(uniqueCpf);
                if (pessoa != null) {
                    m.setId(pessoa.getId());
                }
            } catch (Exception e) {}

            if (m.getId() > 0 || retorno > 0) {
                System.out.println("[PASS] incluirMorador -> inserido com pessoa_id=" + m.getId());
                passed++;
            } else {
                System.err.println("[FAIL] incluirMorador -> não inserido");
                failed++;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] incluirMorador: " + e.getMessage());
            failed++;
        }

        // 2) atualizarMorador
        try {
            m.setTelefone("88888-3333");
            boolean ok = dao.atualizarMorador(m);
            if (ok) {
                System.out.println("[PASS] atualizarMorador -> método retornou true");
                passed++;
            } else {
                System.err.println("[FAIL] atualizarMorador -> método retornou false");
                failed++;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] atualizarMorador: " + e.getMessage());
            failed++;
        }

        // 3) deletarMorador (soft delete via PessoaDAO)
        try {
            boolean apagou = dao.deletarMorador(m);
            if (!apagou) {
                System.err.println("[FAIL] deletarMorador -> método retornou false");
                failed++;
            } else {
                boolean ativa = lerAtivaNoBanco(m.getId());
                if (!ativa) {
                    System.out.println("[PASS] deletarMorador (soft) -> pessoa_ativa = false");
                    passed++;
                } else {
                    System.err.println("[FAIL] deletarMorador -> pessoa_ativa continuou true");
                    failed++;
                }
                boolean reativou = dao.reincluirPessoa(m);
                boolean ativaDepois = lerAtivaNoBanco(m.getId());
                if (reativou && ativaDepois) {
                    System.out.println("[PASS] reincluirPessoa -> pessoa_ativa = true após reinclusão");
                    passed++;
                } else {
                    System.err.println("[FAIL] reincluirPessoa -> não reativou corretamente");
                    failed++;
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] deletarMorador/reincluir: " + e.getMessage());
            failed++;
        }

        // Cleanup: remover registros de MORADORES e PESSOAS
        try (Connection conn = ConnectionFactory.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM MORADORES WHERE morador_id = ?")) {
                ps.setInt(1, m.getId());
                int d1 = ps.executeUpdate();
                System.out.println("Cleanup MORADORES deletados = " + d1);
            } catch (SQLException e) {
                System.err.println("Cleanup MORADORES falhou: " + e.getMessage());
            }

            try (PreparedStatement ps2 = conn.prepareStatement("DELETE FROM PESSOAS WHERE pessoa_cpf = ?")) {
                ps2.setString(1, uniqueCpf);
                int d2 = ps2.executeUpdate();
                System.out.println("Cleanup PESSOAS deletados = " + d2);
            } catch (SQLException e) {
                System.err.println("Cleanup PESSOAS falhou: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] cleanup conexão: " + e.getMessage());
        }

        System.out.println("\n==== Resumo MoradorDAO ====");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("=============================");

        if (failed > 0) System.exit(2);
        else System.exit(0);
    }

    private static String gerarCpfUnico() {
        long now = System.nanoTime();
        return String.format("%011d", Math.abs(now % 100000000000L));
    }

    private static Date parseData(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            return new Date();
        }
    }

    private static boolean lerAtivaNoBanco(int pessoaId) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT pessoa_ativa FROM PESSOAS WHERE pessoa_id = ?")) {
            ps.setInt(1, pessoaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBoolean("pessoa_ativa");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao ler pessoa_ativa: " + e.getMessage());
        }
        return true;
    }
}
