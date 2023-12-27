package com.example.demo1;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.util.Arrays;
import java.util.Optional;

public class CarRent extends Application {
    private TableView<CarRents> tableView;
    private ObservableList<CarRents> list;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Aplikasi Penyewaan Mobil");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        // Labels
        Label labelNamaPenyewa = new Label("Nama Penyewa:");
        Label labelTipeMobil = new Label("Tipe Mobil:");
        Label labelNomorTelpon = new Label("Nomor Telpon:");
        Label labelLamaSewa = new Label("Lama Sewa:");
        Label labelTanggalSewa = new Label("Tanggal Sewa:");

        // TextFields
        TextField textFieldNamaPenyewa = new TextField();
        TextField textFieldTipeMobil = new TextField();
        TextField textFieldNomorTelpon = new TextField();
        TextField textFieldLamaSewa = new TextField();
        TextField textFieldTanggalSewa = new TextField();

        // Button
        Button buttonCreate = new Button("Create");
        buttonCreate.setOnAction(e -> {
            if (isValidInput(textFieldNamaPenyewa, textFieldTipeMobil, textFieldNomorTelpon, textFieldLamaSewa, textFieldTanggalSewa)) {
                createListData(
                        textFieldNamaPenyewa.getText(),
                        textFieldTipeMobil.getText(),
                        textFieldNomorTelpon.getText(),
                        textFieldLamaSewa.getText(),
                        textFieldTanggalSewa.getText()
                );
                // Clear input fields
                clearFields(textFieldNamaPenyewa, textFieldTipeMobil, textFieldNomorTelpon, textFieldLamaSewa, textFieldTanggalSewa);
            } else {
                showAlert("Peringatan", "Harap masukkan input dengan benar.");
            }
        });

        Button buttonUpdate = new Button("Update");
        buttonUpdate.setOnAction(e -> {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            updateListData(selectedIndex);
        });

        Button buttonDelete = new Button("Delete");
        buttonDelete.setOnAction(e -> deleteListData());

        // Add components to the grid
        grid.add(labelNamaPenyewa, 0, 0);
        grid.add(textFieldNamaPenyewa, 1, 0);
        grid.add(labelTipeMobil, 0, 1);
        grid.add(textFieldTipeMobil, 1, 1);
        grid.add(labelNomorTelpon, 0, 2);
        grid.add(textFieldNomorTelpon, 1, 2);
        grid.add(labelLamaSewa, 0, 3);
        grid.add(textFieldLamaSewa, 1, 3);
        grid.add(labelTanggalSewa, 0, 4);
        grid.add(textFieldTanggalSewa, 1, 4);
        grid.add(buttonCreate, 1, 5);
        grid.add(buttonUpdate, 2, 5);
        grid.add(buttonDelete, 3, 5);

        // Inisialisasi TableView dan TableColumn
        tableView = new TableView<>();
        TableColumn<CarRents, String> colNamaPenyewa = new TableColumn<>("Nama Penyewa");
        TableColumn<CarRents, String> colTipeMobil = new TableColumn<>("Tipe Mobil");
        TableColumn<CarRents, String> colNomorTelpon = new TableColumn<>("Nomor Telpon");
        TableColumn<CarRents, String> colLamaSewa = new TableColumn<>("Lama Sewa");
        TableColumn<CarRents, String> colTanggalSewa = new TableColumn<>("Tanggal Sewa");

        // Set PropertyValueFactory untuk setiap TableColumn
        colNamaPenyewa.setCellValueFactory(new PropertyValueFactory<>("namaPenyewa"));
        colTipeMobil.setCellValueFactory(new PropertyValueFactory<>("tipeMobil"));
        colNomorTelpon.setCellValueFactory(new PropertyValueFactory<>("nomorTelpon"));
        colLamaSewa.setCellValueFactory(new PropertyValueFactory<>("lamaSewa"));
        colTanggalSewa.setCellValueFactory(new PropertyValueFactory<>("tanggalSewa"));

        // Tambahkan TableColumn ke TableView
        tableView.getColumns().addAll(colNamaPenyewa, colTipeMobil, colNomorTelpon, colLamaSewa, colTanggalSewa);

