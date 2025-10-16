package ClassesDTO;

import Util.TipoVinculo;

public class MoradorCasaDTO {
    private int moradorId;
    private int casaId;
    private TipoVinculo tipoVinculo;

    public MoradorCasaDTO(int moradorId, int casaId, TipoVinculo tipoVinculo) {
        this.moradorId = moradorId;
        this.casaId = casaId;
        this.tipoVinculo = tipoVinculo;
    }

    public MoradorCasaDTO(){
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

    @Override
    public String toString() {
        return "MoradorCasaDTO{" +
                "moradorId=" + moradorId +
                ", casaId=" + casaId +
                ", tipoVinculo=" + tipoVinculo +
                '}';
    }
}


