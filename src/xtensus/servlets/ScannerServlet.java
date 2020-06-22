package xtensus.servlets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import xtensus.beans.common.ScannerUtils;
import xtensus.services.ScannerService;

@Component("scanner")
public class ScannerServlet implements HttpRequestHandler {

	@Autowired
	ScannerService scannerSevrice;

	@PostConstruct
	public void Initialize() {
		System.out
				.println("################################init Scanner Servlet##################################");
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("ScannerServ-*-!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		ByteArrayOutputStream byteArrayOutputStream = null;
		int len = 0;
		int size = 1024;
//		int size = 
		byte[] data;
		DataInputStream dataInputStream = null;

		try {
			dataInputStream = new DataInputStream(request.getInputStream());
			byteArrayOutputStream = new ByteArrayOutputStream();
			data = new byte[1024];
			while ((len = dataInputStream.read(data, 0, 1024)) != -1) {
				System.out.println("  setData!!!");
				byteArrayOutputStream.write(data, 0, len);
				data = byteArrayOutputStream.toByteArray();
//				scannerSevrice.setData(ScannerUtils
//						.imageByteArrayToPdfByteArray(data));
//				scannerSevrice.setData(data);
			}
				scannerSevrice.setData(ScannerUtils
						.imageByteArrayToPdfByteArray(data));
//			scannerSevrice.setData(data);
//			try {
//				File someFile = new File("C://testSCAN.pdf");
//		        FileOutputStream fos = new FileOutputStream(someFile);
//		        fos.write(data);
//		        fos.flush();
//		        fos.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			 
			
			
			
			
			dataInputStream.close();

			System.out.println("received " + data.length + " bytes from "
					+ request.getRemoteAddr());
			System.out.println("converted image to pdf with : "
					+ scannerSevrice.getData().length + " bytes");
			DataOutputStream dataOutputStream = new DataOutputStream(
					response.getOutputStream());
			dataOutputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
