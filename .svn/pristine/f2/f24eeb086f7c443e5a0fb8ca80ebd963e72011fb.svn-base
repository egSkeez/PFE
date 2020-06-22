package xtensus.ldap.model;

import java.util.List;

public class Service {

	private int idService;
	private String nameService;
	private Person chefService;
	private Person secretaireService;
	private Direction associatedDirection;
	private List<Service> listAdjoiningService;
	private List<Person> membersService;
	private String descriptionService;
	private String rowKeyService;

	public Service() {
		this.chefService = null;
		this.secretaireService = null;
	}

	public Service(int idService, String nameService,
			List<Person> membersService, Direction associatedDirection) {
		this.idService = idService;
		this.nameService = nameService;
		this.membersService = membersService;
		this.associatedDirection = associatedDirection;
	}

	public int getIdService() {
		return idService;
	}

	public void setIdService(int idService) {
		this.idService = idService;
	}

	public String getNameService() {
		return nameService;
	}

	public void setNameService(String nameService) {
		this.nameService = nameService;
	}

	public Direction getAssociatedDirection() {
		return associatedDirection;
	}

	public void setAssociatedDirection(Direction associatedDirection) {
		this.associatedDirection = associatedDirection;
	}

	public List<Person> getMembersService() {
		return membersService;
	}

	public void setMembersService(List<Person> membersService) {
		this.membersService = membersService;
	}

	public String getDescriptionService() {
		return descriptionService;
	}

	public void setDescriptionService(String descriptionService) {
		this.descriptionService = descriptionService;
	}

	public void setRowKeyService(String rowKeyService) {
		this.rowKeyService = rowKeyService;
	}

	public String getRowKeyService() {
		return rowKeyService;
	}

	public void setChefService(Person chefService) {
		this.chefService = chefService;
	}

	public Person getChefService() {
		return chefService;
	}

	public void setSecretaireService(Person secretaireService) {
		this.secretaireService = secretaireService;
	}

	public Person getSecretaireService() {
		return secretaireService;
	}

	public void setListAdjoiningService(List<Service> listAdjoiningService) {
		this.listAdjoiningService = listAdjoiningService;
	}

	public List<Service> getListAdjoiningService() {
		return listAdjoiningService;
	}

}
