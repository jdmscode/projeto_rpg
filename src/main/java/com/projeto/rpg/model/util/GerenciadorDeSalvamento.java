package com.projeto.rpg.model.util;

import com.projeto.rpg.model.personagens.Personagem;
import com.projeto.rpg.interfaces.*;
import com.projeto.rpg.model.personagens.Inimigo;

public class GerenciadorDeSalvamento implements ISalvamento {

    private EstadoDoJogo checkpoint;

    @Override
    public void salvar(EstadoDoJogo estado) {
        if (estado == null) throw new IllegalArgumentException("Estado do jogo não pode ser null");
        this.checkpoint = estado.clone();
    }

    @Override
    public EstadoDoJogo carregar() {
        if (checkpoint == null) return null;
        return checkpoint.clone();
    }

    @Override
    public void salvar(Personagem jogador, int capituloAtual, boolean emCapitulo, int subEtapa, Inimigo inimigoAtual) {
        EstadoDoJogo estado = new EstadoDoJogo();
        estado.setJogador(jogador.clone());
        estado.setCapituloAtual(capituloAtual);
        estado.setEmCapitulo(emCapitulo);
        estado.setSubEtapa(subEtapa);
        if (inimigoAtual != null) estado.setInimigoAtual(inimigoAtual.clone());
        salvar(estado);
    }
}
