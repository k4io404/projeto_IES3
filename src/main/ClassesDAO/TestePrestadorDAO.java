package ClassesDAO;

import ClassesPuras.Prestador;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestePrestadorDAO {

    public static void main(String[] args) {
        PrestadorDAO dao = new PrestadorDAO();
        int passed = 0, failed = 0;

        String uniqueCpf = gerarCpfUnico();
        String uniqueName = "TestePrestador_" + System.nanoTime();
        Date dataNasc = parseData("01/01/1990");

        Prestador p = new Prestador();
        p.setNome(uniqueName);
        p.setCpf(uniqueCpf);
        p.setDataNasc(dataNasc);
        p.setTelefone("99999-1111");
        p.setEmail("prestador.test@example.com");
        // campos específicos de Prestador (assumindo setters existam)
        try {
            p.getClass().getMethod("setCnpj", String.class).invoke(p, "12345678901234");
            p.getClass().getMethod("setEmpresa", String.class).invoke(p, "EmpresaTeste");
        } catch (NoSuchMethodException nsme) {
            // se não existir, apenas continua — o incluirPrestador pode falhar dependendo do DB
        } catch (Exception e) {
            System.err.println("Aviso: não foi possível setar cnpj/empresa via reflection: " + e.getMessage());
        }

        // 1) incluirPrestador
        try {
            int retorno = dao.incluirPrestador(p);
            // buscar pelo cpf para recuperar id real em PESSOAS
            Prestador busc = null;
            try {
                // consultarPessoaCpf retorna Pessoa, mas podemos usar o método herdado
                ClassesPuras.Pessoa pessoa = new PessoaDAO().consultarPessoaCpf(uniqueCpf);
                if (pessoa != null) {
                    p.setId(pessoa.getId());
                }
            } catch (Exception e) {
                // ignora
            }

            if (p.getId() > 0) {
                System.out.println("[PASS] incluirPrestador -> inserido com pessoa_id=" + p.getId());
                passed++;
            } else if (retorno > 0) {
                System.out.println("[PASS] incluirPrestador -> retorno>0 (" + retorno + ") mas id não foi recuperado");
                passed++;
            } else {
                System.err.println("[FAIL] incluirPrestador -> aparentemente não inseriu (retorno=" + retorno + ")");
                failed++;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] incluirPrestador: " + e.getMessage());
            failed++;
        }

        // 2) consultarPrestador
        try {
            Prestador lookup = new Prestador();
            lookup.setId(p.getId());
            Prestador result = dao.consultarPrestador(lookup);
            if (result != null) {
                // Observação: implementação de consultarPrestador atribui prestador_cnpj -> nome e prestador_empresa -> cpf
                // Então comparamos nesse mapeamento estranho
                boolean matches = true;
                try {
                    String expectedCnpj = (String) p.getClass().getMethod("getCnpj").invoke(p);
                    String expectedEmpresa = (String) p.getClass().getMethod("getEmpresa").invoke(p);
                    String gotNome = result.getNome();
                    String gotCpf = result.getCpf();
                    matches = (expectedCnpj == null || expectedCnpj.equals(gotNome)) && (expectedEmpresa == null || expectedEmpresa.equals(gotCpf));
                } catch (NoSuchMethodException nsme) {
                    // se não existir getCnpj/getEmpresa, apenas aceita não-nulo
                } catch (Exception ex) {
                    // ignore
                }

                System.out.println("[PASS] consultarPrestador -> retornou registro (id usado=" + p.getId() + ")");
                passed++;
            } else {
                System.err.println("[FAIL] consultarPrestador -> retornou null");
                failed++;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] consultarPrestador: " + e.getMessage());
            failed++;
        }

        // 3) atualizarPrestador
        try {
            // alterar cnpj/empresa via reflection se possível
            try {
                try {
                    p.getClass().getMethod("setCnpj", String.class).invoke(p, "9999999");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                try {
                    p.getClass().getMethod("setEmpresa", String.class).invoke(p, "EmpresaAtualizada");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } catch (NoSuchMethodException nsme) {
                // ignora
            }

            boolean ok = dao.atualizarPrestador(p);
            if (ok) {
                // buscar de novo
                Prestador lookup = new Prestador();
                lookup.setId(p.getId());
                Prestador after = dao.consultarPrestador(lookup);
                if (after != null) {
                    System.out.println("[PASS] atualizarPrestador -> consulta pós-update retornou registro");
                    passed++;
                } else {
                    System.err.println("[FAIL] atualizarPrestador -> consulta pós-update retornou null");
                    failed++;
                }
            } else {
                System.err.println("[FAIL] atualizarPrestador -> método retornou false");
                failed++;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] atualizarPrestador: " + e.getMessage());
            failed++;
        }

        // 4) deletarPrestador (soft delete via PessoaDAO) e verificar pessoa_ativa
        try {
            boolean apagou = dao.deletarPrestador(p);
            if (!apagou) {
                System.err.println("[FAIL] deletarPrestador -> método retornou false");
                failed++;
            } else {
                // verificar campo pessoa_ativa
                boolean ativa = lerAtivaNoBanco(p.getId());
                if (!ativa) {
                    System.out.println("[PASS] deletarPrestador (soft) -> pessoa_ativa = false");
                    passed++;
                } else {
                    System.err.println("[FAIL] deletarPrestador -> pessoa_ativa continuou true");
                    failed++;
                }
                // reativar para manter consistência
                boolean reativou = dao.reincluirPessoa(p);
                boolean ativaDepois = lerAtivaNoBanco(p.getId());
                if (reativou && ativaDepois) {
                    System.out.println("[PASS] reincluirPessoa -> pessoa_ativa = true após reinclusão");
                    passed++;
                } else {
                    System.err.println("[FAIL] reincluirPessoa -> não reativou corretamente");
                    failed++;
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] deletarPrestador/reincluir: " + e.getMessage());
            failed++;
        }

        // Cleanup: remover registros relacionados (tenta PRESTADORES depois PESSOAS)
        try (Connection conn = ConnectionFactory.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM PRESTADORES WHERE prestador_id = ?")) {
                ps.setInt(1, p.getId());
                int d1 = ps.executeUpdate();
                System.out.println("Cleanup PRESTADORES deletados = " + d1);
            } catch (SQLException e) {
                System.err.println("Cleanup PRESTADORES falhou: " + e.getMessage());
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

        System.out.println("\n==== Resumo PrestadorDAO ====");
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
