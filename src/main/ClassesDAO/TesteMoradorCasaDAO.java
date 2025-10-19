package ClassesDAO;

import ClassesPuras.*;
import Util.TipoVinculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TesteMoradorCasaDAO {

    public static void main(String[] args) {

        MoradorCasaDAO dao = new MoradorCasaDAO();
        MoradorDAO moradorDAO = new MoradorDAO();
        CasaDAO casaDAO = new CasaDAO();

        int passed = 0, failed = 0;

        try {
            // 1) Criar morador de teste
            Morador morador = new Morador();
            morador.setNome("MoradorTest_" + System.nanoTime());
            morador.setCpf(String.format("%011d", System.nanoTime() % 100000000000L));
            morador.setTelefone("11999999999");
            morador.setEmail("morador@teste.com");
            morador.setDataNasc(new java.sql.Date(System.currentTimeMillis()));

            int moradorId = moradorDAO.incluirMorador(morador);
            morador.setId(moradorId);
            System.out.println("[PASS] incluirMorador: id=" + moradorId);
            passed++;

            // 2) Criar casa de teste
            Casa casa = new Casa();
            casa.setEndereco("Rua Teste " + System.nanoTime());
            int casaId = casaDAO.incluirCasa(casa);
            casa.setId(casaId);
            System.out.println("[PASS] incluirCasa: id=" + casaId);
            passed++;

            // 3) Incluir MoradorCasa
            MoradorCasa mc = new MoradorCasa();
            mc.setMoradorId(morador.getId());
            mc.setCasaId(casa.getId());
            mc.setTipoVinculo(TipoVinculo.PROPRIETARIO);

            int mcId = dao.incluirMoradorCasa(mc);
            System.out.println("[PASS] incluirMoradorCasa: id=" + mcId);
            passed++;

            // 4) Consultar MoradorCasa
            MoradorCasa mcConsult = dao.consultarMoradorCasa(mc);
            if (mcConsult != null && mcConsult.getMoradorId() == morador.getId()) {
                System.out.println("[PASS] consultarMoradorCasa");
                passed++;
            } else {
                System.err.println("[FAIL] consultarMoradorCasa");
                failed++;
            }

            // 5) Consultar Casas do Morador
            Casa[] casas = dao.consultarCasasMorador(morador);
            if (casas != null && casas.length > 0 && casas[0].getId() == casa.getId()) {
                System.out.println("[PASS] consultarCasasMorador");
                passed++;
            } else {
                System.err.println("[FAIL] consultarCasasMorador");
                failed++;
            }

            // 6) Consultar Moradores da Casa
            Pessoa[] moradores = dao.consultarMoradoresCasa(casa);
            if (moradores != null && moradores.length > 0 && moradores[0].getId() == morador.getId()) {
                System.out.println("[PASS] consultarMoradoresCasa");
                passed++;
            } else {
                System.err.println("[FAIL] consultarMoradoresCasa");
                failed++;
            }

            // 7) Atualizar MoradorCasa
            mc.setTipoVinculo(TipoVinculo.DEPENDENTE);
            boolean atualizado = dao.atualizarMoradorCasa(mc);
            if (atualizado) {
                System.out.println("[PASS] atualizarMoradorCasa");
                passed++;
            } else {
                System.err.println("[FAIL] atualizarMoradorCasa");
                failed++;
            }

            // 8) Deletar MoradorCasa
            boolean deletado = dao.deletarMoradorCasa(mc);
            if (deletado) {
                System.out.println("[PASS] deletarMoradorCasa");
                passed++;
            } else {
                System.err.println("[FAIL] deletarMoradorCasa");
                failed++;
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
            failed++;
        } finally {
            // Cleanup
        }

        System.out.println("\n==== Resumo de Testes MoradorCasaDAO ====");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("=========================================");

        if (failed > 0) System.exit(2);
        else System.exit(0);
    }
}
