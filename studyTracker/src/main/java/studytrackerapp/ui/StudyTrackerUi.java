package studytrackerapp.ui;

import java.util.LinkedHashSet;
import java.util.Set;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.beans.*;
import javafx.beans.property.ReadOnlyProperty;
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

  @Override
  public void start(Stage mainStage) {

    System.out.println("Application launched...");

    /* CREATE SCENES */
    loginScene = createLoginScene(mainStage);
    newUserScene = createNewUserScene(mainStage);
    courseManagementScene = createCourseManagementScene(mainStage);
    newCourseScene = createNewCourseScene(mainStage);

    // ---------------------------------------

    /* SET UP INITIAL VIEW */
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

  /**
   * Methods for creating the various application views
   * 
   */

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
    return new Scene(loginContainer, SCENE_WIDTH, SCENE_HEIGHT);
  }

  /**
   * @param mainStage
   * @return Scene
   */
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
    return new Scene(createUserContainer, CREATE_USER_WIDTH, CREATE_USER_HEIGHT);

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

    // set up the board for the courses, inc. labels
    HBox courseBoard = new HBox();
    courseBoard.setSpacing(20);
    courseBoard.setPadding(new Insets(30));

    // column labels
    Label backlog = new Label("Backlog");
    Label ongoing = new Label("Ongoing");
    Label completed = new Label("Completed");

    // column areas
    ScrollPane backlogScroll = new ScrollPane();
    ScrollPane ongoingScroll = new ScrollPane();
    ScrollPane completedScroll = new ScrollPane();

    // put labels and areas together to form column
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

    // buttons
    Button logoutButton = new Button("Logout");
    logoutButton.setOnAction(e -> {
      mainStage.setScene(loginScene);
    });

    logoutButton.setAlignment(Pos.BASELINE_LEFT);

    Button addCourseButton = new Button("Add Course");
    addCourseButton.setOnAction(e -> {
      mainStage.setScene(newCourseScene);
    });

    addCourseButton.setAlignment(Pos.BASELINE_RIGHT);

    ToggleButton modifyCourseButton = new ToggleButton("Modify Courses");
    modifyCourseButton.setOnAction(e -> {

    });

    modifyCourseButton.setAlignment(Pos.BASELINE_RIGHT);

    // other components
    Separator separator = new Separator(Orientation.HORIZONTAL);

    // add everything to the container
    manageCoursesContainer.getChildren().addAll(profileContainer, separator, courseBoard, logoutButton, addCourseButton,
        modifyCourseButton);

    // place container within view
    return new Scene(manageCoursesContainer, SCENE_WIDTH, SCENE_HEIGHT);
  }

  private Scene createNewCourseScene(Stage mainStage) {
    // container for field group
    VBox courseFieldGroup = new VBox();
    courseFieldGroup.setPadding(new Insets(10));

    // individual fields
    Label courseNameLabel = new Label("Name");
    TextField courseNameInput = new TextField();

    Label courseCredits = new Label("Credits");
    TextField courseCreditsInput = new TextField();

    Label coursePeriod = new Label("Period");
    TextField coursePeriodInput = new TextField();

    // add fields to field group
    courseFieldGroup.getChildren().addAll(courseNameLabel, courseNameInput, courseCredits, courseCreditsInput,
        coursePeriod, coursePeriodInput);

    // horizontal container for checkbox selection - course mandatory or not
    HBox statusContainer = new HBox();
    Label mandatoryLabel = new Label("Mandatory: ");

    // allow only one checkbox to be selected at once
    String[] options = new String[] { "Yes", "No" };
    final int maxCount = 1;
    final Set<CheckBox> activeBoxes = new LinkedHashSet<>();

    ChangeListener<Boolean> listener = (o, oldValue, newValue) -> {
      // get checkbox containing property
      CheckBox cb = (CheckBox) ((ReadOnlyProperty<Boolean>) o).getBean();

      if (newValue) {
        activeBoxes.add(cb);
        if (activeBoxes.size() > maxCount) {
          // get first checkbox to be activated
          cb = activeBoxes.iterator().next();

          // unselect; change listener will remove
          cb.setSelected(false);
        }
      } else {
        activeBoxes.remove(cb);
      }
    };

    // create checkboxes
    statusContainer.getChildren().add(mandatoryLabel);
    for (int i = 0; i < options.length; i++) {
      CheckBox cb = new CheckBox(options[i]);
      cb.selectedProperty().addListener(listener);
      statusContainer.getChildren().add(cb);
    }

    // buttons
    Button addCourseButton = new Button("Add");
    addCourseButton.setOnAction(e -> {
      // logic when course is added...
    });

    // place containers inside outer container
    BorderPane wrapperContainer = new BorderPane();
    wrapperContainer.setTop(courseFieldGroup);
    wrapperContainer.setCenter(statusContainer);
    wrapperContainer.setCenter(addCourseButton);

    return new Scene(wrapperContainer, SCENE_WIDTH, SCENE_HEIGHT);

  }
}
