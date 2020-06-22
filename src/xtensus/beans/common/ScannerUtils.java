package xtensus.beans.common;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;

import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ScannerUtils {

//	static Document pdfDocument = new Document(PageSize.A4);
//	static ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
	
	public static byte[] imageByteArrayToPdfByteArray(byte[] imageByteArray) {
		Document pdfDocument = new Document(PageSize.A3, 5,5,5,5);
		
		ByteArrayOutputStream arrayOutputStream = null;
		try {
			arrayOutputStream = new ByteArrayOutputStream();
			PdfWriter writer = null;
			writer = PdfWriter.getInstance(pdfDocument, arrayOutputStream);
			writer.open();
			pdfDocument.open();
//			pdfDocument.addTitle("Document GBO");
//			pdfDocument.addSubject("Xtensus");
//			pdfDocument.addKeywords("Document GBO");
//			pdfDocument.addAuthor("Xtensus");
//			pdfDocument.addCreator("Xtensus");
			pdfDocument
					.add(com.itextpdf.text.Image.getInstance(imageByteArray));
			pdfDocument.close();
//			writer = PdfWriter.getInstance(pdfDocument, arrayOutputStream);
//			writer.open();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return arrayOutputStream.toByteArray();
	}
	
	
	public static byte[] concatPdf( byte[] pdf1, byte[] pdf2){
		ByteArrayOutputStream arrayOutputStream = null ;
		try {
			arrayOutputStream = new ByteArrayOutputStream() ;
			PdfReader reader1 = new PdfReader(pdf1);
			PdfReader reader2 = new PdfReader(pdf2);
			PdfCopyFields copy = new PdfCopyFields(arrayOutputStream);
			copy.addDocument(reader1);
			copy.addDocument(reader2);
			copy.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return arrayOutputStream.toByteArray() ;
	}

}
