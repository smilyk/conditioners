package conditioner.utils;

import conditioner.model.PriceEntity;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {

    public static String EXCELTYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    @SneakyThrows
    public static List parseExcelFile(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet("Price");
            Iterator rows = sheet.iterator();

            List lstCustomers = new ArrayList();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = (Row) rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator cellsInRow = currentRow.iterator();

                PriceEntity price = new PriceEntity();

                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = (Cell) cellsInRow.next();
//cellIndex == 0 => uuid generic in server not from file
                    if (cellIndex == 1) { // FIRM
                        price.setFirm(currentCell.getStringCellValue());
                    } else if (cellIndex == 2) { // Name
                        price.setName(currentCell.getStringCellValue());
                    } else if (cellIndex == 3) { // Model
                        price.setModel(currentCell.getStringCellValue());
                    } else if (cellIndex == 4) { // priceUkr
                        price.setPriceUsa(currentCell.getNumericCellValue());
                    } else if (cellIndex == 5) { // priceUsa
                        price.setPriceUkr( currentCell.getNumericCellValue());
                    } else if (cellIndex == 6) { // coef
                        price.setCoefficient( currentCell.getNumericCellValue());
                    }
                    cellIndex++;
                }

                lstCustomers.add(price);
            }

            // Close WorkBook
            workbook.close();

            return lstCustomers;

        } catch (IOException e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }

    public static boolean isExcelFile(MultipartFile file) {

        if(!EXCELTYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }
}

//TODO logger