        // Inisialisasi jadwalList jika belum diinisialisasi
        if (list == null) {
            list = FXCollections.observableArrayList();
        }

        // Set TableView dengan data dari ObservableList
        tableView.setItems(list);

        // Add TableView to the grid
        grid.add(tableView, 0, 6, 2, 1);

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean isValidInput(TextField textFieldNamaPenyewa, TextField textFieldTipeMobil, TextField textFieldNomorTelpon, TextField textFieldLamaSewa, TextField textFieldTanggalSewa) {
        // Validasi input
        String namaPenyewa = textFieldNamaPenyewa.getText();
        String tipeMobil = textFieldTipeMobil.getText();
        String nomorTelpon = textFieldNomorTelpon.getText();
        String lamaSewa = textFieldLamaSewa.getText();
        String tanggalSewa = textFieldTanggalSewa.getText();

        if (!namaPenyewa.isEmpty() && !tipeMobil.isEmpty() && !nomorTelpon.isEmpty() && !lamaSewa.isEmpty() && !tanggalSewa.isEmpty()) {
            if (isNumeric(nomorTelpon)) {
                return true;
            } else {
                showAlert("Peringatan", "Nomor Telpon harus berupa angka!");
            }
        } else {
            showAlert("Peringatan", "Form tidak boleh kosong!");
        }

        return false;
    }
    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    private void createListData(String namaPenyewa, String tipeMobil, String nomorTelpon, String lamaSewa, String tanggalSewa) {
        CarRents jadwal = new CarRents(namaPenyewa, tipeMobil, nomorTelpon, lamaSewa, tanggalSewa);
        list.add(jadwal);

        showAlert("Informasi", "Data Customer berhasil ditambahkan.");
    }

