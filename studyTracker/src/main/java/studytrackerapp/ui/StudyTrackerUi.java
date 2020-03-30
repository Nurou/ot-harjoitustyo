package studytrackerapp.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import studytrackerapp.domain.*;

/**
 *
 * @author joelhassan
 */
// TODO: scene creation should be abstracted
// TODO: get rid of magic numbers - place dimensions in constants

public class StudyTrackerUi extends Application {

  /* Constants */
  // credentials
  private final int MIN_PASS_LENGTH = 3;
  private final int MIN_NAME_LENGTH = 2;
  private final int MIN_USERNAME_LENGTH = 2;

  // dimensions
  private final int CONTAINER_SPACING = 10;
  private final int CONTAINER_PADDING = 30;
  private final int FIELD_GROUP_SPACING = 10;
  private final int FIELD_GROUP_PADDING = 15;

  private final int SCENE_WIDTH = 1000;
  private final int SCENE_HEIGHT = 600;

  private final int CREATE_USER_WIDTH = 700;
  private final int CREATE_USER_HEIGHT = 500;

  // styles
  private final int BUTTON_PADDING = 10;

  /* Application Views */
  private Scene loginScene;
  private Scene newUserScene;
  private Scene newCourseScene;
  private Scene courseManagementScene;

  /**
   * 1. Create all of the views used in the App 2. Set star view
   */
  @Override
  public void start(Stage mainStage) {

    System.out.println("Application launched...");

    /* CREATE SCENES */
    loginScene = createLoginScene(mainStage);
    newUserScene = createNewUserScene(mainStage);
    courseManagementScene = createCourseManagementScene(mainStage);

    // ---------------------------------------

    // set up initial view
    mainStage.setTitle("Study Tracker");
    mainStage.setScene(loginScene);
    mainStage.show();
  }

  @Override
  public void stop() {
    // clean up
    System.out.println("The application is shutting down.");
  }

  public static void main(String[] args) {
    launch(args);
  }

  private Scene createLoginScene(Stage mainStage) {
    // containers
    VBox loginContainer = new VBox(CONTAINER_SPACING); // this is the outer 'wrapper' box
    VBox loginFieldGroup = new VBox(FIELD_GROUP_SPACING); // holds all input elements, nested within outer box

    // container styling
    loginContainer.setPadding(new Insets(CONTAINER_PADDING));

    // login input fields
    Label usernameLabel = new Label("Username");
    TextField usernameInput = new TextField();

    Label passwordLabel = new Label("Password");
    TextField passwordInput = new TextField();

    // add all field elements to field group
    loginFieldGroup.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput);

    // rest of login container elements
    Label loginMessage = new Label("Welcome to Study Tracker!");
    Button loginButton = new Button("Login");
    Label createUserDirection = new Label("New User? Click Here To Sign Up");
    Button createButton = new Button("Create User");

    // event handlers for buttons
    // TODO: abstract these away to a method
    loginButton.setOnAction(e -> {
      String username = usernameInput.getText();
      String password = passwordInput.getText();

      // mainStage.setScene(newUserScene);
      // TODO: login logic
      // login only with valid credentials

      mainStage.setScene(courseManagementScene);
    });

    createButton.setOnAction(e -> {
      usernameInput.setText("");
      passwordInput.setText("");
      mainStage.setScene(newUserScene);
    });

    // add everything to the outer container
    loginContainer.getChildren().addAll(loginMessage, loginFieldGroup, loginButton, createUserDirection, createButton);

    // loginContainer.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());

