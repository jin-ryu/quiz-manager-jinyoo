package fr.epita.quiz.datamodel;

public class Student {

	String name;
	String id;
	
	// constructor
	public Student(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}

	// getter and setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
