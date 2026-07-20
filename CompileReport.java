import net.sf.jasperreports.engine.JasperCompileManager;

public class CompileReport {
    public static void main(String[] args) {
        try {
            JasperCompileManager.compileReportToFile("report/rptSuratPersetujuanUmum.jrxml", "report/rptSuratPersetujuanUmum.jasper");
            System.out.println("Berhasil compile jrxml menjadi jasper!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
