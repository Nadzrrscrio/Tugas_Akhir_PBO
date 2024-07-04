package data;

import books.Book;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.LibrarySystem;

import java.util.*;

public class Admin {

    private List<Book> bookList = new ArrayList<>();
    private List<Student> studentList = new ArrayList<>();
    private Map<String, Map<String, Integer>> borrowedBooksByStudent = new HashMap<>();

    public Admin(String name) {
        super();
    }

    public Admin() {
        super();
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(bookList);
    }

    private Scene adminScene;

    private static final int SCENE_WIDTH = 1540;
    private static final int SCENE_HEIGHT = 790;

    public Scene iAdminScene(Stage primaryStage, LibrarySystem librarySystem) {
        Image iBackground = new Image("file:src/main/java/image/iBackground.png");
        ImageView iBackgroundview = new ImageView(iBackground);

        Button inputBookButton = new Button("Tambah Buku");
        Button displayBooksButton = new Button("Tampilkan daftar Buku");
        Button inputStudentButton = new Button("Tambah Siswa");
        Button displayStudentsButton = new Button("Tampilkan daftar Siswa");
        Button exitButton = new Button("Keluar");

        double buttonWidth = 200;
        inputBookButton.setPrefWidth(buttonWidth);
        displayBooksButton.setPrefWidth(buttonWidth);
        inputStudentButton.setPrefWidth(buttonWidth);
        displayStudentsButton.setPrefWidth(buttonWidth);
        exitButton.setPrefWidth(buttonWidth);

        inputBookButton.setOnAction(e -> primaryStage.setScene(iInputBookScene(primaryStage)));
        displayBooksButton.setOnAction(e -> primaryStage.setScene(iDisplayBooksScene(primaryStage, librarySystem, true)));
        inputStudentButton.setOnAction(e -> primaryStage.setScene(iInputStudentScene(primaryStage)));
        displayStudentsButton.setOnAction(e -> primaryStage.setScene(iDisplayStudentsScene(primaryStage)));
        exitButton.setOnAction(e -> primaryStage.setScene(librarySystem.initialScene));

        VBox adminLayout = new VBox(10, inputBookButton, displayBooksButton, inputStudentButton, displayStudentsButton, exitButton);
        adminLayout.setAlignment(Pos.CENTER);

        VBox borderBox = new VBox(adminLayout);
        borderBox.setAlignment(Pos.CENTER);
        borderBox.setStyle("-fx-border-color: black; -fx-padding: 20;");
        borderBox.setPrefWidth(250);
        borderBox.setMaxWidth(250);
        borderBox.setPadding(new Insets(20));

        Label adminMenuText = new Label("Menu Admin");
        adminMenuText.setFont(Font.font("Impact", FontWeight.BOLD, 24));
        adminMenuText.setStyle("-fx-text-fill: #0097b2;");

        VBox mainLayout = new VBox(5, adminMenuText, borderBox);
        StackPane stackPane = new StackPane(iBackgroundview, mainLayout);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        adminScene = new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
        return adminScene;
    }

