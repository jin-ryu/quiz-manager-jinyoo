package fr.epita.quiz.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import fr.epita.quiz.datamodel.Answer;
import fr.epita.quiz.datamodel.MCQAnswer;
import fr.epita.quiz.datamodel.MCQChoice;
import fr.epita.quiz.datamodel.MCQQuestion;
import fr.epita.quiz.datamodel.Question;
import fr.epita.quiz.datamodel.Quiz;
import fr.epita.quiz.datamodel.Student;
import fr.epita.quiz.services.QuestionJDBCDAO;

public class Launcher {

	// Messages
	private static final String LOGIN = "<< Login >>\n" + "If you do not have an ID,\n"
			+ "you are automatically subscribed to the entered content";
	private static final String START = "<< Select the menu >>\n" + "1. Solve\n" + "2. Search\n" + "3. Edit\n"
			+ "4. Exit";
	private static final String SEARCH = "<< Choose a search method >>\n" + "1. ID\n" + "2. Topic\n" + "3. Go Back\n";
	private static final String EDIT = "<< Choose the function >>\n" + "1. Create quiz\n" + "2. Delete quiz\n"
			+ "3. Edit question\n" + "4. Go Back";
	private static final String EDIT_QUESTION = "<< Choose the function >>\n" + "1. Create\n" + "2. Update\n"
			+ "3. Delete\n" + "4. Go Back";
	private static final String QUESTION_TYPE = "<< Choose the question type >>\n" + "1. MCQ Question\n"
			+ "2. Open Question\n" + "3. Go Back";
	private static final String CREATE = "<< Choose the method to create quiz >>\n" + "1. Topic\n" + "2. Difficulty\n"
			+ "3. Go Back";

	private static final String ENTER_TOPIC = "<< Enter the topic of the question >>";
	private static final String ENTER_ID = "<< Enter the ID of the question >>";
	private static final String ENTER_TITLE = "<< Enter the title of the quiz >>";
	private static final String ENTER_CHOICE = "<< Enter the number of choices to add >>";
	private static final String ENTER_ANSWER = "<< Enter the answer of the question >>";
	private static final String ENTER_QUIZ = "<< Enter the number of the quiz >>";

	private static final String ERROR = "<< Error >>";
	private static final String SCORE = "<< Score >>";
	private static final String CONTOUR = "-----------------------------------------------------------";
	// database
	private static QuestionJDBCDAO questionDAO = new QuestionJDBCDAO();

	// user
	public static Student student;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner sc = new Scanner(System.in);