    private void updateListData(int selectedIndex) {
        if (selectedIndex >= 0 && selectedIndex < list.size()) {
            CarRents selectedListData = list.get(selectedIndex);

            // Membuat dialog untuk memasukkan data baru
            Dialog<CarRents> dialog = new Dialog<>();
            dialog.setTitle("Update Jadwal");
            dialog.setHeaderText(null);

            // Tombol OK hanya diaktifkan jika semua field diisi
            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            // GridPane untuk layout dialog
            GridPane dialogGrid = new GridPane();
            dialogGrid.setHgap(10);
            dialogGrid.setVgap(10);
            dialogGrid.setPadding(new Insets(20, 150, 10, 10));

            // TextFields untuk memasukkan data baru
            TextField textFieldNamaPenyewa = new TextField(selectedListData.getNamaPenyewa());
            TextField textFieldTipeMobil = new TextField(selectedListData.getTipeMobil());
            TextField textFieldNomorTelpon = new TextField(selectedListData.getNomorTelpon());
            TextField textFieldLamaSewa = new TextField(selectedListData.getLamaSewa());
            TextField textFieldTanggalSewa = new TextField(selectedListData.getTanggalSewa());

            dialogGrid.add(new Label("Nama Penyewa:"), 0, 0);
            dialogGrid.add(textFieldNamaPenyewa, 1, 0);
            dialogGrid.add(new Label("Tipe Mobil:"), 0, 1);
            dialogGrid.add(textFieldTipeMobil, 1, 1);
            dialogGrid.add(new Label("Nomor Telpon:"), 0, 2);
            dialogGrid.add(textFieldNomorTelpon, 1, 2);
            dialogGrid.add(new Label("Lama Sewa:"), 0, 3);
            dialogGrid.add(textFieldLamaSewa, 1, 3);
            dialogGrid.add(new Label("Tanggal Sewa:"), 0, 4);
            dialogGrid.add(textFieldTanggalSewa, 1, 4);

            // Menyimpan hasil input dialog
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    return new CarRents(
                            textFieldNamaPenyewa.getText(),
                            textFieldTipeMobil.getText(),
                            textFieldNomorTelpon.getText(),
                            textFieldLamaSewa.getText(),
                            textFieldTanggalSewa.getText()
                    );
                }
                return null;
            });

            // Mengaktifkan tombol OK hanya jika semua field diisi
            Node updateButton = dialog.getDialogPane().lookupButton(updateButtonType);
            updateButton.setDisable(true);

            // Mengaktifkan tombol OK hanya jika semua field diisi
            Arrays.asList(textFieldNamaPenyewa, textFieldTipeMobil, textFieldNomorTelpon, textFieldLamaSewa, textFieldTanggalSewa)
                    .forEach(field -> ((TextField) field).textProperty().addListener((observable, oldValue, newValue) -> {
                        updateButton.setDisable(
                                textFieldNamaPenyewa.getText().trim().isEmpty() ||
                                        textFieldTipeMobil.getText().trim().isEmpty() ||
                                        textFieldNomorTelpon.getText().trim().isEmpty() ||
                                        textFieldLamaSewa.getText().trim().isEmpty() ||
                                        textFieldTanggalSewa.getText().trim().isEmpty()
                        );
                    }));


            dialog.getDialogPane().setContent(dialogGrid);

            // Menampilkan dialog dan mengupdate data jika tombol OK ditekan
            Optional<CarRents> result = dialog.showAndWait();
            result.ifPresent(updatedJadwal -> {
                selectedListData.setNamaPenyewa(updatedJadwal.getNamaPenyewa());
                selectedListData.setTipeMobil(updatedJadwal.getTipeMobil());
                selectedListData.setNomorTelpon(updatedJadwal.getNomorTelpon());
                selectedListData.setLamaSewa(updatedJadwal.getLamaSewa());
                selectedListData.setTanggalSewa(updatedJadwal.getTanggalSewa());
                tableView.refresh();
                showAlert("Informasi", "Data berhasil diupdate.");
            });
        } else {
            showAlert("Peringatan", "Pilih customer yang ingin diupdate.");
        }
    }


    private void deleteListData() {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < list.size()) {
            list.remove(selectedIndex);
            showAlert("Informasi", "Data berhasil dihapus.");
        } else {
            showAlert("Peringatan", "Pilih data yang ingin dihapus.");
        }
    }


    private void clearFields(TextField textFieldNamaPenyewa, TextField textFieldTipeMobil, TextField textFieldNomorTelpon, TextField textFieldLamaSewa, TextField textFieldTanggalSewa) {
        // Clear input fields
        textFieldNamaPenyewa.clear();
        textFieldTipeMobil.clear();
        textFieldNomorTelpon.clear();
        textFieldLamaSewa.clear();
        textFieldTanggalSewa.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static class CarRents {
        private String namaPenyewa;
        private String tipeMobil;
        private String nomorTelpon;
        private String lamaSewa;
        private String tanggalSewa;

        public CarRents(String namaPenyewa, String tipeMobil, String nomorTelpon, String lamaSewa, String tanggalSewa) {
            this.namaPenyewa = namaPenyewa;
            this.tipeMobil = tipeMobil;
            this.nomorTelpon = nomorTelpon;
            this.lamaSewa = lamaSewa;
            this.tanggalSewa = tanggalSewa;
        }

        // Buat getter untuk setiap atribut
        public String getNamaPenyewa() {
            return namaPenyewa;
        }

        public String getTipeMobil() {
            return tipeMobil;
        }

        public String getNomorTelpon() {
            return nomorTelpon;
        }

        public String getLamaSewa() {
            return lamaSewa;
        }

        public String getTanggalSewa() {
            return tanggalSewa;
        }

        // Metode setter untuk setiap atribut
        public void setNamaPenyewa(String namaPenyewa) {
            this.namaPenyewa = namaPenyewa;
        }

        public void setTipeMobil(String tipeMobil) {
            this.tipeMobil = tipeMobil;
        }

        public void setNomorTelpon(String nomorTelpon) {
            this.nomorTelpon = nomorTelpon;
        }

        public void setLamaSewa(String lamaSewa) {
            this.lamaSewa = lamaSewa;
        }

        public void setTanggalSewa(String tanggalSewa) {
            this.tanggalSewa = tanggalSewa;
        }
    }
}