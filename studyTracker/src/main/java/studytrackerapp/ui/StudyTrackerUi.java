package studytrackerapp.ui;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.function.UnaryOperator;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.beans.property.ReadOnlyProperty;

import studytrackerapp.dao.CourseDao;
import studytrackerapp.dao.Database;
import studytrackerapp.dao.UserDao;
import studytrackerapp.domain.*;

/**
 *
 * @author joelhassan
 */
// TODO: scene creation should be abstracted
// TODO: get rid of magic numbers - place dimensions in constants

public class StudyTrackerUi extends Application {

  /**
   * Constants
   */

  // credentials
  private final int MIN_PASS_LENGTH = 3;
  private final int MIN_NAME_LENGTH = 2;
  private final int MIN_USERNAME_LENGTH = 2;

  // dimensions
  private final int CONTAINER_SPACING = 20;
  private final int CONTAINER_PADDING = 30;
  private final int FIELD_GROUP_SPACING = 10;
  private final int FIELD_GROUP_PADDING = 15;

  private final int SCENE_WIDTH = 1000;
  private final int SCENE_HEIGHT = 900;

  private final int CREATE_USER_WIDTH = 700;
  private final int CREATE_USER_HEIGHT = 500;

  private final double CIRCLE_RADIUS = 100;

  // other
  private final String FIRST_COLUMN_LABEL = "Backlog";
  private final String SECOND_COLUMN_LABEL = "Ongoing";
  private final String THIRD_COLUMN_LABEL = "Done";

  /* Globals */
  private boolean deleteMode;
  private boolean addGradeMode;

  // Menu & User Stats
  private final Label userMessageLabel = new Label();
  private final ProgressIndicator progressIndicator = new ProgressIndicator();
  private final Text progressCircleText = new Text();
  private final Text gpaText = new Text();

  // styles
  private final int BUTTON_PADDING = 10;

  /* Application Views */
  private Scene loginScene;
  private Scene newUserScene;
  private Scene newCourseScene;
  private Scene studyTrackingScene;

  /**
   * Nodes
   */
  private final VBox backlogCourses = new VBox();
  private final VBox ongoingCourses = new VBox();
  private final VBox completedCourses = new VBox();

  /**
   * Services
   */
  private UserService userService;
  private CourseService courseService;

  /**
   * Initializes everything needed for the app to run
   * 
   * @throws Exception unable to initialize application
   */

  @Override
  public void init() throws Exception {

    // Globals
    deleteMode = false;
    addGradeMode = false;

    // Database
    final var database = new Database();
    database.createDatabase("study-tracker.db");

    // Daos
    final var userDao = new UserDao(database);
    final var courseDao = new CourseDao(database);

    // Services
    userService = new UserService(userDao);
    courseService = new CourseService(courseDao);

  }

  @Override
  public void start(final Stage window) {

    System.out.println("Application launched...");

    /**
     * CREATE SCENES
     */

    loginScene = createLoginScene(window);

    newUserScene = createNewUserScene(window);

    studyTrackingScene = createStudyTrackingScene(window);

    newCourseScene = createNewCourseScene(window);

    // ---------------------------------------

    /**
     * SET UP INITIAL VIEW
     */

    window.setTitle("Study Tracker");
    window.setScene(loginScene);
    window.show();
    // prevents closing of app prior to logout
    window.setOnCloseRequest(e -> {
      System.out.println(userService.getLoggedUser());
      if (userService.getLoggedUser() != null) {
        e.consume();
      }
    });
  }

  @Override
  public void stop() {
    // clean up
    System.out.println("Application shutting down.");
    deleteMode = false;
    addGradeMode = false;
  }

  public static void main(final String[] args) {
    launch(args);
  }

  /**
   * Methods for creating the various application views
   * 
   */

