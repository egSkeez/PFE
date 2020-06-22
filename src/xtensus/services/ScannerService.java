package xtensus.services;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import xtensus.beans.common.ScannerUtils;
//@Scope(value =WebApplicationContext.SCOPE_SESSION, proxyMode =ScopedProxyMode.TARGET_CLASS)
@Service
@Scope(value =WebApplicationContext.SCOPE_SESSION,proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ScannerService {

	private String scanned = "false";

	private byte[] data;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		try {
			System.out
					.println("ssssssssssssssssssssss*****************____________________**********************");
			if ((this.data == null) || (data == null)) {
				System.out.println("NULL");
				this.data = data;
			} else {
				System.out.println("CONCAT");
				this.data = ScannerUtils.concatPdf(this.data, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getScanned() {
		return scanned;
	}

	public void setScanned(String scanned) {
		this.scanned = scanned;
	}

}
