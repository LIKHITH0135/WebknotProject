package com.authentication.jwtAuth.Service;

import com.authentication.jwtAuth.entities.ImportedEmployee;
import com.authentication.jwtAuth.repositories.ImportedEmployeeRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.slf4j.Logger;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeImportService {

    @Autowired
    private ImportedEmployeeRepository importedEmployeeRepository;

    private static final Logger logger = Logger.getLogger(EmployeeImportService.class.getName());

    public void importEmployeesFromCSV(MultipartFile file) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> lines = reader.readAll();
            List<ImportedEmployee> employees = new ArrayList<>();
            for (String[] line : lines) {
                // Assuming the CSV file has a header row, skip it
                if (line[0].equalsIgnoreCase("name")) {
                    continue;
                }
                if (line.length != 5) {
                    logger.warning("Skipping invalid CSV row: " + String.join(",", line));
                    continue;
                }
                try {
                    ImportedEmployee employee = new ImportedEmployee();
                    employee.setName(line[0]);
                    employee.setEmail(line[1]);
                    employee.setTechStack(line[2]);
                    employee.setYearsOfExperience(Double.parseDouble(line[3]));
                    employee.setYearsOfExperienceInWebknot(Double.parseDouble(line[4]));
                    employees.add(employee);
                } catch (NumberFormatException e) {
                    logger.warning("Invalid number format in row: " + String.join(",", line));
                }
            }
            importedEmployeeRepository.saveAll(employees);
        } catch (IOException | CsvException e) {
            logger.severe("Error processing CSV file: " + e.getMessage());
            throw e;
        }
    }

    public void importEmployeesFromExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<ImportedEmployee> employees = new ArrayList<>();
            for (Row row : sheet) {
                // Assuming the Excel file has a header row, skip it
                if (row.getRowNum() == 0) {
                    continue;
                }
                try {
                    ImportedEmployee employee = new ImportedEmployee();
                    employee.setName(row.getCell(0).getStringCellValue());
                    employee.setEmail(row.getCell(1).getStringCellValue());
                    employee.setTechStack(row.getCell(2).getStringCellValue());
                    employee.setYearsOfExperience(row.getCell(3).getNumericCellValue());
                    employee.setYearsOfExperienceInWebknot(row.getCell(4).getNumericCellValue());
                    employees.add(employee);
                } catch (Exception e) {
                    logger.warning("Skipping invalid Excel row: " + row.getRowNum() + ", Error: " + e.getMessage());
                }
            }
            importedEmployeeRepository.saveAll(employees);
        } catch (IOException e) {
            logger.severe("Error processing Excel file: " + e.getMessage());
            throw e;
        }
    }
}