		login(sc);
		startProgram(sc);	// select menu
	}

	private static void startProgram(Scanner sc) {
		// Run the program
		Boolean run = true;
		while (run) {
			System.out.println(START);
			int menu = sc.nextInt();
			sc.nextLine(); // Removing the Enter character in the buffer

			switch (menu) {
			case 1: // Solve
				solveQuiz(sc);
				break;
			case 2: // Search
				searchQuestion(sc);
				break;
			case 3: // Edit
				editQuiz(sc);
				break;
			case 4: // Exit
				run = false;
				break;
			default:
				break;
			}
		}
	}

	private static void solveQuiz(Scanner sc) {
		List<Quiz> quizList;
		int idx;

		Quiz currentQuiz;
		while (true) {
			// Show quiz list
			System.out.println(ENTER_QUIZ);
			quizList = questionDAO.showQuiz();
			for (int i = 0; i < quizList.size(); i++) {
				System.out.print(i + 1 + ". ");
				System.out.println(quizList.get(i).getTitle());
			}
			idx = sc.nextInt() - 1;
			sc.nextLine(); // Removing the Enter character in the buffer
			currentQuiz = quizList.get(idx);
			List<MCQAnswer> MCQAnswers = questionDAO.searchMCQAnswers(currentQuiz, student);
			
			if (idx >= quizList.size()) {
				System.out.println(ERROR);
				System.out.println("Wrong number. Enter again.\n");
			}else if (!MCQAnswers.isEmpty()) {	
				// erase MCQAnswer data if you have previous solved data
				questionDAO.deleteMCQAnswers(currentQuiz.getTitle(), student.getId());
				break;
			}else {
				break;
			}
		}
		// Show questions
		List<Question> questionList = searchByTitle(currentQuiz.getTitle());
		for (int i = 0; i < questionList.size(); i++) {
			Question currentQuestion = questionList.get(i);
			int QID = currentQuestion.getId();
			System.out.println("(" + QID + ") " + currentQuestion.getQuestion());

			List<MCQChoice> choices = questionDAO.searchChoice(QID);
			if (!choices.isEmpty()) { // MCQ Question
				for (int j = 0; j < choices.size(); j++) {
					System.out.println(j + 1 + ". " + choices.get(j).getChoice());
				}
				System.out.println(CONTOUR);
			}
			
			while(true) {
				System.out.println("<< Write down the answer >>");
				String text = sc.nextLine();
	
				// Save the answer
				if (questionDAO.searchChoice(QID).isEmpty()) { // Open Question
					Answer answer = new Answer(text, currentQuestion, currentQuiz, student);
					questionDAO.createAnswer(answer); // add answer to the database
					
					System.out.println(SCORE);
					System.out.println("Open Question will be evaluated later!");
					break;
				} else { // MCQ Question
					int choice = Integer.parseInt(text) - 1;
					if(choice < choices.size()) {
						MCQChoice MCQChoice = choices.get(choice);
						MCQAnswer MCQAnswer = new MCQAnswer(student, currentQuiz, MCQChoice);
						questionDAO.createMCQAnswer(MCQAnswer); // add MCQAnswer to the database
						
						break;
					} else {	// Error message
						System.out.println(ERROR);
						System.out.println("Wrong Select");
					}
				}
			}
		}
		
		evaluateAnswer(currentQuiz, questionList);
		
	}

	private static void evaluateAnswer(Quiz currentQuiz, List<Question> questionList) {
		// Evaluate the answer
		int score = 0;
		int total = 0;

		for (int i = 0; i < questionList.size(); i++) {
			Question currentQuestion = questionList.get(i);
			int QID = currentQuestion.getId();
			if (!questionDAO.searchChoice(QID).isEmpty()) { // MCQ Question
				MCQAnswer answer = questionDAO.searchMCQAnswer(currentQuiz, currentQuestion, student);
				if (answer.getChoice().isValid()) {
					score++;
				}
				total++;
			}
		}

		System.out.println(SCORE);
		System.out.println("Your score is " + score + "/" + total);
		System.out.println("Open Question will be evaluated later!");
	}

	private static void login(Scanner sc) {
		// Login
		System.out.println(LOGIN);
		System.out.print("Name: ");
		String name = sc.nextLine();
		System.out.print("ID: ");
		String id = sc.nextLine();
		student = new Student(name, id);

		// add student to database
		List<Student> findStudent = questionDAO.searchStudent(student);
		if (findStudent.isEmpty()) {
			questionDAO.createStudent(student);
		}
	}

	/* Quiz */
	private static void editQuiz(Scanner sc) {
		System.out.println(EDIT);
		int type = sc.nextInt();
		sc.nextLine(); // Removing the Enter character in the buffer

		switch (type) {
		case 1: // Create quiz
			createQuiz(sc);
			break;
		case 2: // Delete quiz
			deleteQuiz(sc);
			break;
		case 3: // Edit question
			editQuestion(sc);
			break;
		case 4: // Go Back
			startProgram(sc);
			break;
		default:
			break;
		}
	}

	private static void editQuestion(Scanner sc) {
		System.out.println(EDIT_QUESTION);
		int type = sc.nextInt();
		sc.nextLine(); // Removing the Enter character in the buffer

		switch (type) {
		case 1: // Create
			createQuestion(sc);
			break;
		case 2: // Update
			updateQuestion(sc);
			break;
		case 3: // Delete
			deleteQuestion(sc);
			break;
		case 4: // Go Back
			editQuiz(sc);
			break;
		default:
			break;
		}
	}

	private static void deleteQuiz(Scanner sc) {
		System.out.println(ENTER_TITLE);
		Quiz quiz = new Quiz(sc.nextLine());
		questionDAO.deleteQuiz(quiz);
	}

	private static void createQuiz(Scanner sc) {
		// Create new quiz
		System.out.println(CREATE);
		int type = sc.nextInt();
		sc.nextLine(); // Removing the Enter character in the buffer

		switch (type) {
		case 1: // Topic
			System.out.println(ENTER_TITLE);
			Quiz quiz = new Quiz(sc.nextLine());
			if (questionDAO.searchQuiz(quiz).isEmpty()) { // new title
				questionDAO.createQuiz(quiz); // add to Quiz table

				List<Question> questions = searchByTopic(sc);
				if (questions.isEmpty()) { // no topic results
					System.out.println(ERROR);
					System.out.println("Can not find this topic");
					System.out.println("Please enter another topic");
					System.out.println(CONTOUR);
					createQuiz(sc);

				} else {
					for (int i = 0; i < questions.size(); i++) {
						String title = quiz.getTitle();
						int QID = questions.get(i).getId();
						questionDAO.createQuizContent(title, QID); // add to Quiz_Content table
					}
				}
			} else { // existed title
				System.out.println(ERROR);
				System.out.println("This title is already existed.");
				System.out.println("Please enter another title.");
				System.out.println(CONTOUR);
				createQuiz(sc);
			}
			break;
		case 2: // Difficulty
			System.out.println("This function will be updated in the future");
			createQuiz(sc);
			break;
		case 3: // Go back
			editQuiz(sc);
			break;
		default:
			break;
		}
	}

	/* Question */
	private static void createQuestion(Scanner sc) {
		System.out.println(QUESTION_TYPE);
		int type = sc.nextInt();
		sc.nextLine(); // Removing the Enter character in the buffer

		switch (type) {
		case 1: // MCQ Question
			createMCQ(sc);
			break;
		case 2: // Open Question
			createOpen(sc);
			break;
		case 3: // Go Back
			editQuestion(sc);
			break;
		default:
			break;
		}

	}

	private static void createOpen(Scanner sc) {
		Question question = setQuestionProperties(new Question(), sc);

		// save question to the database
		questionDAO.create(question);
		Question currentQuestion = questionDAO.searchByQuestion(question.getQuestion()).get(0);
		question.setId(currentQuestion.getId());

		List<String> topics = question.getTopics();
		for (int i = 0; i < topics.size(); i++) {
			String currentTopic = topics.get(i);
			List<String> result = questionDAO.searchTopic(currentTopic);
			if (result.isEmpty()) { // new topic
				questionDAO.createTopic(currentTopic); // add to topic list
			}
			questionDAO.createQuestionAbout(currentTopic, question.getId());
		}

		// complete message
		System.out.println("\nSuccess to make a question!");
		System.out.println(CONTOUR);
	}

	private static void createMCQ(Scanner sc) {
		MCQQuestion MCQQuestion = (MCQQuestion) setQuestionProperties(new MCQQuestion(), sc);

		// save question to the database
		questionDAO.create(MCQQuestion);
		Question currentQuestion = questionDAO.searchByQuestion(MCQQuestion.getQuestion()).get(0);
		MCQQuestion.setId(currentQuestion.getId());

		List<String> topics = MCQQuestion.getTopics();
		for (int i = 0; i < topics.size(); i++) {
			String currentTopic = topics.get(i);
			List<String> result = questionDAO.searchTopic(currentTopic);
			if (result.isEmpty()) { // new topic
				questionDAO.createTopic(currentTopic); // add to topic list
			}
			questionDAO.createQuestionAbout(currentTopic, MCQQuestion.getId());
		}

		// Create MCQChoice
		System.out.println(ENTER_CHOICE);
		int count = sc.nextInt();
		sc.nextLine(); // Removing the Enter character in the buffer
		for (int i = 0; i < count; i++) {
			int choiceNum = i + 1;
			System.out.println("( " + choiceNum + " )");
			System.out.print("Choice: ");
			String choice = sc.nextLine();
			System.out.print("Valid(true or false): ");
			Boolean valid = sc.nextBoolean();
			sc.nextLine(); // Removing the Enter character in the buffer

			// add choice to the database
			MCQChoice newChoice = new MCQChoice(choice, valid, MCQQuestion);
			questionDAO.createChoices(newChoice);

		}

		// complete message
		System.out.println("\nSuccess to make a question!");
		System.out.println(CONTOUR);
	}

	private static void updateQuestion(Scanner sc) {
		Question question = searchById(sc).get(0);
		List<MCQChoice> choices = questionDAO.searchChoice(question.getId());
		int QID = question.getId();

		// Show original question
		System.out.println("<< Original Question >>");
		System.out.println(question.getQuestion());
		if (!choices.isEmpty()) { // MCQ Question
			for (int i = 0; i < choices.size(); i++) {
				System.out.println(i + 1 + ". " + choices.get(i).getChoice());
			}
		}
		System.out.print("Topics : ");
		System.out.println(question.getTopics());
		System.out.print("Difficulty (1 to 5) : ");
		System.out.println(question.getDifficulty());

		// Update question
		System.out.println();
		System.out.println("<< Update Question >>");

		Question update = setQuestionProperties(question, sc);
		update.setId(QID);
		questionDAO.update(update);

		// Update topic
		questionDAO.deleteQuestionAbout(QID);
		List<String> topics = update.getTopics();
		for (int i = 0; i < topics.size(); i++) {
			String currentTopic = topics.get(i);
			List<String> result = questionDAO.searchTopic(currentTopic);
			if (result.isEmpty()) { // new topic
				questionDAO.createTopic(currentTopic); // add to topic list
			}
			questionDAO.createQuestionAbout(currentTopic, QID);
		}

		if (!choices.isEmpty()) { // MCQ Question
			// Update choices
			System.out.println(ENTER_CHOICE);
			int count = sc.nextInt();
			sc.nextLine(); // Removing the Enter character in the buffer

			questionDAO.deleteChoices(update); // delete previous choices
			for (int i = 0; i < count; i++) {
				int choiceNum = i + 1;
				System.out.println("( " + choiceNum + " )");
				System.out.print("Choice: ");
				String choice = sc.nextLine();
				System.out.print("Valid(true or false): ");
				Boolean valid = sc.nextBoolean();
				sc.nextLine(); // Removing the Enter character in the buffer

				// add choice to the database
				MCQQuestion updateMCQ = new MCQQuestion(update.getQuestion(), update.getTopics(),
						update.getDifficulty());
				updateMCQ.setId(update.getId());
				MCQChoice newChoice = new MCQChoice(choice, valid, updateMCQ);

				questionDAO.createChoices(newChoice); // add new choice

			}
		}

		// complete message
		System.out.println("\nSuccess to update the question!");
		System.out.println(CONTOUR);
	}

	private static Question setQuestionProperties(Question question, Scanner sc) {

		System.out.print("Question : ");
		question.setQuestion(sc.nextLine());

		System.out.print("Topics (separate by spaces) : ");
		// Separate one string by spaces and save as String array
		String[] topicsArray = sc.nextLine().split(" ");
		// Convert String array to List
		question.setTopics(Arrays.asList(topicsArray));

		System.out.print("Difficulty (1 to 5) : ");
		question.setDifficulty(sc.nextInt());

		return question;
	}

	private static void deleteQuestion(Scanner sc) {
		Question question = searchById(sc).get(0);

		questionDAO.delete(question);

		// complete message
		System.out.println("\nSuccess to delete the question!");
		System.out.println(CONTOUR);

	}

	private static void searchQuestion(Scanner sc) {
		System.out.print(SEARCH);
		int type = sc.nextInt();
		sc.nextLine(); // Removing the Enter character in the buffer

		List<Question> resultList;

		switch (type) {
		case 1: // ID
			resultList = searchById(sc);
			if (resultList.isEmpty()) {
				System.out.println("No Results");
			} else { // Print question and choices
				Question search = resultList.get(0);
				int QID = search.getId();
				System.out.println("(" + QID + ") " + search.getQuestion());

				List<MCQChoice> choices = questionDAO.searchChoice(QID);
				if (!choices.isEmpty()) { // MCQ Question
					for (int i = 0; i < choices.size(); i++) {
						System.out.println(i + 1 + ". " + choices.get(i).getChoice());
					}
					System.out.println(CONTOUR);
				}
			}

			break;
		case 2: // Topic
			resultList = searchByTopic(sc);
			if (resultList.isEmpty()) {
				System.out.println("No Results");
			} else {
				for (Object obj : resultList) {
					Question question = (Question) obj;
					int QID = question.getId();
					System.out.println("(" + QID + ") " + question.getQuestion() + "\n");

					List<MCQChoice> choices = questionDAO.searchChoice(QID);
					for (int i = 0; i < choices.size(); i++) {
						System.out.println(i + 1 + ". " + choices.get(i).getChoice());
					}
					System.out.println(CONTOUR);
				}
			}
			break;
		case 3: // Go Back
			startProgram(sc);
			break;
		default:
			break;
		}
	}

	/* Search Question */
	private static List<Question> searchById(Scanner sc) {
		System.out.println(ENTER_ID);

		List<Question> resultList = questionDAO.search(sc.nextInt());
		sc.nextLine(); // Removing the Enter character in the buffer
		return resultList;
	}

	private static List<Question> searchByTopic(Scanner sc) {
		System.out.println(ENTER_TOPIC);

		List<Question> resultList = questionDAO.searchByTopic(sc.nextLine());
		return resultList;
	}

	private static List<Question> searchByTitle(String title) {
		List<Question> resultList = questionDAO.searchByTitle(title);

		return resultList;
	}

}