    // place container within view
    loginScene = new Scene(loginContainer, SCENE_WIDTH, SCENE_HEIGHT);
    return loginScene;
  }

  private Scene createNewUserScene(Stage mainStage) {

    // containers
    VBox createUserContainer = new VBox(CONTAINER_SPACING);
    VBox createUserFieldGroup = new VBox(FIELD_GROUP_SPACING);
    createUserFieldGroup.setPadding(new Insets(FIELD_GROUP_PADDING));

    // fields
    Label nameLabel = new Label("Name");
    TextField newNameInput = new TextField();
    Label newUsernameLabel = new Label("Username");
    TextField newUsernameInput = new TextField();
    Label newPasswordLabel = new Label("Password");
    TextField newPasswordInput = new TextField();
    Label userCreationMessage = new Label();

    // add all field elements to field group
    createUserFieldGroup.getChildren().addAll(nameLabel, newNameInput, newUsernameLabel, newUsernameInput,
        newPasswordLabel, newPasswordInput);

    // rest of createUser container elements
    Button createUserButton = new Button("Create");
    createUserButton.setPadding(new Insets(BUTTON_PADDING));

    // event handlers for buttons
    createUserButton.setOnAction(e -> {
      String name = newNameInput.getText();
      String username = newUsernameInput.getText();
      String password = newPasswordInput.getText();

      if (username.length() < MIN_USERNAME_LENGTH || name.length() < MIN_NAME_LENGTH
          || password.length() < MIN_PASS_LENGTH) {
        userCreationMessage.setText("The username, password, or name entered was too short");
        userCreationMessage.setTextFill(Color.RED);

      } else {
        userCreationMessage.setText("username has to be unique");
        userCreationMessage.setTextFill(Color.RED);
      }

    });

    // add everything to the container
    createUserContainer.getChildren().addAll(createUserFieldGroup, createUserButton);

    // place container within view
    newUserScene = new Scene(createUserContainer, CREATE_USER_WIDTH, CREATE_USER_HEIGHT);

    return newUserScene;
  }

  private Scene createCourseManagementScene(Stage mainStage) {
    // Containers
    VBox manageCoursesContainer = new VBox();
    manageCoursesContainer.setPadding(new Insets(10));

    VBox profileContainer = new VBox();
    profileContainer.setPadding(new Insets(10));
    profileContainer.setSpacing(20);
    profileContainer.setAlignment(Pos.TOP_CENTER);

    Label welcomeMessage = new Label("Hello *user*! Here's your profile");
    Label progressBarMessage = new Label("Your progress so far:");
    ProgressBar progressBar = new ProgressBar(0);

    // add profile elements to their container
    profileContainer.getChildren().addAll(welcomeMessage, progressBarMessage, progressBar);

    // course board - board and labels

    HBox courseBoard = new HBox();
    courseBoard.setSpacing(20);
    courseBoard.setPadding(new Insets(30));

    // column labels
    Label backlog = new Label("Backlog");
    Label ongoing = new Label("Ongoing");
    Label completed = new Label("Completed");

    // column lists
    ScrollPane backlogScroll = new ScrollPane();
    ScrollPane ongoingScroll = new ScrollPane();
    ScrollPane completedScroll = new ScrollPane();

    VBox backlogColumn = new VBox(15);
    backlogColumn.setMinWidth(250);
    backlogColumn.getChildren().add(backlog);
    backlogColumn.getChildren().add(backlogScroll);
    VBox ongoingColumn = new VBox(15);
    ongoingColumn.setMinWidth(250);
    ongoingColumn.getChildren().add(ongoing);
    ongoingColumn.getChildren().add(ongoingScroll);
    VBox completedColumn = new VBox(15);
    completedColumn.setMinWidth(250);
    completedColumn.getChildren().add(completed);
    completedColumn.getChildren().add(completedScroll);

    courseBoard.getChildren().add(backlogColumn);
    courseBoard.getChildren().add(ongoingColumn);
    courseBoard.getChildren().add(completedColumn);

    courseBoard.setAlignment(Pos.BASELINE_CENTER);

    // add everything to the container
    manageCoursesContainer.getChildren().addAll(profileContainer, courseBoard);

    // place container within view
    courseManagementScene = new Scene(manageCoursesContainer, SCENE_WIDTH, SCENE_HEIGHT);

    return courseManagementScene;
  }
}
