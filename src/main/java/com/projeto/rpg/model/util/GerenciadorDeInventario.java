package com.projeto.rpg.model.util;

import com.projeto.rpg.model.personagens.Personagem;
import com.projeto.rpg.model.inventario.Item;
import java.util.List;
import java.util.Optional;

public class GerenciadorDeInventario {

    private final Personagem jogador;

    public GerenciadorDeInventario(Personagem jogador) {
        if (jogador == null) throw new IllegalArgumentException("Jogador não pode ser null");
        this.jogador = jogador;
    }

    public void adicionarItem(Item item) {
        if (item == null) throw new IllegalArgumentException("Item não pode ser null");
        jogador.getInventario().adicionar(item);
    }

    public boolean removerItem(String nome, int quantidade) {
        return jogador.getInventario().remover(nome, quantidade);
    }

    public List<Item> listarItens() {
        return jogador.getInventario().listarOrdenado();
    }

    public boolean contemItem(String nome) {
        return jogador.getInventario().contem(nome);
    }

    public boolean estaVazio() {
        return jogador.getInventario().estaVazio();
    }

    public Optional<Item> pegarCopia(String nome, int quantidade) {
        return jogador.getInventario().pegarCopia(nome, quantidade);
    }

    public boolean usarItem(Item item) {
        if (item == null) return false;

        switch (item.getEfeito()) {

            case CURA -> aplicarCura(item);

            case BUFF_ATAQUE -> aplicarBuffAtaque(item);

            case BUFF_DEFESA -> aplicarBuffDefesa(item);

            default -> {
                return false;
            }
        }

        jogador.getInventario().remover(item.getNome(), 1);
        return true;
    }

    private void aplicarCura(Item item) {
        int cura = item.getValorDoEfeito();
        int vidaAtual = jogador.getPontosVida();
        int vidaMax = jogador.getVidaMaxima();
        jogador.setPontosVida(Math.min(vidaAtual + cura, vidaMax));
    }

    private void aplicarBuffAtaque(Item item) {
        jogador.setAtaque(jogador.getAtaque() + item.getValorDoEfeito());
    }

    private void aplicarBuffDefesa(Item item) {
        jogador.setDefesa(jogador.getDefesa() + item.getValorDoEfeito());
    }

    public Personagem getJogador() {
        return jogador;
    }
}
