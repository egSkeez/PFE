package xtensus.beans.common;

import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

public class TracerPhaseListener implements javax.faces.event.PhaseListener {

	private static final long serialVersionUID = 1L;
	private static final String PHASE_PARAM = "org.exadel.helper.phaseTracker.cphase";
	private static final Logger logger = Logger.getLogger("org.exadel.helper");
	public static String cphase = null;
	@Override
	public void afterPhase(PhaseEvent phaseEvent) {
		logger.info("afterPhase " + phaseEvent.getPhaseId());
		
	}

	@Override
	public void beforePhase(PhaseEvent phaseEvent) {
		logger.info("beforePhase " + phaseEvent.getPhaseId());
		
	}

	@Override
	public PhaseId getPhaseId() {
		PhaseId phaseId = PhaseId.ANY_PHASE;
		if(cphase == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			if (context == null) return phaseId;
			cphase = (String)context.getExternalContext()
			.getInitParameter(PHASE_PARAM);
			}
		
		if(cphase != null) {
			if("RESTORE_VIEW".equals(cphase))phaseId = PhaseId.RESTORE_VIEW;
			else if("APPLY_REQUEST_VALUES".equals(cphase))
			phaseId = PhaseId.APPLY_REQUEST_VALUES;
			else if("PROCESS_VALIDATIONS".equals(cphase))
			phaseId = PhaseId.PROCESS_VALIDATIONS;
			else if("UPDATE_MODEL_VALUES".equals(cphase))
			phaseId = PhaseId.UPDATE_MODEL_VALUES;
			else if("INVOKE_APPLICATION".equals(cphase))
			phaseId = PhaseId.INVOKE_APPLICATION;
			else if("RENDER_RESPONSE".equals(cphase))
			phaseId = PhaseId.RENDER_RESPONSE;
			else if("ANY_PHASE".equals(cphase)) phaseId = PhaseId.ANY_PHASE;
			}
			return phaseId;
	}

}
