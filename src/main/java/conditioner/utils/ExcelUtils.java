package conditioner.utils;

import conditioner.model.PriceEntity;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
                        System.err.println(currentCell.getCellType());
                        price.setNamePosition(currentCell.getStringCellValue());
                    } else if (cellIndex == 1) { // модель
                        price.setModelPosition(currentCell.getStringCellValue());
                    } else if (cellIndex == 2) { // цена usa
                        if(currentCell.getCellType().equals(CellType.NUMERIC)){
                            price.setPriceUsa(currentCell.getNumericCellValue());
                        }else{
                        System.err.println(currentCell.getCellType());
                        String tmp = currentCell.getStringCellValue();
                        price.setPriceUsa(Double.valueOf(tmp));
                        }
                    } else if (cellIndex == 3) { // цена укр
                        if(currentCell.getCellType().equals(CellType.NUMERIC)){
                            price.setPriceUkr(currentCell.getNumericCellValue());
                        }else {
                            String tmp = currentCell.getStringCellValue();
                            price.setPriceUkr(Double.valueOf(tmp));
                        }
                    } else if (cellIndex == 4) { // удиница измерения
                        price.setUnitsPosition(currentCell.getStringCellValue());
                    } else if (cellIndex == 5) { // цена рынок
                        if(currentCell.getCellType().equals(CellType.NUMERIC)){
                            price.setPriceMarketPosition(currentCell.getNumericCellValue());
                        }else {
                            String tmp = currentCell.getStringCellValue();
                            price.setPriceMarketPosition(Double.valueOf(tmp));
                        }
                    } else if (cellIndex == 6) { // коэф
                        if(currentCell.getCellType().equals(CellType.NUMERIC)){
                            price.setCoefficientPosition(currentCell.getNumericCellValue());
                        }else {
                            String tmp = currentCell.getStringCellValue();
                            price.setCoefficientPosition(Double.valueOf(tmp));
                        }
                    } else if (cellIndex == 7) { // уена работы
                        if(currentCell.getCellType().equals(CellType.NUMERIC)){
                            price.setWorkPricePosition(currentCell.getNumericCellValue());
                        }else {
                            String tmp = currentCell.getStringCellValue();
                            price.setWorkPricePosition(Double.valueOf(tmp));
                        }
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

