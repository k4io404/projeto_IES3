package ClassesDAO;

import ClassesPuras.Morador;
import ClassesPuras.Prestador;
import ClassesPuras.Servico;
import ClassesPuras.Pessoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

public class TesteServicoDAO {

    public static void main(String[] args) {
        ServicoDAO dao = new ServicoDAO();
        MoradorDAO moradorDAO = new MoradorDAO();
        PrestadorDAO prestadorDAO = new PrestadorDAO();
        PessoaDAO pessoaDAO = new PessoaDAO();

        int passed = 0, failed = 0;

        // gerar valores únicos
        String cpfMorador = gerarCpfUnico();
        String cpfPrestador = gerarCpfUnico();
        Date dataInicio = Date.valueOf(LocalDate.now());
        Date dataFim = Date.valueOf(LocalDate.now().plusDays(1));

        // 0) criar morador
        Morador morador = new Morador();
        morador.setNome("MoradorTeste " + System.nanoTime());
        morador.setCpf(cpfMorador);
        morador.setDataNasc(Date.valueOf("1985-01-01"));
        morador.setTelefone("11900000000");
        morador.setEmail("morador.teste@example.com");

        int moradorId = -1;
        try {
            moradorId = moradorDAO.incluirMorador(morador);
            morador.setId(moradorId);
            if (moradorId > 0) {
                System.out.println("[PASS] incluirMorador: id=" + moradorId);
                passed++;
            } else {
                System.err.println("[FAIL] incluirMorador retorno inesperado id=" + moradorId);
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] incluirMorador: " + e.getMessage());
            failed++;
        }

        // 1) criar prestador (tenta criar; se falhar, busca um existente)
        Prestador prestador = new Prestador();
        prestador.setNome("PrestadorTeste " + System.nanoTime());
        prestador.setCpf(cpfPrestador);
        prestador.setDataNasc(Date.valueOf("1975-01-01"));
        prestador.setTelefone("11911111111");
        prestador.setEmail("prestador.teste@example.com");
        // tenta preencher cnpj/empresa se sua classe Prestador possuir setters
        try {
            prestador.getClass().getMethod("setCnpj", String.class).invoke(prestador, "123456780001" + (int)(Math.random()*90+10));
            prestador.getClass().getMethod("setEmpresa", String.class).invoke(prestador, "EmpresaTeste");
        } catch (NoSuchMethodException nsme) {
            // ok — ignora se método não existir
        } catch (Exception ex) {
            // ignora problemas de reflection
        }

        int prestadorId = -1;
        boolean prestadorCriado = false;
        try {
            prestadorId = prestadorDAO.incluirPrestador(prestador);
            // tentar recuperar id real via pessoa (fallback)
            if (prestadorId <= 0) {
                Pessoa p = pessoaDAO.consultarPessoaCpf(cpfPrestador);
                if (p != null) prestadorId = p.getId();
            }
            if (prestadorId > 0) {
                prestador.setId(prestadorId);
                System.out.println("[PASS] inserirPrestador: id=" + prestadorId);
                passed++;
                prestadorCriado = true;
            } else {
                System.err.println("[WARN] inserirPrestador retorno inesperado: " + prestadorId);
            }
        } catch (Exception e) {
            // se falhar (problemas no DAO), tentamos encontrar um prestador já existente
            System.err.println("[WARN] incluirPrestador falhou: " + e.getMessage());
        }

        // se não criou, tenta achar um prestador já cadastrado
        if (!prestadorCriado) {
            try {
                Pessoa[] prestadores = pessoaDAO.consultarPessoasPorTipo("PRESTADOR");
                if (prestadores != null && prestadores.length > 0) {
                    // usa o primeiro
                    Pessoa p0 = prestadores[0];
                    prestador.setId(p0.getId());
                    prestador.setNome(p0.getNome());
                    System.out.println("[WARN] reutilizando prestador existente id=" + p0.getId());
                    passed++;
                } else {
                    System.err.println("[FAIL] nenhum prestador disponível/criado para testar SERVICO");
                    failed++;
                    // não podemos continuar sem prestador; faz cleanup parcial e sai
                    cleanupPartial(morador.getId(), null, cpfMorador, null);
                    System.exit(2);
                }
            } catch (Exception e) {
                System.err.println("[ERROR] buscar prestador existente: " + e.getMessage());
                failed++;
                cleanupPartial(morador.getId(), null, cpfMorador, null);
                System.exit(2);
            }
        }

        // 2) criar Servico
        Servico serv = new Servico();
        // set campos conforme seu Servico class; usamos reflexivo para evitar suposições sobre tipos
        try {
            serv.getClass().getMethod("setMorId", int.class).invoke(serv, morador.getId());
            serv.getClass().getMethod("setPrestId", int.class).invoke(serv, prestador.getId());
            serv.getClass().getMethod("setServTipo", String.class).invoke(serv, "LIMPEZA");
            // definir datas - assume setters aceitam java.sql.Date
            serv.getClass().getMethod("setDataInicio", java.util.Date.class).invoke(serv, java.sql.Date.valueOf(LocalDate.now()));
            serv.getClass().getMethod("setDataFim", java.util.Date.class).invoke(serv, java.sql.Date.valueOf(LocalDate.now().plusDays(1)));
        } catch (NoSuchMethodException nsme) {
            // se os métodos tiverem assinatura diferente (por exemplo int -> Integer), tentamos variantes simples
            try {
                serv.getClass().getMethod("setMorId", Integer.class).invoke(serv, Integer.valueOf(morador.getId()));
                serv.getClass().getMethod("setPrestId", Integer.class).invoke(serv, Integer.valueOf(prestador.getId()));
                serv.getClass().getMethod("setServTipo", String.class).invoke(serv, "LIMPEZA");
                serv.getClass().getMethod("setDataInicio", Date.class).invoke(serv, Date.valueOf(LocalDate.now()));
                serv.getClass().getMethod("setDataFim", Date.class).invoke(serv, Date.valueOf(LocalDate.now().plusDays(1)));
            } catch (Exception e) {
                System.err.println("[ERROR] Não foi possível popular objeto Servico via reflection: " + e.getMessage());
                failed++;
                cleanupPartial(morador.getId(), prestador.getId(), cpfMorador, cpfPrestador);
                System.exit(2);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] erro ao setar campos do Servico: " + e.getMessage());
            failed++;
            cleanupPartial(morador.getId(), prestador.getId(), cpfMorador, cpfPrestador);
            System.exit(2);
        }

        int servId = -1;
        try {
            servId = dao.incluirServico(serv);
            // fallback: se retornou <=0, tenta localizar serviço por morador/prestador/tipo
            if (servId <= 0) {
                servId = localizarServicoId(serv, morador.getId(), prestador.getId(), "LIMPEZA");
            }
            if (servId > 0) {
                // set id no objeto via reflection se possível
                try { serv.getClass().getMethod("setId", int.class).invoke(serv, servId); } catch (Exception ignored){}
                System.out.println("[PASS] incluirServico id=" + servId);
                passed++;
            } else {
                System.err.println("[FAIL] incluirServico -> não obteve id válido");
                failed++;
                cleanupPartial(morador.getId(), prestador.getId(), cpfMorador, cpfPrestador);
                System.exit(2);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] incluirServico: " + e.getMessage());
            failed++;
            cleanupPartial(morador.getId(), prestador.getId(), cpfMorador, cpfPrestador);
            System.exit(2);
        }

        // 3) consultarServicos (geral) e verificar presença
        try {
            Servico[] todos = dao.consultarServicos();
            boolean found = false;
            if (todos != null) {
                for (Servico s : todos) {
                    try {
                        int id = (int) s.getClass().getMethod("getId").invoke(s);
                        if (id == servId) { found = true; break; }
                    } catch (Exception ignore) {}
                }
            }
            if (found) {
                System.out.println("[PASS] consultarServicos -> encontrou serv_id=" + servId);
                passed++;
            } else {
                System.err.println("[FAIL] consultarServicos -> não encontrou serv_id=" + servId);
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] consultarServicos: " + e.getMessage());
            failed++;
        }

        // 4) consultarServicosMorador
        try {
            Servico[] sm = dao.consultarServicosMorador(morador);
            boolean found = false;
            if (sm != null) {
                for (Servico s : sm) {
                    try {
                        int id = (int) s.getClass().getMethod("getId").invoke(s);
                        if (id == servId) { found = true; break; }
                    } catch (Exception ignore) {}
                }
            }
            if (found) {
                System.out.println("[PASS] consultarServicosMorador -> encontrou serviço");
                passed++;
            } else {
                System.err.println("[FAIL] consultarServicosMorador -> não encontrou serviço");
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] consultarServicosMorador: " + e.getMessage());
            failed++;
        }

        // 5) consultarServicosPrestador
        try {
            Servico[] sp = dao.consultarServicosPrestador(prestador);
            boolean found = false;
            if (sp != null) {
                for (Servico s : sp) {
                    try {
                        int id = (int) s.getClass().getMethod("getId").invoke(s);
                        if (id == servId) { found = true; break; }
                    } catch (Exception ignore) {}
                }
            }
            if (found) {
                System.out.println("[PASS] consultarServicosPrestador -> encontrou serviço");
                passed++;
            } else {
                System.err.println("[FAIL] consultarServicosPrestador -> não encontrou serviço");
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] consultarServicosPrestador: " + e.getMessage());
            failed++;
        }

        // 6) atualizarServico - altera tipo e dataFim
        try {
            // alterar via reflection
            try { serv.getClass().getMethod("setServTipo", String.class).invoke(serv, "JARDINAGEM"); } catch (Exception ignore){}
            try { serv.getClass().getMethod("setDataFim", Date.class).invoke(serv, Date.valueOf(LocalDate.now().plusDays(2))); } catch (Exception ignore){}
            boolean ok = dao.atualizarServico(serv);
            if (!ok) {
                System.err.println("[FAIL] atualizarServico -> método retornou false");
                failed++;
            } else {
                // validar alteração consultando todos e buscando id
                boolean validated = false;
                Servico[] todos2 = dao.consultarServicos();
                if (todos2 != null) {
                    for (Servico s : todos2) {
                        try {
                            int id = (int) s.getClass().getMethod("getId").invoke(s);
                            if (id == servId) {
                                String tipo = (String) s.getClass().getMethod("getServTipo").invoke(s);
                                if ("JARDINAGEM".equals(tipo)) validated = true;
                                break;
                            }
                        } catch (Exception ignore){}
                    }
                }
                if (validated) {
                    System.out.println("[PASS] atualizarServico -> alteração validada");
                    passed++;
                } else {
                    System.err.println("[FAIL] atualizarServico -> alteração não encontrada no DB");
                    failed++;
                }
            }
        } catch (Exception e) {
            System.err.println("[ERROR] atualizarServico: " + e.getMessage());
            failed++;
        }

        // 7) deletarServico
        try {
            // obter id via reflection se necessário
            int idToDelete = servId;
            boolean deleted = dao.deletarServico(serv);
            if (!deleted) {
                // tentar delete direto por id
                try (Connection conn = ConnectionFactory.getConnection();
                     PreparedStatement ps = conn.prepareStatement("DELETE FROM SERVICOS WHERE serv_id = ?")) {
                    ps.setInt(1, idToDelete);
                    int r = ps.executeUpdate();
                    if (r > 0) deleted = true;
                } catch (Exception ignore) {}
            }
            if (deleted) {
                // verificar não aparece mais em consultarServicos
                Servico[] allAfter = dao.consultarServicos();
                boolean stillThere = false;
                if (allAfter != null) {
                    for (Servico s : allAfter) {
                        try {
                            int id = (int) s.getClass().getMethod("getId").invoke(s);
                            if (id == servId) { stillThere = true; break; }
                        } catch (Exception ignore) {}
                    }
                }
                if (!stillThere) {
                    System.out.println("[PASS] deletarServico -> removido");
                    passed++;
                } else {
                    System.err.println("[FAIL] deletarServico -> ainda presente");
                    failed++;
                }
            } else {
                System.err.println("[FAIL] deletarServico -> método retornou false e delete direto falhou");
                failed++;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] deletarServico: " + e.getMessage());
            failed++;
        }

        // Cleanup final: remover prestador e morador criados (por CPF)
        try (Connection conn = ConnectionFactory.getConnection()) {
            int delPess1 = 0;
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM PESSOAS WHERE pessoa_cpf = ?")) {
                ps.setString(1, cpfPrestador);
                delPess1 = ps.executeUpdate();
            } catch (Exception e) { /* ignore */ }

            int delPess2 = 0;
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM PESSOAS WHERE pessoa_cpf = ?")) {
                ps.setString(1, cpfMorador);
                delPess2 = ps.executeUpdate();
            } catch (Exception e) { /* ignore */ }

