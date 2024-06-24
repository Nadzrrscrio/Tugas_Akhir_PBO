package data;

import books.Book;
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

import java.util.Map;

public class Student {
    private String name;
    private String nim;
    private String password;
    private String faculty;
    private String program;
    private static final int SCENE_WIDTH = 1540;
    private static final int SCENE_HEIGHT = 790;
    public Student(String name, String nim, String password, String faculty, String program) {
        this.name = name;
        this.nim = nim;
        this.password = password;
        this.faculty = faculty;
        this.program = program;
    }

    public String getName() {
        return name;
    }

    public String getNim() {
        return nim;
    }

    public String getPassword() {
        return password;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getProgram() {
        return program;
    }

    public Scene iStudentScene(Stage primaryStage, LibrarySystem librarySystem, Admin admin, Map<String, Integer> borrowedBooks) {
        Image iBackground = new Image("file:src/main/java/image/iBackground.png");
        ImageView iBackgroundview = new ImageView(iBackground);

        Button displayBooksButton = new Button("Tampilkan Daftar Buku");
        Button borrowBookButton = new Button("Pinjam Buku");
        Button displayBorrowedBooksButton = new Button("Tampilkan Buku Terpinjam");
        Button returnBookButton = new Button("Kembalikan Buku");
        Button exitButton = new Button("Keluar");

        double buttonWidth = 200;
        displayBooksButton.setPrefWidth(buttonWidth);
        borrowBookButton.setPrefWidth(buttonWidth);
        displayBorrowedBooksButton.setPrefWidth(buttonWidth);
        returnBookButton.setPrefWidth(buttonWidth);
        exitButton.setPrefWidth(buttonWidth);

        displayBooksButton.setOnAction(e -> primaryStage.setScene(admin.iDisplayBooksScene(primaryStage, librarySystem, false)));
        borrowBookButton.setOnAction(e -> primaryStage.setScene(iBorrowBookScene(primaryStage, librarySystem, admin, borrowedBooks)));
        displayBorrowedBooksButton.setOnAction(e -> primaryStage.setScene(iDisplayBorrowedBooksScene(primaryStage, librarySystem, admin, borrowedBooks)));
        returnBookButton.setOnAction(e -> primaryStage.setScene(iReturnBookScene(primaryStage, librarySystem, admin, borrowedBooks)));
        exitButton.setOnAction(e -> primaryStage.setScene(librarySystem.initialScene));

        VBox studentLayout = new VBox(10, displayBooksButton, borrowBookButton, displayBorrowedBooksButton, returnBookButton, exitButton);
        studentLayout.setAlignment(Pos.CENTER);

        VBox borderBox = new VBox(studentLayout);
        borderBox.setAlignment(Pos.CENTER);
        borderBox.setStyle("-fx-border-color: black; -fx-padding: 20;");
        borderBox.setPrefWidth(250);
        borderBox.setMaxWidth(250);
        borderBox.setPadding(new Insets(20));

        Label studentMenuText = new Label("Menu Mahasiswa");
        studentMenuText.setFont(Font.font("Impact", FontWeight.BOLD, 24));
        studentMenuText.setStyle("-fx-text-fill: #0097b2;");

        VBox mainLayout = new VBox(5, studentMenuText, borderBox);
        StackPane stackPane = new StackPane(iBackgroundview, mainLayout);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }


    public Scene iBorrowBookScene(Stage primaryStage, LibrarySystem librarySystem, Admin admin, Map<String, Integer> borrowedBooks) {
        Image iBackground = new Image("file:src/main/java/image/iBackground.png");
        ImageView iBackgroundview = new ImageView(iBackground);

        VBox borrowBookLayout = new VBox(10);
        borrowBookLayout.setAlignment(Pos.CENTER);
        StackPane stackPane = new StackPane(iBackgroundview, borrowBookLayout);

        TableView<Book> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(500);
        table.setMaxHeight(150);

        TableColumn<Book, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<Book, String> titleColumn = new TableColumn<>("Judul Buku");
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        titleColumn.setPrefWidth(130);

        TableColumn<Book, String> authorColumn = new TableColumn<>("Penulis");
        authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));
        authorColumn.setPrefWidth(130);

        TableColumn<Book, String> categoryColumn = new TableColumn<>("Kategori");
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        categoryColumn.setPrefWidth(80);

