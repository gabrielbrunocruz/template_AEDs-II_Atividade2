import java.util.NoSuchElementException;

public class Pilha<E> {

	private Celula<E> topo;
	private Celula<E> fundo;

	public Pilha() {

		Celula<E> sentinela = new Celula<E>();
		fundo = sentinela;
		topo = sentinela;

	}

	public boolean vazia() {
		return fundo == topo;
	}

	public void empilhar(E item) {

		topo = new Celula<E>(item, topo);
	}

	public E desempilhar() {

		E desempilhado = consultarTopo();
		topo = topo.getProximo();
		return desempilhado;

	}

	public E consultarTopo() {

		if (vazia()) {
			throw new NoSuchElementException("Nao há nenhum item na pilha!");
		}

		return topo.getItem();

	}

	/**
	 * Cria e devolve uma nova pilha contendo os primeiros numItens elementos
	 * do topo da pilha atual.
	 * 
	 * Os elementos são mantidos na mesma ordem em que estavam na pilha original.
	 * Caso a pilha atual possua menos elementos do que o valor especificado,
	 * uma exceção será lançada.
	 *
	 * @param numItens o número de itens a serem copiados da pilha original.
	 * @return uma nova instância de Pilha<E> contendo os numItens primeiros elementos.
	 * @throws IllegalArgumentException se a pilha não contém numItens elementos.
	 */
	public Pilha<E> subPilha(int numItens) {
		
		// TODO
		return null;
	}

//1
	public void concatenar(Pilha<E> pilha) {
    if (pilha == null || pilha.vazia()) {
        return;
    }
    Celula<E> atual = pilha.topo;

    while (atual.getProximo() != null) {
        atual = atual.getProximo();
    }

    atual.setProximo(this.topo);

    this.topo = pilha.topo;
}

//2
public int obterNumeroItens() {
    int contador = 0;
    Celula<E> atual = topo;
    
    while (atual != fundo) {
        contador++;
        atual = atual.getProximo();
    }
    
    return contador;
}
//3
public void inverter() {
    if (vazia()) {
        return; 
    }

    Celula<E> anterior = fundo; 
    Celula<E> atual = topo;
    Celula<E> proximo;

    while (atual != fundo) {
        proximo = atual.getProximo(); 
        atual.setProximo(anterior);   
        anterior = atual;             
        atual = proximo;              
    }
    topo = anterior;
}
}