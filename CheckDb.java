import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import fungsi.koneksiDB;

public class CheckDb {
    public static void main(String[] args) {
        try {
            Connection conn = koneksiDB.condb();
            Statement stmt = conn.createStatement();
            
            String[] tables = {"resep_obat", "detail_pemberian_obat", "aturan_pakai", "gudangbarang", "tampjurnal", "pcare_obat_diberikan"};
            for (String t : tables) {
                System.out.println("Table: " + t);
                ResultSet rs = stmt.executeQuery("DESCRIBE " + t);
                int colCount = 0;
                while(rs.next()) {
                    colCount++;
                }
                System.out.println("  Total Columns: " + colCount);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
