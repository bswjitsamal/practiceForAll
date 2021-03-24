package restassured.automation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	static XSSFWorkbook workBook;
	static XSSFSheet sheet;

	public ExcelUtils(String excelPath, String sheetName) throws IOException {

		workBook = new XSSFWorkbook(excelPath);
		sheet = workBook.getSheet(sheetName);

	}

	// Main Directory of the proj
	public static final String currentDir = System.getProperty("user.dir");

	// Location of Test data excel file
	public static String testDataExcelPath = null;

	// Excel WorkBook
	private static XSSFWorkbook excelWBook;

	// Excel Sheet
	private static XSSFSheet excelWSheet;

	// Excel cell
	private static XSSFCell cell;

	// Excel row
	private static XSSFRow row;

	// Row Number
	public static int rowNumber;

	// Column Number
	public static int columnNumber;
	
	
	
	//Setter and Getters of row and columns
    public static void setRowNumber(int pRowNumber) {
        rowNumber = pRowNumber;
    }
 
    public static int getRowNumber() {
        return rowNumber;
    }
 
    public static void setColumnNumber(int pColumnNumber) {
        columnNumber = pColumnNumber;
    }
 
    public static int getColumnNumber() {
        return columnNumber;
    }
 
	
    public static String ExcelPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator
			+ "TestDataSource.xls";

	
    // This method has two parameters: "Test data excel file name" and "Excel sheet name"
    // It creates FileInputStream and set excel file and excel sheet to excelWBook and excelWSheet variables.
	public static void setExcelFileSheet(String sheetName) {

		
		try {
			// Open the Excel file
			FileInputStream ExcelFile = new FileInputStream(ExcelPath);
			excelWBook = new XSSFWorkbook(ExcelFile);
			excelWSheet = excelWBook.getSheet(sheetName);
		} catch (Exception e) {
			try {
				throw (e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	
	//This method reads the test data from the Excel cell.
    //We are passing row number and column number as parameters.
    public static String getCellData(int RowNum, int ColNum) {
        try {
            cell = excelWSheet.getRow(RowNum).getCell(ColNum);
            DataFormatter formatter = new DataFormatter();
            String cellData = formatter.formatCellValue(cell);
            return cellData;
        } catch (Exception e) {
            throw (e);
        }
    }
	
	
    //This method takes row number as a parameter and returns the data of given row number.
    public static XSSFRow getRowData(int RowNum) {
        try {
            row = excelWSheet.getRow(RowNum);
            return row;
        } catch (Exception e) {
            throw (e);
        }
    }
	
	
    //This method gets excel file, row and column number and set a value to the that cell.
    public static void setCellData(String value, int RowNum, int ColNum) {
        try {
            row = excelWSheet.getRow(RowNum);
            cell = row.getCell(ColNum);
            if (cell == null) {
                cell = row.createCell(ColNum);
                cell.setCellValue(value);
            } else {
                cell.setCellValue(value);
            }
            // Constant variables Test Data path and Test Data file name
            FileOutputStream fileOut = new FileOutputStream(ExcelPath);
            excelWBook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            try {
                throw (e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    
    
	

	public static void getSpecificCellData(int rowNum, int colNum) throws IOException {

		DataFormatter formatter = new DataFormatter();
		Object obj = formatter.formatCellValue(sheet.getRow(rowNum).getCell(colNum));

		System.out.println(obj);

	}

	public static void getAllDataExceptHeader(int rowNum, int colNum) throws IOException {

		DataFormatter formatter = new DataFormatter();
		Object obj = formatter.formatCellValue(sheet.getRow(rowNum).getCell(colNum));

		System.out.println(obj);

	}

	public static void getAllCellData() throws IOException {

		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();

		// 1. You can obtain a rowIterator and columnIterator and iterate over them
		System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
		Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			// Now let's iterate over the columns of the current row
			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cel = cellIterator.next();
				String celValue = dataFormatter.formatCellValue(cel);
				System.out.println(celValue + "\t");
			}
			System.out.println();

		}

	}

	public static void getRowCount() throws IOException {

		int rowCount = sheet.getPhysicalNumberOfRows();
		System.out.println("Total nu of row count: " + rowCount);
	}

}
