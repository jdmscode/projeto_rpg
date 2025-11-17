package com.projeto.rpg.model.inventario;
import com.projeto.rpg.interfaces.*;
import java.util.Objects;


public class Item implements Comparable<Item>, Cloneable, IItem {
    private final String nome;
    private final String descricao;
    private final Efeito efeito;
    private final int valorDoEfeito;
    private int quantidade;

    public Item(String nome, String descricao, Efeito efeito, int valorDoEfeito, int quantidade) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("nome inválido");
        if (efeito == null) throw new IllegalArgumentException("efeito não pode ser null");
        if (quantidade < 0) throw new IllegalArgumentException("quantidade não pode ser negativa");
        if (valorDoEfeito < 0) throw new IllegalArgumentException("valorDoEfeito não pode ser negativo");

        this.nome = nome;
        this.descricao = descricao == null ? "" : descricao;
        this.efeito = efeito;
        this.valorDoEfeito = valorDoEfeito;
        this.quantidade = quantidade;
    }

    public Item(Item outro) {
        this(outro.nome, outro.descricao, outro.efeito, outro.valorDoEfeito, outro.quantidade);
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Efeito getEfeito() { return efeito; }
    public int getValorDoEfeito() { return valorDoEfeito; }
    public int getQuantidade() { return quantidade; }

    void incrementar(int q) {
        if (q <= 0) throw new IllegalArgumentException("incremento deve ser > 0");
        this.quantidade += q;
    }

    void decrementar(int q) {
        if (q <= 0) throw new IllegalArgumentException("decremento deve ser > 0");
        if (q > this.quantidade) throw new IllegalArgumentException("decremento maior que quantidade");
        this.quantidade -= q;
    }

    void setQuantidade(int q) {
        if (q < 0) throw new IllegalArgumentException("quantidade não pode ser negativa");
        this.quantidade = q;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;

        return nome.equalsIgnoreCase(item.nome) && efeito == item.efeito;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome.toLowerCase(), efeito);
    }

    @Override
    public int compareTo(Item o) {
        int c = this.nome.compareToIgnoreCase(o.nome);
        if (c != 0) return c;
        return this.efeito.compareTo(o.efeito);
    }

    @Override
    public Item clone() {
        return new Item(this);
    }

    @Override
    public String toString() {
        return String.format(
            "%s x%d [%s: %d] - %s",
            nome, quantidade, efeito, valorDoEfeito, descricao
        );
    }
}
