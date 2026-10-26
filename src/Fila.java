import java.util.function.Function;
import java.util.function.Predicate;

public class Fila<E> {
    
    private Celula<E> inicio;
    private Celula<E> fim;

    public Fila(){
        Celula<E> sentinela = new Celula<>();
        inicio = sentinela;
        fim = sentinela;
    }

    public boolean vazia(){
        return inicio == fim;
    }

    // TAREFA 0: Corrigido para garantir o armazenamento no fim (ordem de chegada)
    public void enfileirar(E item){
        Celula<E> nova = new Celula<>(item);
        fim.setProximo(nova);
        fim = nova;
    }

    public E desenfileirar(){
        if(vazia()){
            throw new RuntimeException("A fila atual se encontra vazia");
        }
        Celula<E> primeira = inicio.getProximo();
        E item = primeira.getItem();

        inicio.setProximo(primeira.getProximo());
        
        // Se a fila esvaziou após remover, o fim precisa voltar a apontar para o sentinela
        if(inicio.getProximo() == null) {
            fim = inicio;
        }
        
        return item;
    }

    // TAREFA 1: Calcular valor médio extraindo um atributo numérico
    public double calcularValorMedio(Function<E, Double> extrator, int quantidade){
        if (vazia() || quantidade <= 0) {
            return 0.0;
        }

        double soma = 0.0;
        int cont = 0;
        Celula<E> atual = inicio.getProximo(); // pula o sentinela

        while (atual != null && cont < quantidade) {
            soma += extrator.apply(atual.getItem());
            cont++;
            atual = atual.getProximo();
        }

        return cont > 0 ? soma / cont : 0.0;
    }

    // TAREFA 2: Filtrar a fila usando um condicional (Predicate)
    public Fila<E> filtrar(Predicate<E> condicional, int quantidade){
        Fila<E> novaFila = new Fila<>();
        
        if (vazia() || quantidade <= 0) {
            return novaFila;
        }

        int cont = 0;
        Celula<E> atual = inicio.getProximo(); // pula o sentinela

        while (atual != null && cont < quantidade) {
            if (condicional.test(atual.getItem())) {
                novaFila.enfileirar(atual.getItem());
            }
            cont++;
            atual = atual.getProximo();
        }

        return novaFila;
    }
}