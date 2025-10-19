package ClassesDAO;

import ClassesPuras.Morador;
import ClassesPuras.Visitante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TesteVisitanteDAO {

    public static void main(String[] args) {
        VisitanteDAO visitanteDAO = new VisitanteDAO();
        MoradorDAO moradorDAO = new MoradorDAO();
        PessoaDAO pessoaDAO = new PessoaDAO();

        int passed = 0;
        int failed = 0;

        System.out.println("==== Teste VisitanteDAO ====");

        // ===== Gerar CPFs únicos =====
        String cpfMorador = gerarCpfUnico();
        String cpfVisitante = gerarCpfUnico();

        // 1) Criar morador de teste
        Morador morador = new Morador();
        morador.setNome("Morador Teste " + System.nanoTime());
        morador.setCpf(cpfMorador);
        morador.setDataNasc(Date.valueOf("1990-01-01"));
        morador.setTelefone("11999999999");
        morador.setEmail("morador@teste.com");

        int moradorId = -1;
        try {
            moradorId = moradorDAO.incluirMorador(morador);
            morador.setId(moradorId);
            if (moradorId > 0) {
                System.out.println("[PASS] incluirMorador: inserido com id=" + moradorId);
                passed++;
            } else {
                System.err.println("[FAIL] incluirMorador: retorno inesperado id=" + moradorId);
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] incluirMorador: " + e.getMessage());
            failed++;
        }

        // 2) Criar visitante de teste
        Visitante visitante = new Visitante();
        visitante.setNome("Visitante Teste " + System.nanoTime());
        visitante.setCpf(cpfVisitante);
        visitante.setDataNasc(Date.valueOf("2000-01-01"));
        visitante.setTelefone("11988888888");
        visitante.setEmail("visitante@teste.com");
        // não setamos morCadastraId aqui — o incluirVisitante usa o morador passado

        int visitanteId = -1;
        try {
            visitanteId = visitanteDAO.incluirVisitante(visitante, morador); // idealmente retorna id (pessoa_id)
            // Se a implementação do DAO retornar affectedRows ou 0, tentamos recuperar via consulta por CPF
            if (visitanteId <= 0) {
                ClassesPuras.Pessoa p = pessoaDAO.consultarPessoaCpf(cpfVisitante);
                if (p != null) {
                    visitanteId = p.getId();
                }
            }
            if (visitanteId > 0) {
                visitante.setId(visitanteId);
                System.out.println("[PASS] incluirVisitante: inserido com id=" + visitanteId);
                passed++;
            } else {
                System.err.println("[FAIL] incluirVisitante: não obteve id válido (retorno=" + visitanteId + ")");
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] incluirVisitante: " + e.getMessage());
            failed++;
        }

        // 3) consultarVisitante
        try {
            Visitante consulta = visitanteDAO.consultarVisitante(visitante);
            if (consulta != null && consulta.getId() == visitante.getId()) {
                System.out.println("[PASS] consultarVisitante");
                passed++;
            } else {
                System.err.println("[FAIL] consultarVisitante -> retornou null ou id diferente (esperado="
                        + visitante.getId() + ", obtido=" + (consulta == null ? "null" : consulta.getId()) + ")");
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] consultarVisitante: " + e.getMessage());
            failed++;
        }

        // 4) consultarVisitantesPorMorador
        try {
            Visitante[] lista = visitanteDAO.consultarVisitantesPorMorador(morador);
            boolean found = false;
            if (lista != null) {
                for (Visitante v : lista) {
                    if (v != null && v.getId() == visitante.getId()) {
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                System.out.println("[PASS] consultarVisitantesPorMorador -> encontrou visitante vinculado");
                passed++;
            } else {
                System.err.println("[FAIL] consultarVisitantesPorMorador -> não encontrou visitante vinculado");
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] consultarVisitantesPorMorador: " + e.getMessage());
            failed++;
        }

        // 5) atualizarVisitante (atualiza Pessoa e depois a FK morador se necessário)
        try {
            visitante.setNome("Visitante Teste Atualizado " + System.nanoTime());
            boolean ok = visitanteDAO.atualizarVisitante(visitante, morador);
            if (ok) {
                // validar que nome foi atualizado na tabela PESSOAS
                ClassesPuras.Pessoa p = pessoaDAO.consultarPessoaCpf(cpfVisitante);
                if (p != null && p.getNome().equals(visitante.getNome())) {
                    System.out.println("[PASS] atualizarVisitante -> nome atualizado");
                    passed++;
                } else {
                    System.err.println("[FAIL] atualizarVisitante -> atualização não refletida (nome banco="
                            + (p == null ? "null" : p.getNome()) + ")");
                    failed++;
                }
            } else {
                System.err.println("[FAIL] atualizarVisitante -> método retornou false");
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] atualizarVisitante: " + e.getMessage());
            failed++;
        }

        // 6) deletarVisitante (soft delete via PessoaDAO.deletarPessoa)
        try {
            boolean deleted = visitanteDAO.deletarVisitante(visitante);
            if (deleted) {
                // checar pessoa_ativa no banco
                boolean ativa = lerPessoaAtiva(visitante.getId());
                if (!ativa) {
                    System.out.println("[PASS] deletarVisitante (soft) -> pessoa_ativa = false");
                    passed++;
                } else {
                    System.err.println("[FAIL] deletarVisitante -> pessoa_ativa continua true");
                    failed++;
                }
                // reativar para manter banco limpo (opcional)
                boolean reativou = visitanteDAO.reincluirPessoa(visitante);
                if (reativou && lerPessoaAtiva(visitante.getId())) {
                    System.out.println("[PASS] reincluirPessoa -> pessoa_ativa = true após reativação");
                    passed++;
                } else {
                    System.err.println("[FAIL] reincluirPessoa -> não reativou corretamente");
                    failed++;
                }
            } else {
                System.err.println("[FAIL] deletarVisitante -> método retornou false");
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] deletarVisitante/reincluir: " + e.getMessage());
            failed++;
        }

        // Cleanup: tentar remover VISITANTES e PESSOAS criados pelo teste
        try (Connection conn = ConnectionFactory.getConnection()) {
            int delV = 0;
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM VISITANTES WHERE visitante_id = ?")) {
                if (visitante.getId() > 0) {
                    ps.setInt(1, visitante.getId());
                    delV = ps.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println("Cleanup VISITANTES falhou: " + e.getMessage());
            }

            int delP = 0;
            try (PreparedStatement ps2 = conn.prepareStatement("DELETE FROM PESSOAS WHERE pessoa_cpf = ?")) {
                ps2.setString(1, cpfVisitante);
                delP = ps2.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Cleanup PESSOAS (visitante) falhou: " + e.getMessage());
            }

            int delMor = 0;
            try (PreparedStatement ps3 = conn.prepareStatement("DELETE FROM MORADORES WHERE morador_id = ?")) {
                if (morador.getId() > 0) {
                    ps3.setInt(1, morador.getId());
                    delMor = ps3.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println("Cleanup MORADORES falhou: " + e.getMessage());
            }

            // Também remover pessoa do morador
            int delPM = 0;
            try (PreparedStatement ps4 = conn.prepareStatement("DELETE FROM PESSOAS WHERE pessoa_cpf = ?")) {
                ps4.setString(1, cpfMorador);
                delPM = ps4.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Cleanup PESSOAS (morador) falhou: " + e.getMessage());
            }

            System.out.println("Cleanup: VISITANTES deleted=" + delV + ", PESSOAS(visitante)=" + delP
                    + ", MORADORES=" + delMor + ", PESSOAS(morador)=" + delPM);
        } catch (SQLException e) {
            System.err.println("Erro no cleanup geral: " + e.getMessage());
        }

        System.out.println("\n==== Resumo Teste VisitanteDAO ====");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("==================================");

        System.exit(failed > 0 ? 2 : 0);
    }

    private static String gerarCpfUnico() {
        long now = System.nanoTime();
        // 11 dígitos
        return String.format("%011d", Math.abs(now % 100000000000L));
    }

    private static boolean lerPessoaAtiva(int pessoaId) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT pessoa_ativa FROM PESSOAS WHERE pessoa_id = ?")) {
            ps.setInt(1, pessoaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("pessoa_ativa");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao ler pessoa_ativa: " + e.getMessage());
        }
        // Em caso de erro, devolve true para evitar falso positivo
        return true;
    }
}
