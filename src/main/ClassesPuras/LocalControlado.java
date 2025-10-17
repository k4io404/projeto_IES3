package ClassesPuras;

public class LocalControlado {

    private int id;
    private String nome;

    public LocalControlado(String nome){
        this.nome = nome;
    }
    public LocalControlado(int id, String nome) {
        this(nome);
        this.id = id;
    }

    public LocalControlado(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "LocalControlado{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}

