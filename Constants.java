import java.time.format.DateTimeFormatter;

public class Constants {
    // Database name
    public static final String DBNAME = "transactions";

    // Table name
    public static final String TABLENAME = "records";

    // Column labels
    public static final String[] HEADINGS = new String[] { "Date", "Debit", "Credit", "Where", "Account",
            "Description" };

    // Display pattern for LocalDates
    public static final DateTimeFormatter FORMATPATTERN = DateTimeFormatter.ofPattern("MMM d, yyyy");
}
