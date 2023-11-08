import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Interacts with the .csv file to read and write data and perform operations
 * using data.
 */
public class CSVInteractor {
    /**
     * Checks if the csv file exists; if it does, verify the header; if it doesn't
     * exist, create a new csv file at that path and add the header.
     */
    public static void InitFile() {
        File csvFile = new File(Constants.CSVFILEPATH);

        // Create a new csv file if a file at the default path was not found.
        if (!csvFile.exists()) {
            try {
                csvFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Verify the file header
        try {
            Scanner scnr = new Scanner(csvFile);

            // If file has no header, write one
            if (!scnr.hasNextLine()) {
                FileWriter writer = new FileWriter(csvFile);
                writer.write(Constants.HEADER);
                writer.close();

            }
            // If the file header doesn't match the default header, get the rest of the
            // data, overwrite the file with proper header, and add the rest of the data.
            else if (scnr.nextLine() != Constants.HEADER) {
                ArrayList<String> lines = new ArrayList<String>();
                while (scnr.hasNextLine()) {
                    lines.add(scnr.nextLine());
                }
                FileWriter writer = new FileWriter(csvFile);
                writer.write(Constants.HEADER + "\n");
                for (String line : lines) {
                    writer.write(line + "\n");
                }
                writer.close();
            }
            scnr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read all the data in the file and print it out neatly to the console.
     */
    public static void DisplayData() {

        // Display the headings
        String[] headings = Constants.HEADER.split(",");

        System.out.printf("%-15s", headings[0]);
        System.out.printf("%-15s", headings[1]);
        System.out.printf("%-15s", headings[2]);
        System.out.printf("%-15s", headings[3]);
        System.out.printf("%-15s", headings[4]);
        System.out.printf("%-35s", headings[5]);

        System.out.println();

        try {
            Scanner reader = new Scanner(new File(Constants.CSVFILEPATH));
            // Skip the header
            reader.nextLine();

            // Display each row of data
            while (reader.hasNextLine()) {
                String[] recordData = reader.nextLine().split(",");
                System.out.printf("%-15s",
                        LocalDate.parse(recordData[0], Constants.DEFAULTFORMATTER)
                                .format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
                System.out.printf("% -15.2f", Double.parseDouble(recordData[1]));
                System.out.printf("% -15.2f", Double.parseDouble(recordData[2]));
                System.out.printf("%-15s", recordData[3]);
                System.out.printf("%-15s", recordData[4]);
                System.out.printf("%-35s\n", recordData[5]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a row of expense data.
     * 
     * Prompts user for each field, and appends the row to the csv file.
     */
    public static void AddExpense() {
        // Store fields in a java array
        String[] expenseRecord = new String[6];

        System.out.print("Date ('m-d-yyyy') of transaction:\n > ");
        expenseRecord[0] = System.console().readLine();
        System.out.print("Debit or cash amount ('0' if credit was used):\n > ");
        expenseRecord[1] = "-" + System.console().readLine();
        System.out.print("Credit amount ('0' if debit or cash was used):\n > ");
        expenseRecord[2] = "-" + System.console().readLine();
        System.out.print("Where did this transaction take place:\n > ");
        expenseRecord[3] = System.console().readLine();
        System.out.print("What account was used ('Cash' if cash was used):\n > ");
        expenseRecord[4] = System.console().readLine();
        System.out.print("Transaction description:\n > ");
        expenseRecord[5] = System.console().readLine();

        try {
            // For each field, append to the line with a comma or newline
            FileWriter writer = new FileWriter(Constants.CSVFILEPATH, true);
            for (int i = 0; i < expenseRecord.length; ++i) {
                writer.append(expenseRecord[i]);
                if (i < expenseRecord.length - 1) {
                    writer.append(",");
                } else {
                    writer.append("\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a row of income data.
     * 
     * Prompts user for each field, and appends the row to the csv file.
     * 
     * Basically the same as AddExpense() but don't add the negative sign for the
     * debit and credit amounts.
     */
    public static void AddIncome() {
        String[] incomeRecord = new String[6];

        System.out.print("Date ('m-d-yyyy') of transaction:\n > ");
        incomeRecord[0] = System.console().readLine();
        System.out.print("Debit or cash amount ('0' if credit was used):\n > ");
        incomeRecord[1] = System.console().readLine();
        System.out.print("Credit amount ('0' if debit or cash was used):\n > ");
        incomeRecord[2] = System.console().readLine();
        System.out.print("Where did this transaction take place:\n > ");
        incomeRecord[3] = System.console().readLine();
        System.out.print("What account was used ('Cash' if cash was used):\n > ");
        incomeRecord[4] = System.console().readLine();
        System.out.print("Transaction description:\n > ");
        incomeRecord[5] = System.console().readLine();

        try {
            FileWriter writer = new FileWriter(Constants.CSVFILEPATH, true);
            for (int i = 0; i < incomeRecord.length; ++i) {
                writer.append(incomeRecord[i]);
                if (i != 5) {
                    writer.append(",");
                } else {
                    writer.append("\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prompt user for which month and year to get insights for, read all records,
     * filter out the date ranges that don't match, and display total income, total
     * expense, and total cash flow.
     */
    public static void GetInsights() {
        System.out.print("What month do you want insights for (as a number 1-12):\n > ");
        int insightMonth = Integer.parseInt(System.console().readLine());
        System.out.print("Year:\n > ");
        int insightYear = Integer.parseInt(System.console().readLine());

        try {
            Scanner reader = new Scanner(new File(Constants.CSVFILEPATH));
            // Skip the header
            reader.nextLine();

            // Get all data
            ArrayList<String> lines = new ArrayList<String>();
            while (reader.hasNextLine()) {
                lines.add(reader.nextLine());
            }
            reader.close();

            double totalExpenses = 0;
            double totalIncome = 0;

            // Use java.time.LocalDate class to get the month and year of each record
            for (String line : lines) {
                LocalDate date = LocalDate.parse(line.split(",")[0], Constants.DEFAULTFORMATTER);
                if (date.getMonthValue() != insightMonth || date.getYear() != insightYear) {
                    continue;
                }

                double debitAmount = Double.parseDouble(line.split(",")[1]);
                if (debitAmount < 0) {
                    totalExpenses += debitAmount;
                } else {
                    totalIncome += debitAmount;
                }
            }

            System.out.printf("\nTotal Monthly Expenses: %.2f\n", totalExpenses);
            System.out.printf("Total Monthly Income: %.2f\n", totalIncome);
            System.out.printf("\nCash Flow: %.2f", (totalIncome + totalExpenses));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Prompt user for which month and year to get tithing for, read all records,
     * filter out the date ranges that don't match, and display tithing amount.
     * 
     * Works basically the same way that GetInsights() does.
     */
    public static void GetTithing() {
        System.out.print("What month do you want insights for (as a number 1-12):\n > ");
        int tithingMonth = Integer.parseInt(System.console().readLine());
        System.out.print("Year:\n > ");
        int tithingYear = Integer.parseInt(System.console().readLine());

        try {
            Scanner reader = new Scanner(new File(Constants.CSVFILEPATH));
            // Skip the header
            reader.nextLine();

            ArrayList<String> lines = new ArrayList<String>();
            while (reader.hasNextLine()) {
                lines.add(reader.nextLine());
            }
            reader.close();

            double monthlyIncome = 0;

            for (String line : lines) {
                LocalDate date = LocalDate.parse(line.split(",")[0], Constants.DEFAULTFORMATTER);
                if (date.getMonthValue() != tithingMonth || date.getYear() != tithingYear) {
                    continue;
                }

                double debitAmount = Double.parseDouble(line.split(",")[1]);
                if (debitAmount > 0) {
                    monthlyIncome += debitAmount;
                }
            }

            System.out.printf("\nTithing for this month: %.2f", monthlyIncome / 10);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
