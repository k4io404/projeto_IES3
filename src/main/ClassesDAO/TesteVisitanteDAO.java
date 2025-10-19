package ClassesDAO;

import ClassesPuras.Morador;
import ClassesPuras.Visitante;

import java.sql.Date;
import java.time.LocalDate;

public class TesteVisitanteDAO {

    public static void main(String[] args) throws Exception {

        VisitanteDAO visitanteDAO = new VisitanteDAO();
        MoradorDAO moradorDAO = new MoradorDAO();

        System.out.println("==== Teste VisitanteDAO ====");

        // 1. Criar morador de teste
        Morador morador = new Morador();
        morador.setNome("Morador Teste");
        morador.setCpf(String.valueOf(System.currentTimeMillis() % 10000000000L)); // CPF único
        morador.setDataNasc(Date.valueOf("1990-01-01"));
        morador.setTelefone("11999999999");
        morador.setEmail("morador@teste.com");

        int moradorId = moradorDAO.incluirMorador(morador); // ID real gerado
        morador.setId(moradorId);

        System.out.println("[PASS] incluirMorador: inserido com id=" + moradorId);

        // 2. Criar visitante de teste
        Visitante visitante = new Visitante();
        visitante.setNome("Visitante Teste");
        visitante.setCpf(String.valueOf(System.currentTimeMillis() % 10000000000L + 1000)); // CPF único
        visitante.setDataNasc(Date.valueOf("2000-01-01"));
        visitante.setTelefone("11988888888");
        visitante.setEmail("visitante@teste.com");

        int visitanteId = visitanteDAO.incluirVisitante(visitante, morador); // usa ID correto do morador
        visitante.setId(visitanteId);

        System.out.println("[PASS] incluirVisitante: inserido com id=" + visitanteId);

        // 3. Consultar visitante
        Visitante visitanteConsulta = visitanteDAO.consultarVisitante(visitante);
        if (visitanteConsulta != null && visitanteConsulta.getId() == visitanteId) {
            System.out.println("[PASS] consultarVisitante");
        } else {
            System.out.println("[FAIL] consultarVisitante");
        }

        // 4. Consultar visitantes por morador
        Morador moradorConsulta = visitanteDAO.consultarVisitantesPorMorador(morador);
        if (moradorConsulta != null && moradorConsulta.getId() == moradorId) {
            System.out.println("[PASS] consultarVisitantesPorMorador");
        } else {
            System.out.println("[FAIL] consultarVisitantesPorMorador");
        }

        // 5. Atualizar visitante
        visitante.setNome("Visitante Teste Atualizado");
        boolean atualizado = visitanteDAO.atualizarVisitante(visitante, morador);
        if (atualizado) {
            System.out.println("[PASS] atualizarVisitante");
        } else {
            System.out.println("[FAIL] atualizarVisitante");
        }

        // 6. Deletar visitante
        boolean deletado = visitanteDAO.deletarVisitante(visitante);
        if (deletado) {
            System.out.println("[PASS] deletarVisitante");
        } else {
            System.out.println("[FAIL] deletarVisitante");
        }

        // 7. Cleanup - deletar morador
        moradorDAO.deletarMorador(morador);
        System.out.println("Cleanup: morador deletado");

        System.out.println("==== Teste VisitanteDAO finalizado ====");
    }
}
