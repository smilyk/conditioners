package conditioner.utils;

import conditioner.model.PriceEntity;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger LOGGER = LogManager.getLogger(ExcelUtils.class);

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
                    if (cellIndex == 0) { // наименование
                        price.setNamePosition(currentCell.getStringCellValue());
                    } else if (cellIndex == 1) { // модель
                        price.setModelPosition(currentCell.getStringCellValue());
                    } else if (cellIndex == 2) { // цена usa
                        price.setPriceUsa(currentCell.getNumericCellValue());
                    } else if (cellIndex == 3) { // цена укр
                        price.setPriceUkr(currentCell.getNumericCellValue());
                    } else if (cellIndex == 4) { // удиница измерения
                        price.setUnitsPosition(currentCell.getStringCellValue());
                    } else if (cellIndex == 5) { // уена рынок
                        price.setPriceMarketPosition(currentCell.getNumericCellValue());
                    } else if (cellIndex == 6) { // коэф
                        price.setCoefficientPosition(currentCell.getNumericCellValue());
                    } else if (cellIndex == 7) { // уена работы
                        price.setWorkPricePosition(currentCell.getNumericCellValue());
                    } else if (cellIndex == 8) { // описание
                        price.setDescriptionPosition(currentCell.getStringCellValue());
                    }
                    cellIndex++;
                }
                LOGGER.info("Parsing of file was finished");
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

        if (!EXCELTYPE.equals(file.getContentType())) {
            LOGGER.error("wrong format of file");
            return false;
        }
        return true;
    }
}

