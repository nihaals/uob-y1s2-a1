package fsd.assignment.assignment1;

import fsd.assignment.assignment1.datamodel.Student;
import fsd.assignment.assignment1.datamodel.StudentData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;

public class Controller {

    //these variables correspond to the <top> of main-view.fxml
    @FXML
    private TextField studId;

    @FXML
    private TextField yearStudy;

    @FXML
    private ChoiceBox<String> mod1Choice;

    @FXML
    private ChoiceBox<String> mod2Choice;

    @FXML
    private ChoiceBox<String> mod3Choice;

    private String choice1, choice2, choice3;

    private String modChoices[] = {"OOP", "Data Algo", "DS", "Maths", "AI",
            "Adv Programming", "Project"};

    @FXML
    private Label validateStudent; //remember this is the Label that you only see when there is an invalid "add"

    //validateStudent is the last element corresponding to <top>

    //these variables correspond to the <left> i.e. the studentListView
    @FXML
    private ListView<Student> studentListView;

    //these variables correspond to the <bottom> part of the border
    @FXML
    private Label yearStudyView;

    @FXML
    private Label mod1View;

    @FXML
    private Label mod2View;

    @FXML
    private Label mod3View;

    //mod3View is the last element for the bottom part of the border

    //the contextMenu is used for the right-click regarding Edit / Delete
    @FXML
    private ContextMenu listContextMenu;

    //this variable is used when switching windows from add to edit
    @FXML
    private BorderPane mainWindow;

    //used to add a student to the ArrayList for addStudentData()
    public Student studentToAdd;


    public void initialize() {

        studentListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
                if (newValue != null) {
                    yearStudyView.setText(newValue.getYearOfStudy());
                    mod1View.setText(newValue.getModule1());
                    mod2View.setText(newValue.getModule2());
                    mod3View.setText(newValue.getModule3());
                }
            }
        });
        //the setOnAction ensures that when a ChoiceBox is selected the getChoice() grabs the selected choice
        mod1Choice.setOnAction(this::getChoice);
        mod2Choice.setOnAction(this::getChoice);
        mod3Choice.setOnAction(this::getChoice);

        //insert the code to addAll() the modChoices [] to each ChoiceBox here
        mod1Choice.getItems().addAll(modChoices);
        mod2Choice.getItems().addAll(modChoices);
        mod3Choice.getItems().addAll(modChoices);

        //deleting a student
        listContextMenu = new ContextMenu();
        MenuItem deleteStudent = new MenuItem("Delete?");

        deleteStudent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Student student = studentListView.getSelectionModel().getSelectedItem();
                deleteStudent(student);
            }
        });

        //editing a student
        listContextMenu = new ContextMenu();

        MenuItem editStudent = new MenuItem("Edit?");

        editStudent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Student student = studentListView.getSelectionModel().getSelectedItem();
                editStudent(student);
            }
        });

        //code provided to ensure that contextMenu appears as part of the above actions
        listContextMenu.getItems().add(deleteStudent);
        listContextMenu.getItems().add(editStudent);

        //to ensure access to a particular cell in the studentListView
        studentListView.setCellFactory(new Callback<ListView<Student>, ListCell<Student>>() {
            public ListCell<Student> call(ListView<Student> param) {
                ListCell<Student> cell = new ListCell<Student>() {
                    @Override
                    protected void updateItem(Student stu, boolean empty) {
                        super.updateItem(stu, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(stu.getStudId());
                        }
                    }//end of update()
                };
                //code included as part of the delete
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        });
                return cell;
            }
        }); //end of setting the cell factory

        SortedList<Student> sortedByYear = new SortedList<>(StudentData.getInstance().getStudents(), new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getYearOfStudy().compareTo(o2.getYearOfStudy());
            }
        });

        studentListView.setItems(sortedByYear);
        studentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        studentListView.getSelectionModel().selectFirst();
    }

    public void getChoice(ActionEvent event) {
        if (event.getSource() == mod1Choice) {
            choice1 = mod1Choice.getValue();
        } else if (event.getSource() == mod2Choice) {
            choice2 = mod2Choice.getValue();
        } else if (event.getSource() == mod3Choice) {
            choice3 = mod3Choice.getValue();
        }
    }


    @FXML
    public void addStudentData() {
        String studIdS = studId.getText();
        String yearStudyS = yearStudy.getText();
        //do the if...here
        if (studIdS.isEmpty() || yearStudyS.isEmpty()) {
            validateStudent.setText("Error: cannot add student if studId or year of study not filled in");
        } else {
            //do the else...here, first ensure that the validateStudent label is clear of any text
            validateStudent.setText("");
            studentToAdd = new Student(studIdS, yearStudyS, choice1, choice2, choice3);
            //use the getInstance() to addStudentData()
            StudentData.getInstance().addStudentData(studentToAdd);
            //select the student that has been added so that it is highlighted on the list
            studentListView.getSelectionModel().select(studentToAdd);
        }
    }

    public void deleteStudent(Student stu) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete a student from the list");
        alert.setHeaderText("Deleting student " + stu.getStudId());
        alert.setContentText("Are you sure you want to delete the student?");
        //the result object with the showAndWait() has been completed for you
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            StudentData.getInstance().deleteStudent(stu);
        }
    }

    public void editStudent(Student stu) {
        //the dialog object has been completed for you
        Dialog<ButtonType> dialog = new Dialog<>();
        //insert the 3 lines of code here
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setTitle("Edit a student's details");
        dialog.setHeaderText("Editing student Id: " + stu.getStudId());
        FXMLLoader fxmlLoader = new FXMLLoader();
        //insert the line of code here
        fxmlLoader.setLocation(getClass().getResource("edit-students.fxml"));
        //remove the comments and complete the try...catch
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException event) {
            System.out.println("Could not load the dialog");
            event.printStackTrace();
            return;
        }
        EditStudentController ec = fxmlLoader.getController();
        //insert the line of code here
        ec.setToEdit(stu);
        //insert the 2 lines of code here
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        //the result object with the showAndWait() has been completed for you
        Optional<ButtonType> result = dialog.showAndWait();

        //remove the comments and complete the if...
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Student editStudent = ec.processEdit(stu);
            //select the edited studId here
            studentListView.getSelectionModel().select(editStudent);
        }
    }
}