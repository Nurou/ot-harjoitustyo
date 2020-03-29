package studytrackerapp.ui;

import studytrackerapp.domain.*;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

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

  private final int LOGIN_WIDTH = 700;
  private final int LOGIN_HEIGHT = 500;

  private final int CREATE_USER_WIDTH = 700;
  private final int CREATE_USER_HEIGHT = 500;

  // styles
  private final int BUTTON_PADDING = 10;

  /* Application Views */
  private Scene welcomeScene;
  private Scene loginScene;
  private Scene createUserScene;
  private Scene profileScene;
  private Scene newCourseScene;
  private Scene couseManagementScene;

  public Node createCourseNode(Course course) {
    HBox box = new HBox(CONTAINER_SPACING);
    return box;
  }

  public void redrawCourseList() {

  }

  @Override
  public void start(Stage primaryStage) {

    System.out.println("Application launched!");

    /* LOGIN SCENE */

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

      // primaryStage.setScene(newUserScene);
      // TODO: login logic
      // login only with valid credentials
    });

    createButton.setOnAction(e -> {
      usernameInput.setText("");
      passwordInput.setText("");
      primaryStage.setScene(createUserScene);
    });

    // add everything to the outer container
    loginContainer.getChildren().addAll(loginMessage, loginFieldGroup, loginButton, createUserDirection, createButton);

    // place container within view
    loginScene = new Scene(loginContainer, LOGIN_WIDTH, LOGIN_HEIGHT);

    /* CREATE NEW USER SCENE */

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
    createUserScene = new Scene(createUserContainer, CREATE_USER_WIDTH, CREATE_USER_HEIGHT);

    /* Initial View Set up */
    primaryStage.setTitle("Study Tracker");
    primaryStage.setScene(loginScene);
    primaryStage.show();
  }

  @Override
  public void stop() {
    // clean up
    System.out.println("The application is shutting down.");
  }

  public static void main(String[] args) {
    launch(args);
  }

}
