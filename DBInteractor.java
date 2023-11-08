import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBInteractor {
    private static final String url = "jdbc:postgresql://localhost:5432/transactions";
    private static final String user = "postgres";
    private static final String password = "Nathanis!1post";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Unsuccessful connection to `transactions` database.");
            e.printStackTrace();
        }
        return conn;
    }

    public static void displayData() {
        Connection cn = connect();
        try {
            PreparedStatement stmt = cn.prepareStatement("SELECT * FROM records");
            ResultSet rs = stmt.executeQuery();

            // Display the headings
            String[] headings = Constants.HEADINGS;

            System.out.printf("%-15s", headings[0]);
            System.out.printf("%-15s", headings[1]);
            System.out.printf("%-15s", headings[2]);
            System.out.printf("%-15s", headings[3]);
            System.out.printf("%-15s", headings[4]);
            System.out.printf("%-35s", headings[5]);

            while (rs.next()) {
                System.out.printf("date=%s, debit=%f, credit=%f, location=%s, account=%s, description=%s",
                        rs.getDate("date").toString(),
                        rs.getFloat("debit"),
                        rs.getFloat("credit"),
                        rs.getString("location"),
                        rs.getString("account"),
                        rs.getString("description"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
