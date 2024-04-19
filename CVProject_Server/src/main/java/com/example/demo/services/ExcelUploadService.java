package com.example.demo.services;

import com.example.demo.models.Cv;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@RequiredArgsConstructor
public class ExcelUploadService {
    public static boolean isValidExcelFile(MultipartFile file) {
        return (file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx"));
    }

    public static List<Cv> getCvsFromExcel(InputStream inputStream){
        List<Cv> cvs = new ArrayList<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Cv");
            int rows = 0;
            for (Row row : sheet){
                if(rows==0){
                    rows++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cells = 0;
                Cv cv = new Cv();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cells){
                        case 0:
                            cv.setFullName(cell.getStringCellValue());
                            break;
                        case 1:
                            cv.setDateOfBirth(cell.getLocalDateTimeCellValue().toLocalDate());
                            break;
                        case 2:
                            cv.setTrainingSystem(cell.getStringCellValue());
                            break;
                        case 3:
                            cv.setUniversity(cell.getStringCellValue());
                            break;
                        case 4:
                            cv.setGpa(cell.getStringCellValue());
                            break;
                        case 5:
                            cv.setSkill(cell.getStringCellValue());
                            break;
                        case 6:
                            cv.setApplyPosition(cell.getStringCellValue());
                            break;
                        case 7:
                            cv.setLinkCV(cell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cells++;
                }
                cvs.add(cv);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return cvs;
    }

}
