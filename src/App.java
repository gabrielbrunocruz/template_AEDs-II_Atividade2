import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class App {

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    
    /** Scanner para leitura de dados do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados */
    static Produto[] produtosCadastrados;

    /** Quantidade de produtos cadastrados atualmente no vetor */
    static int quantosProdutos = 0;

    // TAREFA 0: Substituição da Pilha por Fila para armazenar por ordem de chegada
    /** Fila de pedidos */
    static Fila<Pedido> filaPedidos = new Fila<>();
        
    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }
   
    static <T extends Number> T lerOpcao(String mensagem, Class<T> classe) {
        
        T valor;
        
        System.out.println(mensagem);
        try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException 
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }
    
    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * @return Um inteiro com a opção do usuário.
     */
    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar por um produto, por código");
        System.out.println("3 - Procurar por um produto, por nome");
        System.out.println("4 - Iniciar novo pedido");
        System.out.println("5 - Fechar pedido");
        System.out.println("6 - Listar produtos dos pedidos mais recentes");
        System.out.println("7 - Exibir o valor total médio dos N primeiros pedidos");
        System.out.println("8 - Exibir os primeiros pedidos com valor total acima de um determinado valor");
        System.out.println("9 - Exibir os primeiros pedidos que apresentam um determinado produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }
    
    static Produto[] lerProdutos(String nomeArquivoDados) {
        
        Scanner arquivo = null;
        int numProdutos;
        String linha;
        Produto produto;
        Produto[] produtosCadastrados;
        
        try {
            arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
            
            numProdutos = Integer.parseInt(arquivo.nextLine());
            produtosCadastrados = new Produto[numProdutos];
            
            for (int i = 0; i < numProdutos; i++) {
                linha = arquivo.nextLine();
                produto = Produto.criarDoTexto(linha);
                produtosCadastrados[i] = produto;
            }
            quantosProdutos = numProdutos;
            
        } catch (IOException excecaoArquivo) {
            produtosCadastrados = null;
        } finally {
            if(arquivo != null) arquivo.close();
        }
        
        return produtosCadastrados;
    }
    
    static Produto localizarProduto() {
        
        Produto produto = null;
        Boolean localizado = false;
        
        cabecalho();
        System.out.println("Localizando um produto...");
        int idProduto = lerOpcao("Digite o código identificador do produto desejado: ", Integer.class);
        for (int i = 0; (i < quantosProdutos && !localizado); i++) {
            if (produtosCadastrados[i].hashCode() == idProduto) {
                produto = produtosCadastrados[i];
                localizado = true;
            }
        }
        
        return produto;   
    }
    
    static Produto localizarProdutoDescricao() {
        
        Produto produto = null;
        Boolean localizado = false;
        String descricao;
        
        cabecalho();
        System.out.println("Localizando um produto...");
        System.out.println("Digite o nome ou a descrição do produto desejado:");
        descricao = teclado.nextLine();
        for (int i = 0; (i < quantosProdutos && !localizado); i++) {
            if (produtosCadastrados[i].descricao.toLowerCase().equals(descricao.toLowerCase())) {
                produto = produtosCadastrados[i];
                localizado = true;
            }
        }
        
        return produto;
    }
    
    private static void mostrarProduto(Produto produto) {
        
        cabecalho();
        String mensagem = "Dados inválidos para o produto!";
        
        if (produto != null){
            mensagem = String.format("Dados do produto:\n%s", produto);
        }
        
        System.out.println(mensagem);
    }
    
    static void listarTodosOsProdutos() {
        
        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < quantosProdutos; i++) {
            System.out.println(String.format("%02d - %s", (i + 1), produtosCadastrados[i].toString()));
        }
    }
    
    public static Pedido iniciarPedido() {
        
        int formaPagamento = lerOpcao("Digite a forma de pagamento do pedido, sendo 1 para pagamento à vista e 2 para pagamento a prazo", Integer.class);
        Pedido pedido = new Pedido(LocalDate.now(), formaPagamento);
        Produto produto;
        int numProdutos;
        int quantidade;
        
        listarTodosOsProdutos();
        System.out.println("Incluindo produtos no pedido...");
        numProdutos = lerOpcao("Quantos produtos serão incluídos no pedido?", Integer.class);
        for (int i = 0; i < numProdutos; i++) {
            produto = localizarProdutoDescricao();
            if (produto == null) {
                System.out.println("Produto não encontrado");
                i--;
            } else {
                quantidade = lerOpcao("Quantos itens desse produto serão incluídos no pedido?", Integer.class);
                pedido.incluirProduto(produto, quantidade);
            }
        }
        
        return pedido;
    }
    
    // TAREFA 0: Implementação do armazenamento em fila
    public static void finalizarPedido(Pedido pedido) {
        if (pedido != null) {
            filaPedidos.enfileirar(pedido);
            System.out.println("\nPedido finalizado com sucesso e armazenado na fila!");
        } else {
            System.out.println("\nNenhum pedido em andamento para ser finalizado.");
        }
    }
    
    public static void listarProdutosPedidosRecentes() {
        System.out.println("Implementação de listar produtos recentes...");
    }

    public static void exibirValorMedioPrimeirosPedidos() {
        cabecalho();
        int qtd = lerOpcao("Deseja calcular a média de quantos (N) primeiros pedidos?", Integer.class);
        
        // Usa o extrator (Function) chamando o método valorFinal() do Pedido
        double media = filaPedidos.calcularValorMedio(p -> p.valorFinal(), qtd);
        
        System.out.printf("\nO valor total médio dos primeiros %d pedidos é: R$ %.2f\n", qtd, media);
    }

    public static void exibirPedidosAcimaDeValor() {
        cabecalho();
        int qtd = lerOpcao("Deseja analisar quantos (N) primeiros pedidos?", Integer.class);
        double valorMinimo = lerOpcao("Qual é o valor mínimo do pedido? (ex: 50.0)", Double.class);

        // Usa o condicional (Predicate) testando se o valorFinal é maior que o valor mínimo
        Fila<Pedido> filtrados = filaPedidos.filtrar(p -> p.valorFinal() > valorMinimo, qtd);

        System.out.println("\n--- Pedidos que atendem ao critério ---");
        if (filtrados.vazia()) {
            System.out.println("Nenhum pedido encontrado com valor acima de R$ " + valorMinimo);
        } else {
            // Imprime e esvazia a fila temporária de resultados
            while (!filtrados.vazia()) {
                System.out.println(filtrados.desenfileirar().toString());
            }
        }
    }

    public static void exibirPedidosComProduto() {
        cabecalho();
        int qtd = lerOpcao("Deseja analisar quantos (N) primeiros pedidos?", Integer.class);
        
        System.out.println("Qual produto deseja buscar nos pedidos?");
        Produto produtoBuscado = localizarProduto(); // Reaproveita o método de busca

        if (produtoBuscado == null) {
            System.out.println("\nProduto não encontrado. Busca cancelada.");
            return;
        }

        // Usa o condicional (Predicate) verificando se a função existeNoPedido NÃO retorna nulo
        Fila<Pedido> filtrados = filaPedidos.filtrar(p -> p.existeNoPedido(produtoBuscado) != null, qtd);

        System.out.println("\n--- Pedidos que contêm o produto: " + produtoBuscado.descricao + " ---");
        if (filtrados.vazia()) {
            System.out.println("Nenhum pedido contém este produto.");
        } else {
            // Imprime e esvazia a fila temporária de resultados
            while (!filtrados.vazia()) {
                System.out.println(filtrados.desenfileirar().toString());
            }
        }
    }
    
    public static void main(String[] args) {
        
        teclado = new Scanner(System.in, Charset.forName("UTF-8"));
        
        nomeArquivoDados = "produtos.txt";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        
        Pedido pedido = null;
        
        int opcao = -1;
      
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> mostrarProduto(localizarProduto());
                case 3 -> mostrarProduto(localizarProdutoDescricao());
                case 4 -> pedido = iniciarPedido();
                case 5 -> finalizarPedido(pedido);
                case 6 -> listarProdutosPedidosRecentes();
                case 7 -> exibirValorMedioPrimeirosPedidos();
                case 8 -> exibirPedidosAcimaDeValor();
                case 9 -> exibirPedidosComProduto();
            }
            if (opcao != 0) pausa();
        } while(opcao != 0);       

        teclado.close();    
    }
}