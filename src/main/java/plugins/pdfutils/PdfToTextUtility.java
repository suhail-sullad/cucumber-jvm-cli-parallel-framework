package plugins.pdfutils;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripper;

public class PdfToTextUtility {
	
	private String extractext(File source,int startpage,int endpage){
		
		try {
			PDDocument doc = PDDocument.load(source);
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setStartPage(startpage);
			stripper.setEndPage(endpage);
			return stripper.getText(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static double comparepdftext(File file1,File file2){
		
		
		
		return 0;
	}
	
	
	private void dumppagetoimage(PDPage page)
	{
		
	}

}