  private Scene createLoginScene(final Stage window) {

    // login input fields
    final var usernameLabel = new Label("Username");
    usernameLabel.setPrefWidth(100);
    usernameLabel.setMaxWidth(100);
    final var usernameInput = new TextField();

    final var passwordLabel = new Label("Password");
    passwordLabel.setPrefWidth(100);
    passwordLabel.setMaxWidth(100);
    final var passwordInput = new PasswordField();

    // container for input fields
    final var loginFieldGroup = new VBox(FIELD_GROUP_SPACING);

    // add all field elements to field group
    loginFieldGroup.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput);

    // rest of login container elements
    final var loginMessage = new Label("Welcome to Study Tracker!");

    final var loginStatusMessage = new Label();
    final var loginButton = createLoginButton("Login", usernameInput, passwordInput, window, loginStatusMessage);

    final var createUserSceneLabel = new Label("New User? Click Here To Sign Up");
    final var createUserSceneButton = createNewUserDirectButton("Create User", usernameInput, passwordInput, window,
        loginStatusMessage);

    // the outer 'wrapper' box
    final var loginContainer = new VBox(CONTAINER_SPACING);
    loginContainer.setPadding(new Insets(CONTAINER_PADDING));
    loginContainer.setStyle("-fx-alignment: center");

    // add all components to the outer container
    loginContainer.getChildren().addAll(loginMessage, loginStatusMessage, loginFieldGroup, loginButton,
        createUserSceneLabel, createUserSceneButton);

