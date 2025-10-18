package ClassesDAO;

import java.sql.SQLException;
import java.util.Date;
import ClassesPuras.*; // Classes Puras não definidas, mas necessárias para compilação lógica
import Util.TipoVinculo; // Enum TipoVinculo não definida, mas necessária

public class TesteDAO {

    public static void main(String[] args) {

        // --- Variáveis de Teste (Mocks) ---
        // A data de nascimento deve ser inicializada para evitar NullPointerException durante a conversão SQL
        Date dataNasc = new Date();

        // Pessoa / Morador / Prestador / Visitante
        Pessoa p1 = new Pessoa();
        p1.setId(101); // ID para operações de atualização/deleção
        p1.setNome("Fulano de Teste");
        p1.setCpf("00000000001");
        p1.setEmail("teste@email.com");
        p1.setTelefone("999999999");
        p1.setDataNasc(dataNasc);

        Morador m1 = new Morador();
        m1.setId(201);
        m1.setNome("Morador Teste");
        // Assume que Morador herda todos os atributos de Pessoa

        Prestador pr1 = new Prestador();
        pr1.setId(301);
        pr1.setNome("Prestador Servico Teste");
        pr1.setCnpj("11223344556677");
        pr1.setEmpresa("Empresa Teste SA");

        Visitante v1 = new Visitante();
        v1.setId(401);
        v1.setNome("Visitante Teste");

        // Outras Entidades
        Casa c1 = new Casa();
        c1.setId(501);
        c1.setEndereco("Rua Teste, 100");

        LocalControlado l1 = new LocalControlado();
        l1.setId(601);
        l1.setNome("Portaria Principal");

        Veiculo veic1 = new Veiculo();
        veic1.setPlaca("ABC1234");
        veic1.setPesId(p1.getId()); // Vincula Veiculo à Pessoa p1
        veic1.setCor("Azul");
        veic1.setModelo("Sedan");

        Autorizacao aut1 = new Autorizacao();
        aut1.setPesId(p1.getId());
        aut1.setLocId(l1.getId());
        aut1.setAutorizacao(1); // True (Autorizado)

        MoradorCasa mc1 = new MoradorCasa();
        mc1.setMoradorId(m1.getId());
        mc1.setCasaId(c1.getId());
        // Assume que TipoVinculo.PROPRIETARIO é um enum válido
        mc1.setTipoVinculo(TipoVinculo.PROPRIETARIO);

        Servico s1 = new Servico();
        s1.setId(801);
        s1.setMorId(m1.getId());
        s1.setPrestId(pr1.getId());
        s1.setServTipo("Manutencao Predial");
        s1.setDataInicio(dataNasc);
        s1.setDataFim(dataNasc);

        // --- Inicialização dos DAOs ---
        PessoaDAO pessoaDAO = new PessoaDAO();
        MoradorDAO moradorDAO = new MoradorDAO();
        PrestadorDAO prestadorDAO = new PrestadorDAO();
        VisitanteDAO visitanteDAO = new VisitanteDAO();
        CasaDAO casaDAO = new CasaDAO();
        LocalControladoDAO localDAO = new LocalControladoDAO();
        AutorizacaoDAO autorizacaoDAO = new AutorizacaoDAO();
        MoradorCasaDAO moradorCasaDAO = new MoradorCasaDAO();
        ServicoDAO servicoDAO = new ServicoDAO();
        VeiculoDAO veiculoDAO = new VeiculoDAO();


        try {
            System.out.println("--- TESTES PESSOA DAO ---");

            // 1. incluirPessoa (Protegido, simulamos através de subclasse ou chamando diretamente se possível)
            // Chamado por incluirMorador/Prestador/Visitante
            System.out.println("Incluindo Pessoa (via Morador): ID gerado: " + moradorDAO.incluirMorador(m1));

            // 2. consultarPessoasGeral()
            System.out.println("Consulta Geral de Pessoas: " + pessoaDAO.consultarPessoasGeral().length + " resultados.");

            // 3. consultarPessoaCpf(String cpf)
            System.out.println("Consulta por CPF: " + pessoaDAO.consultarPessoaCpf(p1.getCpf()));

            // 4. consultarPessoaNome(String pessoa_nome)
            System.out.println("Consulta por Nome: " + pessoaDAO.consultarPessoaNome(p1.getNome()));

            // 5. consultarPessoasPorTipo(String tipo)
            System.out.println("Consultar Pessoas por Tipo (MORADOR): " + pessoaDAO.consultarPessoasPorTipo("MORADOR").length + " resultados.");

            // 6. atualizarPessoa(Pessoa pessoa) (Protegido, simulamos a chamada)
            // Se o ID 101 existir:
            p1.setNome("Novo Nome Atualizado");
            // Nota: Este método é protegido. Em um contexto de teste real, seria testado via `atualizarMorador` ou similar.
            // System.out.println("Atualizar Pessoa (Direto): " + pessoaDAO.atualizarPessoa(p1)); 

            // 7. deletarPessoa(Pessoa pessoa) (Soft Delete)
            System.out.println("Deletar Pessoa (ID " + p1.getId() + "): " + pessoaDAO.deletarPessoa(p1));

            // 8. reincluirPessoa(Pessoa pessoa) (Ativar Soft Delete)
            System.out.println("Reincluir Pessoa (ID " + p1.getId() + "): " + pessoaDAO.reincluirPessoa(p1));


            System.out.println("\n--- TESTES PRESTADOR DAO ---");
            // 9. incluirPrestador(Prestador prestador)
            System.out.println("Incluindo Prestador: ID gerado: " + prestadorDAO.incluirPrestador(pr1));

            // 10. consultarPrestador(Prestador prestador)
            System.out.println("Consultar Prestador por ID: " + prestadorDAO.consultarPrestador(pr1));

            // 11. atualizarPrestador(Prestador prestador)
            pr1.setEmpresa("Nova Empresa LTDA");
            System.out.println("Atualizar Prestador: " + prestadorDAO.atualizarPrestador(pr1));

            // 12. deletarPrestador(Prestador prestador) (Chama deletarPessoa)
            System.out.println("Deletar Prestador (ID " + pr1.getId() + "): " + prestadorDAO.deletarPrestador(pr1));


            System.out.println("\n--- TESTES VISITANTE DAO ---");
            // 13. incluirVisitante(Visitante visitante, Morador morador)
            System.out.println("Incluindo Visitante: ID gerado: " + visitanteDAO.incluirVisitante(v1, m1));

            // 14. consultarVisitante(Visitante visitante)
            System.out.println("Consultar Visitante por ID: " + visitanteDAO.consultarVisitante(v1));

            // 15. consultarVisitantesPorMorador(Morador morador)
            System.out.println("Consultar Morador vinculado ao Visitante: " + visitanteDAO.consultarVisitantesPorMorador(m1));

            // 16. atualizarVisitante(Visitante visitante, Morador morador)
            // Atualiza o Visitante v1 e associa ele novamente ao Morador m1
            System.out.println("Atualizar Visitante: " + visitanteDAO.atualizarVisitante(v1, m1));

            // 17. deletarVisitante(Visitante visitante) (Chama deletarPessoa)
            System.out.println("Deletar Visitante (ID " + v1.getId() + "): " + visitanteDAO.deletarVisitante(v1));


            System.out.println("\n--- TESTES CASA DAO ---");
            // 18. incluirCasa(Casa casa)
            System.out.println("Incluindo Casa: ID gerado: " + casaDAO.incluirCasa(c1));

            // 19. consultarCasa(Casa casa)
            System.out.println("Consultar Casa por ID: " + casaDAO.consultarCasa(c1));

            // 20. consultarCasaGeral(Casa casa)
            System.out.println("Consulta Geral de Casas: " + casaDAO.consultarCasaGeral(c1).length + " resultados.");

            // 21. deletarCasa(Casa casa)
            System.out.println("Deletar Casa (ID " + c1.getId() + "): " + casaDAO.deletarCasa(c1));


            System.out.println("\n--- TESTES LOCAL CONTROLADO DAO ---");
            // 22. incluirLocalControlado(LocalControlado localControlado)
            System.out.println("Incluindo Local Controlado: ID gerado: " + localDAO.incluirLocalControlado(l1));

            // 23. atualizarLocalControlado(LocalControlado localControlado)
            // Nota: Esta atualização parece usar 'nome' como chave WHERE e tentar atualizar 'autorizacao'
            System.out.println("Atualizar Local Controlado: " + localDAO.atualizarLocalControlado(l1));

            // 24. consultarLocalControlado(LocalControlado localControlado)
            System.out.println("Consultar Local Controlado por Nome: " + localDAO.consultarLocalControlado(l1));

            // 25. consultarLocaisControladosGeral(LocalControlado localControlado)
            System.out.println("Consulta Geral de Locais Controlados: " + localDAO.consultarLocaisControladosGeral(l1).length + " resultados.");

            // 26. deletarLocalControlado(LocalControlado localControlado)
            System.out.println("Deletar Local Controlado: " + localDAO.deletarLocalControlado(l1));


            System.out.println("\n--- TESTES AUTORIZACAO DAO ---");
            // 27. incluirAutorizacao(Autorizacao autorizacao)
            System.out.println("Incluindo Autorização: ID gerado: " + autorizacaoDAO.incluirAutorizacao(aut1));

            // 28. atualizarAutorizacao(Autorizacao aut, LocalControlado lc, boolean novoValor)
            System.out.println("Atualizar Autorização: " + autorizacaoDAO.atualizarAutorizacao(aut1, l1, false));

            // 29. consultarAutorizacoesPorPessoa(Pessoa pessoa)
            System.out.println("Consultar Autorizações por Pessoa: " + autorizacaoDAO.consultarAutorizacoesPorPessoa(p1).length + " resultados.");

            // 30. consultarAutorizacoesPorLocal(LocalControlado locaisControlados)
            System.out.println("Consultar Autorizações por Local: " + autorizacaoDAO.consultarAutorizacoesPorLocal(l1).length + " resultados.");

            // 31. deletarAutorizacao(Autorizacao aut, LocalControlado lc, Pessoa p)
            System.out.println("Deletar Autorização: " + autorizacaoDAO.deletarAutorizacao(aut1, l1, p1));


            System.out.println("\n--- TESTES MORADOR CASA DAO ---");
            // 32. incluirMoradorCasa(MoradorCasa moradorCasa)
            System.out.println("Incluindo Vinculo Morador-Casa: ID gerado: " + moradorCasaDAO.incluirMoradorCasa(mc1));

            // 33. consultarMoradorCasa(MoradorCasa moradorCasa)
            System.out.println("Consultar Vinculo Morador-Casa por Casa ID: " + moradorCasaDAO.consultarMoradorCasa(mc1));

            // 34. consultarCasasMorador(Morador morador)
            System.out.println("Consultar Casas do Morador: " + moradorCasaDAO.consultarCasasMorador(m1).length + " resultados.");

            // 35. consultarMoradoresCasa(Casa casa)
            System.out.println("Consultar Moradores da Casa: " + moradorCasaDAO.consultarMoradoresCasa(c1).length + " resultados.");

            // 36. atualizarMoradorCasa(MoradorCasa moradorCasa)
            // Simular alteração do tipo de vínculo
            mc1.setTipoVinculo(TipoVinculo.INQUILINO);
            System.out.println("Atualizar Vinculo Morador-Casa: " + moradorCasaDAO.atualizarMoradorCasa(mc1));

            // 37. deletarMoradorCasa(MoradorCasa moradorCasa)
            System.out.println("Deletar Vinculo Morador-Casa: " + moradorCasaDAO.deletarMoradorCasa(mc1));


            System.out.println("\n--- TESTES SERVICO DAO ---");
            // 38. incluirServico(Servico servico)
            System.out.println("Incluindo Serviço: ID gerado: " + servicoDAO.incluirServico(s1));

            // 39. consultarServicos()
            System.out.println("Consulta Geral de Serviços: " + servicoDAO.consultarServicos().length + " resultados.");

            // 40. consultarServicosMorador(Morador morador)
            System.out.println("Consultar Serviços por Morador: " + servicoDAO.consultarServicosMorador(m1).length + " resultados.");

            // 41. consultarServicosPrestador(Prestador prestador)
            System.out.println("Consultar Serviços por Prestador: " + servicoDAO.consultarServicosPrestador(pr1).length + " resultados.");

            // 42. atualizarServico(Servico servico)
            s1.setServTipo("Nova Manutencao");
            System.out.println("Atualizar Serviço: " + servicoDAO.atualizarServico(s1));

            // 43. deletarServico(Servico servico)
            System.out.println("Deletar Serviço: " + servicoDAO.deletarServico(s1));


            System.out.println("\n--- TESTES VEICULO DAO ---");
            // 44. incluirVeiculo(Veiculo veiculo)
            System.out.println("Incluindo Veículo: ID gerado: " + veiculoDAO.incluirVeiculo(veic1));

            // 45. consultarVeiculo(Veiculo veiculo)
            System.out.println("Consultar Veículo por Placa: " + veiculoDAO.consultarVeiculo(veic1));

            // 46. consultarVeiculosGeral()
            System.out.println("Consulta Geral de Veículos: " + veiculoDAO.consultarVeiculosGeral().length + " resultados.");

            // 47. consultarVeiculosPorTipoPessoa(String tipo)
            System.out.println("Consultar Veículos por Tipo (MORADOR): " + veiculoDAO.consultarVeiculosPorTipoPessoa("MORADOR").length + " resultados.");

            // 48. atualizarVeiculo(Veiculo veiculo)
            veic1.setCor("Preto");
            System.out.println("Atualizar Veículo: " + veiculoDAO.atualizarVeiculo(veic1));

            // 49. deletarVeiculo(Veiculo veiculo)
            System.out.println("Deletar Veículo: " + veiculoDAO.deletarVeiculo(veic1));


        } catch (SQLException e) {
            System.err.println("Ocorreu um erro de SQL: " + e.getMessage());
            // Lança uma exceção de tempo de execução (RuntimeException) para interromper o processo
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            System.err.println("Ocorreu um erro de argumento inválido: " + e.getMessage());
        }
    }
}