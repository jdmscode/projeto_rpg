package com.projeto.rpg.model.util;

import com.projeto.rpg.model.personagens.Personagem;
import com.projeto.rpg.model.personagens.Inimigo;

public class EstadoDoJogo implements Cloneable {

    private Personagem jogador;
    private Inimigo inimigoAtual;
    private int capituloAtual = 1;
    private boolean emCapitulo = false;
    private int subEtapa = 0;
    private boolean fugiu = false; 

    private Checkpoint checkpoint = null;

    public Personagem getJogador() { return jogador; }
    public void setJogador(Personagem jogador) { this.jogador = jogador; }

    public Inimigo getInimigoAtual() { return inimigoAtual; }
    public void setInimigoAtual(Inimigo inimigoAtual) { this.inimigoAtual = inimigoAtual; }

    public int getCapituloAtual() { return capituloAtual; }
    public void setCapituloAtual(int capituloAtual) { this.capituloAtual = capituloAtual; }

    public boolean isEmCapitulo() { return emCapitulo; }
    public void setEmCapitulo(boolean emCapitulo) { this.emCapitulo = emCapitulo; }

    public int getSubEtapa() { return subEtapa; }
    public void setSubEtapa(int subEtapa) { this.subEtapa = subEtapa; }

    public boolean isFugiu() { return fugiu; }     
    public void setFugiu(boolean fugiu) { this.fugiu = fugiu; }

    public Checkpoint getCheckpoint() { return checkpoint; }
    public void setCheckpoint(Checkpoint checkpoint) { this.checkpoint = checkpoint; }

    @Override
    public EstadoDoJogo clone() {
        try {
            EstadoDoJogo clone = (EstadoDoJogo) super.clone();
            clone.jogador = jogador.clone();
            if (inimigoAtual != null) clone.inimigoAtual = inimigoAtual.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Falha ao clonar EstadoDoJogo", e);
        }
    }

    public static class Checkpoint {
        public Personagem jogadorSnapshot;
        public Inimigo inimigoSnapshot;
        public int capitulo;
        public boolean emCapitulo;
        public int subEtapa;
        public boolean inEmCombate;
    }
}
