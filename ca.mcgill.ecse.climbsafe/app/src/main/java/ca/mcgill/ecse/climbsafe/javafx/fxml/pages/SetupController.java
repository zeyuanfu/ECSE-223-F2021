package ca.mcgill.ecse.climbsafe.javafx.fxml.pages;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import java.sql.Date;
import java.time.LocalDate;

import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet1Controller;
import ca.mcgill.ecse.climbsafe.javafx.fxml.ClimbSafeFxmlView;
import javafx.event.ActionEvent;

import javafx.scene.control.DatePicker;

public class SetupController {

  @FXML
  private TextField nrWeeksField;
  @FXML
  private TextField guideWeeklyCostField;
  @FXML
  private Button setupButton;
  @FXML
  private DatePicker dateField;

  /**
   * SetUp the program when the user presses the set up button. It calls the controller method
   * setup, with the values from the textfields
   *
   * @author magnus Gao
   */
  // Event Listener on Button[#setupButton].onAction
  public void setupProgram(ActionEvent event) {
    LocalDate selectedDate = dateField.getValue();
    if (selectedDate == null) {
      ViewUtils.showError("Please select a valid date");
    }

    Date date = Date.valueOf(selectedDate);
    int nrWeeks = Integer.parseInt(nrWeeksField.getText());
    int guideWeeklyCost = Integer.parseInt(guideWeeklyCostField.getText());

    ViewUtils
        .successful(() -> ClimbSafeFeatureSet1Controller.setup(date, nrWeeks, guideWeeklyCost));

    dateField.getEditor().clear();
    guideWeeklyCostField.clear();
    nrWeeksField.clear();
    String toastMsg = "successfully setup climbsafe";
    ClimbSafeFxmlView.makeText(toastMsg);

  }
}