    private Scene iInputBookScene(Stage primaryStage) {
        Image iBackground = new Image("file:src/main/java/image/iBackground.png");
        ImageView iBackgroundview = new ImageView(iBackground);

        Label titleLabel = new Label("Masukkan judul :");
        TextField titleField = new TextField();
        titleField.setMaxWidth(300);

        Label authorLabel = new Label("Masukkan penulis :");
        TextField authorField = new TextField();
        authorField.setMaxWidth(300);

        Label categoryLabel = new Label("Kategori :");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.setMaxWidth(300);
        categoryComboBox.getItems().addAll("History", "Story", "Text");

        Label stockLabel = new Label("Masukkan jumlah stok :");
        TextField stockField = new TextField();
        stockField.setMaxWidth(300);

        Button addButton = new Button("Tambah buku");
        Button backButton = new Button("Keluar");
        Label confirmationLabel = new Label();

        addButton.setOnAction(e -> {
            String id = generateBookID();
            String title = titleField.getText();
            String author = authorField.getText();
            String category = categoryComboBox.getValue();
            int stock;
            try {
                stock = Integer.parseInt(stockField.getText());
            } catch (NumberFormatException ex) {
                confirmationLabel.setText("Tolong masukkan angka yang valid untuk stok.");
                confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                confirmationLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            bookList.add(new Book(id, title, author, category, stock));
            confirmationLabel.setText("Buku berhasil ditambahkan dengan ID : " + id);
            confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
            confirmationLabel.setStyle("-fx-text-fill: green;");
        });

        backButton.setOnAction(e -> primaryStage.setScene(adminScene));

        VBox inputBookLayout = new VBox(10, titleLabel, titleField, authorLabel, authorField, categoryLabel, categoryComboBox, stockLabel, stockField, confirmationLabel);
        inputBookLayout.setAlignment(Pos.CENTER);

        VBox borderBox = new VBox(inputBookLayout);
        borderBox.setAlignment(Pos.CENTER);
        borderBox.setStyle("-fx-border-color: black; -fx-padding: 20;");
        borderBox.setPrefWidth(350);
        borderBox.setMaxWidth(350);
        borderBox.setPadding(new Insets(20));

        VBox mainLayout = new VBox(10, borderBox, addButton, backButton);
        StackPane stackPane = new StackPane(iBackgroundview, mainLayout);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }

    public Scene iDisplayBooksScene(Stage primaryStage, LibrarySystem librarySystem, boolean isAdmin) {
        Image iBackground = new Image("file:src/main/java/image/iBackground.png");
        ImageView iBackgroundview = new ImageView(iBackground);

        TableView<Book> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(500);

        TableColumn<Book, String> idColumn = new TableColumn<>("ID Buku");
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));
        idColumn.setPrefWidth(80);

        TableColumn<Book, String> titleColumn = new TableColumn<>("Judul");
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        titleColumn.setPrefWidth(130);

