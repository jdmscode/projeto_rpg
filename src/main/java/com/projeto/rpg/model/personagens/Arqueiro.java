package com.projeto.rpg.model.personagens;

import com.projeto.rpg.model.util.Dado;

public class Arqueiro extends Personagem {

    private final int bonusPrecisao;
    private final int chancePrecisao;

    public Arqueiro(String nome, int hp, int ataque, int defesa, int nivel) {
        super(nome, hp, ataque, defesa, nivel);
        this.bonusPrecisao = 2; 
        this.chancePrecisao = 4;
    }

    public Arqueiro(Arqueiro outro) {
        super(outro);
        this.bonusPrecisao = outro.bonusPrecisao;
        this.chancePrecisao = outro.chancePrecisao;
    }

    @Override
    protected int aplicarEfeitosDeAtaque(int danoBase) {
        int rolagemPrecisao = Dado.rolar(chancePrecisao);
        if (rolagemPrecisao == 1) {
            int danoFinal = danoBase + bonusPrecisao;
            System.out.println(getNome() + " acerta tiro preciso! Dano +"
                    + bonusPrecisao + " = " + danoFinal);
            return danoFinal;
        }
        return danoBase;
    }

    @Override
    public boolean temEfeitoDeAtaque() {
        return true;
    }

    @Override
    public Arqueiro clone() {
        return new Arqueiro(this);
    }

    @Override
    public String toString() {
        return String.format("%s (HP:%d/%d Atq:%d Def:%d Nv:%d) [Arqueiro]",
                getNome(), getPontosVida(), getVidaMaxima(),
                getAtaque(), getDefesa(), getNivel());
    }
}
