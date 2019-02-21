package fr.epita.quiz.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.epita.quiz.datamodel.Answer;
import fr.epita.quiz.datamodel.MCQAnswer;
import fr.epita.quiz.datamodel.MCQChoice;
import fr.epita.quiz.datamodel.MCQQuestion;
import fr.epita.quiz.datamodel.Question;
import fr.epita.quiz.datamodel.Quiz;
import fr.epita.quiz.datamodel.Student;

public class QuestionJDBCDAO {

	private static final String QUIZ_CONTENT_INSERT_STATEMENT = "INSERT INTO QUIZ_CONTENT (TITLE, QID) VALUES (?,?)";
	private static final String QUIZ_CONTENT_DELETE_TITLE_STATEMENT = "DELETE FROM QUIZ_CONTENT WHERE TITLE = ?";
	private static final String QUIZ_CONTENT_DELETE_QUESTION_STATEMENT = "DELETE FROM QUIZ_CONTENT WHERE QUESTION = ?";

	private static final String STUDENT_INSERT_STATEMENT = "INSERT INTO STUDENT (SID, NAME) VALUES (?,?)";
	private static final String STUDENT_SEARCH_STATEMENT = "SELECT * FROM STUDENT WHERE SID = ?";

	private static final String QUIZ_INSERT_STATEMENT = "INSERT INTO QUIZ VALUES (?)";
	private static final String QUIZ_DELETE_STATEMENT = "DELETE FROM QUIZ WHERE TITLE = ?";
	private static final String QUIZ_SHOW_STATEMENT = "SELECT * FROM QUIZ";
	private static final String QUIZ_SEARCH_STATEMENT = "SELECT * FROM QUIZ WHERE TITLE = ?";

	private static final String CHOICE_INSERT_STATEMENT = "INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES (?, ?, ?)";
	private static final String CHOICE_DELETE_STATEMENT = "DELETE FROM MCQCHOICE WHERE QID = ?";
	private static final String CHOICE_SEARCH_STATEMENT = "SELECT * FROM MCQCHOICE WHERE QID = ?";
	private static final String CHOICE_SEARCH_CID_STATEMENT = "SELECT * FROM MCQCHOICE WHERE CID = ?";
	private static final String CHOICE_UPDATE_STATEMENT = "UPDATE MCQCHOICE SET CHOICE = ?, VALID = ? WHERE CID = ?";

	private static final String ANSWER_INSERT_STATEMENT = "INSERT INTO ANSWER (TEXT, QID, SID, TITLE) VALUES (?, ?, ?, ?)";
	private static final String ANSWER_DELETE_STATEMENT = "DELETE FROM ANSWER WHERE QID = ?";
	private static final String ANSWER_SEARCH_STATEMENT = "SELECT * FROM ANSWER WHERE QID = ?";

	private static final String MCQANSWER_INSERT_STATEMENT = "INSERT INTO MCQANSWER (TITLE, QID, SID, CID) VALUES (?, ?, ?, ?)";
	private static final String MCQANSWER_DELETE_STATEMENT = "DELETE FROM MCQANSWER WHERE QID = ?";
	private static final String MCQANSWERS_DELETE_STATEMENT = "DELETE FROM MCQANSWER WHERE (TITLE, SID) = (?, ?)";
	private static final String MCQANSWER_SEARCH_STATEMENT = "SELECT * FROM MCQANSWER WHERE (TITLE, QID, SID) = (?, ?, ?)";
	private static final String MCQANSWERS_SEARCH_STATEMENT = "SELECT * FROM MCQANSWER WHERE (TITLE, SID) = (?, ?)";

	private static final String INSERT_STATEMENT = "INSERT INTO QUESTION (QUESTION, DIFFICULTY) VALUES (?, ?)";
	private static final String SEARCH_STATEMENT = "SELECT * FROM QUESTION WHERE ID=?";
	private static final String SEARCH_BY_QUESTION_STATEMENT = "SELECT * FROM QUESTION WHERE QUESTION = ?";
	private static final String SEARCH_BY_TOPIC_STATEMENT = "SELECT * FROM QUESTION_ABOUT WHERE TOPIC=?";
	private static final String SEARCH_BY_TITLE_STATEMENT = "SELECT * FROM QUIZ_CONTENT WHERE TITLE=?";
	private static final String UPDATE_STATEMENT = "UPDATE QUESTION SET QUESTION = ?, DIFFICULTY = ? WHERE ID=?";
	private static final String DELETE_STATEMENT = "DELETE FROM QUESTION WHERE ID = ?";