        TableColumn<Book, String> authorColumn = new TableColumn<>("Penulis");
        authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));
        authorColumn.setPrefWidth(130);


        TableColumn<Book, String> categoryColumn = new TableColumn<>("Kategori");
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        categoryColumn.setPrefWidth(80);

        TableColumn<Book, Integer> stockColumn = new TableColumn<>("Stok");
        stockColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStock()).asObject());
        stockColumn.setPrefWidth(80);

        table.getColumns().addAll(idColumn, titleColumn, authorColumn, categoryColumn, stockColumn);

        for (Book book : bookList) {
            table.getItems().add(book);
        }

        Button backButton = new Button("Keluar");
        backButton.setOnAction(e -> {
            if (isAdmin) {
                primaryStage.setScene(adminScene);
            } else {
                primaryStage.setScene(librarySystem.studentScene);
            }
        });

        VBox displayLayout = new VBox(10, table, backButton);
        displayLayout.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane(iBackgroundview, displayLayout);
        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }


    private Scene iInputStudentScene(Stage primaryStage) {
        Image iBackground = new Image("file:src/main/java/image/iBackground.png");
        ImageView iBackgroundview = new ImageView(iBackground);

        Label nameLabel = new Label("Masukkan nama :");
        TextField nameField = new TextField();
        nameField.setMaxWidth(300);

        Label nimLabel = new Label("Masukkan NIM (15 digits) :");
        TextField nimField = new TextField();
        nimField.setMaxWidth(300);

        Label passwordLabel = new Label("Masukkan password :");
        TextField passwordField = new TextField();
        passwordField.setMaxWidth(300);

        Label facultyLabel = new Label("Masukkan fakultas :");
        TextField facultyField = new TextField();
        facultyField.setMaxWidth(300);

        Label programLabel = new Label("Masukkan program studi :");
        TextField programField = new TextField();
        programField.setMaxWidth(300);


        Button addButton = new Button("Tambah siswa");
        Button backButton = new Button("Keluar");
        Label confirmationLabel = new Label();

        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String nim = nimField.getText();
            String password = passwordField.getText();
            String faculty = facultyField.getText();
            String program = programField.getText();
            if (nim.length() == 15) {
                studentList.add(new Student(name, nim, password, faculty, program));
                confirmationLabel.setText("Penambahan siswa berhasil.");
                confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                confirmationLabel.setStyle("-fx-text-fill: green;");
            } else {
                confirmationLabel.setText("NIM harus 15 digit!");
                confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                confirmationLabel.setStyle("-fx-text-fill: red;");
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(adminScene));

        VBox inputStudentLayout = new VBox(10, nameLabel, nameField, nimLabel, nimField, passwordLabel, passwordField, facultyLabel, facultyField, programLabel, programField, confirmationLabel);
        inputStudentLayout.setAlignment(Pos.CENTER);

        VBox borderBox = new VBox(inputStudentLayout);
        borderBox.setAlignment(Pos.CENTER);
        borderBox.setStyle("-fx-border-color: black; -fx-padding: 20;");
        borderBox.setPrefWidth(350);
        borderBox.setMaxWidth(350);
        borderBox.setPadding(new Insets(20));

        VBox mainLayout = new VBox(10, borderBox, addButton, backButton);
        StackPane stackPane = new StackPane(iBackgroundview, mainLayout);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }

    public Scene iDisplayStudentsScene(Stage primaryStage) {
        Image iBackground = new Image("file:src/main/java/image/iBackground.png");
        ImageView iBackgroundview = new ImageView(iBackground);

        TableView<Student> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(600);

        TableColumn<Student, String> nameColumn = new TableColumn<>("Nama");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        nameColumn.setPrefWidth(130);

        TableColumn<Student, String> nimColumn = new TableColumn<>("NIM");
        nimColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNim()));
        nimColumn.setPrefWidth(120);

        TableColumn<Student, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPassword()));
        passwordColumn.setPrefWidth(80);

        TableColumn<Student, String> facultyColumn = new TableColumn<>("Fakultas");
        facultyColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFaculty()));
        facultyColumn.setPrefWidth(85);

        TableColumn<Student, String> programColumn = new TableColumn<>("Program Studi");
        programColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProgram()));
        programColumn.setPrefWidth(85);

        table.getColumns().addAll(nameColumn, nimColumn, passwordColumn, facultyColumn, programColumn);

        for (Student student : studentList) {
            table.getItems().add(student);
        }

        Button backButton = new Button("Keluar");
        backButton.setOnAction(e -> primaryStage.setScene(adminScene));

        VBox displayLayout = new VBox(10, table, backButton);
        displayLayout.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane(iBackgroundview, displayLayout);
        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }


    private String generateBookID() {
        String uniqueID = UUID.randomUUID().toString();
        String bookId = uniqueID.replaceAll("-", "").toLowerCase();
        return String.format("%s-%s-%s", bookId.substring(0, 3), bookId.substring(3, 6), bookId.substring(6, 9));
    }

    public Book getBookById(String id) {
        for (Book book : bookList) {
            if (book.getId().equals(id)) {
                SendEmail sendEmail = new SendEmail();
                try {
                    String recipientEmail = "secarionadzar@gmail.com"; //email penerima

                    String subject = "Notifikasi peminjaman!";
                    String body = "Kabar baik! Peminjaman buku telah berhasil\n"
                            + "Terimakasih telah mengunjungi iBook\n"
                            + "Berikut informasi tentang buku yang dipinjam :\n\n"
                            + "Book ID    : " + book.getId() + "\n"
                            + "Title      : " + book.getTitle() + "\n"
                            + "Duration of borrowing : " + "7" + " days\n\n"
                            + sendEmail.dateinfo_now();
                    sendEmail.sendEmail(recipientEmail, subject, body);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                    return book;
                }

        }
        return null;
    }


    public Student getStudentByNim(String nim) {
        for (Student student : studentList) {
            if (student.getNim().equals(nim)) {
                return student;
            }
        }
        return null;
    }

    public boolean verifyStudent(String nim, String password) {
        for (Student student : studentList) {
            if (student.getNim().equals(nim) && student.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Integer> getBorrowedBooksByStudent(String nim) {
        return borrowedBooksByStudent.getOrDefault(nim, new HashMap<>());
    }

    public void updateBorrowedBooks(String nim, Map<String, Integer> borrowedBooks) {
        borrowedBooksByStudent.put(nim, borrowedBooks);
    }

}