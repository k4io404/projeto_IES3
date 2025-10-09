package java.ClassesPuras;

public class LocaisControlados {

    private int localId;
    private String localNome;

    public LocaisControlados(int localId, String localNome) {
        this.localId = localId;
        this.localNome = localNome;
    }

    public LocaisControlados() {
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getLocalNome() {
        return localNome;
    }

    public void setLocalNome(String localNome) {
        this.localNome = localNome;
    }
}
