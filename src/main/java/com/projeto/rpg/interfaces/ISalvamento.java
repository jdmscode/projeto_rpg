package com.projeto.rpg.interfaces;

import com.projeto.rpg.model.personagens.Personagem;
import com.projeto.rpg.model.personagens.Inimigo;
import com.projeto.rpg.model.util.*;

public interface ISalvamento {
    void salvar(EstadoDoJogo estado);
    EstadoDoJogo carregar();
    void salvar(Personagem jogador, int capituloAtual, boolean emCapitulo, int subEtapa, Inimigo inimigoAtual);
}
