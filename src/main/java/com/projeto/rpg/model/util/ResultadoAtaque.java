package com.projeto.rpg.model.util;

public class ResultadoAtaque {
    private final int dano;
    private final boolean ataqueMagico;
    private final int manaRestante;

    public ResultadoAtaque(int dano, boolean ataqueMagico, int manaRestante) {
        this.dano = dano;
        this.ataqueMagico = ataqueMagico;
        this.manaRestante = manaRestante;
    }

    public int getDano() { return dano; }
    public boolean isAtaqueMagico() { return ataqueMagico; }
    public int getManaRestante() { return manaRestante; }
}
