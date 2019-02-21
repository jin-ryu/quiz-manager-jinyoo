package fr.epita.quiz.datamodel;

import java.util.List;

/*
 * properties that one question should include
 * (question sentence, tags, id, difficulty) 
 */

public class Question {

	private int id;
	
	private String question; 
	private List<String> topics;	 
	private Integer difficulty;
	
	// constructor
	
	public Question() {
		super();
	}

	public Question(int id, String question) {
		super();
		this.id = id;
		this.question = question;
	}

	public Question(String question, List<String> topics, Integer difficulty) {
		this.question = question;
		this.topics = topics;
		this.difficulty = difficulty;
	}
	
	public Question(int id, String question, List<String> topics, Integer difficulty) {
		super();
		this.id = id;
		this.question = question;
		this.topics = topics;
		this.difficulty = difficulty;
	}

	// getter and setter
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public List<String> getTopics() {
		return topics;
	}
	public void setTopics(List<String> topics) {
		this.topics = topics;
	}
	public Integer getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
