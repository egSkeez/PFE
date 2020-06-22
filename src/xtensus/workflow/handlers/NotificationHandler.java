package xtensus.workflow.handlers;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;



public class NotificationHandler implements WorkItemHandler {

	public NotificationHandler() {

	}

	class testThread extends Thread {

		protected long wiID;

		public testThread(long wiID) {

			this.wiID = wiID;

		}

		public void run() {
			System.out.println("Thread");

		}

	}

	@Override
	public void abortWorkItem(WorkItem wi, WorkItemManager wm) {

	}

	@Override
	public void executeWorkItem(WorkItem i, WorkItemManager m) {

		//new testThread(i.getId()).start();
		//System.out.println("Passé par " + i.getParameter("Message"));

		//while (StartApplication.flag == true) {

			m.completeWorkItem(i.getId(), null);

			//StartApplication.flag = false;
		}

	}


