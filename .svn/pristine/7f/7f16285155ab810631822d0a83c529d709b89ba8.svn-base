package xtensus.beans.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.beans.common.VariableGlobale;
import xtensus.services.ScannerService;

@Component
@Scope("request")
public class ReInitializeUploadBean {

	@Autowired
	private VariableGlobale vbg;
	@Autowired
	private ScannerService scannerService;

	
	public ReInitializeUploadBean() {
		System.out
				.println("*************Bean ReInitializeUploadBean Injecte***************");
	}

	public void Initialze() {
		try {
			vbg.setUploadedData(null);
			scannerService.setData(null);
			System.out
					.println("***********ReInitializeUploadBean vider**************");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/****************** Getter & &Setter *******************/

	public void setVbg(VariableGlobale vbg) {
		this.vbg = vbg;
	}

	public VariableGlobale getVbg() {
		return vbg;
	}

	public void setScannerService(ScannerService scannerService) {
		this.scannerService = scannerService;
	}

	public ScannerService getScannerService() {
		return scannerService;
	}
}