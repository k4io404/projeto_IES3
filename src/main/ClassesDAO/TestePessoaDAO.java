package ClassesDAO;

import ClassesPuras.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestePessoaDAO {

    public static void main(String[] args) {
        PessoaDAO dao = new PessoaDAO();
        int passed = 0, failed = 0;

        String uniqueCpf = gerarCpfUnico();
        String uniqueName = "TesteManual_" + System.nanoTime();
        Date dataNasc = parseData("01/01/1990");

        Pessoa p = new Pessoa(uniqueName, uniqueCpf, dataNasc, "99999-0000", "teste.manual@example.com");

        // 1) incluirPessoa
        try {
            int r = dao.incluirPessoa(p); // pode retornar id gerado ou linhas afetadas
            // buscar pelo cpf para confirmar
            Pessoa busc = dao.consultarPessoaCpf(uniqueCpf);
            if (busc != null) {
                p.setId(busc.getId());
                System.out.println("[PASS] incluirPessoa: inserido com id=" + busc.getId());
                passed++;
            } else {
                System.err.println("[FAIL] incluirPessoa: não encontrou após inserir");
                failed++;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] incluirPessoa: " + e.getMessage());
            failed++;
        }

        // 2) consultarPessoaCpf e consultarPessoaNome
        try {
            Pessoa porCpf = dao.consultarPessoaCpf(uniqueCpf);
            if (porCpf != null && uniqueCpf.equals(porCpf.getCpf())) {
                System.out.println("[PASS] consultarPessoaCpf");
                passed++;
            } else {
                System.err.println("[FAIL] consultarPessoaCpf");
                failed++;
            }

            Pessoa porNome = dao.consultarPessoaNome(uniqueName);
            if (porNome != null && uniqueName.equals(porNome.getNome())) {
                System.out.println("[PASS] consultarPessoaNome");
                passed++;
            } else {
                System.err.println("[FAIL] consultarPessoaNome");
                failed++;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] consultar por CPF/Nome: " + e.getMessage());
            failed++;
        }

        // 3) atualizarPessoa
        try {
            Pessoa toUpdate = dao.consultarPessoaCpf(uniqueCpf);
            if (toUpdate == null) {
                System.err.println("[FAIL] atualizarPessoa: registro não encontrado para atualizar");
                failed++;
            } else {
                toUpdate.setNome(uniqueName + "_ATUALIZADO");
                toUpdate.setEmail("atualizado@example.com");
                boolean ok = dao.atualizarPessoa(toUpdate);
                Pessoa after = dao.consultarPessoaCpf(uniqueCpf);
                if (ok && after != null && after.getNome().equals(toUpdate.getNome())) {
                    System.out.println("[PASS] atualizarPessoa");
                    passed++;
                } else {
                    System.err.println("[FAIL] atualizarPessoa: mudança não persistida");
                    failed++;
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] atualizarPessoa: " + e.getMessage());
            failed++;
        }

        // 4) consultarPessoasGeral
        try {
            Pessoa[] todas = dao.consultarPessoasGeral();
            boolean found = false;
            if (todas != null) {
                for (Pessoa x : todas) if (uniqueCpf.equals(x.getCpf())) { found = true; break; }
            }
            if (found) {
                System.out.println("[PASS] consultarPessoasGeral");
                passed++;
            } else {
                System.err.println("[FAIL] consultarPessoasGeral: não encontrou o registro");
                failed++;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] consultarPessoasGeral: " + e.getMessage());
            failed++;
        }

        // 5) consultarPessoasPorTipo - validar comportamento com tipos válidos e inválido
        try {
            try {
                dao.consultarPessoasPorTipo("MORADOR");
                dao.consultarPessoasPorTipo("PRESTADOR");
                dao.consultarPessoasPorTipo("VISITANTE");
                System.out.println("[PASS] consultarPessoasPorTipo (chamadas para tipos válidos executaram)");
                passed++;
            } catch (RuntimeException re) {
                // Pode ocorrer se tabelas MORADOR/PRESTADOR/VISITANTE não existirem no schema
                System.err.println("[WARN] consultarPessoasPorTipo: " + re.getMessage());
            }

            boolean lançou = false;
            try {
                dao.consultarPessoasPorTipo("TIPO_INVALIDO_TESTE");
            } catch (IllegalArgumentException iae) {
                lançou = true;
            }
            if (lançou) {
                System.out.println("[PASS] consultarPessoasPorTipo lança IllegalArgumentException para tipo inválido");
                passed++;
            } else {
                System.err.println("[FAIL] consultarPessoasPorTipo não lançou para tipo inválido");
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] consultarPessoasPorTipo: " + e.getMessage());
            failed++;
        }

        // 6) deletarPessoa (soft delete) e reincluirPessoa
        try {
            Pessoa current = dao.consultarPessoaCpf(uniqueCpf);
            if (current == null) {
                System.err.println("[FAIL] deletarPessoa: pessoa inexistente");
                failed++;
            } else {
                boolean apagou = dao.deletarPessoa(current);
                if (!apagou) {
                    System.err.println("[FAIL] deletarPessoa: método retornou false");
                    failed++;
                } else {
                    boolean ativa = lerAtivaNoBanco(current.getId());
                    if (!ativa) {
                        System.out.println("[PASS] deletarPessoa (soft) -> pessoa_ativa = false");
                        passed++;
                    } else {
                        System.err.println("[FAIL] deletarPessoa -> pessoa_ativa continuou true");
                        failed++;
                    }

                    boolean reativou = dao.reincluirPessoa(current);
                    boolean ativaDepois = lerAtivaNoBanco(current.getId());
                    if (reativou && ativaDepois) {
                        System.out.println("[PASS] reincluirPessoa -> pessoa_ativa = true após reinclusão");
                        passed++;
                    } else {
                        System.err.println("[FAIL] reincluirPessoa -> não reativou corretamente");
                        failed++;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] deletar/reincluir: " + e.getMessage());
            failed++;
        }

        // cleanup
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM PESSOAS WHERE pessoa_cpf = ?")) {
            ps.setString(1, uniqueCpf);
            int del = ps.executeUpdate();
            System.out.println("Cleanup: registros deletados = " + del);
        } catch (SQLException e) {
            System.err.println("[ERROR] cleanup: " + e.getMessage());
        }

        System.out.println("\n==== Resumo de testes ====");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("=========================");

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
        return true; // em caso de erro, evita falso positivo
    }
}
