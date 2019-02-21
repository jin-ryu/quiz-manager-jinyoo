package fr.epita.quiz.datamodel;

/* Open question answer */
public class Answer {
	
	String text;
	
	Question question;
	Quiz quiz;
	Student student;
	
	// constructor
	public Answer() {
	}

	public Answer(String text, Question question, Student student) {
		super();
		this.text = text;
		this.question = question;
		this.student = student;
	}

	public Answer(String text, Question question, Quiz quiz, Student student) {
		super();
		this.text = text;
		this.question = question;
		this.quiz = quiz;
		this.student = student;
	}

	// getter and setter
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
	
}
