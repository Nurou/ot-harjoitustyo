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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
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

  private final double CIRCLE_RADIUS = 80;

  // other
  private final String FIRST_COLUMN_LABEL = "Backlog";
  private final String SECOND_COLUMN_LABEL = "Ongoing";
  private final String THIRD_COLUMN_LABEL = "Done";

  /* Globals */
  private boolean deleteMode;

  // Menu & User Stats
  private final Label userMessageLabel = new Label();
  private final ProgressBar progressBar = new ProgressBar();
  private final Text progressCircleText = new Text();

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
    // loginScene.getStylesheets().add(getClass().getResource("css/login.css").toExternalForm());
    window.setScene(loginScene);
    window.show();
    // prevents closing of app prior to logout
    // window.setOnCloseRequest(e -> {
    // System.out.println(userService.getLoggedUser());
    // if (userService.getLoggedUser() != null) {
    // e.consume();
    // }
    // });
  }

  @Override
  public void stop() {
    // clean up
    System.out.println("Application shutting down.");
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

    // containers
    final var studyTrackerBoardContainer = new VBox();
    studyTrackerBoardContainer.setPadding(new Insets(10));

    final var userStatsContainer = new VBox();
    userStatsContainer.setPadding(new Insets(10));
    userStatsContainer.setSpacing(20);
    userStatsContainer.setAlignment(Pos.TOP_CENTER);

    // buttons for menu
    final var logoutButton = new Button("Logout");
    logoutButton.setOnAction(e -> {
      userService.logout();
      window.setScene(loginScene);
    });

    logoutButton.setAlignment(Pos.TOP_LEFT);

    final var deleteModeToggleButton = new Button("Toggle Delete Mode");

    deleteModeToggleButton.setOnAction(e -> {
      deleteMode = !deleteMode;
      System.out.println(deleteMode);
      if (deleteMode) {
        deleteModeToggleButton.setStyle("-fx-base: #E74C3C;");
        deleteModeToggleButton.setText("Delete Mode");
      } else {
        deleteModeToggleButton.setStyle("-fx-base: #080;");
        deleteModeToggleButton.setText("Normal Mode");
      }
      redrawList();
    });

    deleteModeToggleButton.setAlignment(Pos.TOP_RIGHT);

    // set up the board for the courses, inc. labels
    final HBox courseBoard = new HBox();
    courseBoard.setSpacing(20);
    courseBoard.setPadding(new Insets(30));

    courseBoard.getChildren().add(createNewColumn(FIRST_COLUMN_LABEL, backlogCourses));
    courseBoard.getChildren().add(createNewColumn(SECOND_COLUMN_LABEL, ongoingCourses));
    courseBoard.getChildren().add(createNewColumn(THIRD_COLUMN_LABEL, completedCourses));

    courseBoard.setAlignment(Pos.BASELINE_CENTER);

    // other components
    final var progressBarMessage = new Label("Your progress so far:");
    final var progressInNumericalForm = createNumericalProgressRep();

    userStatsContainer.getChildren().addAll(userMessageLabel, progressBarMessage, progressBar, progressInNumericalForm);

    // add menu components to to menu
    final HBox menuContainer = new HBox(15);
    menuContainer.setPadding(new Insets(10, 10, 10, 10));
    menuContainer.getChildren().addAll(logoutButton, deleteModeToggleButton);

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

    final var modifyCourseButton = new ToggleButton("Modify Courses");
    modifyCourseButton.setOnAction(e -> {
      // TODO: logic here...
    });

    modifyCourseButton.setAlignment(Pos.BASELINE_RIGHT);

    boardButtonsContainer.getChildren().addAll(addCourseButton, modifyCourseButton);
    boardButtonsContainer.setAlignment(Pos.BOTTOM_LEFT);

    // add everything to the outer container
    final var separator = new Separator(Orientation.HORIZONTAL);
    studyTrackerBoardContainer.getChildren().addAll(separator, courseBoard, boardButtonsContainer);

    final VBox verticalContainer = new VBox(10);
    // nest menu and board inside scene
    verticalContainer.getChildren().addAll(menuContainer, userStatsContainer, studyTrackerBoardContainer);
    return new Scene(verticalContainer, SCENE_WIDTH, SCENE_HEIGHT);
  }

  private Scene createNewCourseScene(final Stage window) {
    // container for field group
    final var newCourseFieldGroup = new VBox();
    newCourseFieldGroup.setSpacing(10);

    // individual fields
    final var courseNameLabel = new Label("Name");
    final var courseNameInput = new TextField();

    final var courseCreditsLabel = new Label("Credits");
    final var courseCreditsInput = createIntTextField();

    final var statusLabel = new Label("Status");
    final var statusInput = createIntTextField();

    final var courseLinkLabel = new Label("Link");
    final var courseLinkInput = new TextField();

    // add fields to field group
    newCourseFieldGroup.getChildren().addAll(courseNameLabel, courseNameInput, courseCreditsLabel, courseCreditsInput,
        statusLabel, statusInput, courseLinkLabel, courseLinkInput);

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

      final var status = Integer.parseInt(statusInput.getText());

      final var link = courseLinkInput.getText();

      if (courseService.createCourse(name, credits, compulsoryInteger, status, link)) {

        // refresh data
        redrawList();
        updateProgress();

        // change view back to course manager
        window.setScene(studyTrackingScene);

        // clear inputs
        courseNameInput.clear();
        statusInput.clear();
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
    final var courseNode = new HBox(10);
    courseNode.setPadding(new Insets(5, 5, 5, 5));
    courseNode.setStyle("-fx-stroke: grey; -fx-stroke-width: 5;");

    final var courseName = course.getName();

    final var courseNameLabel = new Label(courseName);
    courseNameLabel.setMinHeight(28);
    // courseNameLabel.setStyle("-fx-text-fill: ladder(background, white 49%, black
    // 50%);");

    switch (course.getStatus()) {
      case 0:
        courseNode.setStyle("-fx-background-color:#ffd5c0;");
        break;
      case 1:
        courseNode.setStyle("-fx-background-color: #AED6F1;");
        break;
      case 2:
        courseNode.setStyle("-fx-background-color: #ABEBC6;");
        break;
    }

    final Button deleteButton = new Button("Delete");
    deleteButton.setOnAction(e -> {
      courseService.deleteCourse(courseName);
      redrawList();
      updateProgress();
    });

    deleteButton.setStyle("-fx-base: #E74C3C;");

    // button only visible in delete mode
    deleteButton.setVisible(deleteMode);

    courseNode.getChildren().addAll(courseNameLabel, deleteButton);

    // drag & drop functionality
    courseNode.setOnDragDetected(e -> {

      System.out.println("onDragDetected");

      currentCourse = (HBox) e.getSource();
      final Dragboard db = courseNode.startDragAndDrop(TransferMode.ANY);
      final ClipboardContent content = new ClipboardContent();
      content.putString(courseNameLabel.getText());
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

    // first need to check if user has any courses
    if (courseService.getCourses().isEmpty()) {
      progressBar.setProgress(0);
      progressCircleText.setText(0 + " / " + targetCredits);
      return;
    }

    final var currentCredits = courseService.getCourses().stream()
        .filter(course -> course.getStatus() == completedStatus).map(completed -> completed.getCredits())
        .reduce(0, (totalCredits, courseCredits) -> totalCredits + courseCredits);

    final var currentProgressAsFraction = (double) currentCredits / targetCredits;
    System.out.println(currentProgressAsFraction);

    progressBar.setProgress(currentProgressAsFraction);
    progressCircleText.setText(currentCredits + " / " + targetCredits);
  }

  public void setLoginMessage() {
    var studentName = "";
    studentName = userService.getLoggedUser().getName();
    // capitalize first letter of name
    studentName = studentName.substring(0, 1).toUpperCase() + studentName.substring(1).toLowerCase();

    userMessageLabel.setText("Hey " + studentName + "! Here's your profile");
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

  private StackPane createNumericalProgressRep() {
    final var progressCircle = createCircle();
    progressCircleText.setBoundsType(TextBoundsType.VISUAL);
    progressCircleText.setFont(Font.font(null, FontWeight.BOLD, 24));

    final var stack = new StackPane();
    stack.getChildren().addAll(progressCircle, progressCircleText);
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