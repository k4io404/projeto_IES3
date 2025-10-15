package java.ClassesPuras;
import java.sql.Date;
import java.sql.Timestamp;

public class Acesso {
    private int id;
    private int pesId;
    private int locId;
    private Timestamp data;
    private TipoAcesso tipoAcesso;
    private StatusAcesso statusAcesso;

    public Acesso(int id, int pesId, int locId, Timestamp data, TipoAcesso tipoAcesso, StatusAcesso statusAcesso) {
        this.id = id;
        this.pesId = pesId;
        this.locId = locId;
        this.data = data;
        this.tipoAcesso = tipoAcesso;
        this.statusAcesso = statusAcesso;
    }

    public Acesso() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Timestamp getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public TipoAcesso getTipoAcesso() {
        return tipoAcesso;
    }

    public void setTipoAcesso(TipoAcesso tipoAcesso) {
        this.tipoAcesso = tipoAcesso;
    }

    public StatusAcesso getStatusAcesso() {
        return statusAcesso;
    }

    public void setStatusAcesso(StatusAcesso statusAcesso) {
        this.statusAcesso = statusAcesso;
    }
}
