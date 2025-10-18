package ClassesService;

import ClassesDAO.AutorizacaoDAO;
import ClassesPuras.Autorizacao;

import java.sql.SQLException;

public class AutorizacaoService {

    private final AutorizacaoDAO autorizacaoDAO;

    public AutorizacaoService(AutorizacaoDAO autorizacaoDAO) {
        this.autorizacaoDAO = autorizacaoDAO;
    }

    public int incluirAutorizacao(Autorizacao autorizacao) throws SQLException {

        if (autorizacao == null){
            throw new IllegalArgumentException("O objeto Autorizado não pode ser nulo.");
        }

        if (autorizacao.getId() <= 0){
            throw new IllegalArgumentException("O ID da casa deve ser um número positivo.");
        }

        if (autorizacao.getAutorizacao() <= 0){
            throw new IllegalArgumentException("O ID da casa deve ser um número positivo.");
        }



    }

}
