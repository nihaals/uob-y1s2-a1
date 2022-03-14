package fsd.assignment.assignment1;

import fsd.assignment.assignment1.datamodel.Student;
import fsd.assignment.assignment1.datamodel.StudentData;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;


public class EditStudentController {
    //all of the variables declared below correspond with the edit-students.fxml
    @FXML
    private Label yearStudyDisplay;

    @FXML
    private Label mod1Edit;

    @FXML
    private Label mod2Edit;

    @FXML
    private Label mod3Edit;

    @FXML
    private ChoiceBox<String> mod1ChoiceEdit;

    @FXML
    private ChoiceBox<String> mod2ChoiceEdit;

    @FXML
    private ChoiceBox<String> mod3ChoiceEdit;

    //the modChoices variables correspond to the []
    private String mod1S, mod2S, mod3S;

    private String modChoices[] = {"OOP", "Data Algo", "DS", "Maths", "AI",
            "Adv Programming", "Project"};

    public void initialize() {
        //insert 3 lines of code here
        mod1ChoiceEdit.getItems().addAll(modChoices);
        mod2ChoiceEdit.getItems().addAll(modChoices);
        mod3ChoiceEdit.getItems().addAll(modChoices);
        //these lines have been given to you includes the setOnAction if a ChoiceBox is selected
        mod1ChoiceEdit.setOnAction(this::getChoiceEdit);
        mod2ChoiceEdit.setOnAction(this::getChoiceEdit);
        mod3ChoiceEdit.setOnAction(this::getChoiceEdit);
    }

    //to ensure that detail pops up to edit
    public void setToEdit(Student stu) {
        //insert 4 lines of code here
        yearStudyDisplay.setText(stu.getYearOfStudy());
        mod1Edit.setText(stu.getModule1());
        mod2Edit.setText(stu.getModule2());
        mod3Edit.setText(stu.getModule3());

        mod1S = stu.getModule1();
        mod2S = stu.getModule2();
        mod3S = stu.getModule3();
    }

    public Student processEdit(Student stu) {
        ObservableList<Student> allStudents = StudentData.getInstance().getStudents();

        String studIdS = stu.getStudId();
        String yearStudyS = stu.getYearOfStudy();

        //insert the line to remove the student here
        allStudents.remove(stu);

        Student changedStu = new Student(studIdS, yearStudyS, mod1S, mod2S, mod3S);
        //call the addStudentData()
        StudentData.getInstance().addStudentData(changedStu);
        return changedStu;
    }

    public void getChoiceEdit(ActionEvent event) {
        if (event.getSource() == mod1ChoiceEdit) {
            mod1S = mod1ChoiceEdit.getValue();
        } else if (event.getSource() == mod2ChoiceEdit) {
            mod2S = mod2ChoiceEdit.getValue();
        } else if (event.getSource() == mod3ChoiceEdit) {
            mod3S = mod3ChoiceEdit.getValue();
        }
    }
}
