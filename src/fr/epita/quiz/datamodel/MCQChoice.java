package fr.epita.quiz.datamodel;

/* Choices in MCQ question */
public class MCQChoice {

	int id;
	
	String choice;
	boolean valid;
	
	MCQQuestion question;

	// constructor
	public MCQChoice() {
		super();
	}

	public MCQChoice(String choice, boolean valid, MCQQuestion question) {
		super();
		this.choice = choice;
		this.valid = valid;
		this.question = question;
	}
	
	public MCQChoice(int id, String choice, boolean valid, MCQQuestion question) {
		super();
		this.id = id;
		this.choice = choice;
		this.valid = valid;
		this.question = question;
	}

	// getter and setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public MCQQuestion getQuestion() {
		return question;
	}

	public void setQuestion(MCQQuestion question) {
		this.question = question;
	}

	
	
}