    // place container within view
    return new Scene(loginContainer, 350, 500);
  }

  /**
   * @param window
   * @return Scene
   */
  private Scene createNewUserScene(final Stage window) {

    // main container
    final var createUserContainer = new VBox(CONTAINER_SPACING);
    createUserContainer.setPadding(new Insets(CONTAINER_PADDING));

    // container for fields
    final var createUserFieldGroup = new VBox(FIELD_GROUP_SPACING);
    createUserFieldGroup.setPadding(new Insets(FIELD_GROUP_PADDING));

    // container for buttons
    final var buttonContainer = new HBox(FIELD_GROUP_SPACING);
    buttonContainer.setPadding(new Insets(FIELD_GROUP_PADDING));

    // fields
    final var nameLabel = new Label("Name");
    final var newNameInput = new TextField();
    final var newUsernameLabel = new Label("Username");
    final var newUsernameInput = new TextField();
    final var newPasswordLabel = new Label("Password");
    final var newPasswordInput = new TextField();
    final var programNameLabel = new Label("Study Program");
    final var programNameInput = new TextField();
    final var targetCreditsLabel = new Label("Target Credits");
    final var targetCreditsInput = createIntTextField();

    // other elements
    final var createUserMessage = new Label();

    // add all field elements to field group
    createUserFieldGroup.getChildren().addAll(nameLabel, newNameInput, newUsernameLabel, newUsernameInput,
        newPasswordLabel, newPasswordInput, programNameLabel, programNameInput, targetCreditsLabel, targetCreditsInput);

    // rest of createUser container elements
    final var createUserButton = new Button("Create");
    createUserButton.setPadding(new Insets(BUTTON_PADDING));
    final var returnButton = new Button("Return");
    returnButton.setPadding(new Insets(BUTTON_PADDING));

    // add buttons to their container
    buttonContainer.getChildren().addAll(createUserButton, returnButton);

    // event handlers for buttons
    createUserButton.setOnAction(e -> {
      final var name = newNameInput.getText();
      final var username = newUsernameInput.getText();
      final var password = newPasswordInput.getText();
      final var programName = programNameInput.getText();
      final var targetCredits = targetCreditsInput.getText();

      System.out.println(targetCredits);

      // credentials validation
      if ((username.length() < MIN_USERNAME_LENGTH) || (name.length() < MIN_NAME_LENGTH)
          || (password.length() < MIN_PASS_LENGTH)) {
        createUserMessage.setText("Either the username, password, or name that was entered was too short.");
        createUserMessage.setTextFill(Color.RED);
        createUserMessage.setFont(Font.font(null, FontWeight.BOLD, 18));
      } else if (userService.createUser(username, name, password, programName, Integer.parseInt(targetCredits))) {
        System.out.println("All good!");
        createUserMessage.setText("Success!");
        createUserMessage.setTextFill(Color.GREEN);
        createUserMessage.setFont(Font.font(null, FontWeight.BOLD, 18));
        // clear fields
        newNameInput.clear();
        newUsernameInput.clear();
        newPasswordInput.clear();
        programNameInput.clear();
        targetCreditsInput.clear();
        window.setScene(loginScene);
      } else {
        createUserMessage.setText("A user with the username: '" + username + "' already exists.");
        createUserMessage.setTextFill(Color.RED);
      }

    });

    returnButton.setOnAction(e -> {
      window.setScene(loginScene);
    });

    // add nested containers to main container
    createUserContainer.getChildren().addAll(createUserMessage, createUserFieldGroup, buttonContainer);

    // place container within view
    return new Scene(createUserContainer, 400, CREATE_USER_HEIGHT);

  }

  private Scene createStudyTrackingScene(final Stage window) {
    // top menu section
    final HBox menuContainer = new HBox(15);
    menuContainer.setPadding(new Insets(10, 10, 10, 10));

    final var logoutButton = new Button("Logout");
    logoutButton.setOnAction(e -> {
      userService.logout();
      window.setScene(loginScene);
    });

    logoutButton.setAlignment(Pos.TOP_LEFT);

    final var deleteModeButton = new Button("Remove Courses");
    deleteModeButton.setStyle("-fx-base: #add8e6;");
    deleteModeButton.setOnAction(e -> {
      deleteMode = !deleteMode;
      redrawList();
    });

    final var addGradeButton = new Button("Add Grades");
    addGradeButton.setStyle("-fx-base: #add8e6;");
    addGradeButton.setOnAction(e -> {
      addGradeMode = !addGradeMode;
      redrawList();
    });

    menuContainer.getChildren().addAll(logoutButton, deleteModeButton, addGradeButton);

    // user stats section
    final var userStatsContainer = new HBox();
    userStatsContainer.setPadding(new Insets(10));
    userStatsContainer.setSpacing(20);
    userStatsContainer.setAlignment(Pos.TOP_CENTER);

    final var progressInNumericalForm = createCircleStat(progressCircleText);
    final var gpaStat = createCircleStat(gpaText);
    progressIndicator.setStyle(" -fx-progress-color: green;");
    progressIndicator.setMinSize(160, 160);

    userStatsContainer.getChildren().addAll(progressInNumericalForm, gpaStat);

    // board section
    final var courseBoard = new HBox();
    courseBoard.setSpacing(20);
    courseBoard.setPadding(new Insets(30));

    courseBoard.getChildren().add(createNewColumn(FIRST_COLUMN_LABEL, backlogCourses));
    courseBoard.getChildren().add(createNewColumn(SECOND_COLUMN_LABEL, ongoingCourses));
    courseBoard.getChildren().add(createNewColumn(THIRD_COLUMN_LABEL, completedCourses));

    courseBoard.setAlignment(Pos.BASELINE_CENTER);

    // buttons for modifying courses
    final var boardButtonsContainer = new HBox();
    boardButtonsContainer.setPadding(new Insets(10));
    boardButtonsContainer.setSpacing(20);
    boardButtonsContainer.setAlignment(Pos.BOTTOM_LEFT);

    final var addCourseButton = new Button("Add Course");
    addCourseButton.setOnAction(e -> {
      window.setScene(newCourseScene);
    });

    addCourseButton.setAlignment(Pos.BASELINE_RIGHT);

    boardButtonsContainer.getChildren().add(addCourseButton);
    boardButtonsContainer.setAlignment(Pos.BOTTOM_LEFT);

    final var separator = new Separator(Orientation.HORIZONTAL);

    final var dragAndDropInstruction = new Text("Use drag & drop to move courses");
    dragAndDropInstruction.setFont(Font.font(null, FontWeight.BOLD, 18));

    final var studyTrackerBoardContainer = new VBox();
    studyTrackerBoardContainer.setPadding(new Insets(10));
    studyTrackerBoardContainer.setAlignment(Pos.CENTER);
    studyTrackerBoardContainer.getChildren().addAll(separator, dragAndDropInstruction, courseBoard,
        boardButtonsContainer);

    // add everything to the outer container
    final var verticalContainer = new VBox(10);
    verticalContainer.getChildren().addAll(menuContainer, userMessageLabel, userStatsContainer,
        studyTrackerBoardContainer);
    verticalContainer.setAlignment(Pos.CENTER);
    return new Scene(verticalContainer, SCENE_WIDTH, SCENE_HEIGHT);
  }

  private Scene createNewCourseScene(final Stage window) {

    final var CHOICE_1 = "Not started";
    final var CHOICE_2 = "Ongoing";
    final var CHOICE_3 = "Completed";

    // container for field group
    final var newCourseFieldGroup = new VBox();
    newCourseFieldGroup.setSpacing(10);

    // individual fields
    final var courseNameLabel = new Label("Name");
    final var courseNameInput = new TextField();

    final var courseCreditsLabel = new Label("Credits");
    final var courseCreditsInput = createIntTextField();

    // dropdown menu for selecting course status

    final var completionStatusLabel = new Label("Status");
    final ChoiceBox<String> completionStatusChoice = new ChoiceBox<>();
    completionStatusChoice.getItems().addAll(CHOICE_1, CHOICE_2, CHOICE_3);
    completionStatusChoice.setValue(CHOICE_1);

    final var courseLinkLabel = new Label("Link");
    final var courseLinkInput = new TextField();

    // add fields to field group
    newCourseFieldGroup.getChildren().addAll(courseNameLabel, courseNameInput, courseCreditsLabel, courseCreditsInput,
        completionStatusLabel, completionStatusChoice, courseLinkLabel, courseLinkInput);

    // horizontal container for checkbox selection - course mandatory or not
    final var statusContainer = new HBox();
    final var mandatoryLabel = new Label("Mandatory: ");
    statusContainer.getChildren().add(mandatoryLabel);

    // allow only one checkbox to be selected at once
    final var checkboxOptions = new String[] { "Yes ", "No " };
    // max number of active selections
    final var maxCount = 1;
    final var activeBoxes = new LinkedHashSet<CheckBox>();

    // define listener for checkbox
    final ChangeListener<Boolean> selectionListener = (o, oldValue, newValue) -> {
      // cast object to checkbox type
      var cb = (CheckBox) ((ReadOnlyProperty<Boolean>) o).getBean();

      if (newValue) {
        // we have another active checkbox
        activeBoxes.add(cb);

        // over limit?
        if (activeBoxes.size() > maxCount) {
          // get first checkbox to be activated
          cb = activeBoxes.iterator().next();
          // de-activate it; change listener will remove
          cb.setSelected(false);
        }
      } else {
        activeBoxes.remove(cb);
      }
    };

    // create checkboxes by looping over options
    // and adding the listener to each one
    for (int i = 0; i < checkboxOptions.length; i++) {
      final var cb = new CheckBox(checkboxOptions[i]);
      cb.selectedProperty().addListener(selectionListener);
      statusContainer.getChildren().add(cb);
    }

    // buttons
    final var buttonContainer = new HBox();
    final var addCourseButton = new Button("Add");
    addCourseButton.setOnAction(e -> {
      // swoop all the course info
      final var name = courseNameInput.getText();
      final var credits = Integer.parseInt(courseCreditsInput.getText());

      // logic for compulsory property
      var compulsoryString = "";
      for (final CheckBox checkBox : activeBoxes) {
        compulsoryString = checkBox.getText();
      }

      var compulsoryInteger = 0;

      switch (compulsoryString) {
        case "Yes":
          compulsoryInteger = 1;
          break;
        case "No":
          compulsoryInteger = 0;
          break;
        default:
          compulsoryInteger = 1;
          break;
      }

      final var status = (String) completionStatusChoice.getValue();
      var statusInt = 0;

      switch (status) {
        case CHOICE_1:
          statusInt = 0;
          break;
        case CHOICE_2:
          statusInt = 1;
          break;
        case CHOICE_3:
          statusInt = 2;
          break;
        default:
          break;
      }

      final var link = courseLinkInput.getText();

      if (courseService.createCourse(name, credits, compulsoryInteger, statusInt, link)) {

        // refresh data
        redrawList();
        updateProgress();

        // change view back to course manager
        window.setScene(studyTrackingScene);

        // clear inputs
        courseNameInput.clear();
        // statusInput.clear();
        courseLinkInput.clear();
        courseCreditsInput.clear();
      }

    });

    final var returnButton = new Button("Return");

    returnButton.setOnAction(e -> {
      window.setScene(studyTrackingScene);
    });

    buttonContainer.getChildren().addAll(addCourseButton, returnButton);

    // place containers inside outer container
    final var wrapperContainer = new BorderPane();
    wrapperContainer.setPadding(new Insets(CONTAINER_PADDING));
    wrapperContainer.setTop(newCourseFieldGroup);
    wrapperContainer.setMargin(newCourseFieldGroup, new Insets(10, 0, 10, 0));
    wrapperContainer.setCenter(statusContainer);
    wrapperContainer.setBottom(buttonContainer);

    return new Scene(wrapperContainer, 400, SCENE_HEIGHT);

  }

  private HBox currentCourse = new HBox();

  private Node createCourseNode(final Course course) {
    final var courseName = course.getName();
    final var courseNode = new HBox(10);
    courseNode.setPadding(new Insets(5, 5, 5, 5));
    courseNode.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, Color.LIGHTGRAY, Color.LIGHTGRAY, Color.LIGHTGRAY,
        BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID,
        CornerRadii.EMPTY, new BorderWidths(4), Insets.EMPTY)));

    final var courseLabel = new Label(courseName);
    courseLabel.setMinHeight(30);
    courseLabel.setFont(Font.font(null, FontWeight.BOLD, 16));

    switch (course.getStatus()) {
      case 0:
        courseNode.setStyle("-fx-background-color:#ffcccb;");
        break;
      case 1:
        courseNode.setStyle("-fx-background-color: #ffffed;");
        break;
      case 2:
        courseNode.setStyle("-fx-background-color: #90ee90;");
        break;
    }

    final var deleteButton = new Button("Delete");
    deleteButton.setOnAction(e -> {
      courseService.deleteCourse(courseName);
      redrawList();
      updateProgress();
    });

    deleteButton.setStyle("-fx-base: #E74C3C;");

    final var gradeField = createIntTextField();
    gradeField.setPrefWidth(60);

    final var addGradeButton = new Button("Grade");

    addGradeButton.setOnAction(e -> {
      try {
        courseService.changeCourseGrade(courseName, Integer.parseInt(gradeField.getText()));
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
      redrawList();
      updateProgress();
    });

    addGradeButton.setVisible(addGradeMode & course.getStatus() == 2);
    gradeField.setVisible(addGradeMode & course.getStatus() == 2);
    deleteButton.setVisible(deleteMode);

    courseNode.getChildren().addAll(courseLabel, deleteButton, addGradeButton, gradeField);

    // drag & drop functionality
    courseNode.setOnDragDetected(e -> {

      System.out.println("onDragDetected");

      currentCourse = (HBox) e.getSource();
      final Dragboard db = courseNode.startDragAndDrop(TransferMode.ANY);
      final ClipboardContent content = new ClipboardContent();
      content.putString(courseName);
      db.setContent(content);
      e.consume();

    });

    return courseNode;
  }

  /**
   * Util Methods
   */

  // TODO: Place these somewhere else

  /**
   * This method is used for text-field inputs that need to be restricted to
   * numbers only
   * 
   * @param targetCreditsInput
   */
  private TextField createIntTextField() {
    final var textFieldInput = new TextField();
    final UnaryOperator<Change> filter = change -> {
      final var text = change.getText();

      if (text.matches("[0-9]*")) {
        return change;
      }

      return null;
    };
    // attach the filter to the formatter
    final var textFormatter = new TextFormatter<>(filter);
    textFieldInput.setTextFormatter(textFormatter);
    return textFieldInput;
  }

  /**
   * Calculates a user's current progress in their program
   * 
   * @return - the progress as a decimal fraction
   */
  private void updateProgress() {
    final var targetCredits = userService.getLoggedUser().getTarget();
    final var completedStatus = 2;
    var hasCompletedACourse = false;

    // first need to check if user has any courses
    if (courseService.getCourses().isEmpty()) {
      progressCircleText.setText("Progress: N/A");
      gpaText.setText("GPA: N/A");
      return;
    }

    // are any completed?
    if ((courseService.getCourses().stream().filter(c -> (c.getStatus() == 2))).findFirst().orElse(null) == null) {
      progressCircleText.setText("Progress: N/A");
      gpaText.setText("GPA: N/A");
      return;
    }

    final var currentCredits = courseService.getCourses().stream()
        .filter(course -> course.getStatus() == completedStatus).map(completed -> completed.getCredits())
        .reduce(0, (totalCredits, courseCredits) -> totalCredits + courseCredits);

    final var currentProgressAsFraction = (double) currentCredits / targetCredits;
    System.out.println(currentProgressAsFraction);

    progressCircleText.setText("Progress: " + currentCredits + " / " + targetCredits);

    var weightedAverage = 0;
    var numerator = 0;
    var denominator = 0;

    for (Course c : courseService.getCourses()) {
      if (c.getGrade() == 0)
        continue;
      denominator += c.getCredits();
      numerator += c.getCredits() * c.getGrade();
    }

    weightedAverage = numerator / denominator;

    gpaText.setText("GPA: " + String.valueOf(weightedAverage));
  }

  public void setLoginMessage() {
    var studentName = "";
    studentName = userService.getLoggedUser().getName();
    // capitalize first letter of name
    studentName = studentName.substring(0, 1).toUpperCase() + studentName.substring(1).toLowerCase();

    userMessageLabel.setText("Hey " + studentName + "! Here's your profile and progress");
    userMessageLabel.setFont(Font.font(null, FontWeight.BOLD, 18));
  }

  /**
   * Refreshes the board used to manage studies
   */

  public void redrawList() {

    // clear current state
    backlogCourses.getChildren().clear();
    ongoingCourses.getChildren().clear();
    completedCourses.getChildren().clear();

    // add each course to the board based on status
    courseService.getCourses().forEach(course -> {
      switch (course.getStatus()) {
        case 1:
          ongoingCourses.getChildren().add(createCourseNode(course));
          break;
        case 2:
          completedCourses.getChildren().add(createCourseNode(course));
          break;
        default:
          backlogCourses.getChildren().add(createCourseNode(course));
          break;
      }
    });
  }

  private Circle createCircle() {
    final var circle = new Circle(CIRCLE_RADIUS);

    circle.setStroke(Color.FORESTGREEN);
    circle.setStrokeWidth(5);
    circle.setStrokeType(StrokeType.INSIDE);
    circle.setFill(Color.AZURE);
    circle.relocate(0, 0);

    return circle;
  }

  private StackPane createCircleStat(Text text) {
    final var progressCircle = createCircle();
    text.setBoundsType(TextBoundsType.VISUAL);
    text.setFont(Font.font(null, FontWeight.BOLD, 18));

    final var stack = new StackPane();
    stack.getChildren().addAll(progressCircle, text);
    stack.setLayoutX(30);
    stack.setLayoutY(30);

    return stack;
  }

  private Button createLoginButton(final String buttonText, final TextField usernameInput,
      final TextField passwordInput, final Stage window, final Label loginStatusMessage) {
    final var loginButton = new Button(buttonText);

    // on the user clicking to login
    loginButton.setOnAction(e -> {
      // get user info
      final var username = usernameInput.getText();
      final var password = passwordInput.getText();

      // attempt login...
      if (userService.login(username, password)) {
        // all good! Get user details
        final var loggedUser = userService.getLoggedUser();
        // assign user to courses
        courseService.assignUser(loggedUser);
        // welcome user
        setLoginMessage();
        // fetch stats
        updateProgress();
        // fetch their courses
        redrawList();

        window.setScene(studyTrackingScene);
      } else {
        // login unsuccessful
        loginStatusMessage
            .setText("Invalid credentials, or the user: '  " + username + "' has not been created. Try again.");

        // display message
        loginStatusMessage.setTextFill(Color.RED);
        loginStatusMessage.setFont(Font.font(null, FontWeight.BOLD, 14));
      }
      // reset fields
      usernameInput.clear();
      passwordInput.clear();
    });

    return loginButton;

  }

  private Button createNewUserDirectButton(final String buttonText, final TextField usernameInput,
      final TextField passwordInput, final Stage window, final Label loginStatusMessage) {
    final var createUserSceneButton = new Button(buttonText);

    // on the user clicking to login
    // user wants to create a new account
    createUserSceneButton.setOnAction(e -> {
      usernameInput.clear();
      passwordInput.clear();
      loginStatusMessage.setText("");
      window.setScene(newUserScene);
    });

    return createUserSceneButton;

  }

  private VBox createNewColumn(final String label, final VBox courseList) {

    final var columnLabel = new Label(label);

    final var scroll = new ScrollPane();
    scroll.setMinHeight(300);

    courseList.setMaxWidth(310);
    courseList.setMinWidth(310);

    scroll.setContent(courseList);

    final var column = new VBox(15);
    column.setMinWidth(250);
    column.getChildren().add(columnLabel);
    column.getChildren().add(scroll);

    return addDragAndDropFunctionality(column, label);
  }

  private VBox addDragAndDropFunctionality(final VBox column, final String label) {
    column.setOnDragOver(event -> {
      if (event.getGestureSource() == currentCourse && event.getDragboard().hasString()) {
        event.acceptTransferModes(TransferMode.MOVE);
      }
      event.consume();
    });

    column.setOnDragDropped(event -> {
      final Dragboard db = event.getDragboard();
      boolean success = false;

      if (db.hasString()) {
        switch (label) {
          case FIRST_COLUMN_LABEL:
            try {
              courseService.changeCourseStatus(db.getString(), 0);
              redrawList();
              updateProgress();
            } catch (final SQLException e) {
              // TODO: do something more helpful with these
              e.printStackTrace();
            }
            break;
          case SECOND_COLUMN_LABEL:
            try {
              courseService.changeCourseStatus(db.getString(), 1);
              redrawList();
              updateProgress();
            } catch (final SQLException e) {
              e.printStackTrace();
            }
            break;
          case THIRD_COLUMN_LABEL:
            try {
              courseService.changeCourseStatus(db.getString(), 2);
              redrawList();
              updateProgress();
            } catch (final SQLException e) {
              e.printStackTrace();
            }
            break;
          default:
            break;
        }
        success = true;
      } else {
      }
      event.setDropCompleted(success);
      event.consume();
    });

    column.setOnDragDone(event -> {
      if (event.getTransferMode() == TransferMode.MOVE) {
        System.out.println("Drag Done");
      }
      event.consume();
    });
    return column;
  }

}