package java.ClassesPuras;

public class MoradorCasa {
    private int moradorId;
    private int casaId;
    private TipoVinculo tipoVinculo;

    public MoradorCasa(int moradorId, int casaId, TipoVinculo tipoVinculo) {
        this.moradorId = moradorId;
        this.casaId = casaId;
        this.tipoVinculo = tipoVinculo;
    }

    public MoradorCasa(){
    };

    public int getMoradorId() {
        return moradorId;
    }

    public void setMoradorId(int moradorId) {
        this.moradorId = moradorId;
    }

    public int getCasaId() {
        return casaId;
    }

    public void setCasaId(int casaId) {
        this.casaId = casaId;
    }

    public TipoVinculo getTipoVinculo() {
        return tipoVinculo;
    }

    public void setTipoVinculo(TipoVinculo tipoVinculo) {
        this.tipoVinculo = tipoVinculo;
    }

    public void setTipoVinculo(java.ClassesUtil.TipoVinculo tipoVinculo) {
    }
}


