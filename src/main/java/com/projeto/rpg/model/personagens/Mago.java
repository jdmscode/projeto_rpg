package com.projeto.rpg.model.personagens;

import com.projeto.rpg.model.util.Dado;

public class Mago extends Personagem {

    private int mana;
    private final int manaMax;
    private final int bonusMagicoPorAtaque;

    public Mago(String nome, int hp, int ataque, int defesa, int nivel, int manaMax, int bonusMagicoPorAtaque) {
        super(nome, hp, ataque, defesa, nivel);

        if (manaMax < 0) throw new IllegalArgumentException("manaMax inválido");
        if (bonusMagicoPorAtaque < 0) throw new IllegalArgumentException("bonusMagicoPorAtaque inválido");

        this.manaMax = manaMax;
        this.mana = manaMax;
        this.bonusMagicoPorAtaque = bonusMagicoPorAtaque;
    }

    public Mago(Mago outro) {
        super(outro);
        this.mana = outro.mana;
        this.manaMax = outro.manaMax;
        this.bonusMagicoPorAtaque = outro.bonusMagicoPorAtaque;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int m) {
        this.mana = Math.max(0, Math.min(m, manaMax));
    }

    public int getManaMax() {
        return manaMax;
    }

    public int getBonusMagicoPorAtaque() {
        return bonusMagicoPorAtaque;
    }

    @Override
    public int calcularAtaqueEfetivo() {
        if (mana > 0) {
            int rolagem = Dado.rolar(6);
            int ataqueMagico = getAtaque() + rolagem + bonusMagicoPorAtaque;
            setMana(mana - 1);
            return ataqueMagico;
        } else {
            return super.calcularAtaqueEfetivo();
        }
    }

    @Override
    public Mago clone() {
        return new Mago(this);
    }

    @Override
    public String toString() {
        return String.format("%s (HP:%d/%d Atq:%d Def:%d Nv:%d Mana:%d/%d BônusMágico:%d)",
                getNome(), getPontosVida(), getVidaMaxima(),
                getAtaque(), getDefesa(), getNivel(),
                mana, manaMax, bonusMagicoPorAtaque);
    }
}
