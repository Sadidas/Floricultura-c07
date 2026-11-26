import java.math.BigDecimal;

public class Planta {
    private final int id;
    private final String nome;
    private final String especie;
    private final BigDecimal preco;
    private final int estoque;

    public Planta(int id, String nome, String especie, BigDecimal preco, int estoque) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.preco = preco;
        this.estoque = estoque;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEspecie() { return especie; }
    public BigDecimal getPreco() { return preco; }
    public int getEstoque() { return estoque; }

    @Override
    public String toString() {
        return "Planta{id=" + id + ", nome='" + nome + "', especie='" + especie
                + "', preco=" + preco + ", estoque=" + estoque + "}";
    }
}
