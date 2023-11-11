/**
 * Entry point for program. Houses main.
 */
public class Program {
    /**
     * Display a fancy title for the program.
     */
    public static void displayTitle() {
        System.out.println("");
        System.out.println("  ______                                  _______             _             ");
        System.out.println(" |  ____|                                |__   __|           | |            ");
        System.out.println(" | |__  __  ___ __   ___ _ __  ___  ___     | |_ __ __ _  ___| | _____ _ __ ");
        System.out.println(" |  __| \\ \\/ / '_ \\ / _ \\ '_ \\/ __|/ _ \\    | | '__/ _` |/ __| |/ / _ \\ '__|");
        System.out.println(" | |____ >  <| |_) |  __/ | | \\__ \\  __/    | | | | (_| | (__|   <  __/ |   ");
        System.out.println(" |______/_/\\_\\ .__/ \\___|_| |_|___/\\___|    |_|_|  \\__,_|\\___|_|\\_\\___|_|   ");
        System.out.println("             | |                                                            ");
        System.out.println("             |_|                                                            ");
    }

    /**
     * Prompt the user what action they want to perform.
     * 
     * @return the user's choice of action
     */
    public static int getAction() {
        System.out.println("\n");
        System.out.println("  1. Display all data");
        System.out.println("  2. Add expense record");
        System.out.println("  3. Add income record");
        System.out.println("  4. Get monthly insights");
        System.out.println("  5. Get monthly tithing");
        System.out.println("  0. Quit program");
        System.out.println("");
        System.out.print("> ");

        int choice = Integer.parseInt(System.console().readLine());
        return choice;
    }

    /**
     * Program main.
     * 
     * @param args
     */
    public static void main(String[] args) {
        DBInteractor.initDB();
        displayTitle();
        if (DBInteractor.connect() == null) {
            System.exit(1);
        }
        while (true) {
            switch (getAction()) {
                // Print all data
                case 1:
                    DBInteractor.displayData();
                    break;
                // Add expense record
                case 2:
                    DBInteractor.addRecord(true);
                    break;
                // Add income record
                case 3:
                    DBInteractor.addRecord();
                    break;
                // Print monthly insights
                case 4:
                    DBInteractor.getInsights();
                    break;
                // Print monthly tithing
                case 5:
                    DBInteractor.getInsights(true);
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("ERROR:  Invalid action");
            }
        }
    }
}