	private static final String TOPIC_INSERT_STATEMENT = "INSERT INTO TOPIC VALUES (?)";
	private static final String TOPIC_DELETE_STATEMENT = "DELETE FROM TOPIC WHERE TOPIC = ?";
	private static final String TOPIC_SEARCH_STATEMENT = "SELECT * FROM TOPIC WHERE TOPIC = ?";
	
	private static final String QUESTION_ABOUT_INSERT_STATEMENT = "INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES (?, ?)";
	private static final String QUESTION_ABOUT_DELETE_STATEMENT = "DELETE FROM QUESTION_ABOUT WHERE QID = ?";
	private static final String QUESTION_ABOUT_SEARCH_STATEMENT = "SELECT * FROM QUESTION_ABOUT WHERE QID = ?";
	
	// database connect
	private Connection getConnection() throws SQLException {
		Configuration conf = Configuration.getInstance();
		String jdbcUrl = conf.getConfigurationValue("jdbc.url");
		String user = conf.getConfigurationValue("jdbc.user");
		String password = conf.getConfigurationValue("jdbc.password");
		Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
		return connection;
	}

	/* Quiz_Content */
	public void createQuizContent(String title, int QID) {
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(QUIZ_CONTENT_INSERT_STATEMENT);) {

			insertStatement.setString(1, title);
			insertStatement.setInt(2, QID);
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	

	public void deleteQuizContentbyTitle(Quiz quiz) {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(QUIZ_CONTENT_DELETE_TITLE_STATEMENT);) {

			deleteStatement.setString(1, quiz.getTitle());
			deleteStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteQuizContentbyQuestion(Question question) {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection
						.prepareStatement(QUIZ_CONTENT_DELETE_QUESTION_STATEMENT);) {

			deleteStatement.setInt(1, question.getId());
			deleteStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* Student */
	public void createStudent(Student student) {
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(STUDENT_INSERT_STATEMENT);) {

			insertStatement.setString(1, student.getId());
			insertStatement.setString(2, student.getName());
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Student> searchStudent(Student student) {
		List<Student> resultList = new ArrayList<Student>();

		try (Connection connection = getConnection();
				PreparedStatement searchStatement = connection.prepareStatement(STUDENT_SEARCH_STATEMENT)) {
			searchStatement.setString(1, student.getId());
			ResultSet results = searchStatement.executeQuery();
			while (results.next()) {
				String id = results.getString("SID");
				String name = results.getString("name");
				Student currentStudent = new Student(id, name);
				resultList.add(currentStudent);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/* Quiz */
	public void createQuiz(Quiz quiz) {
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(QUIZ_INSERT_STATEMENT);) {

			insertStatement.setString(1, quiz.getTitle());
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Quiz> searchQuiz(Quiz quiz) {
		List<Quiz> resultList = new ArrayList<Quiz>();

		try (Connection connection = getConnection();
				PreparedStatement searchStatement = connection.prepareStatement(QUIZ_SEARCH_STATEMENT)) {
			searchStatement.setString(1, quiz.getTitle());
			ResultSet results = searchStatement.executeQuery();
			while (results.next()) {
				Quiz currentQuiz = new Quiz(results.getString("title"));
				resultList.add(currentQuiz);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	public List<Quiz> showQuiz() {
		List<Quiz> resultList = new ArrayList<Quiz>();

		try (Connection connection = getConnection();
				PreparedStatement searchStatement = connection.prepareStatement(QUIZ_SHOW_STATEMENT)) {
			ResultSet results = searchStatement.executeQuery();
			while (results.next()) {
				Quiz currentQuiz = new Quiz(results.getString("title"));
				resultList.add(currentQuiz);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	public void deleteQuiz(Quiz quiz) {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(QUIZ_DELETE_STATEMENT);) {

			deleteStatement.setString(1, quiz.getTitle());
			deleteStatement.execute();

			// delete quiz and question link info from database
			deleteQuizContentbyTitle(quiz);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* Choice */
	public void createChoices(MCQChoice choice) {
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(CHOICE_INSERT_STATEMENT);) {

			insertStatement.setString(1, choice.getChoice());
			insertStatement.setInt(2, choice.getQuestion().getId());
			insertStatement.setBoolean(3, choice.isValid());
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteChoices(Question question) {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(CHOICE_DELETE_STATEMENT);) {

			deleteStatement.setInt(1, question.getId());
			deleteStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void updateChoices(MCQChoice choice) {

		try (Connection connection = getConnection();
				PreparedStatement updateStatement = connection.prepareStatement(CHOICE_UPDATE_STATEMENT)) {
			updateStatement.setString(1, choice.getChoice());
			updateStatement.setBoolean(2, choice.isValid());
			updateStatement.setInt(3, choice.getQuestion().getId());
			updateStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	public List<MCQChoice> searchChoiceByCID(int CID) {
		List<MCQChoice> resultList = new ArrayList<MCQChoice>();

		try (Connection connection = getConnection();
				PreparedStatement searchStatement = connection.prepareStatement(CHOICE_SEARCH_CID_STATEMENT)) {
			searchStatement.setInt(1, CID);
			ResultSet results = searchStatement.executeQuery();
			while (results.next()) {
				int QID = results.getInt("QID");
				String choice = results.getString("choice");
				Boolean valid = results.getBoolean("valid");
				Question question = search(QID).get(0);
				MCQQuestion MCQQuestion = new MCQQuestion(question.getQuestion(), question.getTopics(), question.getDifficulty());
				MCQQuestion.setId(QID);
				MCQChoice currentChoice = new MCQChoice(CID, choice, valid, MCQQuestion);
				resultList.add(currentChoice);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	public List<MCQChoice> searchChoice(int QID) {
		List<MCQChoice> resultList = new ArrayList<MCQChoice>();

		try (Connection connection = getConnection();
				PreparedStatement searchStatement = connection.prepareStatement(CHOICE_SEARCH_STATEMENT)) {
			searchStatement.setInt(1, QID);
			ResultSet results = searchStatement.executeQuery();
			while (results.next()) {
				int id = results.getInt("CID");
				String choice = results.getString("choice");
				Boolean valid = results.getBoolean("valid");
				Question question = search(QID).get(0);
				MCQQuestion MCQQuestion = new MCQQuestion(question.getQuestion(), question.getTopics(), question.getDifficulty());
				MCQQuestion.setId(QID);
				MCQChoice currentChoice = new MCQChoice(id, choice, valid, MCQQuestion);
				resultList.add(currentChoice);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/* MCQAnswer */
	public void createMCQAnswer(MCQAnswer MCQAnswer) {

		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(MCQANSWER_INSERT_STATEMENT);) {

			insertStatement.setString(1, MCQAnswer.getQuiz().getTitle());
			insertStatement.setInt(2, MCQAnswer.getChoice().getQuestion().getId());
			insertStatement.setString(3, MCQAnswer.getStudent().getId());
			insertStatement.setInt(4, MCQAnswer.getChoice().getId());
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void deleteMCQAnswers (String title, String SID) {

		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(MCQANSWERS_DELETE_STATEMENT);) {

			insertStatement.setString(1, title);
			insertStatement.setString(2, SID);
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<MCQAnswer> searchMCQAnswers(Quiz quiz, Student student) {
		List<MCQAnswer> resultList = new ArrayList<MCQAnswer>();
		
		try (Connection connection = getConnection();
				PreparedStatement searchStatement = connection.prepareStatement(MCQANSWERS_SEARCH_STATEMENT)) {
			searchStatement.setString(1, quiz.getTitle());
			searchStatement.setString(2, student.getId());
			ResultSet results = searchStatement.executeQuery();
			while (results.next()) {
				int CID = results.getInt("CID");
				MCQChoice choice = searchChoiceByCID(CID).get(0);
				int QID = results.getInt("QID");
				Question question = search(QID).get(0);
				MCQQuestion MCQQuestion = new MCQQuestion(question.getQuestion(), question.getTopics(), question.getDifficulty());
				MCQChoice MCQChoice = new MCQChoice(CID, choice.getChoice(), choice.isValid(), MCQQuestion);
				MCQAnswer MCQAnswer = new MCQAnswer(student, quiz, MCQChoice);	
				resultList.add(MCQAnswer);
			}
			
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	public MCQAnswer searchMCQAnswer(Quiz quiz, Question question, Student student) {
		MCQAnswer MCQAnswer = new MCQAnswer();
		
		try (Connection connection = getConnection();
				PreparedStatement searchStatement = connection.prepareStatement(MCQANSWER_SEARCH_STATEMENT)) {
			searchStatement.setString(1, quiz.getTitle());
			searchStatement.setInt(2, question.getId());
			searchStatement.setString(3, student.getId());
			ResultSet results = searchStatement.executeQuery();
			while (results.next()) {
				int CID = results.getInt("CID");
				MCQChoice MCQChoice = searchChoiceByCID(CID).get(0);
	
				/*
				List<MCQChoice> choiceList = searchChoice(question.getId());
				for (int i = 0; i < choiceList.size(); i++) {
					MCQChoice currentChoice = choiceList.get(i);
					if (currentChoice.getId() == CID) {
						MCQChoice = currentChoice;
						break;
					}
				}
				*/
				MCQAnswer = new MCQAnswer(student, quiz, MCQChoice);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MCQAnswer;
	}

	/* Answer */
	public void createAnswer(Answer answer) {

		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(ANSWER_INSERT_STATEMENT);) {

			insertStatement.setString(1, answer.getText());
			insertStatement.setInt(2, answer.getQuestion().getId());
			insertStatement.setString(3, answer.getStudent().getId());
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	/* Question_about */
	public void createQuestionAbout(String topic, int QID) {

		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(QUESTION_ABOUT_INSERT_STATEMENT);) {

			insertStatement.setString(1, topic);
			insertStatement.setInt(2, QID);
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deleteQuestionAbout(int QID) {

		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(QUESTION_ABOUT_DELETE_STATEMENT);) {

			insertStatement.setInt(1, QID);
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<String> searchQuestionAbout(int QID) {
		List<String> resultList = new ArrayList<String>();

		try (Connection connection = getConnection();
				PreparedStatement searchStatement = connection.prepareStatement(QUESTION_ABOUT_SEARCH_STATEMENT)) {
			
			searchStatement.setInt(1, QID);
			ResultSet results = searchStatement.executeQuery();
			while (results.next()) {
				String currentTopic = results.getString("topic");
				resultList.add(currentTopic);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	/* Topic */
	public void createTopic(String topic) {

		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(TOPIC_INSERT_STATEMENT);) {
			insertStatement.setString(1, topic);
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void deleteTopic(String topic) {

		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(TOPIC_DELETE_STATEMENT);) {
			insertStatement.setString(1, topic);
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<String> searchTopic(String topic) {
		List<String> resultList = new ArrayList<String>();

		try (Connection connection = getConnection();
				PreparedStatement searchStatement = connection.prepareStatement(TOPIC_SEARCH_STATEMENT)) {
			
			searchStatement.setString(1, topic);
			ResultSet results = searchStatement.executeQuery();
			while (results.next()) {
				String currentTopic = results.getString("topic");
				resultList.add(currentTopic);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/* Question */
	public void create(Question question) {

		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(INSERT_STATEMENT);) {

			insertStatement.setString(1, question.getQuestion());
			insertStatement.setInt(2, question.getDifficulty());
			insertStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void update(Question question) {

		try (Connection connection = getConnection();
				PreparedStatement updateStatement = connection.prepareStatement(UPDATE_STATEMENT)) {
			updateStatement.setString(1, question.getQuestion());
			updateStatement.setInt(2, question.getDifficulty());
			updateStatement.setInt(3, question.getId());
			updateStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void delete(Question question) {

		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(DELETE_STATEMENT)) {
			deleteStatement.setInt(1, question.getId());
			deleteStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Question> search(int id) {
		List<Question> resultList = new ArrayList<Question>();

		try (Connection connection = getConnection();
				PreparedStatement searchStatement = connection.prepareStatement(SEARCH_STATEMENT)) {
			searchStatement.setInt(1, id);
			ResultSet results = searchStatement.executeQuery();
			while (results.next()) {
				String question = results.getString("question");
				int difficulty = results.getInt("difficulty");
				List<String> topics = searchQuestionAbout(id);
				Question currentQuestion = new Question(id, question, topics, difficulty);
				resultList.add(currentQuestion);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	public List<Question> searchByQuestion(String question) {
		List<Question> resultList = new ArrayList<Question>();

		try (Connection connection = getConnection();
				PreparedStatement searchStatement = connection.prepareStatement(SEARCH_BY_QUESTION_STATEMENT)) {
			searchStatement.setString(1, question);
			ResultSet results = searchStatement.executeQuery();
			while (results.next()) {
				int QID = results.getInt("ID");
				Question currentQuestion = new Question(QID, question);
				resultList.add(currentQuestion);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	public List<Question> searchByTopic(String topic) {
		List<Question> resultList = new ArrayList<Question>();

		try (Connection connection = getConnection();
				PreparedStatement searchStatement = connection.prepareStatement(SEARCH_BY_TOPIC_STATEMENT)) {
			searchStatement.setString(1, topic);
			ResultSet results = searchStatement.executeQuery();
			while (results.next()) {
				int QID = results.getInt("QID");
				Question currentQuestion = search(QID).get(0);
				resultList.add(currentQuestion);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	public List<Question> searchByTitle(String title) {
		List<Question> resultList = new ArrayList<Question>();

		try (Connection connection = getConnection();
				PreparedStatement searchStatement = connection.prepareStatement(SEARCH_BY_TITLE_STATEMENT)) {
			searchStatement.setString(1, title);
			ResultSet results = searchStatement.executeQuery();
			while (results.next()) {
				int QID = results.getInt("QID");
				Question currentQuestion = search(QID).get(0);
				resultList.add(currentQuestion);
			}
			results.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

}
