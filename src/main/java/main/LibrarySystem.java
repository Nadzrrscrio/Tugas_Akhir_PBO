package main;

import data.Admin;
import data.Student;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class LibrarySystem extends Application {
    public Scene initialScene;
    private Scene loginScene;
    private Scene adminScene;
    public Scene studentScene;
    private Admin admin = new Admin();
    private Map<String, Map<String, Integer>> studentBorrowedBooks = new HashMap<>();
    private Student currentStudent;

    private static final int SCENE_WIDTH = 1540;
    private static final int SCENE_HEIGHT = 790;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library System");

        initialScene = iInitialScene(primaryStage);
        loginScene = iLoginScene(primaryStage);
        adminScene = admin.iAdminScene(primaryStage, this);


        primaryStage.setScene(initialScene);
        primaryStage.show();
    }

    private Scene iInitialScene(Stage primaryStage) {

        Image iBackground = new Image("file:src/main/java/image/iBackground.png");
        ImageView iBackgroundview = new ImageView(iBackground);
        Image ibook = new Image("file:src/main/java/image/iBook.png");
        ImageView ibookview = new ImageView(ibook);

        //size image settings
        ibookview.setFitWidth(500);
        ibookview.setFitHeight(500);

        //position image settings
        ibookview.setTranslateY(-60);

        //style button settings
        BackgroundFill backgroundFill = new BackgroundFill(Color.web("#0097b2"), new CornerRadii(20), new Insets(1));
        Background background = new Background(backgroundFill);

        Button adminButton = new Button("Admin");
        adminButton.setPrefWidth(120);
        adminButton.setPrefHeight(50);
        adminButton.setFont(new Font("verdana", 20));
        adminButton.setStyle("-fx-text-fill: #000000;");
        adminButton.setBackground(background);
        adminButton.setStyle("-fx-border-color: #000000; -fx-border-width: 1px; -fx-border-radius: 20px;");


        Button studentButton = new Button("Student");
        studentButton.setPrefWidth(120);
        studentButton.setPrefHeight(50);
        studentButton.setFont(new Font("Verdana", 20));
        studentButton.setStyle("-fx-text-fill: black;");
        studentButton.setBackground(background);
        studentButton.setStyle("-fx-border-color: #000000; -fx-border-width: 1px; -fx-border-radius: 20px;");


        adminButton.setOnAction(e -> primaryStage.setScene(loginScene));
        studentButton.setOnAction(e -> primaryStage.setScene(iStudentLoginScene(primaryStage)));

        VBox initialLayout = new VBox(10, adminButton, studentButton);
        StackPane stackPane = new StackPane(iBackgroundview, ibookview, initialLayout);
        initialLayout.setAlignment(Pos.CENTER);
        initialLayout.setPadding(new Insets(200, 0, 0, 0));
        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }

    private Scene iLoginScene(Stage primaryStage) {
        Image iBackground = new Image("file:src/main/java/image/iBackground.png");
        ImageView iBackgroundview = new ImageView(iBackground);

        Label userLabel = new Label("Masukkan Username :");
        TextField userTextField = new TextField();
        Label passLabel = new Label("Masukkan Password :");
        PasswordField passField = new PasswordField();
        Button loginButton = new Button("Login");
        Button exitButton = new Button("Keluar");
        Label errorLabel = new Label();

        loginButton.setOnAction(e -> {
            if (userTextField.getText().equals("admin") && passField.getText().equals("admin")) {
                primaryStage.setScene(adminScene);
            } else {
                errorLabel.setText("Error: Invalid Credentials");
                errorLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                errorLabel.setStyle("-fx-text-fill: red;");
            }
        });

        exitButton.setOnAction(e -> primaryStage.setScene(initialScene));

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(userLabel, 0, 0);
        gridPane.add(userTextField, 1, 0);
        gridPane.add(passLabel, 0, 1);
        gridPane.add(passField, 1, 1);

        VBox executed = new VBox(10, loginButton, exitButton);
        executed.setAlignment(Pos.CENTER);

        VBox borderBox = new VBox(10, gridPane, executed, errorLabel);
        borderBox.setAlignment(Pos.CENTER);
        borderBox.setStyle("-fx-border-color: black; -fx-padding: 20;");
        borderBox.setPrefWidth(400);

        borderBox.setMaxWidth(400);
        borderBox.setPadding(new Insets(20));

        VBox mainLayout = new VBox(borderBox);
        StackPane stackPane = new StackPane(iBackgroundview, mainLayout);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }

    private Scene iStudentLoginScene(Stage primaryStage) {
        Image iBackground = new Image("file:src/main/java/image/iBackground.png");
        ImageView iBackgroundview = new ImageView(iBackground);

        Label nimLabel = new Label("Masukkan NIM :");
        TextField nimField = new TextField();

        Label passLabel = new Label("Masukkan Password :");
        PasswordField passField = new PasswordField();

        Button loginButton = new Button("Login");
        Button exitButton = new Button("Keluar");
        Label errorLabel = new Label();

        loginButton.setOnAction(e -> {
            String nim = nimField.getText();
            String password = passField.getText();
            if (admin.verifyStudent(nim, password)) {
                currentStudent = admin.getStudentByNim(nim);
                studentScene = currentStudent.iStudentScene(primaryStage, this, admin, studentBorrowedBooks.getOrDefault(nim, new HashMap<>()));
                primaryStage.setScene(studentScene);
            } else {
                errorLabel.setText("NIM atau password salah, Coba Lagi!");
                errorLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                errorLabel.setStyle("-fx-text-fill: red;");
            }
        });

        exitButton.setOnAction(e -> primaryStage.setScene(initialScene));

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(nimLabel, 0, 0);
        gridPane.add(nimField, 1, 0);
        gridPane.add(passLabel, 0, 1);
        gridPane.add(passField, 1, 1);

        VBox executed = new VBox(10, loginButton, exitButton);
        executed.setAlignment(Pos.CENTER);

        VBox borderBox = new VBox(10, gridPane, executed, errorLabel);
        borderBox.setAlignment(Pos.CENTER);
        borderBox.setStyle("-fx-border-color: black; -fx-padding: 20;");
        borderBox.setPrefWidth(400);

        borderBox.setMaxWidth(400);
        borderBox.setPadding(new Insets(20));

        VBox mainLayout = new VBox(borderBox);
        StackPane stackPane = new StackPane(iBackgroundview, mainLayout);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }

    public static void main(String[] args) {
        launch(args);
    }
}