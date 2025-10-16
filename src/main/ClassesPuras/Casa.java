package ClassesPuras;

public class Casa {
    private int id;
    private String endereco;

    public Casa(int id, String endereco) {
        this.id = id;
        this.endereco = endereco;
    }

    public Casa() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Casa{" +
                "id=" + id +
                ", endereco='" + endereco + '\'' +
                '}';
    }
}
