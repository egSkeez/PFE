package xtensus.beans.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.Request;
import com.asprise.imaging.core.RequestOutputItem;
import com.asprise.imaging.core.Result;
import com.asprise.imaging.core.scan.twain.TwainException;

public class TestScann {

	/**
	 * @param args
	 */
	
	@Autowired
	private VariableGlobale vbg;
	public static void main(String[] args) {
		  System.out.println("scanBox");
			/*  ----------------- ASPRISE ------------------- */		
					
				      Imaging im = new Imaging(null);
				      
				      
				      // System.out.println(im.scanListSources());
				      
			 	   try {   
				      String emplacement="${TMP}\\\\doc_scanne${EXT}";
				   
				     System.out.println("emplacement "+emplacement);
				      
//				      Request req = new Request().addOutputItem(new RequestOutputItem(Imaging.OUTPUT_SAVE, Imaging.FORMAT_PDF)
//					    	               				    	               .setSavePath(emplacement)
//					    	             						);
				      
				      
				      
//				      RequestOutputItem roi=new RequestOutputItem();
//				      roi.setFormat(Imaging.FORMAT_PDF);
				      Request req = new Request().addOutputItem(new RequestOutputItem(Imaging.OUTPUT_SAVE, Imaging.FORMAT_PDF)
				      .setSavePath("${TMP}\\\\doc02${EXT}"));
				      
				      //Result res = im.scan(req, im.scanGetDefaultSourceName(), true, true);
				      System.out.println("\n>>>>>>>>>>>>"+im.scanListSources());
				      System.out.println("\n"+im.scanListSources().get(1).getSourceName());
				      Result res = im.scan(req, im.scanListSources().get(1).getSourceName(), true, true);
				      System.out.println("\n Sacanner par défaut "+im.scanGetDefaultSourceName());
//				       String ress = im.scanAndReturnRaw("", "", true, true);				      
				      
				      System.out.println(res == null ? "(null)" : res.getPdfFile());
				      
			            byte[] tab = new byte[((int) res.getPdfFile().length())];
			            BufferedInputStream input = new BufferedInputStream(new FileInputStream(res.getPdfFile()));
			            input.read(tab, 0, tab.length);
			            System.out.println("\n Size "+tab.length);
			            System.out.println("\n name "+tab.length);
			            System.out.println("\n extension "+tab.length);
			                      
			            
			            
			             //tab;
				      
				      
				      
				      //C:\Users\AMANIH~1\AppData\Local\Temp\doc02.pdf
				   }catch (TwainException e) {
					   e.printStackTrace();
					   System.out.println("Nothing to show !");
				   } catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	}
	
	
	

}