        TableColumn<Book, String> stockColumn = new TableColumn<>("Stok");
        stockColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStock())));
        stockColumn.setPrefWidth(80);

        table.getColumns().addAll(idColumn, titleColumn, authorColumn, categoryColumn, stockColumn);

        for (Book book : admin.getAllBooks()) {
            table.getItems().add(book);
        }

        Label idLabel = new Label("Masukkan ID buku untuk meminjam :");
        TextField idField = new TextField();
        idField.setMaxWidth(300);

        Label durationLabel = new Label("Masukkan durasi peminjaman (max 7 hari):");
        TextField durationField = new TextField();
        durationField.setMaxWidth(300);

        Button borrowButton = new Button("Pinjam");
        Button backButton = new Button("Keluar");
        Label confirmationLabel = new Label();

        borrowButton.setOnAction(e -> {
            String id = idField.getText();
            int duration;
            try {
                duration = Integer.parseInt(durationField.getText());
                if (duration > 7) {
                    confirmationLabel.setText("Durasi peminjaman maksimal 7 hari.");
                    confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                    confirmationLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
            } catch (NumberFormatException ex) {
                confirmationLabel.setText("Tolong masukkan angka yang valid untuk durasi!.");
                confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                confirmationLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            Book book = admin.getBookById(id);
            if (book != null) {
                if (book.getStock() > 0) {
                    borrowedBooks.put(id, duration);
                    book.setStock(book.getStock() - 1); // Mengurangi stok
                    confirmationLabel.setText("Peminjaman buku berhasil.");
                    confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                    confirmationLabel.setStyle("-fx-text-fill: green;");
                    // Update borrowed books for this student
                    admin.updateBorrowedBooks(getNim(), borrowedBooks);
                } else {
                    confirmationLabel.setText("Tidak ada stok yang tersedia pada buku ini.");
                    confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                    confirmationLabel.setStyle("-fx-text-fill: red;");
                }
            } else {
                confirmationLabel.setText("Buku dengan ID tersebut tidak ditemukan.");
                confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                confirmationLabel.setStyle("-fx-text-fill: red;");
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(librarySystem.studentScene));

        borrowBookLayout.getChildren().addAll(table, idLabel, idField, durationLabel, durationField, borrowButton, backButton, confirmationLabel);
        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }


    public Scene iDisplayBorrowedBooksScene(Stage primaryStage, LibrarySystem librarySystem, Admin admin, Map<String, Integer> borrowedBooks) {
        Image iBackground = new Image("file:src/main/java/image/iBackground.png");
        ImageView iBackgroundview = new ImageView(iBackground);

        VBox displayLayout = new VBox(10);
        displayLayout.setAlignment(Pos.CENTER);
        StackPane stackPane = new StackPane(iBackgroundview, displayLayout);

        TableView<Book> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(500);
        table.setMaxHeight(150);

        TableColumn<Book, String> idColumn = new TableColumn<>("ID Buku");
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<Book, String> titleColumn = new TableColumn<>("Judul Buku");
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        titleColumn.setPrefWidth(130);

        TableColumn<Book, String> authorColumn = new TableColumn<>("Penulis");
        authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));
        authorColumn.setPrefWidth(130);

        TableColumn<Book, String> categoryColumn = new TableColumn<>("Kategori");
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        categoryColumn.setPrefWidth(80);

        TableColumn<Book, String> durationColumn = new TableColumn<>("Sisa Durasi");
        durationColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(borrowedBooks.get(data.getValue().getId()))));
        durationColumn.setPrefWidth(80);

        table.getColumns().addAll(idColumn, titleColumn, authorColumn, categoryColumn, durationColumn);

        for (Map.Entry<String, Integer> entry : admin.getBorrowedBooksByStudent(getNim()).entrySet()) {
            Book book = admin.getBookById(entry.getKey());
            if (book != null) {
                table.getItems().add(book);
            }
        }

        Button extendDurationButton = new Button("Perpanjang durasi");
        extendDurationButton.setOnAction(e -> primaryStage.setScene(iExtendDurationScene(primaryStage, librarySystem, admin, borrowedBooks)));

        Button backButton = new Button("Keluar");
        backButton.setOnAction(e -> primaryStage.setScene(librarySystem.studentScene));

        displayLayout.getChildren().addAll(table,extendDurationButton, backButton);
        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }

    public Scene iExtendDurationScene(Stage primaryStage, LibrarySystem librarySystem, Admin admin, Map<String, Integer> borrowedBooks) {
        Image iBackground = new Image("file:src/main/java/image/iBackground.png");
        ImageView iBackgroundview = new ImageView(iBackground);

        VBox extendLayout = new VBox(10);
        extendLayout.setAlignment(Pos.CENTER);
        StackPane stackPane = new StackPane(iBackgroundview, extendLayout);

        TableView<Book> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(500);
        table.setMaxHeight(150);

        TableColumn<Book, String> idColumn = new TableColumn<>("ID Buku");
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<Book, String> titleColumn = new TableColumn<>("Judul Buku");
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        titleColumn.setPrefWidth(130);

        TableColumn<Book, String> authorColumn = new TableColumn<>("Penulis");
        authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));
        authorColumn.setPrefWidth(130);

        TableColumn<Book, String> categoryColumn = new TableColumn<>("Kategori");
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        categoryColumn.setPrefWidth(80);

        TableColumn<Book, String> durationColumn = new TableColumn<>("Sisa Durasi");
        durationColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(borrowedBooks.get(data.getValue().getId()))));
        durationColumn.setPrefWidth(80);

        table.getColumns().addAll(idColumn, titleColumn, authorColumn, categoryColumn, durationColumn);

        for (Map.Entry<String, Integer> entry : borrowedBooks.entrySet()) {
            Book book = admin.getBookById(entry.getKey());
            if (book != null) {
                table.getItems().add(book);
            }
        }

        Label idLabel = new Label("Masukkan ID buku untuk perpanjang :");
        TextField idField = new TextField();
        idField.setMaxWidth(300);

        Button extendButton = new Button("Perpanjang");
        Label confirmationLabel = new Label();

        extendButton.setOnAction(e -> {
            String id = idField.getText();
            if (borrowedBooks.containsKey(id)) {
                int currentDuration = borrowedBooks.get(id);
                if (currentDuration <= 14) {
                    borrowedBooks.put(id, currentDuration + 7);
                    confirmationLabel.setText("Perpanjangan durasi berhasil.");
                    confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                    confirmationLabel.setStyle("-fx-text-fill: green;");
                    admin.updateBorrowedBooks(getNim(), borrowedBooks);
                    primaryStage.setScene(iExtendDurationScene(primaryStage, librarySystem, admin, borrowedBooks));
                } else {
                    confirmationLabel.setText("Maksimal perpanjangan buku adalah 2 kali.");
                    confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                    confirmationLabel.setStyle("-fx-text-fill: red;");
                }
            } else {
                confirmationLabel.setText("Buku dengan ID tersebut tidak ditemukan.");
                confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                confirmationLabel.setStyle("-fx-text-fill: red;");
            }
        });

        Button backButton = new Button("Keluar");
        backButton.setOnAction(e -> primaryStage.setScene(librarySystem.studentScene));

        extendLayout.getChildren().addAll(table, idLabel, idField, extendButton, backButton, confirmationLabel);
        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }

    public Scene iReturnBookScene(Stage primaryStage, LibrarySystem librarySystem, Admin admin, Map<String, Integer> borrowedBooks) {
        Image iBackground = new Image("file:src/main/java/image/iBackground.png");
        ImageView iBackgroundview = new ImageView(iBackground);

        VBox returnBookLayout = new VBox(10);
        StackPane stackPane = new StackPane(iBackgroundview, returnBookLayout);
        returnBookLayout.setAlignment(Pos.CENTER);

        returnBookLayout.getChildren().clear();

        TableView<Book> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(500);
        table.setMaxHeight(150);

        TableColumn<Book, String> idColumn = new TableColumn<>("ID Buku");
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<Book, String> titleColumn = new TableColumn<>("Judul Buku");
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        titleColumn.setPrefWidth(130);

        TableColumn<Book, String> authorColumn = new TableColumn<>("Penulis");
        authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));
        authorColumn.setPrefWidth(130);

        TableColumn<Book, String> categoryColumn = new TableColumn<>("Kategori");
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        categoryColumn.setPrefWidth(80);

        TableColumn<Book, String> durationColumn = new TableColumn<>("Durasi Peminjaman");
        durationColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(borrowedBooks.get(data.getValue().getId()))));
        durationColumn.setPrefWidth(80);

        table.getColumns().addAll(idColumn, titleColumn, authorColumn, categoryColumn, durationColumn);

        for (Map.Entry<String, Integer> entry : borrowedBooks.entrySet()) {
            Book book = admin.getBookById(entry.getKey());
            if (book != null) {
                table.getItems().add(book);
            }
        }

        Label idLabel = new Label("Masukkan ID buku untuk mengembalikan :");
        TextField idField = new TextField();
        idField.setMaxWidth(300);

        Button returnButton = new Button("Kembalikan");
        Button backButton = new Button("Keluar");
        Label confirmationLabel = new Label();

        returnButton.setOnAction(e -> {
            String id = idField.getText();
            if (borrowedBooks.containsKey(id)) {
                borrowedBooks.remove(id);
                Book book = admin.getBookById(id);
                if (book != null) {
                    book.setStock(book.getStock() + 1); // Menambah stok
                }
                confirmationLabel.setText("Pengembalian buku berhasil.");
                confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                confirmationLabel.setStyle("-fx-text-fill: green;");
                admin.updateBorrowedBooks(getNim(), borrowedBooks);
                primaryStage.setScene(iReturnBookScene(primaryStage, librarySystem, admin, borrowedBooks));
            } else {
                confirmationLabel.setText("Buku dengan ID tersebut tidak ditemukan.");
                confirmationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
                confirmationLabel.setStyle("-fx-text-fill: red;");
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(librarySystem.studentScene));

        returnBookLayout.getChildren().addAll(table, idLabel, idField, returnButton, backButton, confirmationLabel);
        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }
}