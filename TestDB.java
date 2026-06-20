import java.sql.*;

public class TestDB {
    public static void main(String[] args) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/sik_new", "root", "Permata!24");
            
            PreparedStatement ps = conn.prepareStatement(
                "select group_concat(diagnosa_pasien.kd_penyakit separator ', ') as kd_penyakit, " +
                "sum(ifnull(inacbg_dummy.biaya, 0)) as tarif " +
                "from diagnosa_pasien " +
                "left join inacbg_dummy on diagnosa_pasien.kd_penyakit=inacbg_dummy.kd_penyakit " +
                "where diagnosa_pasien.no_rawat=?");
            // Let's find NY RAIHANIA's no_rawat
            PreparedStatement ps2 = conn.prepareStatement("select rp.no_rawat from reg_periksa rp join pasien p on rp.no_rkm_medis=p.no_rkm_medis where p.nm_pasien like '%RAIHANIA%'");
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                String no_rawat = rs2.getString("no_rawat");
                System.out.println("Found no_rawat: " + no_rawat);
                ps.setString(1, no_rawat);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("kd_penyakit: " + rs.getString("kd_penyakit"));
                    System.out.println("tarif: " + rs.getDouble("tarif"));
                }
            } else {
                System.out.println("Patient not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