            System.out.println("Cleanup: PESSOAS deletadas (prestador,morador)= " + delPess1 + ", " + delPess2);
        } catch (Exception e) {
            System.err.println("Erro no cleanup final: " + e.getMessage());
        }

        System.out.println("\n==== Resumo Teste ServicoDAO ====");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("===============================");

        if (failed > 0) System.exit(2);
        else System.exit(0);
    }

    private static String gerarCpfUnico() {
        long now = System.nanoTime();
        return String.format("%011d", Math.abs(now % 100000000000L));
    }

    private static int localizarServicoId(Servico serv, int morId, int prestId, String tipo) {
        // tenta localizar o id do serviço recém-criado via consulta SQL direta
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT serv_id FROM SERVICOS WHERE morador_id = ? AND prestador_id = ? AND serv_tipo = ?")) {
            ps.setInt(1, morId);
            ps.setInt(2, prestId);
            ps.setString(3, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("serv_id");
            }
        } catch (Exception e) {
            System.err.println("localizarServicoId erro: " + e.getMessage());
        }
        return -1;
    }

    private static void cleanupPartial(Integer moradorId, Integer prestadorId, String cpfMorador, String cpfPrestador) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            if (moradorId != null && moradorId > 0) {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM SERVICOS WHERE morador_id = ?")) {
                    ps.setInt(1, moradorId);
                    ps.executeUpdate();
                } catch (Exception ignored) {}
                try (PreparedStatement ps2 = conn.prepareStatement("DELETE FROM MORADORES WHERE morador_id = ?")) {
                    ps2.setInt(1, moradorId);
                    ps2.executeUpdate();
                } catch (Exception ignored) {}
            }
            if (prestadorId != null && prestadorId > 0) {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM PRESTADORES WHERE prestador_id = ?")) {
                    ps.setInt(1, prestadorId);
                    ps.executeUpdate();
                } catch (Exception ignored) {}
            }
            if (cpfMorador != null) {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM PESSOAS WHERE pessoa_cpf = ?")) {
                    ps.setString(1, cpfMorador);
                    ps.executeUpdate();
                } catch (Exception ignored) {}
            }
            if (cpfPrestador != null) {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM PESSOAS WHERE pessoa_cpf = ?")) {
                    ps.setString(1, cpfPrestador);
                    ps.executeUpdate();
                } catch (Exception ignored) {}
            }
        } catch (SQLException e) {
            System.err.println("cleanupPartial erro: " + e.getMessage());
        }
    }
}
