package com.aws.codestar.projecttemplates.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.net.URL;
import java.lang.*;
import java.net.URISyntaxException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Basic Spring web service controller that handles all GET requests.
 */
@RestController
@RequestMapping("/")
public class HelloWorldController {

	private static final String MESSAGE_FORMAT = "Hello %s!";

    @RequestMapping(value="/validate", method = RequestMethod.GET, produces = "application/json")
    public String helloWorldGet(@RequestParam(value = "id", defaultValue = "") String id) {
        return excelResponse(id);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity helloWorldPost(@RequestParam(value = "name", defaultValue = "World") String name) {
        return ResponseEntity.ok(createResponse(name));
    }

    private String createResponse(String name) {
        return new JSONObject().put("Output", String.format(MESSAGE_FORMAT, name)).toString();
    }
    
    private String excelResponse(String id) {
        String str = "Append here: ";
        File excelFile = new File("event.xlsx");
        boolean emp_present = false;
        //str += HelloWorldController.class.getResource("event.xlsx").getPath();
        try {
        		ClassLoader classLoader = getClass().getClassLoader();
        		File file = new File(classLoader.getResource("/event.xlsx").getFile());
        		FileInputStream fis = new FileInputStream(file);
        
        		// we create an XSSF Workbook object for our XLSX Excel File
        		XSSFWorkbook workbook = new XSSFWorkbook(fis);
        		// we get first sheet
        		XSSFSheet sheet = workbook.getSheetAt(0);
        		System.out.println("Sheet : "+ sheet.getFirstRowNum());
        		// we iterate on rows
        		Iterator<Row> rowIt = sheet.iterator();
            
        		
        		/*int masterSheetColumnIndex = sheet.getColumns();
        	    List<String> ExpectedColumns = new ArrayList<String>();
        	    for (int x = 0; x < masterSheetColumnIndex; x++) {
        	        Cell celll = sheet.getCell(x, 0);
        	        String d = celll.getContents();
        	        ExpectedColumns.add(d);
        	    }
        	    LinkedHashMap<String, List<String>> columnDataValues = new LinkedHashMap<String, List<String>>();

        	    List<String> column1 = new ArrayList<String>();
        	    // read values from driver sheet for each column
        	    for (int j = 0; j < masterSheetColumnIndex; j++) {
        	        column1 = new ArrayList<String>();
        	        for (int i = 1; i < sheet.getRows(); i++) {
        	            Cell cell = sheet.getCell(j, i);
        	            column1.add(cell.getContents());
        	        }
        	        columnDataValues.put(ExpectedColumns.get(j), column1);
        	    }*/
        		
        		
        		
        		while(rowIt.hasNext() && !emp_present) {
        			Row row = rowIt.next();     
        			// iterate on cells for the current row
        			Iterator<Cell> cellIterator = row.cellIterator();
        
        			while (cellIterator.hasNext()) {
        				Cell cell = cellIterator.next();
        				switch (cell.getCellType()) {
        	               case Cell.CELL_TYPE_NUMERIC:
        	            	   if(id.trim().equals(cell.toString().trim())) {
	               					emp_present = true;
	               					break;
               					}
        	                  break;
        	               case Cell.CELL_TYPE_STRING:
        	            	   if(id.trim().equals(cell.toString().trim())) {
               					emp_present = true;
               					break;
               				}
        	                  break;
        	            }
        				
        				/*str += cell.toString()+" ";
        				if(id.trim().equals(cell.toString().trim())) {
        					emp_present = true;
        					break;
        				}*/
        			}
        		}   
        		workbook.close();
        		fis.close();      
        } catch (FileNotFoundException e) {
            str += "File not found";
        } catch (IOException e) {
            str += "IO Exception caught";
        }
        //String val = str + " " + id + " " + emp_present;
        String msg = new JSONObject().put("present", emp_present).toString();
        return msg;
    }
}