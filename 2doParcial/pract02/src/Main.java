import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Pattern;

public class Main {

    // ===== Colores ANSI (personalización de la terminal) =====
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String WHITE = "\033[0;37m";
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String RESET = "\033[0m";

    // Usuario: empieza con letra, 4 a 16 caracteres (letras, números o guion bajo)
    private static final Pattern USER_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z0-9_]{3,15}$");

    private static String currentUser = "invitado";

    public static void main(String[] args) {
        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r)) {

            printBanner();
            String input;
            boolean running = true;

            while (running) {
                printPrompt();
                input = br.readLine();

                // Ctrl+D / fin de la entrada
                if (input == null) break;

                if (input.trim().equalsIgnoreCase("exit()")) {
                    System.out.println(YELLOW + "Hasta luego, " + currentUser + " 👋" + RESET);
                    running = false;
                } else {
                    String res = fnInput(input);
                    if (!res.isEmpty()) System.out.println(res);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ===== Procesa el comando escrito =====
    public static String fnInput(String input) {
        if (input.trim().isEmpty()) return "";

        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();

        switch (command) {
            case "help":
                return cmdHelp();
            case "login":
                return cmdLogin(tokens);
            default:
                return RED + "Comando no reconocido: '" + tokens[0] + "'. Escribe 'help' para ver los comandos." + RESET;
        }
    }

    // ===== Comando 1: help =====
    private static String cmdHelp() {
        return YELLOW + "Comandos disponibles:\n" + RESET
                + "  help            Muestra esta ayuda\n"
                + "  login <usuario> Inicia sesión (4-16 caracteres, empieza con letra, solo letras/números/_)\n"
                + "  exit()          Cierra la terminal";
    }

    // ===== Comando 2: login (verifica el usuario con regex) =====
    private static String cmdLogin(String[] tokens) {
        if (tokens.length != 2) {
            return RED + "Uso: login <usuario>" + RESET;
        }
        String user = tokens[1];
        if (!USER_PATTERN.matcher(user).matches()) {
            return RED + "Usuario inválido. Debe empezar con letra, tener de 4 a 16 caracteres y solo letras, números o '_'." + RESET;
        }
        currentUser = user;
        return GREEN + "Sesión iniciada. Bienvenido, " + currentUser + " ✔" + RESET;
    }

    // ===== Personalización =====
    private static void printBanner() {
        System.out.println(BLUE_BACKGROUND + WHITE + " Terminal CyP - Práctica 02 " + RESET);
        System.out.println("Escribe 'help' para ver los comandos.\n");
    }

    private static void printPrompt() {
        System.out.println(CYAN_BACKGROUND + "@" + currentUser + " 📁/~" + RESET);
        System.out.print("--> ");
    }
}