import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

public class DBInteractor {
    private static final String url = "jdbc:postgresql://localhost:5432/";
    private static final String user = "postgres";
    private static final String password = "Nathanis!1post";

    /**
     * Establishes a connection with the local PostgreSQL server in order to verify
     * aspects of the {@code transactions} database.
     * <p>
     * Checks if the database already exists then creates it if not.
     * <p>
     * Checks if database has the {@code records} table then creates it if not.
     */
    public static void initDB() {
        Connection conn = null;
        try {
            // Default connection to PostgreSQL server.
            conn = DriverManager.getConnection(url, user, password);

            // Check if the database is already initialized.
            try {
                PreparedStatement stmt = conn
                        .prepareStatement("SELECT datname FROM pg_database WHERE datname = '" + Constants.DBNAME + "'");
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Database `" + Constants.DBNAME + "` already exists.");
                }
                // Create database if it doesn't already exist.
                else {
                    stmt = conn.prepareStatement("CREATE DATABASE " + Constants.DBNAME);
                    stmt.executeUpdate();

                    System.out.println("Database `" + Constants.DBNAME + "` created successfully.");
                }
            } catch (SQLException er) {
                System.out.println("Error in creating `" + Constants.DBNAME + "` database.");
                er.printStackTrace();
            }

            // Check if database has `records` table.
            try {
                // Connection to `transactions` database now that it is gauranteed to exist
                conn = connect();
                PreparedStatement stmt = conn
                        .prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_name = '"
                                + Constants.TABLENAME + "'");
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Table `" + Constants.TABLENAME + "` was already in the database.");
                    // TODO Not sure why it wouldn't, but verify table has correct fields.
                }
                // Create table if it doesn't already exist.
                else {
                    stmt = conn.prepareStatement(
                            "CREATE TABLE " + Constants.TABLENAME
                                    + " (id SERIAL PRIMARY KEY, date DATE, debit DECIMAL, credit DECIMAL, location VARCHAR(25), account VARCHAR(25), description VARCHAR(50))");
                    stmt.executeUpdate();

                    System.out.println("Table `" + Constants.TABLENAME + "` created successfully.");
                }
            } catch (SQLException er) {
                System.out.println("Error in creating `" + Constants.TABLENAME + "` table.");
                er.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("ERROR:\tUnsuccessful connection to PostgreSQL server.");
            e.printStackTrace();
        }
    }

    /**
     * Creates a connection to the local PostgreSQL server and returns the
     * Connection object.
     * 
     * @return {@code Connection} - The PostgreSQL connection to
     *         {@code transactions} database;
     *         <p>
     *         {@code null} if unable to establish
     *         connection.
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url + Constants.DBNAME, user, password);

        } catch (SQLException e) {
            System.out.println("Error in connecting to `" + Constants.DBNAME + "` database.");
            e.printStackTrace();
        }

        return conn;
    }

    /**
     * Gets all rows of record data from the database and displays it to the user.
     */
    public static void displayData() {
        Connection cn = connect();
        try {
            // Display the headings
            String[] headings = Constants.HEADINGS;
            System.out.printf("%-15s%-15s%-15s%-15s%-15s%-35s%n",
                    headings[0],
                    headings[1],
                    headings[2],
                    headings[3],
                    headings[4],
                    headings[5]);

            // PostgreSQL query
            PreparedStatement stmt = cn.prepareStatement("SELECT * FROM records ORDER BY date");
            ResultSet rs = stmt.executeQuery();

            // Display each row of data
            while (rs.next()) {
                System.out.printf("%-15s% -15.2f% -15.2f%-15s%-15s%-35s%n",
                        rs.getDate("date").toLocalDate().format(Constants.FORMATPATTERN),
                        rs.getFloat("debit"),
                        rs.getFloat("credit"),
                        rs.getString("location"),
                        rs.getString("account"),
                        rs.getString("description"));
            }
        } catch (SQLException e) {
            System.out.println("ERROR:\tCould not display data.");
            e.printStackTrace();
        }
    }

    /**
     * Adds an income record to the {@code records} table of the database.
     * <p>
     * Default {@code addRecord} function with {@code boolean expense} argument set
     * to {@code false}.
     */
    public static void addRecord() {
        addRecord(false);
    }

    /**
     * Gets values for a new row from the user and inserts them into the
     * {@code records} table.
     * 
     * @param expense - Type of record to add: expense record if {@code true};
     *                income if {@code false}.
     */
    public static void addRecord(boolean expense) {
        String[] record = new String[6];

        System.out.print("Date ('yyyy-mm-dd') of transaction:\n> ");
        record[0] = System.console().readLine();
        System.out.print("Debit or cash amount ('0' if credit was used):\n> ");
        record[1] = (expense ? "-" : "") + System.console().readLine();
        System.out.print("Credit amount ('0' if debit or cash was used):\n> ");
        record[2] = (expense ? "-" : "") + System.console().readLine();
        System.out.print("Where did this transaction take place:\n> ");
        record[3] = System.console().readLine();
        System.out.print("What account was used ('Cash' if cash was used):\n> ");
        record[4] = System.console().readLine();
        System.out.print("Transaction description:\n> ");
        record[5] = System.console().readLine();

        try {
            Connection conn = connect();
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO " + Constants.TABLENAME + " VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)");
            stmt.setDate(1, Date.valueOf(record[0]));
            stmt.setFloat(2, Float.parseFloat(record[1]));
            stmt.setFloat(3, Float.parseFloat(record[2]));
            stmt.setString(4, record[3]);
            stmt.setString(5, record[4]);
            stmt.setString(6, record[5]);

            stmt.executeUpdate();

            System.out.println((expense ? "Expense" : "Income") + " record added successfully.");

        } catch (SQLException e) {
            System.out.println("ERROR:\tCould not add record.");
            e.printStackTrace();
        }
    }

    /**
     * Gets month and year from user and displays cash flow insights for that date
     * range.
     * <p>
     * Default {@code getInsights} function with {@code boolean tithing} argument
     * set to {@code false}.
     */
    public static void getInsights() {
        getInsights(false);
    }

    /**
     * Gets month and year from user to get insights for.
     * 
     * @param tithing - Display tithing insights if {@code true}; regular insights
     *                if {@code false}.
     */
    public static void getInsights(boolean tithing) {
        System.out.print("Enter month you want insights for as a number (1-12):\n> ");
        int insightMonth = Integer.parseInt(System.console().readLine());
        System.out.print("Year:\n> ");
        int insightYear = Integer.parseInt(System.console().readLine());

        try {
            Connection conn = connect();

            // Get sum of all expenses
            PreparedStatement stmt = conn.prepareStatement("SELECT SUM(debit) FROM " + Constants.TABLENAME
                    + " WHERE debit < 0 AND EXTRACT(YEAR FROM date) = " + insightYear
                    + " AND EXTRACT(MONTH FROM date) = " + insightMonth);
            ResultSet rsExpenses = stmt.executeQuery();
            float totalExpenses = rsExpenses.next() ? rsExpenses.getFloat(1) : 0;

            // Get sum of all income
            stmt = conn.prepareStatement("SELECT SUM(debit) FROM " + Constants.TABLENAME
                    + " WHERE debit > 0 AND EXTRACT(YEAR FROM date) = " + insightYear
                    + " AND EXTRACT(MONTH FROM date) = " + insightMonth);
            ResultSet rsIncome = stmt.executeQuery();
            float totalIncome = rsIncome.next() ? rsIncome.getFloat(1) : 0;

            if (!tithing) {
                System.out.printf("\nTotal Monthly Expenses: %.2f\n", totalExpenses);
                System.out.printf("Total Monthly Income: %.2f\n", totalIncome);
                System.out.printf("\nCash Flow: %.2f", (totalIncome + totalExpenses));
            } else {
                System.out.printf("\nTithing for this month: %.2f", totalIncome / 10);
            }

        } catch (SQLException e) {
            System.out.println("ERROR:\tCoudn't get insights.");
            e.printStackTrace();
        }
    }
}
