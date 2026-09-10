import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import fungsi.koneksiDB;

public class CheckDb2 {
    public static void main(String[] args) {
        try {
            Connection conn = koneksiDB.condb();
            Statement stmt = conn.createStatement();
            
            System.out.println("Table: detail_pemberian_obat");
            ResultSet rs = stmt.executeQuery("DESCRIBE detail_pemberian_obat");
            while(rs.next()) {
                System.out.println(rs.getString("Field"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
