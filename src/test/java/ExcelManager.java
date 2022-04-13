import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class ExcelManager {

    //method to return a object which contains excel data
    public org.json.simple.JSONObject get_data(int value) throws IOException {
        String excelPath = "./src/test/resources/exceldata.xlsx";
        String name = null;
        String gender = null;
        String email = null;
        String status = null;
        XSSFWorkbook workbook = new XSSFWorkbook(excelPath);
        XSSFSheet sheet = workbook.getSheetAt(0);

        //loop for traversing all the columns in the row
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                name = sheet.getRow(value).getCell(i).getStringCellValue();
            }
            if (i == 1) {
                gender = sheet.getRow(value).getCell(i).getStringCellValue();
            }
            if (i == 2) {
                email = sheet.getRow(value).getCell(i).getStringCellValue();
            }
            if (i == 3) {
                status = sheet.getRow(value).getCell(i).getStringCellValue();
            }
        }

        //creating a new json object to store the values
        org.json.simple.JSONObject obj = new org.json.simple.JSONObject();
        obj.put("name", name);
        obj.put("gender", gender);
        obj.put("email", email);
        obj.put("status", status);
        return obj;
    }
}
