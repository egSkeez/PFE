package xtensus.ldap.model;

import java.util.List;

public class Direction implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1314008149973852796L;
	private int idDirection;
	private String nameDirection;
	private Person directeurDirection;
	private Person secretaireDirection;
	private List<Person> membersDirection;
	private List<Direction> listDirectionsChildDirection;
	private List<Direction> listAdjoiningDirectionsDirection;
	private List<Service> listService;
	private Direction associatedDirection;
	private BOC associatedBOC;
	private int niveauDirection;
	private String descriptionDirection;
	private String rowKeyDirection;

	public Direction() {
		this.associatedDirection = null;
		this.associatedBOC = null;
	}

	public Direction(int idDirection, String nameDirection,
			Person directeurDirection, Person secretaireDirection,
			List<Person> membersDirection,
			List<Direction> listDirectionsChildDirection,
			List<Service> listService, Direction associatedDirection,
			int niveauDirection) {
		this.idDirection = idDirection;
		this.nameDirection = nameDirection;
		this.directeurDirection = directeurDirection;
		this.secretaireDirection = secretaireDirection;
		this.membersDirection = membersDirection;
		this.listDirectionsChildDirection = listDirectionsChildDirection;
		this.listService = listService;
		this.associatedDirection = associatedDirection;
		this.niveauDirection = niveauDirection;
	}

	public Direction(int idDirection, String nameDirection,
			Person directeurDirection, Person secretaireDirection,
			List<Person> membersDirection,
			List<Direction> listDirectionsChildDirection,
			List<Service> listService, BOC associatedBOC, int niveauDirection) {
		this.idDirection = idDirection;
		this.nameDirection = nameDirection;
		this.directeurDirection = directeurDirection;
		this.secretaireDirection = secretaireDirection;
		this.membersDirection = membersDirection;
		this.listDirectionsChildDirection = listDirectionsChildDirection;
		this.listService = listService;
		this.associatedBOC = associatedBOC;
		this.niveauDirection = niveauDirection;
	}

	public int getIdDirection() {
		return idDirection;
	}

	public void setIdDirection(int idDirection) {
		this.idDirection = idDirection;
	}

	public String getNameDirection() {
		return nameDirection;
	}

	public void setNameDirection(String nameDirection) {
		this.nameDirection = nameDirection;
	}

	public Person getDirecteurDirection() {
		return directeurDirection;
	}

	public void setDirecteurDirection(Person directeurDirection) {
		this.directeurDirection = directeurDirection;
	}

	public Person getSecretaireDirection() {
		return secretaireDirection;
	}

	public void setSecretaireDirection(Person secretaireDirection) {
		this.secretaireDirection = secretaireDirection;
	}

	public List<Person> getMembersDirection() {
		return membersDirection;
	}

	public void setMembersDirection(List<Person> membersDirection) {
		this.membersDirection = membersDirection;
	}

	public List<Direction> getListDirectionsChildDirection() {
		return listDirectionsChildDirection;
	}

	public void setListDirectionsChildDirection(
			List<Direction> listDirectionsChildDirection) {
		this.listDirectionsChildDirection = listDirectionsChildDirection;
	}

	public List<Service> getListService() {
		return listService;
	}

	public void setListService(List<Service> listService) {
		this.listService = listService;
	}

	public Direction getAssociatedDirection() {
		return associatedDirection;
	}

	public void setAssociatedDirection(Direction associatedDirection) {
		this.associatedDirection = associatedDirection;
	}

	public BOC getAssociatedBOC() {
		return associatedBOC;
	}

	public void setAssociatedBOC(BOC associatedBOC) {
		this.associatedBOC = associatedBOC;
	}

	public int getNiveauDirection() {
		return niveauDirection;
	}

	public void setNiveauDirection(int niveauDirection) {
		this.niveauDirection = niveauDirection;
	}

	public String getDescriptionDirection() {
		return descriptionDirection;
	}

	public void setDescriptionDirection(String descriptionDirection) {
		this.descriptionDirection = descriptionDirection;
	}

	public void setRowKeyDirection(String rowKeyDirection) {
		this.rowKeyDirection = rowKeyDirection;
	}

	public String getRowKeyDirection() {
		return rowKeyDirection;
	}

	public void setListAdjoiningDirectionsDirection(
			List<Direction> listAdjoiningDirectionsDirection) {
		this.listAdjoiningDirectionsDirection = listAdjoiningDirectionsDirection;
	}

	public List<Direction> getListAdjoiningDirectionsDirection() {
		return listAdjoiningDirectionsDirection;
	}
}
