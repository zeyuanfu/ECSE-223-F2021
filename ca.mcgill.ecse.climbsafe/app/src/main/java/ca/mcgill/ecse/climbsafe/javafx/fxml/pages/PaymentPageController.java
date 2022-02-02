package ca.mcgill.ecse.climbsafe.javafx.fxml.pages;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import java.util.List;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.controller.AssignmentController;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet6Controller;
import ca.mcgill.ecse.climbsafe.javafx.fxml.ClimbSafeFxmlView;
import javafx.event.ActionEvent;

import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;

public class PaymentPageController {

  @FXML
  private TextField authCode;

  @FXML
  private Button cancelButton;

  @FXML
  private Button finishButton;

  @FXML
  private ChoiceBox<String> memberChoiceBox;

  @FXML
  private ChoiceBox<String> memberEmail;

  @FXML
  private Button payButton;

  @FXML
  private Button startButton;

  @FXML
  private ChoiceBox<String> weekNumber;

  /**
   * Once pressing on Cancel, this method calls the controller method using the argument Email,
   * that's from a choicebox
   *
   * @author Magnus Gao
   */
  // Event Listener on Button[#cancelButton].onAction
  @FXML
  public void cancelTrip(ActionEvent event) {
    if (memberEmail.getValue() != null) {

      String email = memberEmail.getValue().toString();
      ViewUtils.successful(() -> AssignmentController.cancelTrip(email));
      String toastMsg = "successfully cancelled trip for " + memberEmail.getValue();
      ClimbSafeFxmlView.makeText(toastMsg);
    }
  }

  /**
   * Once pressing on Finish Trip, this method calls the controller method using the argument Email,
   * that's from a choicebox
   *
   * @author Magnus Gao
   */
  // Event Listener on Button[#finishButton].onAction
  @FXML
  public void finishTrip(ActionEvent event) {
    if (memberEmail.getValue() != null) {
      String email = memberEmail.getValue().toString();
      ViewUtils.successful(() -> AssignmentController.finishTrip(email));
      String toastMsg = "successfully finished trip for " + memberEmail.getValue();
      ClimbSafeFxmlView.makeText(toastMsg);
    }
  }

  /**
   * Once pressing on start, this method calls the controller method using the argument week, that's
   * from a choicebox
   *
   * @author Magnus Gao
   */
  // Event Listener on Button[#startButton].onAction
  @FXML
  public void startTrips(ActionEvent event) {
    if (weekNumber.getValue() != null) {
      String weeks = weekNumber.getValue().toString();
      ViewUtils.successful(() -> AssignmentController.startTrips(weeks));
      String toastMsg = "successfully started trips";
      ClimbSafeFxmlView.makeText(toastMsg);
    }
  }

  /**
   * Once pressing on Pay, this method calls the controller method using the argument AuthCode,
   * that's from a textBox Email, that's from a choicebox
   *
   * @author Magnus Gao
   */
  // Event Listener on Button[#payButton].onAction
  @FXML
  public void pay(ActionEvent event) {
    if (memberChoiceBox.getValue() != null) {
      String email = memberChoiceBox.getValue().toString();
      String authcode = authCode.getText();

      ViewUtils.successful(() -> AssignmentController.confirmPayment(email, authcode));
      String toastMsg = "successfully payed the trip for " + email;
      ClimbSafeFxmlView.makeText(toastMsg);
    }

  }

  /**
   * This fetches all weeks into an arr[], which will be used to display the weekChoices
   *
   * @author Yazdan Zinati
   */
  public void fetchWeeks() {
    int nrWeeks = ClimbSafeApplication.getClimbSafe().getNrWeeks();
    String[] weeks = new String[nrWeeks + 1];

    for (int i = 1; i <= nrWeeks; i++) {
      weeks[i] = Integer.toString(i);
    }

    weekNumber.getItems().clear();
    weekNumber.getItems().addAll(weeks);
  }

  /**
   * This fetches all weeks into an arr[], which will be used to display the weekChoices
   *
   * @author Yazdan Zinati
   */
  public void fetchMembers() {
    List<String> memberNamesList = ClimbSafeFeatureSet6Controller.getMembersEmails();
    String[] memberNames = new String[memberNamesList.size()];

    for (int i = 0; i < memberNamesList.size(); i++) {
      memberNames[i] = memberNamesList.get(i);
    }

    memberChoiceBox.getItems().clear();
    memberChoiceBox.getItems().addAll(memberNames);

    memberEmail.getItems().clear();
    memberEmail.getItems().addAll(memberNames);
  }

  public void initialize() {
    fetchMembers();
    fetchWeeks();

    memberChoiceBox.setOnMouseEntered((event) -> {
      fetchMembers();
    });
    memberEmail.setOnMouseEntered((event) -> {
      fetchMembers();
    });
    weekNumber.setOnMouseEntered((event) -> {
      fetchWeeks();
    });


  }
}