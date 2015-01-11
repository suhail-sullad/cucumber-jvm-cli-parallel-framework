
package core.customhandlers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelHandler {

	private HashMap<String, Sheet> importedSheets = new HashMap<String, Sheet>();
	private Sheet selectedSheet = null;

	public ExcelHandler(File excelWorkbook) throws Exception {
		Workbook workbook = WorkbookFactory.create(excelWorkbook);
		List<String> sheetNames = new ArrayList<String>();
		List<Sheet> sheetData = new ArrayList<Sheet>();
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			this.importedSheets.put(workbook.getSheetName(i),
					workbook.getSheetAt(i));
		}
		if (sheetData.size() != sheetNames.size()) {
			throw new Exception("Cannot map sheets to sheet names");
		}
		for (int sheetNumber = 0; sheetNumber < sheetData.size(); sheetNumber++) {

		}
	}

	public void selectSheet(String sheetName) throws Exception {
		if (this.importedSheets.containsKey(sheetName)) {
			this.selectedSheet = importedSheets.get(sheetName);
		} else {
			throw new Exception("Sheet with name '" + sheetName
					+ "' doesn't exist!");
		}
	}

	public String selectedSheetName() throws Exception {
		return this.selectedSheet.getSheetName();
	}

	/**
	 * Get a specific column from the Excel Worksheet (The first column is
	 * column 1)
	 *
	 * @param columnNumber
	 * @return
	 * @throws Exception
	 */
	public HashMap<Integer, Cell> getColumn(int columnNumber) throws Exception {
		return getColumn(columnNumber, false);
	}

	/**
	 * Get a specific column from the Excel Worksheet You can optionally skip
	 * the top row of the column to ensure column titles are not pulled into the
	 * data set (The first column is column 1)
	 *
	 * @param columnNumber
	 * @param skipFirstRow
	 * @return
	 * @throws Exception
	 */
	public HashMap<Integer, Cell> getColumn(int columnNumber,
			boolean skipFirstRow) throws Exception {
		if (this.selectedSheet.equals(null)) {
			throw new Exception(
					"No sheet selected.  You must select a sheet before trying to get data!");
		} else if (columnNumber > this.selectedSheet.getRow(0)
				.getPhysicalNumberOfCells()) {
			throw new Exception("There are only "
					+ this.selectedSheet.getRow(0).getPhysicalNumberOfCells()
					+ " columns in this sheet.  Unable to select column "
					+ columnNumber + "!");
		}
		HashMap<Integer, Cell> selectedColumn = new HashMap<Integer, Cell>();
		for (int i = 0; i < this.selectedSheet.getPhysicalNumberOfRows(); i++)
			selectedColumn.put(selectedColumn.size() + 1, this.selectedSheet
					.getRow(i).getCell(columnNumber));
		if (skipFirstRow) {
			selectedColumn.remove(1);
		}
		return selectedColumn;
	}

	/**
	 * Get a specific row from the Excel Worksheet (The first row is row 1)
	 *
	 * @param rowNumber
	 * @return
	 * @throws Exception
	 */
	public HashMap<Integer, Cell> getRow(int rowNumber) throws Exception {
		return getRow(rowNumber, false);
	}

	/**
	 * Get a specific row from the Excel Worksheet You can optionally skip the
	 * first column of the row to ensure row titles are not pulled into the data
	 * set (The first row is row 1)
	 *
	 * @param rowNumber
	 * @param skipFirstColumn
	 * @return
	 * @throws Exception
	 */
	public HashMap<Integer, Cell> getRow(int rowNumber, boolean skipFirstColumn)
			throws Exception {
		if (this.selectedSheet.equals(null)) {
			throw new Exception(
					"No sheet selected.  You must select a sheet before trying to get data!");
		} else if (rowNumber > this.selectedSheet.getPhysicalNumberOfRows()) {
			throw new Exception("There are only "
					+ this.selectedSheet.getPhysicalNumberOfRows()
					+ " rows in this sheet.  Unable to select row " + rowNumber
					+ "!");
		}
		HashMap<Integer, Cell> selectedRow = new HashMap<Integer, Cell>();
		for (Cell currentCell : this.selectedSheet.getRow(rowNumber - 1)) {
			selectedRow.put(selectedRow.size() + 1, currentCell);
		}
		if (skipFirstColumn) {
			selectedRow.remove(1);
		}
		return selectedRow;
	}

	/**
	 * This will map two rows into a HashMap. The key row will be converted into
	 * a string that can be used to reference the matching data in he value row.
	 * You can optionally skip the first column of the row to ensure row titles
	 * are not pulled into the data set (The first row is row 1)
	 */
	public HashMap<String, Cell> mapTwoRows(int keyRow, int valueRow)
			throws Exception {
		return mapTwoRows(keyRow, valueRow, false);
	}

	/**
	 * This will map two rows into a HashMap. The key row will be converted into
	 * a string that can be used to reference the matching data in he value row.
	 * You can optionally skip the first column of the row to ensure row titles
	 * are not pulled into the data set (The first row is row 1)
	 *
	 * @param keyRow
	 *            The row number to be used as the HashMap key.
	 * @param valueRow
	 *            The row number to be used as the HashMap value.
	 * @param skipFirstColumn
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Cell> mapTwoRows(int keyRow, int valueRow,
			boolean skipFirstColumn) throws Exception {
		if (this.selectedSheet.equals(null)) {
			throw new Exception(
					"No sheet selected.  You must select a sheet before trying to get data!");
		} else if ((keyRow > this.selectedSheet.getPhysicalNumberOfRows())
				|| (valueRow > this.selectedSheet.getPhysicalNumberOfRows())) {
			throw new Exception("There are only "
					+ this.selectedSheet.getPhysicalNumberOfRows()
					+ " rows in this sheet.  Unable to select rows " + keyRow
					+ " and " + valueRow + "!");
		}
		HashMap<String, Cell> selectedRows = new HashMap<String, Cell>();
		for (int i = 0; i < this.selectedSheet.getRow(keyRow - 1)
				.getLastCellNum(); i++)
			selectedRows.put(this.selectedSheet.getRow(keyRow - 1).getCell(i)
					.getStringCellValue(), this.selectedSheet
					.getRow(valueRow - 1).getCell(i));

		int startPoint = 0;
		if (skipFirstColumn) {
			startPoint = 1;
		}
		return selectedRows;
	}

	/**
	 * This will map two columns into a HashMap. The key column will be
	 * converted into a string that can be used to reference the matching data
	 * in he value column. You can optionally skip the first row of the column
	 * to ensure column titles are not pulled into the data set (The first row
	 * is row 1)
	 */
	public HashMap<String, Cell> mapTwoColumns(int keyColumn, int valueColumn)
			throws Exception {
		return mapTwoColumns(keyColumn, valueColumn, false);
	}

	/**
	 * This will map two columns into a HashMap. The key column will be
	 * converted into a string that can be used to reference the matching data
	 * in he value column. You can optionally skip the first row of the column
	 * to ensure column titles are not pulled into the data set (The first row
	 * is row 1)
	 *
	 * @param keyColumn
	 *            The row number to be used as the HashMap key.
	 * @param valueColumn
	 *            The row number to be used as the HashMap value.
	 * @param skipFirstColumn
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Cell> mapTwoColumns(int keyColumn, int valueColumn,
			boolean skipFirstColumn) throws Exception {
		if (this.selectedSheet.equals(null)) {
			throw new Exception(
					"No sheet selected.  You must select a sheet before trying to get data!");
		} else if ((keyColumn > this.selectedSheet.getPhysicalNumberOfRows())
				|| (valueColumn > this.selectedSheet.getPhysicalNumberOfRows())) {
			throw new Exception("There are only "
					+ this.selectedSheet.getPhysicalNumberOfRows()
					+ " columnss in this sheet.  Unable to select columns "
					+ keyColumn + " and " + valueColumn + "!");
		}
		
		int startPoint = 0;
		if (skipFirstColumn) {
			startPoint = 1;
		}
		HashMap<String, Cell> selectedColumns = new HashMap<String, Cell>();
		for (int i = 0; i < this.selectedSheet.getPhysicalNumberOfRows(); i++)
			selectedColumns.put(this.selectedSheet.getRow(i).getCell(keyColumn-1).getStringCellValue(), this.selectedSheet.getRow(i).getCell(valueColumn-1));
		return selectedColumns;
	}

	/**
	 * Get a specific cell from the Excel Worksheet (The top left cell is
	 * assumed to be in position 1, 1)
	 *
	 * @param column
	 * @param row
	 * @return
	 * @throws Exception
	 */
	public Cell getCellData(int column, int row) throws Exception {
		column--;
		row--;
		if (this.selectedSheet.equals(null)) {
			throw new Exception(
					"No sheet selected.  You must select a sheet before trying to get data!");
		}
		return this.selectedSheet.getRow(row).getCell(column);
	}

}
