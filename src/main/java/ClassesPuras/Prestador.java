package java.ClassesPuras;

import java.util.Date;

public class Prestador extends Pessoa {
    private String cnpj;
    private String empresa;

    public Prestador(int id, String nome, String cpf, Date dataNasc, String telefone, String email, String cnpj, String empresa) {
        super(id, nome, cpf, dataNasc, telefone, email);
        this.cnpj = cnpj;
        this.empresa = empresa;
    }

    public Prestador() {
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
}
