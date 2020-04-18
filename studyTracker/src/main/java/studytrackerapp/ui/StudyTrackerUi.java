package studytrackerapp.ui;

import java.util.LinkedHashSet;
import java.util.List;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
    private final int CONTAINER_SPACING = 10;
    private final int CONTAINER_PADDING = 30;
    private final int FIELD_GROUP_SPACING = 10;
    private final int FIELD_GROUP_PADDING = 15;

    private final int SCENE_WIDTH = 1000;
    private final int SCENE_HEIGHT = 600;

    private final int CREATE_USER_WIDTH = 700;
    private final int CREATE_USER_HEIGHT = 500;

    /* Globals */
    private boolean deleteMode;
    private boolean userLoggedIn;

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
    private VBox courseNodes;

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
        userLoggedIn = false;

        // Database
        var database = new Database();
        database.createDatabase("study-tracker.db");

        // Daos
        var userDao = new UserDao(database);
        var courseDao = new CourseDao(database);

        // Services
        userService = new UserService(userDao);
        courseService = new CourseService(courseDao);

    }

    @Override
    public void start(Stage mainStage) {

        System.out.println("Application launched...");

        /**
         * CREATE SCENES
         */

        loginScene = createLoginScene(mainStage);

        newUserScene = createNewUserScene(mainStage);

        studyTrackingScene = createStudyTrackingScene(mainStage);

        newCourseScene = createNewCourseScene(mainStage);

        // ---------------------------------------

        /**
         * SET UP INITIAL VIEW
         */
        mainStage.setTitle("Study Tracker");
        mainStage.setScene(loginScene);
        mainStage.show();
        mainStage.setOnCloseRequest(e -> {
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

        // the outer 'wrapper' box
        var loginContainer = new VBox(CONTAINER_SPACING);
        // container for input fields
        var loginFieldGroup = new VBox(FIELD_GROUP_SPACING);

        // container styling
        loginContainer.setPadding(new Insets(CONTAINER_PADDING));

        // login input fields
        var usernameLabel = new Label("Username");
        var usernameInput = new TextField();

        var passwordLabel = new Label("Password");
        var passwordInput = new PasswordField();

        // add all field elements to field group
        loginFieldGroup.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput);

        // rest of login container elements
        var loginMessage = new Label("Welcome to Study Tracker!");
        var loginStatusMessage = new Label();
        var loginButton = new Button("Login");
        var createUserDirection = new Label("New User? Click Here To Sign Up");
        var createButton = new Button("Create User");

        // on the user clicking to login
        loginButton.setOnAction(e -> {
            // get user info
            var username = usernameInput.getText();
            var password = passwordInput.getText();
            System.out.println("user: " + username);
            System.out.println("pass: " + password);

            // attempt login...
            if (userService.login(username, password)) {
                // all good! Get user details
                var loggedUser = userService.getLoggedUser();
                // assign user to courses
                courseService.assignUser(loggedUser);
                // initial course-list fetch
                redrawList();
                mainStage.setScene(studyTrackingScene);
            } else {
                // login unsuccessful
                loginStatusMessage.setText(
                        "Invalid credentials, or the user: '  " + username + "' has not been created. Try again.");

                // display message
                loginStatusMessage.setTextFill(Color.RED);
                loginStatusMessage.setFont(Font.font(null, FontWeight.BOLD, 18));
            }
            // reset fields
            usernameInput.clear();
            passwordInput.clear();
        });

        // user wants to create a new account
        createButton.setOnAction(e -> {
            usernameInput.setText("");
            passwordInput.setText("");
            loginStatusMessage.setText("");
            mainStage.setScene(newUserScene);
        });

        // add all components to the outer container
        loginContainer.getChildren().addAll(loginMessage, loginStatusMessage, loginFieldGroup, loginButton,
                createUserDirection, createButton);

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
        // main
        var createUserContainer = new VBox(CONTAINER_SPACING);
        // container for fields
        var createUserFieldGroup = new VBox(FIELD_GROUP_SPACING);
        createUserFieldGroup.setPadding(new Insets(FIELD_GROUP_PADDING));
        // container for buttons
        var buttonContainer = new HBox(FIELD_GROUP_SPACING);
        buttonContainer.setPadding(new Insets(FIELD_GROUP_PADDING));

        // container styling
        createUserContainer.setPadding(new Insets(CONTAINER_PADDING));

        // fields
        var nameLabel = new Label("Name");
        var newNameInput = new TextField();
        var newUsernameLabel = new Label("Username");
        var newUsernameInput = new TextField();
        var newPasswordLabel = new Label("Password");
        var newPasswordInput = new TextField();
        var programNameLabel = new Label("Study Program");
        var programNameInput = new TextField();
        var targetCreditsLabel = new Label("Target Credits");
        var targetCreditsInput = new TextField();
        // credits can only be integers
        restrictToInt(targetCreditsInput);

        // other elements
        var createUserMessage = new Label();

        // add all field elements to field group
        createUserFieldGroup.getChildren().addAll(nameLabel, newNameInput, newUsernameLabel, newUsernameInput,
                newPasswordLabel, newPasswordInput, programNameLabel, programNameInput, targetCreditsLabel,
                targetCreditsInput);

        // rest of createUser container elements
        var createUserButton = new Button("Create");
        createUserButton.setPadding(new Insets(BUTTON_PADDING));
        var returnButton = new Button("Return");
        returnButton.setPadding(new Insets(BUTTON_PADDING));

        // add buttons to their container
        buttonContainer.getChildren().addAll(createUserButton, returnButton);

        // event handlers for buttons
        createUserButton.setOnAction(e -> {
            var name = newNameInput.getText();
            var username = newUsernameInput.getText();
            var password = newPasswordInput.getText();
            var programName = programNameInput.getText();
            var targetCredits = targetCreditsInput.getText();

            System.out.println(targetCredits);

            // credentials validation
            if ((username.length() < MIN_USERNAME_LENGTH) || (name.length() < MIN_NAME_LENGTH)
                    || (password.length() < MIN_PASS_LENGTH)) {
                createUserMessage.setText("Either the username, password, or name that was entered was too short.");
                createUserMessage.setTextFill(Color.RED);
                createUserMessage.setFont(Font.font(null, FontWeight.BOLD, 18));

                // clear fields
                newNameInput.clear();
                newUsernameInput.clear();
                newPasswordInput.clear();

                return;
            }

            if (userService.createUser(username, name, password, programName, Integer.parseInt(targetCredits))) {
                System.out.println("All good!");
                createUserMessage.setText("Success!");
                createUserMessage.setTextFill(Color.GREEN);
                createUserMessage.setFont(Font.font(null, FontWeight.BOLD, 18));
                mainStage.setScene(loginScene);
                return;
            } else {
                createUserMessage.setText("A user with the username: '" + username + "' already exists.");
                createUserMessage.setTextFill(Color.RED);
                return;
            }

        });

        returnButton.setOnAction(e -> {
            mainStage.setScene(loginScene);
        });

        // add nested containers to main container
        createUserContainer.getChildren().addAll(createUserMessage, createUserFieldGroup, buttonContainer);

        // place container within view
        return new Scene(createUserContainer, CREATE_USER_WIDTH, CREATE_USER_HEIGHT);

    }

    private Scene createStudyTrackingScene(Stage mainStage) {

        // containers
        var verticalCourseManagementContainer = new VBox();
        verticalCourseManagementContainer.setPadding(new Insets(10));

        var profileContainer = new VBox();
        profileContainer.setPadding(new Insets(10));
        profileContainer.setSpacing(20);
        profileContainer.setAlignment(Pos.TOP_CENTER);

        var buttonContainer = new HBox();
        buttonContainer.setPadding(new Insets(10));
        buttonContainer.setSpacing(20);
        buttonContainer.setAlignment(Pos.BOTTOM_LEFT);

        // user info
        var studentName = "";

        if (userLoggedIn) {
            studentName = userService.getLoggedUser().getName();
            // capitalize first letter of name
            studentName = studentName.substring(0, 1).toUpperCase() + studentName.substring(1).toLowerCase();
        }
        var welcomeMessage = new Label("Hey " + studentName + "! Here's your profile");
        var progressBarMessage = new Label("Your progress so far:");

        var logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            userService.logout();
            mainStage.setScene(loginScene);
        });

        logoutButton.setAlignment(Pos.TOP_LEFT);

        var progress = userLoggedIn ? getProgress() : 0;
        var progressBar = new ProgressBar(progress);

        // add profile elements to their container
        profileContainer.getChildren().addAll(welcomeMessage, progressBarMessage, progressBar);

        // set up the board for the courses, inc. labels
        HBox courseBoard = new HBox();
        courseBoard.setSpacing(20);
        courseBoard.setPadding(new Insets(30));

        // column labels
        var backlog = new Label("Backlog");
        var ongoing = new Label("Ongoing");
        var completed = new Label("Completed");

        // column areas
        var backlogScroll = new ScrollPane();
        var ongoingScroll = new ScrollPane();
        var completedScroll = new ScrollPane();

        // add nodes to columns
        courseNodes = new VBox(0);
        courseNodes.setMaxWidth(310);
        courseNodes.setMinWidth(310);

        if (userLoggedIn)
            redrawList();

        backlogScroll.setContent(courseNodes);

        // put labels and areas together to form column
        var backlogColumn = new VBox(15);
        var ongoingColumn = new VBox(15);
        var completedColumn = new VBox(15);

        backlogColumn.setMinWidth(250);
        backlogColumn.getChildren().add(backlog);
        backlogColumn.getChildren().add(backlogScroll);

        ongoingColumn.setMinWidth(250);
        ongoingColumn.getChildren().add(ongoing);
        ongoingColumn.getChildren().add(ongoingScroll);

        completedColumn.setMinWidth(250);
        completedColumn.getChildren().add(completed);
        completedColumn.getChildren().add(completedScroll);

        // add the columns to the board
        courseBoard.getChildren().add(backlogColumn);
        courseBoard.getChildren().add(ongoingColumn);
        courseBoard.getChildren().add(completedColumn);

        courseBoard.setAlignment(Pos.BASELINE_CENTER);

        // course modification buttons
        var addCourseButton = new Button("Add Course");
        addCourseButton.setOnAction(e -> {
            mainStage.setScene(newCourseScene);
        });

        addCourseButton.setAlignment(Pos.BASELINE_RIGHT);

        var modifyCourseButton = new ToggleButton("Modify Courses");
        modifyCourseButton.setOnAction(e -> {

        });

        modifyCourseButton.setAlignment(Pos.BASELINE_RIGHT);

        buttonContainer.getChildren().addAll(addCourseButton, modifyCourseButton);

        // other components
        var separator = new Separator(Orientation.HORIZONTAL);

        // add everything to the container
        verticalCourseManagementContainer.getChildren().addAll(logoutButton, profileContainer, separator, courseBoard,
                buttonContainer);

        // place container within view
        return new Scene(verticalCourseManagementContainer, SCENE_WIDTH, SCENE_HEIGHT);
    }

    private Scene createNewCourseScene(Stage mainStage) {
        // container for field group
        var newCourseFieldGroup = new VBox();
        newCourseFieldGroup.setSpacing(10);

        // individual fields
        var courseNameLabel = new Label("Name");
        var courseNameInput = new TextField();

        var courseCreditsLabel = new Label("Credits");
        var courseCreditsInput = new TextField();
        restrictToInt(courseCreditsInput);

        var statusLabel = new Label("Status");
        var statusInput = new TextField();
        restrictToInt(statusInput);

        var courseLinkLabel = new Label("Link");
        var courseLinkInput = new TextField();

        // add fields to field group
        newCourseFieldGroup.getChildren().addAll(courseNameLabel, courseNameInput, courseCreditsLabel,
                courseCreditsInput, statusLabel, statusInput, courseLinkLabel, courseLinkInput);

        // horizontal container for checkbox selection - course mandatory or not
        var statusContainer = new HBox();
        var mandatoryLabel = new Label("Mandatory: ");
        statusContainer.getChildren().add(mandatoryLabel);

        // allow only one checkbox to be selected at once
        var checkboxOptions = new String[] { "Yes ", "No " };
        // max number of active selections
        final var maxCount = 1;
        final var activeBoxes = new LinkedHashSet<CheckBox>();

        // define listener for checkbox
        ChangeListener<Boolean> selectionListener = (o, oldValue, newValue) -> {
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
            var cb = new CheckBox(checkboxOptions[i]);
            cb.selectedProperty().addListener(selectionListener);
            statusContainer.getChildren().add(cb);
        }

        // buttons
        var buttonContainer = new HBox();
        var addCourseButton = new Button("Add");
        addCourseButton.setOnAction(e -> {
            // swoop all the course info
            var name = courseNameInput.getText();
            var credits = Integer.parseInt(courseCreditsInput.getText());

            // logic for compulsory property
            var compulsoryString = "";
            for (CheckBox checkBox : activeBoxes) {
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

            var status = Integer.parseInt(statusInput.getText());

            var link = courseLinkInput.getText();

            if (courseService.createCourse(name, credits, compulsoryInteger, status, link)) {

                // refresh board
                redrawList();

                // change view back to course manager
                mainStage.setScene(studyTrackingScene);

                // clear fields
                courseNameInput.clear();
                statusInput.clear();
                courseLinkInput.clear();
                courseCreditsInput.clear();
            }

        });

        var returnButton = new Button("Return");

        returnButton.setOnAction(e -> {
            mainStage.setScene(studyTrackingScene);
        });

        buttonContainer.getChildren().addAll(addCourseButton, returnButton);

        // place containers inside outer container
        var wrapperContainer = new BorderPane();
        wrapperContainer.setPadding(new Insets(CONTAINER_PADDING));
        wrapperContainer.setTop(newCourseFieldGroup);
        wrapperContainer.setMargin(newCourseFieldGroup, new Insets(10, 0, 10, 0));
        wrapperContainer.setCenter(statusContainer);
        wrapperContainer.setBottom(buttonContainer);

        return new Scene(wrapperContainer, SCENE_WIDTH, SCENE_HEIGHT);

    }

    private Node createCourseNode(Course course) {
        var courseContainer = new HBox(10);
        courseContainer.setPadding(new Insets(5, 5, 5, 5));

        var label = new Label(course.getName());
        label.setMinHeight(28);

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-base: #E74C3C;");
        // button only visible in delete mode
        deleteButton.setVisible(deleteMode);

        courseContainer.getChildren().addAll(label, deleteButton);

        return courseContainer;
    }

    // TODO: Place these somewhere else
    /**
     * Util Methods
     */

    private void restrictToInt(TextField targetCreditsInput) {
        // restrict credits input to numbers
        UnaryOperator<Change> filter = change -> {
            var text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        // attach the filter to the formatter
        var textFormatter = new TextFormatter<>(filter);
        targetCreditsInput.setTextFormatter(textFormatter);
    }

    private double getProgress() {
        // calculate progress
        var targetCredits = userService.getLoggedUser().getTarget();
        var currentCredits = courseService.getCourses().stream().filter(course -> course.getStatus() == 2)
                .map(completed -> completed.getCredits())
                .reduce(0, (totalCredits, courseCredits) -> totalCredits + courseCredits);

        var currentProgressAsFraction = (double) currentCredits / targetCredits;
        System.out.println(currentProgressAsFraction);

        return currentProgressAsFraction;
    }

    public void redrawList() {
        // clear list
        courseNodes.getChildren().clear();
        // add each course to the list
        courseService.getCourses().forEach(course -> {
            courseNodes.getChildren().add(createCourseNode(course));
        });
    }
}
