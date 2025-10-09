package java.ClassesPuras;

public class Autorizacao {
    private int pesId;
    private int locId;
    private int autorizacao;

    public Autorizacao(int pesId, int locId, int autorizacao) {
        this.pesId = pesId;
        this.locId = locId;
        this.autorizacao = autorizacao;
    }

    public Autorizacao() {
    }

    public int getPesId() {
        return pesId;
    }

    public void setPesId(int pesId) {
        this.pesId = pesId;
    }

    public int getLocId() {
        return locId;
    }

    public void setLocId(int locId) {
        this.locId = locId;
    }

    public int getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(int autorizacao) {
        this.autorizacao = autorizacao;
    }
}