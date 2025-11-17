package com.projeto.rpg;

import java.io.IOException;
import java.util.List;

import com.projeto.rpg.interfaces.IO;
import com.projeto.rpg.model.util.ConsoleIO;
import com.projeto.rpg.model.personagens.Personagem;
import com.projeto.rpg.model.inventario.Item;
import com.projeto.rpg.model.inventario.Efeito;
import com.projeto.rpg.model.inventario.Inventario;
import com.projeto.rpg.model.util.EstadoDoJogo;
import com.projeto.rpg.model.util.GerenciadorDeCapitulos;
import com.projeto.rpg.model.util.GerenciadorDeCombate;
import com.projeto.rpg.model.util.GerenciadorDeSalvamento;

public class Jogo {

    private final IO io;
    private final EstadoDoJogo estado;
    private final GerenciadorDeSalvamento gerenciadorSalvamento;
    private final GerenciadorDeCombate gerenciadorCombate;
    private final GerenciadorDeCapitulos gerenciadorCapitulos;
    private boolean rodando = true;

    public Jogo(Personagem jogador) {
        this(jogador, new ConsoleIO());
    }

    public Jogo(Personagem jogador, IO io) {
        this.io = io;
        this.estado = new EstadoDoJogo();
        this.estado.setJogador(jogador);

        this.gerenciadorSalvamento = new GerenciadorDeSalvamento();
        this.gerenciadorCombate = new GerenciadorDeCombate();
        this.gerenciadorCombate.setEstado(estado);

        this.gerenciadorCapitulos = new GerenciadorDeCapitulos(io, gerenciadorSalvamento);
    }

    public void iniciar() throws IOException {
        while (rodando) {
            menuPrincipal();
        }
        io.println("Obrigado por jogar!");
    }

    private void menuPrincipal() throws IOException {
        io.println("\nMenu");
        io.println("1) Modo história");
        io.println("2) Explorar (modo livre)");
        io.println("3) Ver status");
        io.println("4) Usar item");
        io.println("5) Salvar ponto (checkpoint)");
        io.println("6) Carregar checkpoint");
        io.println("7) Sair");

        String escolha = lerOpcao("1","2","3","4","5","6","7");

        switch (escolha) {
            case "1" -> jogarHistoria();
            case "2" -> explorar();
            case "3" -> verStatus();
            case "4" -> usarItem();
            case "5" -> salvarCheckpoint();
            case "6" -> carregarCheckpoint();
            case "7" -> rodando = false;
        }
    }

 private void jogarHistoria() throws IOException {
    estado.setEmCapitulo(true);

    while (estado.isEmCapitulo()) {
        boolean passou = gerenciadorCapitulos.executarCapitulo(estado, gerenciadorCombate);

        if (estado.getJogador().estaMorto()) {
            io.println("\nVocê morreu!");
            estado.setEmCapitulo(false);
            return;
        }

        if (estado.isFugiu()) {
            io.println("\nVocê fugiu do combate! Voltando ao menu...");
            estado.setEmCapitulo(false);
            estado.setFugiu(false);

            estado.setSubEtapa(1); 
            return;
        }

        if (!passou) {
            estado.setEmCapitulo(false);
            return;
        }

        if (!estado.isFugiu()) {
            io.println("\nCapítulo " + estado.getCapituloAtual() + " concluído!");
            estado.getJogador().setPontosVida(estado.getJogador().getVidaMaxima());

            io.println("1) Continuar");
            io.println("2) Voltar ao menu");

            String opc = lerOpcao("1","2");
            if ("1".equals(opc)) {
                estado.setCapituloAtual(estado.getCapituloAtual() + 1);
                estado.setSubEtapa(1);
            } else {
                estado.setEmCapitulo(false);
            }
        }
    }
}

    private void explorar() throws IOException {
        estado.setEmCapitulo(false);
        estado.setSubEtapa(0);
        estado.setInimigoAtual(null);

        io.println("\nExplorando...");
        gerenciadorCombate.explorar(estado, gerenciadorSalvamento);
    }

    private void usarItem() throws IOException {
        Inventario inv = estado.getJogador().getInventario();

        if (inv.estaVazio()) {
            io.println("Inventário vazio.");
            return;
        }

        List<Item> itens = inv.listarOrdenado();
        io.println("\n--- Inventário ---");
        for (int i = 0; i < itens.size(); i++) io.println((i+1) + ") " + itens.get(i));
        io.println("0) Cancelar");

        int escolha;
        try {
            escolha = Integer.parseInt(io.lerLinha().trim());
        } catch (Exception e) {
            io.println("Opção inválida.");
            return;
        }

        if (escolha == 0) return;
        if (escolha < 1 || escolha > itens.size()) {
            io.println("Escolha fora do intervalo.");
            return;
        }

        Item item = itens.get(escolha - 1);

        if (item.getEfeito() != Efeito.CURA) {
            io.println("Não pode usar itens de dano fora de combate!");
            return;
        }

        int cura = item.getValorDoEfeito();
        Personagem jogador = estado.getJogador();
        int novaVida = Math.min(jogador.getPontosVida() + cura, jogador.getVidaMaxima());
        jogador.setPontosVida(novaVida);

        io.println("Você usou " + item.getNome() + " e recuperou " + cura + " de vida!");
        io.println("Vida atual: " + novaVida + "/" + jogador.getVidaMaxima());

        inv.remover(item.getNome(), 1);
    }

    private void verStatus() {
        io.println("\n--- Status ---");
        io.println(estado.getJogador().toString());
        io.println("Inventário:");
        io.println(estado.getJogador().getInventario().toString());
    }

    private void salvarCheckpoint() {
        gerenciadorSalvamento.salvar(
                estado.getJogador(),
                estado.getCapituloAtual(),
                estado.isEmCapitulo(),
                estado.getSubEtapa(),
                estado.getInimigoAtual()
        );
        io.println("Checkpoint criado com sucesso!");
    }

    private void carregarCheckpoint() throws IOException {
        EstadoDoJogo checkpoint = gerenciadorSalvamento.carregar();
        if (checkpoint == null) {
            io.println("Nenhum checkpoint salvo.");
            return;
        }

        estado.setJogador(checkpoint.getJogador());
        estado.setCapituloAtual(checkpoint.getCapituloAtual());
        estado.setEmCapitulo(checkpoint.isEmCapitulo());
        estado.setSubEtapa(checkpoint.getSubEtapa());
        estado.setInimigoAtual(checkpoint.getInimigoAtual());
        estado.setFugiu(false);

        io.println("Checkpoint restaurado!");

        if (estado.getInimigoAtual() != null) {
            gerenciadorCombate.retomaCombate(estado, gerenciadorSalvamento);
        }
    }

    private String lerOpcao(String... validas) throws IOException {
        while (true) {
            String l = io.lerLinha();
            if (l == null) return validas[0];
            l = l.trim();
            for (String v : validas)
                if (v.equals(l)) return l;
            io.println("Opção inválida.");
        }
    }
}
