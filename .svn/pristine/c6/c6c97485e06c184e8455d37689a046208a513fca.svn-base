package xtensus.beans.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.entity.File;
import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.Request;
import com.asprise.imaging.core.RequestOutputItem;
import com.asprise.imaging.core.Result;
import com.asprise.imaging.core.scan.twain.TwainException;
import com.asprise.imaging.scan.ui.workbench.AspriseScanUI;
import com.asprise.*;
/**
 * @author issam
 * 
 */
@Component
@Scope("request")
public class FileUploadBean {

	@Autowired
	private VariableGlobale vbg;

	private ArrayList<File> files = new ArrayList<File>();
	private int uploadsAvailable = 1;
	private boolean autoUpload = true;
	private boolean useFlash = true;

	public int getSize() {
		if (getFiles().size() > 0) {
			return getFiles().size();
		} else {
			return 0;
		}
	}

	public FileUploadBean() {
	}

	public void paint(OutputStream stream, Object object) throws IOException {
		stream.write(getFiles().get((Integer) object).getData());
	}

	synchronized public void listener(UploadEvent event) throws Exception {

		UploadItem item = event.getUploadItem();
		
		vbg.setDocumentPath(item.getFileName());
		vbg.setUploadedData(item.getData());
	    System.out.println("File Name : " + item.getFileName());
        String fileName = item.getFileName();
        String documentType = fileName.substring(fileName.lastIndexOf("."));
        System.out.println("Document Type  : "+documentType);
        vbg.setDocumentType(documentType);
		File file = new File();
		file.setLength(item.getData().length);
		file.setName(item.getFileName());
		file.setData(item.getData());
		files.add(file);
		uploadsAvailable--;

	}

	public String clearUploadData() {
		files.clear();
		setUploadsAvailable(1);
		return null;
	}

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}

	public ArrayList<File> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<File> files) {
		this.files = files;
	}

	public int getUploadsAvailable() {
		return uploadsAvailable;
	}

	public void setUploadsAvailable(int uploadsAvailable) {
		this.uploadsAvailable = uploadsAvailable;
	}

	public boolean isAutoUpload() {
		return autoUpload;
	}

	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}

	public boolean isUseFlash() {
		return useFlash;
	}

	public void setUseFlash(boolean useFlash) {
		this.useFlash = useFlash;
	}

	
	public void scanBox() {
		  System.out.println("scanBox");
		/*  ----------------- ASPRISE ------------------- */		
				
			      //Imaging im = new Imaging(null);
			      Imaging im = new Imaging(null,0);
			      
			      // System.out.println(im.scanListSources());
			      
		 	   try {   
		 		  String emplacement="/var/www/html/tmp/doc02";
				   
				     System.out.println("emplacement "+emplacement);
				      
//				      Request req = new Request().addOutputItem(new RequestOutputItem(Imaging.OUTPUT_SAVE, Imaging.FORMAT_PDF)
//					    	               				    	               .setSavePath(emplacement)
//					    	             						);
				 
//				      Request req = new Request().addOutputItem(new RequestOutputItem(Imaging.OUTPUT_SAVE, Imaging.FORMAT_PDF)
//				      .setSavePath("${TMP}"+ java.io.File.separator+java.io.File.separator+"doc02${EXT}"));
				      Request req = new Request().addOutputItem(new RequestOutputItem(Imaging.OUTPUT_SAVE, Imaging.FORMAT_PDF)
				      .setSavePath("/var/www/html/tmp/doc02"));
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
			            vbg.setScannedData(tab);
			            
			      
			      
			   }catch (TwainException e) {
				   e.printStackTrace();
				   System.out.println("Nothing to show !");
			   } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   }
	
}