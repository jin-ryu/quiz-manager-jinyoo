package fr.epita.quiz.datamodel;

/* MCQ question answer */
public class MCQAnswer {

	Student student;
	Quiz quiz;
	MCQChoice choice;
	
	// constructor
	public MCQAnswer() {
		super();
	}

	public MCQAnswer(Student student, MCQChoice choice) {
		super();
		this.student = student;
		this.choice = choice;
	}

	public MCQAnswer(Student student, Quiz quiz, MCQChoice choice) {
		super();
		this.student = student;
		this.quiz = quiz;
		this.choice = choice;
	}

	// getter and setter
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public MCQChoice getChoice() {
		return choice;
	}

	public void setChoice(MCQChoice choice) {
		this.choice = choice;
	}
	
	
	
	
}
