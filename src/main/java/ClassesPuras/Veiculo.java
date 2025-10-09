package java.ClassesPuras;

public class Veiculo {
    private String placa;
    private int pesId;
    private String cor;
    private String modelo;

    public Veiculo(String placa, int pesId, String cor, String modelo) {
        this.placa = placa;
        this.pesId = pesId;
        this.cor = cor;
        this.modelo = modelo;
    }

    public Veiculo() {
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getPesId() {
        return pesId;
    }

    public void setPesId(int pesId) {
        this.pesId = pesId;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}
