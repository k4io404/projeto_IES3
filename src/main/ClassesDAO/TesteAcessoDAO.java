package ClassesDAO;

import ClassesPuras.Acesso;
import ClassesPuras.LocalControlado;
import ClassesPuras.Pessoa;
import Util.StatusAcesso;
import Util.TipoAcesso;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;

public class TesteAcessoDAO {

    public static void main(String[] args) throws SQLException {
        PessoaDAO pessoaDAO = new PessoaDAO();
        LocalControladoDAO localDAO = new LocalControladoDAO();
        AcessoDAO acessoDAO = new AcessoDAO();

        // Criar Pessoa com CPF único
        Pessoa p = new Pessoa();
        p.setNome("Teste Pessoa");
        p.setCpf("123456789" + System.currentTimeMillis() % 10000); // CPF único
        p.setEmail("teste@teste.com");
        p.setTelefone("11999999999");
        p.setDataNasc(new java.sql.Date(System.currentTimeMillis()));
        p.setAtiva(true);

        int pessoaId = pessoaDAO.incluirPessoa(p);
        p.setId(pessoaId);
        System.out.println("[PASS] incluirPessoa id=" + pessoaId);

        // Criar Local Controlado
        LocalControlado local = new LocalControlado();
        local.setNome("Portaria");
        int localId = localDAO.incluirLocalControlado(local);
        local.setId(localId);
        System.out.println("[PASS] incluirLocal id=" + localId);

        // Criar Acesso
        Acesso acesso = new Acesso();
        acesso.setLocId(localId);
        acesso.setPesId(pessoaId);
        acesso.setData(new Timestamp(System.currentTimeMillis()));
        acesso.setTipoAcesso(TipoAcesso.ENTRADA); // enum correto
        acesso.setStatusAcesso(StatusAcesso.PERMITIDO);

        int acessoId = acessoDAO.incluirAcesso(acesso);
        acesso.setId(acessoId);
        System.out.println("[PASS] incluirAcesso id=" + acessoId);

        // Consultar Acesso
        Acesso a1 = acessoDAO.consultarAcesso(acesso);
        if (a1 != null && a1.getId() == acessoId) System.out.println("[PASS] consultarAcesso");
        else System.out.println("[FAIL] consultarAcesso");

        // Consultar por Local
        Acesso[] acessosLocal = acessoDAO.consultarAcessosPorLocal(local);
        if (acessosLocal.length > 0) System.out.println("[PASS] consultarAcessosPorLocal: " + Arrays.toString(acessosLocal));
        else System.out.println("[FAIL] consultarAcessosPorLocal");

        // Consultar por Pessoa
        Acesso[] acessosPessoa = acessoDAO.consultarAcessosPorPessoa(pessoaId);
        if (acessosPessoa.length > 0) System.out.println("[PASS] consultarAcessosPorPessoa: " + Arrays.toString(acessosPessoa));
        else System.out.println("[FAIL] consultarAcessosPorPessoa");

        // Consultar todos ordenados por data
        Acesso[] acessosTodos = acessoDAO.consultarAcessosOrderData();
        if (acessosTodos.length > 0) System.out.println("[PASS] consultarAcessosOrderData: " + Arrays.toString(acessosTodos));
        else System.out.println("[FAIL] consultarAcessosOrderData");
    }
}
