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

  /* Application Views */
  private Scene welcomeScene;
  private Scene loginScene;
  private Scene newUserScene;
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

    /* Login Scene */

    // containers
    VBox loginContainer = new VBox(CONTAINER_SPACING); // this is the outer 'wrapper' pane
    HBox loginFieldGroup = new HBox(CONTAINER_SPACING); // holds all input elements, nested within outer pane

    // container styling
    loginContainer.setPadding(new Insets(10));

    // login input fields
    Label usernameLabel = new Label("Username");
    TextField usernameInput = new TextField();

    Label passwordLabel = new Label("Password");
    TextField passwordInput = new TextField();

    // add all field elements to field group
    loginFieldGroup.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput);

    // rest of login container elements
    Button loginButton = new Button("Login");
    Button createButton = new Button("Create User");
    Label loginMessage = new Label("Welcome to Study Tracker!");

    // event handlers for buttons
    // TODO: abstract these away to a method
    loginButton.setOnAction(e -> {
      String username = usernameInput.getText();
      String password = passwordInput.getText();

      primaryStage.setScene(newUserScene);
      // TODO: login logic
      // login only with valid credentials
    });

    createButton.setOnAction(e -> {
      usernameInput.setText("");
      passwordInput.setText("");
      primaryStage.setScene(newUserScene);
    });

    // add everything to the container
    loginContainer.getChildren().addAll(loginMessage, loginFieldGroup, loginButton, createButton);

    // place container within view
    loginScene = new Scene(loginContainer, 400, 350, Color.INDIANRED);

    /* Create New User Scene */

    // containers
    VBox newUserContainer = new VBox(CONTAINER_SPACING);
    HBox createUserFieldGroup = new HBox(CONTAINER_SPACING);
    createUserFieldGroup.setPadding(new Insets(CONTAINER_SPACING));

    // reuse fields from login view & define scene-specif ones
    Label nameLabel = new Label("Name");
    TextField nameInput = new TextField();
    Label userCreationMessage = new Label();

    // add all field elements to field group
    createUserFieldGroup.getChildren().addAll(nameLabel, nameInput, usernameLabel, usernameInput, passwordLabel,
        passwordInput);

    // rest of createUser container elements
    Button createUserButton = new Button("Create");
    createUserButton.setPadding(new Insets(10));

    // event handlers for buttons
    createUserButton.setOnAction(e -> {
      String name = nameInput.getText();
      String username = usernameInput.getText();
      String password = passwordInput.getText();

      if (username.length() < MIN_USERNAME_LENGTH || name.length() < MIN_NAME_LENGTH
          || password.length() < MIN_PASS_LENGTH) {
        userCreationMessage.setText("The username, password, or name entered was too short");
        userCreationMessage.setTextFill(Color.RED);
      } else if () {
        // userCreationMessage.setText("");
        // loginMessage.setText("new user created");
        // loginMessage.setTextFill(Color.GREEN);
        // primaryStage.setScene(loginScene);
      } else {
        userCreationMessage.setText("username has to be unique");
        userCreationMessage.setTextFill(Color.RED);
      }

    });

    // add everything to the container

    // place container within view


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
    System.out.println("here now");
    launch(args);
  }

}
