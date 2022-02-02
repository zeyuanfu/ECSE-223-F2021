package ca.mcgill.ecse.climbsafe.javafx.fxml.pages;

import javafx.fxml.FXML;
import java.util.List;
import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.controller.AssignmentController;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet6Controller;
import ca.mcgill.ecse.climbsafe.controller.TOAssignment;
import ca.mcgill.ecse.climbsafe.javafx.fxml.ClimbSafeFxmlView;
import javafx.event.ActionEvent;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class InitiateAssignmentController {

  @FXML
  private ChoiceBox<String> namesOfParticipants;
  @FXML
  private Label memberName;
  @FXML
  private Label memberEmail;
  @FXML
  private Label startWeek;
  @FXML
  private Label guideEmail;
  @FXML
  private Label guideName;
  @FXML
  private Label endWeek;
  @FXML
  private Label hotelName;
  @FXML
  private Label authCode;
  @FXML
  private Label guideCost;
  @FXML
  private Label equipmentCost;
  @FXML
  private Label refund;
  @FXML
  private Label status;

  /**
   * Initiate assignments.
   *
   * @author Elie Abdo
   */
  @FXML
  public void initiateAssignments(ActionEvent event) {

    try {
      AssignmentController.initiateAssignment();

      List<TOAssignment> memberAssignments = ClimbSafeFeatureSet6Controller.getAssignments();
      String[] memberEmail = new String[memberAssignments.size()];

      for (int i = 0; i < memberAssignments.size(); i++) {
        memberEmail[i] = memberAssignments.get(i).getMemberEmail();
      }

      namesOfParticipants.getItems().clear();
      namesOfParticipants.getItems().addAll(memberEmail);
      String toastMsg = "successfully initiated assignments";
      ClimbSafeFxmlView.makeText(toastMsg);

    } catch (Exception e) {
      ViewUtils.showError(e.toString());
    }

  }

  /**
   * Initiate the page.
   *
   * @author Elie Abdo
   */
  public void initialize() {

    namesOfParticipants.setOnAction((event) -> {

      String chosenEmail = namesOfParticipants.getValue().toString();

      TOAssignment toDisplay = null;
      List<TOAssignment> memberAssignments = ClimbSafeFeatureSet6Controller.getAssignments();

      for (TOAssignment curAssignment : memberAssignments) {
        if (curAssignment.getMemberEmail().equals(chosenEmail)) {
          toDisplay = curAssignment;
          break;
        }
      }

      memberName.setText(toDisplay.getMemberName());
      memberEmail.setText(chosenEmail);
      startWeek.setText(Integer.toString(toDisplay.getStartWeek()));
      endWeek.setText(Integer.toString(toDisplay.getEndWeek()));
      guideName.setText(toDisplay.getGuideName());
      guideEmail.setText(toDisplay.getGuideEmail());
      hotelName.setText(toDisplay.getHotelName());
      authCode.setText(toDisplay.getAuthCode());
      guideCost.setText(Integer.toString(toDisplay.getTotalCostForGuide()));
      equipmentCost.setText(Integer.toString(toDisplay.getTotalCostForEquipment()));
      refund.setText(Integer.toString(toDisplay.getDiscount()));
      status.setText(toDisplay.getStatus());
      authCode.setText(toDisplay.getAuthCode());

    });

    List<TOAssignment> memberAssignments = ClimbSafeFeatureSet6Controller.getAssignments();
    String[] memberEmail = new String[memberAssignments.size()];

    for (int i = 0; i < memberAssignments.size(); i++) {
      memberEmail[i] = memberAssignments.get(i).getMemberEmail();
    }

    namesOfParticipants.getItems().clear();
    namesOfParticipants.getItems().addAll(memberEmail);
  }


}
