package xtensus.services;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.jasper.Constants;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.AcroFields.Item;

import xtensus.beans.utils.CourrierInformations;
import xtensus.beans.utils.ExpediteurType;
import xtensus.entity.Entite;

@Service
public class Export {
	/**** actions ****/
	// Generate EXCEL file
	public void exportToExcel(List<? extends Entite> entitysList,
			String jasperFile, String exportFileName) throws Exception {
		// get context path
		ExternalContext jsfContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		ServletContext servletContext = (ServletContext) jsfContext
				.getContext();
		String webContentRoot = servletContext.getRealPath("/");

		// String res = webContentRoot.substring(0, 16) + "GBO_v1.k";
		// System.out.println(res);
		// webContentRoot = res;

		// Specify compiled jasper file(.jasper)
		String compJasperFile = webContentRoot + "\\WEB-INF\\rapports\\"
				+ jasperFile + ".jasper";
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				entitysList);

		// jasper print to generate .jrprint
		JasperPrint jasperPrint = null;
		jasperPrint = JasperFillManager.fillReport(compJasperFile, null, ds);

		// coding For Excel
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		JRXlsExporter exporterXLS = new JRXlsExporter();
		exporterXLS.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporterXLS.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
				exportFileName + ".xls");
		exporterXLS.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
		exporterXLS.setParameter(
				JExcelApiExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		exporterXLS.exportReport();

		// send redirect to generated excel file
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition", "attachment;filename="
				+ exportFileName + ".xls");
		response.setContentType("application/vnd.ms-excel");
		response.setContentLength(output.size());
		response.getOutputStream()
				.write(output.toByteArray(), 0, output.size());
		FacesContext.getCurrentInstance().responseComplete();
	}

	// Generate WORD file
	public void exportToWord(List<? extends Entite> entitysList,
			String jasperFile, String exportFileName) throws Exception {
		// get context path
		ExternalContext jsfContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		ServletContext servletContext = (ServletContext) jsfContext
				.getContext();
		String webContentRoot = servletContext.getRealPath("/");

		// String res = webContentRoot.substring(0, 16) + "GBO_v1.k";
		// System.out.println(res);
		// webContentRoot = res;

		// Specify compiled jasper file(.jasper)
		String compJasperFile = webContentRoot + "\\WEB-INF\\rapports\\"
				+ jasperFile + ".jasper";
		// Datasource=bean collection
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				entitysList);

		// jasper print to generate .jrprint
		JasperPrint jasperPrint = null;
		jasperPrint = JasperFillManager.fillReport(compJasperFile, null, ds);

		// coding for Word-
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		JRRtfExporter exporterWORD = new JRRtfExporter();
		exporterWORD
				.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporterWORD.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
				exportFileName + ".doc");
		exporterWORD.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
		exporterWORD.exportReport();

		// send redirect to generated word file
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition", "attachment;filename="
				+ exportFileName + ".doc");
		response.setContentType("application/vnd.ms-word");
		response.setContentLength(output.size());
		response.getOutputStream()
				.write(output.toByteArray(), 0, output.size());
		FacesContext.getCurrentInstance().responseComplete();
	}

	// Generate PDF file
	public void exportToPDF(List<? extends Entite> entitysList,
			String jasperFile, String exportFileName) throws Exception {
		// Specify compiled jasper file(.jasper)
		// get context path
		ExternalContext jsfContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		ServletContext servletContext = (ServletContext) jsfContext
				.getContext();
		String webContentRoot = servletContext.getRealPath("/");

		// String res = webContentRoot.substring(0, 16) + "GBO_v1.k";
		// System.out.println(res);
		// webContentRoot = res;

		// Specify compiled jasper file(.jasper)
		String compJasperFile = webContentRoot + File.separator + "WEB-INF"
				+ File.separator + "rapports" + File.separator + jasperFile
				+ ".jasper";
		System.out.println(compJasperFile);
		// Datasource=bean collection
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				entitysList);
		// jasper print to generate .jrprint
		// //////////////////////
		JasperDesign jasperDesign = JRXmlLoader.load(webContentRoot
				+ File.separator + "WEB-INF" + File.separator + "rapports"
				+ File.separator + jasperFile + ".jrxml");// ici met le chemin
															// de ton .jrxml que
															// tu veux récupérer

		JasperReport jasperReport = JasperCompileManager
				.compileReport(jasperDesign);

		JasperCompileManager.compileReportToFile(jasperDesign, compJasperFile);

		// ////////////////////////////////////////////////

		// JasperReport jasperReport =
		// JasperCompileManager.compileReport(jasperFile);
		JasperPrint jasperPrint = null;
		jasperPrint = JasperFillManager.fillReport(compJasperFile, null, ds);

		// jasperPrint = JasperFillManager.fillReport(jasperReport, null, ds);
		// coding for Pdf
		byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

		// send redirect to generated pdf file
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition", "inline;filename="
				+ exportFileName + ".pdf");
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes);
		response.setContentType("application/pdf");
		FacesContext.getCurrentInstance().responseComplete();

		// Visualisation du fichier PDF
		/* JasperViewer.viewReport(jasperPrint); */
	}

	// Generate PDF file
	public void exportToImage(List<? extends Entite> entitysList,
			String jasperFile, String exportFileName) throws Exception {
		ExternalContext jsfContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		ServletContext servletContext = (ServletContext) jsfContext
				.getContext();
		String fileName = exportFileName;
		String webContentRoot = servletContext.getRealPath("/");
		String compJasperFile = webContentRoot + File.separator + "WEB-INF"
				+ File.separator + "rapports" + File.separator + jasperFile
				+ ".jasper";
		System.out.println(compJasperFile);
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				entitysList);
		JasperPrint jasperPrint = null;
		jasperPrint = JasperFillManager.fillReport(compJasperFile, null, ds);

		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		DefaultJasperReportsContext.getInstance();
		JasperPrintManager printManager = JasperPrintManager
				.getInstance(DefaultJasperReportsContext.getInstance());
		File my_file = new File(fileName);
		OutputStream out = response.getOutputStream();
		BufferedImage rendered_image = null;
		rendered_image = (BufferedImage) JasperPrintManager.printPageToImage(
				jasperPrint, 0, 3.6f);
		ImageIO.write(rendered_image, "jpeg", out);

		String fileType = "image/jpeg";
		response.setContentType(fileType);
		response.setHeader("Content-disposition", "attachment; filename="
				+ exportFileName + ".jpeg");
		FacesContext.getCurrentInstance().responseComplete();

		FileInputStream in = new FileInputStream(my_file);

		byte[] buffer = new byte[4096];
		int length;
		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}

		in.close();
		out.flush();
	}

	// Generate PDF file
	public void exportToXML(List<? extends Entite> entitysList,
			String jasperFile, String exportFileName) throws Exception {
		// Specify compiled jasper file(.jasper)
		// get context path
		ExternalContext jsfContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		ServletContext servletContext = (ServletContext) jsfContext
				.getContext();
		String webContentRoot = servletContext.getRealPath("/");

		// String res = webContentRoot.substring(0, 16) + "GBO_v1.k";
		// System.out.println(res);
		// webContentRoot = res;

		// Specify compiled jasper file(.jasper)
		String compJasperFile = webContentRoot + "\\WEB-INF\\rapports\\"
				+ jasperFile + ".jasper";
		System.out.println(compJasperFile);
		// Datasource=bean collection
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				entitysList);
		// jasper print to generate .jrprint

		JasperPrint jasperPrint = null;
		jasperPrint = JasperFillManager.fillReport(compJasperFile, null, ds);

		// coding for XML
		/*
		 * JasperExportManager.exportReportToXmlFile(jasperPrint, "C:\\" +
		 * exportFileName + ".xml", true);
		 */
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JasperExportManager.exportReportToXmlStream(jasperPrint, bos);
		byte[] bytes = bos.toByteArray();
		// send redirect to generated pdf file
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-disposition", "attachment;filename="
				+ exportFileName + ".xml");
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes);
		response.setContentType("application/XML");
		FacesContext.getCurrentInstance().responseComplete();
	}

	private void extractPrintImage(String filePath, JasperPrint print)
			throws IOException {
		File file = new File(filePath);
		OutputStream ouputStream = null;
		try {
			ouputStream = new FileOutputStream(file);
			DefaultJasperReportsContext.getInstance();
			JasperPrintManager printManager = JasperPrintManager
					.getInstance(DefaultJasperReportsContext.getInstance());

			BufferedImage rendered_image = null;
			rendered_image = (BufferedImage) printManager.printPageToImage(
					print, 0, 2.6f);
			ImageIO.write(rendered_image, "jpeg", ouputStream);
			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			String fileName = System.getProperty("user.home")
					+ "/Desktop\\image.jpeg";
			String fileType = "image/jpeg";
			// Find this file id in database to get file name, and file type

			// You must tell the browser the file type you are going to send
			// for example application/pdf, text/plain, text/html, image/jpg
			response.setContentType(fileType);

			// Make sure to show the download dialog
			response.setHeader("Content-disposition",
					"attachment; filename=imageTest.jpeg");

			// Assume file name is retrieved from database
			// For example D:\\file\\test.pdf

			File my_file = new File(fileName);

			// This should send the file to browser
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream(my_file);
			byte[] buffer = new byte[4096];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void exportToCSV(List<CourrierInformations> entitysList,
			String exportFileName) throws Exception {
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// DataOutputStream out = new DataOutputStream(baos);
		// for (CourrierInformations element : entitysList) {
		// out.writeUTF(element.getCourrierReference());
		// }
		// byte[] csv = baos.toByteArray();
		try {
			StringBuffer str = new StringBuffer(20);
			str.append("Reference Courrier" + ";");
			str.append("Objet" + ";");
			str.append("Expediteur" + ";");
			str.append("Destinataire" + ";");
			str.append("Nature" + ";");
			str.append("Transmission" + System.getProperty("line.separator"));
			for (CourrierInformations element : entitysList) {
				if (element.getCourrierDestinataireReelleDirection() == null){
					str.append(" "+ ";");
				}else{
					String test = element.getCourrierDestinataireReelleDirection();
					test = test.replaceAll(" ", "");
					System.out.println("#### test == " + test);
					str.append(test	+ ";");
				}

				if (element.getCourrierObjet() == null){
					str.append(" " + ";");
				}else{
					str.append(element.getCourrierObjet() + ";");
				}

				if (element.getCourrierExpediteur() == null){
					str.append(" "+ ";");
				}else{
					str.append(element.getCourrierExpediteur() + ";");
				}
				
				if (element.getCourrierDestinataireReelle() == null){
					str.append(" "+ ";");
				}else{
					str.append(element.getCourrierDestinataireReelle() + ";");
				}
				
				if (element.getCourrierNature() != null){
					str.append(element.getCourrierNature() + ";");
					
				}else{
					str.append(" " + ";");
				}
				
				if (element.getTransmission() == null) {
					str.append("" + System.getProperty("line.separator"));
				} else {
					str.append(element.getTransmission()
							.getTransmissionLibelle()
							+ System.getProperty("line.separator"));
				}

			}
			byte[] csv = str.toString().getBytes();
			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			response.addHeader("Content-Disposition", "attachment; filename="
					+ exportFileName + ".csv");
			response.setContentType("text/csv");
			response.setCharacterEncoding("UTF-8");

			// response.getOutputStream().write(csv);
			// response.getOutputStream().flush();
			// response.getOutputStream().close();
			response.setContentLength(csv.length);
			response.getOutputStream().write(csv);
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exportToCSVGen(List<String> listEntete, List<String> listDonnees,
			String exportFileName , String separator) throws Exception {

		try {
			StringBuffer str = new StringBuffer();
			for(String entete : listEntete){
				str.append(entete + separator);	
			}
			str.append("" + System.getProperty("line.separator"));
			
			for(String donnee : listDonnees){
				System.out.println("#### donnee == " + donnee);
				String[] aaaa = donnee.split(",");
				System.out.println("#### aaaa == "+ aaaa.length);
				for(int i = 1 ; i<aaaa.length ; i++){
					System.out.println("##### aaaa[i] == " + aaaa[i]);
				str.append(aaaa[i] + separator);
				}
				str.append("" + System.getProperty("line.separator"));
			}
			
			byte[] csv = str.toString().getBytes("UTF-8");

			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			response.addHeader("Content-Disposition", "attachment; filename="
					+ exportFileName + ".csv");

			response.setContentType("text/csv");

			response.setContentLength(csv.length);
			response.getOutputStream().write(csv);
			response.setCharacterEncoding("UTF-8");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			System.out.println("*******Erreur Export pdf*******");
			e.printStackTrace();
		}
	}

//	public void exportToCSVGen(List<ExpediteurType> listcontact,
//			String exportFileName) throws Exception {
//
//		StringBuffer str = new StringBuffer();
//		str.append("Nom/Raison sociale" + ";");
//		str.append("Type" + ";");
//		str.append("E-mail" + ";");
//		str.append("Telephone" + ";");
//		str.append("Adresse" + System.getProperty("line.separator"));
//		for (ExpediteurType element : listcontact) {
//			if (element.getExpdestexterne().getExpDestExterneNom() == null)
//				element.getExpdestexterne().setExpDestExterneNom("");
//			if (element.getExpdestexterne().getExpDestExternePrenom() == null)
//				element.getExpdestexterne().setExpDestExternePrenom("");
//			str.append(element.getExpdestexterne().getExpDestExterneNom() + " "
//					+ element.getExpdestexterne().getExpDestExternePrenom()
//					+ ";");
//			if (element.getType() == null)
//				element.setType("");
//			str.append(element.getType() + ";");
//			if (element.getExpdestexterne().getExpDestExterneMail() == null)
//				element.getExpdestexterne().setExpDestExterneMail("");
//			str.append(element.getExpdestexterne().getExpDestExterneMail()
//					+ ";");
//			if (element.getExpdestexterne().getExpDestExterneTelephone() == null)
//				element.getExpdestexterne().setExpDestExterneTelephone("");
//			str.append(element.getExpdestexterne().getExpDestExterneTelephone()
//					+ ";");
//			if (element.getExpdestexterne().getExpDestExterneAdresse() == null)
//				element.getExpdestexterne().setExpDestExterneAdresse("");
//			if (element.getExpdestexterne().getExpDestExterneVille() == null)
//				element.getExpdestexterne().setExpDestExterneVille("");
//			if (element.getExpdestexterne().getExpDestExterneGouvernerat() == null)
//				element.getExpdestexterne().setExpDestExterneGouvernerat("");
//
//			str.append(element.getExpdestexterne().getExpDestExterneAdresse()
//					+ " "
//					+ element.getExpdestexterne().getExpDestExterneVille()
//					+ " "
//					+ element.getExpdestexterne()
//							.getExpDestExterneGouvernerat() + ";");
//
//			str.append(element.getId() + System.getProperty("line.separator"));
//		}
//		byte[] csv = str.toString().getBytes("UTF-8");
//
//		HttpServletResponse response = (HttpServletResponse) FacesContext
//				.getCurrentInstance().getExternalContext().getResponse();
//		response.addHeader("Content-Disposition", "attachment; filename="
//				+ exportFileName + ".csv");
//
//		response.setContentType("text/csv");
//
//		response.setContentLength(csv.length);
//		response.getOutputStream().write(csv);
//		response.setCharacterEncoding("UTF-8");
//		FacesContext.getCurrentInstance().responseComplete();
//	}

	private int urgence() {
		Random r = new Random();
		return (r.nextInt(2) + 1);
	}

	private int nature() {
		Random r = new Random();
		return (r.nextInt(48) + 1);
	}

	private int transmission() {
		Random r = new Random();
		return (r.nextInt(3) + 1);
	}

	private int annotation() {
		Random r = new Random();
		return (r.nextInt(9) + 1);
	}

	@SuppressWarnings("deprecation")
	private List<String> dates(int max, int id, boolean rep) {// max = 50000 =>
																// (12 * 4166)
																// or (355 *
																// 140)
		Random r = new Random();
		// return (r.nextInt(9) + 1);
		// Integer perMonth = max / 12;
		// Integer month = max / 355;
		Integer perWeek = max / 52;
		Integer week = id / perWeek;
		List<String> list = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2015);
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.DAY_OF_WEEK, (r.nextInt(9) + 1));
		Date date = calendar.getTime();
		String dateStr = "2015-" + date.getMonth() + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + " 00:00:00";
		list.add(dateStr);
		list.add(dateStr);
		list.add(dateStr);
		list.add(dateStr);
		list.add(dateStr);
		list.add(dateStr);
		list.add(dateStr);
		list.add(dateStr);
		return list;
	}

	@SuppressWarnings("deprecation")
	public void exportToTXT(String exportFileName) throws Exception {
		StringBuffer str = new StringBuffer();
		Date date = new Date();
		String dateStr = (date.getYear() + 1900) + "-" + date.getMonth() + "-"
				+ date.getDate() + " 00:00:00";
		Integer min = 1;
		Integer max = 2000;
		Integer idConf = 1;
		Integer idA = 1;
		Integer idD = 1;
		Integer idED = 1;
		Integer idLien = 1;
		Integer idT = 1;
		Integer idTDR = 1;

		// courrier
		System.out.println("courrier");
		for (int id = min; id <= max; id += 8) {
			dates(max, id, false);// ////////////
			str.append("INSERT INTO `courrier` (`idCourrier`,`idTransmission`,`idConfidentialite`,`idUrgence`,`idNature`,`courrierNecessiteReponse`,`courrierDateReponse`,`courrierReferenceCorrespondant`,`courrierCommentaire`,`courrierObjet`,`courrierDateReception`,`courrierCircuit`,`keywords`,`courrierDateSysteme`,`courrierDateReponseSysteme`,`courrierSupprime`,`courrierflagArchive`,`courrierDateCourrierReelle`,`courrierType`,`courrierTypeOrdre`) VALUES ");
			str.append("(" + id + "," + transmission() + "," + idConf + ","
					+ urgence() + "," + nature() + ",'Non',NULL,'A" + idA
					+ "','','Serah (PM) to KG','" + dateStr + "','Libre','','"
					+ dateStr + "',NULL,1,0,'" + dateStr + "','A'," + idA
					+ "),");
			str.append("(" + (id + 1) + "," + transmission() + "," + idConf
					+ "," + urgence() + "," + nature() + ",'Non',NULL,'D" + idD
					+ "','','KN to S T E G','" + dateStr + "','Libre','','"
					+ dateStr + "',NULL,1,0,'" + dateStr + "','D'," + idD
					+ "),");
			str.append("(" + (id + 2) + "," + transmission() + "," + idConf
					+ "," + urgence() + "," + nature() + ",'Non',NULL,'D"
					+ (idD + 1) + "','','KN to Telecom / Tunisair','" + dateStr
					+ "','Libre','','" + dateStr + "',NULL,1,0,'" + dateStr
					+ "','D'," + (idD + 1) + "),");
			str.append("("
					+ (id + 3)
					+ ","
					+ transmission()
					+ ","
					+ idConf
					+ ","
					+ urgence()
					+ ","
					+ nature()
					+ ",'Oui','2016-02-25 00:00:00','A"
					+ (idA + 1)
					+ "','','Banques (PM) to SDAF','"
					+ dateStr
					+ "','Libre','','2016-02-18 00:00:00','2016-02-18 00:00:00',1,0,'2016-02-18 00:00:00','A',"
					+ (idA + 1) + "),");
			str.append("(" + (id + 4) + "," + transmission() + "," + idConf
					+ "," + urgence() + "," + nature()
					+ ",'Oui','2016-02-25 00:00:00','D" + (idD + 2)
					+ "','RE : ','RE : Banques (PM) to SDAF','" + dateStr
					+ "','Libre',NULL,'2016-02-18 00:00:00',NULL,1,0,'D',"
					+ (idD + 2) + "),");
			str.append("(" + (id + 5) + "," + transmission() + "," + idConf
					+ "," + urgence() + "," + nature() + ",'Non',NULL,'D"
					+ (idD + 3) + "','','RBR to Serah','" + dateStr
					+ "','Libre','','" + dateStr
					+ "',NULL,1,0,'2016-02-18 00:00:00','D'," + (idD + 3)
					+ "),");
			str.append("(" + (id + 6) + "," + transmission() + "," + idConf
					+ "," + urgence() + "," + nature() + ",'Non',NULL,'D"
					+ (idD + 4) + "','','DPRD to AN / ON','" + dateStr
					+ "','Libre','','" + dateStr
					+ "',NULL,1,0,'2016-02-18 00:00:00','D'," + (idD + 4)
					+ "),");
			str.append("(" + (id + 7) + "," + transmission() + "," + idConf
					+ "," + urgence() + "," + nature()
					+ ",'Oui','2016-02-25 00:00:00','A" + (idA + 2)
					+ "','','AFH (PM) to KN','" + dateStr + "','Libre','','"
					+ dateStr + "',NULL,1,0,'2016-02-18 00:00:00','A',"
					+ (idA + 2) + ");" + System.getProperty("line.separator"));
			idA += 3;
			idD += 5;
		}

		for (int id = min; id <= max; id += 8) {
			dates(max, id, false);// ////////////
			str.append("INSERT INTO `courrierdossier` (`idCourrier`,`dossierId`) VALUES ("
					+ id
					+ ","
					+ id
					+ "),("
					+ (id + 1)
					+ ","
					+ (id + 1)
					+ "),("
					+ (id + 2)
					+ ","
					+ (id + 2)
					+ "),("
					+ (id + 3)
					+ ","
					+ (id + 3)
					+ "),("
					+ (id + 4)
					+ ","
					+ (id + 4)
					+ "),("
					+ (id + 5)
					+ ","
					+ (id + 5)
					+ "),("
					+ (id + 6)
					+ ","
					+ (id + 6)
					+ "),("
					+ (id + 7)
					+ ","
					+ (id + 7)
					+ ");"
					+ System.getProperty("line.separator"));

			str.append("INSERT INTO `dossier` (`dossierId`,`idConf`,`typeDossierId`,`dossierDateCreation`,`dossierIntitule`,`dossierDescription`,`dossierSupprime`) VALUES ");
			str.append("(" + id + ",1,1,'2016-02-18 00:00:00','Courrier_A"
					+ idA + "','',1),");
			str.append("(" + (id + 1)
					+ ",1,1,'2016-02-18 00:00:00','Courrier_D" + idD
					+ "','',1),");
			str.append("(" + (id + 2)
					+ ",1,1,'2016-02-18 00:00:00','Courrier_D" + (idD + 1)
					+ "','',1),");
			str.append("(" + (id + 3)
					+ ",1,1,'2016-02-18 00:00:00','Courrier_A" + (idA + 1)
					+ "','',1),");
			str.append("(" + (id + 4)
					+ ",1,1,'2016-02-18 00:00:00','Courrier_D" + (idD + 2)
					+ "','RE : ',1),");
			str.append("(" + (id + 5)
					+ ",1,1,'2016-02-18 00:00:00','Courrier_D" + (idD + 3)
					+ "','',1),");
			str.append("(" + (id + 6)
					+ ",1,1,'2016-02-18 00:00:00','Courrier_D" + (idD + 4)
					+ "','',1),");
			str.append("(" + (id + 7)
					+ ",1,1,'2016-02-18 00:00:00','Courrier_A" + (idA + 2)
					+ "','',1);" + System.getProperty("line.separator"));
			idA += 3;
			idD += 5;
		}

		for (int id = min; id <= max; id += 8) {
			str.append("INSERT INTO `expdest` (`idExpDest`,`typeExpDest`,`idExpDestLdap`,`idExpDestExterne`) VALUES ");
			str.append("(" + idED + ",'Externe',NULL,92),(" + (idED + 1)
					+ ",'Interne-Boc',1,NULL),");
			str.append("(" + (idED + 2) + ",'Interne-Person',87,NULL),("
					+ (idED + 3) + ",'Externe',NULL,178),");
			str.append("(" + (idED + 4) + ",'Interne-Person',87,NULL),("
					+ (idED + 5) + ",'Interne-Boc',1,NULL),");
			str.append("(" + (idED + 6) + ",'Interne-Boc',1,NULL),("
					+ (idED + 7) + ",'Externe',NULL,221),");
			str.append("(" + (idED + 8) + ",'Interne-Person',75,NULL),("
					+ (idED + 9) + ",'Interne-Person',71,NULL),");
			str.append("(" + (idED + 10) + ",'Externe',NULL,154),("
					+ (idED + 11) + ",'Interne-Boc',1,NULL),");
			str.append("(" + (idED + 12) + ",'Interne-Person',75,NULL),("
					+ (idED + 13) + ",'Interne-Unité',10,NULL),");
			str.append("(" + (idED + 14) + ",'Interne-Person',90,NULL),("
					+ (idED + 15) + ",'Interne-Boc',1,NULL),");
			str.append("(" + (idED + 16) + ",'Externe',NULL,154),("
					+ (idED + 17) + ",'Interne-Person',97,NULL),");
			str.append("(" + (idED + 18) + ",'Interne-Boc',1,NULL),("
					+ (idED + 19) + ",'Interne-Unité',6,NULL),");
			str.append("(" + (idED + 20) + ",'Interne-Boc',1,NULL),("
					+ (idED + 21) + ",'Interne-Boc',1,NULL),");
			str.append("(" + (idED + 22) + ",'Externe',NULL,92),("
					+ (idED + 23) + ",'Externe',NULL,166),");
			str.append("(" + (idED + 24) + ",'Externe',NULL,188),("
					+ (idED + 25) + ",'Interne-Boc',1,NULL),");
			str.append("(" + (idED + 26) + ",'Interne-Person',75,NULL),("
					+ (idED + 27) + ",'Interne-Person',87,NULL);"
					+ System.getProperty("line.separator"));
			idED += 28;
		}

		for (int id = min; id <= max; id += 8) {
			str.append("INSERT INTO `liencourrier` (`liensCourrier`,`idCourrier`,`liencourrierType`) VALUES ("
					+ idLien
					+ ","
					+ (id + 4)
					+ ",1),("
					+ (idLien + 1)
					+ ","
					+ (id + 3) + ",1);" + System.getProperty("line.separator"));

			str.append("INSERT INTO `lienscourriers` (`liensCourrier`,`idCourrier`) VALUES ("
					+ idLien
					+ ","
					+ (id + 3)
					+ "),("
					+ (idLien + 1)
					+ ","
					+ (id + 4) + ");" + System.getProperty("line.separator"));

			str.append("INSERT INTO `transactionannotation` (`idTransaction`,`idAnnotation`) VALUES ");
			str.append("(" + (idT + 1) + "," + annotation() + "),(" + idT + ","
					+ annotation() + "),");
			str.append("(" + (idT + 5) + "," + annotation() + "),(" + (idT + 1)
					+ "," + annotation() + "),");
			str.append("(" + (idT + 6) + "," + annotation() + "),(" + (idT + 7)
					+ "," + annotation() + ");"
					+ System.getProperty("line.separator"));
			idLien += 2;
			idT += 17;
		}

		for (int id = min; id <= max; id += 8) {
			dates(max, id, false);// ////////////
			str.append("INSERT INTO `transactiondest` (`idTransaction`,`idExpDest`,`transactionDestTypeIntervenant`,`transactionDestIdIntervenant`,`transactionDestDateTransfert`,`transactionDestDateReponse`) VALUES ");
			str.append("(" + idT + "," + (idED + 1)
					+ ",'boc_1',NULL,'2016-02-18 09:34:33',NULL),");
			str.append("(" + (idT + 1) + "," + (idED + 3)
					+ ",NULL,NULL,NULL,NULL),");
			str.append("(" + (idT + 2) + "," + (idED + 5)
					+ ",'boc_1',NULL,NULL,NULL),");
			str.append("(" + (idT + 3) + "," + (idED + 6)
					+ ",'boc_1',NULL,'2016-02-18 09:34:28',NULL),");
			str.append("(" + (idT + 4) + "," + (idED + 7)
					+ ",NULL,NULL,NULL,NULL),");
			str.append("(" + (idT + 5) + "," + (idED + 9)
					+ ",'sub_71',NULL,NULL,NULL),");
			str.append("("
					+ (idT + 6)
					+ ","
					+ (idED + 11)
					+ ",'boc_1',NULL,'2016-02-18 09:40:03','2016-02-25 00:00:00'),");
			str.append("("
					+ (idT + 7)
					+ ","
					+ (idED + 13)
					+ ",'unit_10',NULL,'2016-02-18 09:44:11','2016-02-25 00:00:00'),");
			str.append("("
					+ (idT + 8)
					+ ","
					+ (idED + 15)
					+ ",'boc_1',NULL,'2016-02-18 09:45:01','2016-02-25 00:00:00'),");
			str.append("(" + (idT + 9) + "," + (idED + 16)
					+ ",NULL,NULL,NULL,NULL),");
			str.append("(" + (idT + 10) + "," + (idED + 18)
					+ ",'boc_1',NULL,'2016-02-18 09:48:55',NULL),");
			str.append("(" + (idT + 11) + "," + (idED + 20)
					+ ",'boc_1',NULL,NULL,NULL),");
			str.append("(" + (idT + 12) + "," + (idED + 21)
					+ ",'boc_1',NULL,'2016-02-18 09:48:58',NULL),");
			str.append("(" + (idT + 13) + "," + (idED + 22)
					+ ",NULL,NULL,NULL,NULL),");
			str.append("(" + (idT + 14) + "," + (idED + 23)
					+ ",NULL,NULL,NULL,NULL),");
			str.append("(" + (idT + 15) + "," + (idED + 25)
					+ ",'boc_1',NULL,'2016-02-18 09:50:43',NULL),");
			str.append("(" + (idT + 16) + "," + (idED + 27)
					+ ",'sub_87',NULL,NULL,NULL);"
					+ System.getProperty("line.separator"));
			idED += 28;
			idT += 17;
		}

		for (int id = min; id <= max; id += 8) {
			dates(max, id, false);// ////////////
			str.append("INSERT INTO `transactiondestinationreelle` (`transactionDestinationReelleId`,`transactionDestinationReelleIdDestinataire`,`transactionDestinationReelleDateTraitement`,`transactionDestinationReelleDateReception`,`transactionDestinationReelleTypeDestinataire`) VALUES ");
			str.append("(" + idTDR + ",71,NULL,NULL,'Interne-Person'),");
			str.append("(" + (idTDR + 1) + ",333,NULL,NULL,'Externe'),");
			str.append("(" + (idTDR + 2)
					+ ",221,'2016-02-18 00:00:00',NULL,'Externe'),");
			str.append("(" + (idTDR + 3) + ",10,NULL,NULL,'Interne-Unité'),");
			str.append("(" + (idTDR + 4)
					+ ",154,'2016-02-18 00:00:00',NULL,'Externe'),");
			str.append("(" + (idTDR + 5)
					+ ",92,'2016-02-18 00:00:00',NULL,'Externe'),");
			str.append("(" + (idTDR + 6) + ",164,NULL,NULL,'Externe'),");
			str.append("(" + (idTDR + 7)
					+ ",166,'2016-02-18 00:00:00',NULL,'Externe'),");
			str.append("(" + (idTDR + 8) + ",87,NULL,NULL,'Interne-Person');"
					+ System.getProperty("line.separator"));
			idTDR += 9;
		}

		for (int id = min; id <= max; id += 8) {
			dates(max, id, false);// ////////////
			str.append("INSERT INTO `transactionn` (`transactionId`,`idExpDest`,`dossierId`,`idUtilisateur`,`transactionTypeId`,`etatId`,`transactionDateTransaction`,`transactionDateReponse`,`transactionSupprimer`,`transactionOrdre`,`transactionAccuseReception`,`transactionDateConsultation`,`transactionDestinationReelleId`,`transactionTypeIntervenant`) VALUES ");
			str.append("(" + idT + "," + idED + "," + id
					+ ",75,2,6,'2016-02-18 00:00:00',NULL,1,1," + idTDR
					+ ",NULL),");
			str.append("("
					+ (idT + 1)
					+ ","
					+ (idED + 2)
					+ ","
					+ (id + 1)
					+ ",75,2,6,'2016-02-18 00:00:00',NULL,1,NULL,NULL,'sub_87'),");
			str.append("(" + (idT + 2) + "," + (idED + 4) + "," + (id + 2)
					+ ",87,2,5,'2016-02-18 00:00:00',NULL,1,1," + (idTDR + 1)
					+ ",'sub_87'),");
			str.append("(" + (idT + 3) + "," + (idED + 4) + "," + (id + 2)
					+ ",87,2,6,'2016-02-18 00:00:00',NULL,1,1," + (idTDR + 2)
					+ ",'sub_87'),");
			str.append("(" + (idT + 4) + "," + (idED + 4) + "," + (id + 2)
					+ ",75,2,6,'2016-02-18 00:00:00',NULL,1,2," + (idTDR + 2)
					+ ",NULL),");
			str.append("(" + (idT + 5) + "," + (idED + 8) + "," + id
					+ ",75,2,6,'2016-02-18 00:00:00',NULL,1,2," + idTDR
					+ ",NULL),");
			str.append("(" + (idT + 6) + "," + (idED + 10) + "," + (id + 3)
					+ ",75,2,6,'2016-02-18 00:00:00',NULL,1,1," + (idTDR + 3)
					+ ",NULL),");
			str.append("(" + (idT + 7) + "," + (idED + 12) + "," + (id + 3)
					+ ",75,2,6,'2016-02-18 00:00:00',NULL,1,2," + (idTDR + 3)
					+ ",NULL),");
			str.append("("
					+ (idT + 8)
					+ ","
					+ (idED + 14)
					+ ","
					+ (id + 4)
					+ ",90,2,6,'2016-02-18 00:00:00','2016-02-25 00:00:00',1,1,5,'sub_90'),");
			str.append("(" + (idT + 9) + "," + (idED + 14) + "," + (id + 4)
					+ ",75,2,6,'2016-02-18 00:00:00',NULL,1,2," + (idTDR + 4)
					+ ",NULL),");
			str.append("(" + (idT + 10) + "," + (idED + 17) + "," + (id + 5)
					+ ",97,2,6,'2016-02-18 00:00:00',NULL,1,1," + (idTDR + 5)
					+ ",'sub_97'),");
			str.append("(" + (idT + 11) + "," + (idED + 19) + "," + (id + 6)
					+ ",76,2,5,'2016-02-18 00:00:00',NULL,1,1," + (idTDR + 6)
					+ ",'unit_6'),");
			str.append("(" + (idT + 13) + "," + (idED + 19) + "," + (id + 6)
					+ ",76,2,6,'2016-02-18 00:00:00',NULL,1,1," + (idTDR + 7)
					+ ",'unit_6'),");
			str.append("(" + (idT + 14) + "," + (idED + 17) + "," + (id + 5)
					+ ",75,2,6,'2016-02-18 00:00:00',NULL,1,2," + (idTDR + 5)
					+ ",NULL),");
			str.append("(" + (idT + 15) + "," + (idED + 19) + "," + (id + 6)
					+ ",75,2,6,'2016-02-18 00:00:00',NULL,1,2," + (idTDR + 7)
					+ ",NULL),");
			str.append("("
					+ (idT + 16)
					+ ","
					+ (idED + 24)
					+ ","
					+ (id + 7)
					+ ",75,2,6,'2016-02-18 00:00:00','2016-02-25 00:00:00',1,1,"
					+ (idTDR + 8) + ",NULL),");
			str.append("(" + (idT + 17) + "," + (idED + 26) + "," + (id + 7)
					+ ",75,2,6,'2016-02-18 00:00:00',NULL,1,2," + (idTDR + 8)
					+ ",NULL);" + System.getProperty("line.separator"));
			idED += 28;
			idT += 17;
			idTDR += 9;
		}

		byte[] txt = str.toString().getBytes("UTF-8");

		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-Disposition", "attachment; filename="
				+ exportFileName + ".txt");
		response.setContentType("text/plain");

		response.setContentLength(txt.length);
		response.getOutputStream().write(txt);
		FacesContext.getCurrentInstance().responseComplete();
	}

	// Print
	public void print(List<? extends Entite> entitysList, String jasperFile)
			throws Exception {
		// Specify compiled jasper file(.jasper)
		// get context path
		ExternalContext jsfContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		ServletContext servletContext = (ServletContext) jsfContext
				.getContext();
		String webContentRoot = servletContext.getRealPath("/");

		// Specify compiled jasper file(.jasper)
		String compJasperFile = webContentRoot + "\\WEB-INF\\rapports\\"
				+ jasperFile + ".jasper";
		System.out.println(compJasperFile);

		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				entitysList);
		// jasper print to generate .jrprint

		JasperPrint jasperPrint = JasperFillManager.fillReport(compJasperFile,
				null, ds);

		// JasperPrintManager.printReport(jasperPrint, true);
		// JRPrintServiceExporter exporter = new JRPrintServiceExporter();
		// exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		// exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,
		// Boolean.FALSE);
		// exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
		// Boolean.TRUE);
		// exporter.setParameter(JRPdfExporterParameter.PDF_JAVASCRIPT,
		// "this.print();");
		// exporter.exportReport();

		JRExporter exporter = null;
		ByteArrayOutputStream out = null;
		ByteArrayInputStream input = null;
		BufferedOutputStream output = null;

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext
				.getResponse();

		try {
			out = new ByteArrayOutputStream();

			exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
			exporter.setParameter(JRPdfExporterParameter.PDF_JAVASCRIPT,
					"this.print();");
			exporter.exportReport();

			input = new ByteArrayInputStream(out.toByteArray());

			response.reset();
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-Length",
					String.valueOf(out.toByteArray().length));
			response.setHeader("Content-Disposition",
					"inline; filename=\"fileName.pdf\"");
			output = new BufferedOutputStream(response.getOutputStream(),
					Constants.DEFAULT_BUFFER_SIZE);

			byte[] buffer = new byte[Constants.DEFAULT_BUFFER_SIZE];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			output.flush();

		} catch (Exception exception) {
			/* ... */
		} finally {
			try {
				if (output != null) {
					output.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (Exception exception) {
				/* ... */
			}
		}
		facesContext.responseComplete();
	}
}