import java.text.Normalizer;
import java.util.Scanner;

public class VerificadorPalindromo {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        
        System.out.println("Digite a frase ou palavra para verificar se e um palindromo:");
        String frase = teclado.nextLine(); 
        
        String limpa = Normalizer.normalize(frase, Normalizer.Form.NFD);
        limpa = limpa.replaceAll("[^a-zA-Z]", "").toLowerCase();

        Pilha<Character> pilha = new Pilha<>();

        for (int i = 0; i < limpa.length(); i++) {
            pilha.empilhar(limpa.charAt(i));
        }

        boolean ehPalindromo = true;
        for (int i = 0; i < limpa.length(); i++) {
            if (limpa.charAt(i) != pilha.desempilhar()) {
                ehPalindromo = false;
                break; 
            }
        }

        if (ehPalindromo) {
            System.out.println("\"" + frase + "\" e um palindromo!");
        } else {
            System.out.println("\"" + frase + "\" nao e um palindromo.");
        }
        teclado.close();
    }
}