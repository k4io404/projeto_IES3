package java.ClassesPuras;

import java.util.Date;

public class Visitante extends Pessoa {
    private int morCadastraId;
    private Date dataAutorizacao;

    public Visitante(int id, String nome, String cpf, Date dataNasc, String telefone, String email, int morCadastraId, Date dataAutorizacao) {
        super(id, nome, cpf, dataNasc, telefone, email);
        this.morCadastraId = morCadastraId;
        this.dataAutorizacao = dataAutorizacao;
    }
    public Visitante(){};

    public int getMorCadastraId() {
        return morCadastraId;
    }

    public void setMorCadastraId(int morCadastraId) {
        this.morCadastraId = morCadastraId;
    }

    public Date getDataAutorizacao() {
        return dataAutorizacao;
    }

    public void setDataAutorizacao(Date dataAutorizacao) {
        this.dataAutorizacao = dataAutorizacao;
    }
}
