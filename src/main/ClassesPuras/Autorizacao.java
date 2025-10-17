package ClassesPuras;

public class Autorizacao {
    private int id;
    private int pesId;
    private int locId;
    private int autorizacao;

    public Autorizacao(int pesId, int locId, int autorizacao) {
        this.pesId = pesId;
        this.locId = locId;
        this.autorizacao = autorizacao;
    }

    public Autorizacao(int id,int pesId, int locId, int autorizacao ){
        this(pesId,locId,autorizacao);
        this.id = id;
    }

    public Autorizacao() {
    }

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

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

    @Override
    public String toString() {
        return "Autorizacao{" +
                "id=" + id +
                ", pesId=" + pesId +
                ", locId=" + locId +
                ", autorizacao=" + autorizacao +
                '}';
    }
}