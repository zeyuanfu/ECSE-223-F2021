package ca.mcgill.ecse.climbsafe.javafx.fxml.pages;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

import java.util.List;

import ca.mcgill.ecse.climbsafe.controller.*;
import ca.mcgill.ecse.climbsafe.javafx.fxml.ClimbSafeFxmlView;
import javafx.scene.control.ChoiceBox;

public class GuidePageController {

  @FXML
  private TextField guideName;
  @FXML
  private TextField guideEmail;
  @FXML
  private TextField guideEmergencyContact;
  @FXML
  private Button registerButton;
  @FXML
  private ChoiceBox<String> deleteGuideChoiceBox;
  @FXML
  private ChoiceBox<String> updateGuideChoiceBox;
  @FXML
  private TextField guidePassword;
  @FXML
  private Button updateButton;
  @FXML
  private Button deleteButton;
  @FXML
  private TextField updateGuideName;
  @FXML
  private TextField updateGuidePassword;
  @FXML
  private TextField updateGuideEmergencyContact;

  /**
   * This fetches all guides into an arr[], which will be used to display the guides
   *
   * @author Magnus Gao
   */
  // Event Listener on ChoiceBox[#deleteGuideChoiceBox].onDragDetected
  public void fetchAllGuides() {
    List<String> guideNamesList = ClimbSafeFeatureSet6Controller.getGuidesName();
    String[] guideNames = new String[guideNamesList.size()];
    for (int i = 0; i < guideNamesList.size(); i++) {
      guideNames[i] = guideNamesList.get(i);
    }

    deleteGuideChoiceBox.getItems().clear();
    deleteGuideChoiceBox.getItems().addAll(guideNames);

    updateGuideChoiceBox.getItems().clear();
    updateGuideChoiceBox.getItems().addAll(guideNames);

  }

  /**
   * This clears all the choiceboxes
   *
   * @author Magnus Gao
   */
  public void clearAllRegisterBoxes() {
    guideEmail.clear();
    guideName.clear();
    guidePassword.clear();
    guideEmergencyContact.clear();
  }

  /**
   * This clears all the choiceboxes
   *
   * @author Magnus Gao
   */
  public void clearAllUpdateBoxes() {
    updateGuidePassword.clear();
    updateGuideName.clear();
    updateGuideEmergencyContact.clear();
  }

  /**
   * Once pressing on Register, this method calls the controller method using the arguments from the
   * textfields
   *
   * @author Magnus Gao
   */
  // Event Listener on Button[#registerButton].onAction
  @FXML
  public void registerGuide(ActionEvent event) {
    String name = guideName.getText();
    String email = guideEmail.getText();
    String password = guidePassword.getText();
    String emergencyContact = guideEmergencyContact.getText();

    if (ViewUtils.successful(() -> ClimbSafeFeatureSet3Controller.registerGuide(email, password,
        name, emergencyContact))) {
      fetchAllGuides();
      clearAllRegisterBoxes();
      String toastMsg = "successfully registered guide " + name + " <" + email + ">";
      ClimbSafeFxmlView.makeText(toastMsg);
    }
  }

  /**
   * Once pressing on Update, this method calls the controller method using the arguments from the
   * textfields
   *
   * @author Magnus Gao
   */
  // Event Listener on Button[#updateButton].onAction
  @FXML
  public void updateGuide(ActionEvent event) {
    if (updateGuideChoiceBox.getValue() != null) {
      String email = updateGuideChoiceBox.getValue().toString().split(", ")[1];
      String name = updateGuideName.getText();
      String password = updateGuidePassword.getText();
      String emergencyContact = updateGuideEmergencyContact.getText();

      if (ViewUtils.successful(() -> ClimbSafeFeatureSet3Controller.updateGuide(email, password,
          name, emergencyContact))) {
        fetchAllGuides();
        clearAllUpdateBoxes();
        String toastMsg = "successfully updated guide " + name + " <" + email + ">";
        ClimbSafeFxmlView.makeText(toastMsg);
      }
    }

  }

  /**
   * Once pressing on Delete, this method calls the controller method using the arguments from the
   * textfields
   *
   * @author Magnus Gao
   */
  // Event Listener on Button[#deleteButton].onAction
  @FXML
  public void deleteGuide(ActionEvent event) {
    if (deleteGuideChoiceBox.getValue() != null) {

      String email = deleteGuideChoiceBox.getValue().toString().split(", ")[1];

      if (ViewUtils.successful(() -> ClimbSafeFeatureSet1Controller.deleteGuide(email))) {
        fetchAllGuides();
        String toastMsg = "successfully deleted guide " + " <" + email + ">";
        ClimbSafeFxmlView.makeText(toastMsg);
      }

    }
  }

  public void initialize() {
    fetchAllGuides();
  }

}
