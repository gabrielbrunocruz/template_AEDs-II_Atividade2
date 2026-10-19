import java.util.function.Function;

import javax.management.RuntimeErrorException;

public class Fila<E> {
    
    private Celula <E> inicio;
    private Celula <E> fim;


    public Fila(){
        Celula<E>sentinela = new Celula<>();
        inicio = sentinela;
        fim = sentinela;
    }

    public boolean vazia(){
        return inicio ==fim;
    }

    public void enfileirar(E item){
        Celula nova = new Celula<>(item);
        nova = inicio.getProximo();
    }

    public E desenfileirar(){
        if(vazia()){
            throw new RuntimeException("A fila atual se encontra vazia");
        }
        Celula<E> primeira = inicio.getProximo();
        E item = primeira.getItem();

        inicio.setProximo(primeira.getProximo());
        return item;
        /* 
        Celula<E> cel = inicio;
        while(cel.getProximo()!= null && !cel.getProximo().equals(item)){
            cel = cel.getProximo();
        }
        if(cel.getProximo() == null){
            throw new RuntimeException("Essa celula nao se enontra na fila");
        }
        Celula<E> tmp = cel.getProximo();
        E elementoEncontrado = tmp.getItem();
        cel.getProximo() = tmp.getProximo();
        */
    }
    public double calcularValorMedio(Function<E, Double> extrator, int quantidade){
        return 3.0;
    }
}
