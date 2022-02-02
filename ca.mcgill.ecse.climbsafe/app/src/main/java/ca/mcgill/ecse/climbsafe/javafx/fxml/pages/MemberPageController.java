package ca.mcgill.ecse.climbsafe.javafx.fxml.pages;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet1Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet2Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet5Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet6Controller;
import ca.mcgill.ecse.climbsafe.javafx.fxml.ClimbSafeFxmlView;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

import javafx.scene.control.PasswordField;

import javafx.scene.control.CheckBox;

import javafx.scene.control.ChoiceBox;

public class MemberPageController {

  @FXML
  private TextField memberName;
  @FXML
  private TextField memberEmail;
  @FXML
  private TextField memberEmergencyContact;
  @FXML
  private PasswordField memberPassword;
  @FXML
  private Button registerButton;
  @FXML
  private ChoiceBox<String> deleteMemberChoiceBox;
  @FXML
  private Button deleteButton;
  @FXML
  private ChoiceBox<String> updateMemberChoiceBox;
  @FXML
  private TextField updateMemberName;
  @FXML
  private PasswordField updateMemberPassword;
  @FXML
  private TextField updateMemberEmergencyContact;
  @FXML
  private Button updateButton;
  @FXML
  private TextField nrWeeks;
  @FXML
  private CheckBox guideRequired;
  @FXML
  private CheckBox hotelRequired;
  @FXML
  private TextField updateNrWeeks;
  @FXML
  private CheckBox updateGuideRequired;
  @FXML
  private CheckBox updateHotelRequired;
  @FXML
  private ChoiceBox<String> bookables;
  @FXML
  private TextField quantity;
  @FXML
  private Button addBookableButton;
  @FXML
  private Button removeBookableButton;
  @FXML
  private ChoiceBox<String> bookableMember;
  @FXML
  private Button removeAllBookableButton;

  /**
   * Initialize pages
   *
   * @author Yazdan Zinati
   */
  @FXML
  public void initialize() {
    bookables.addEventHandler(ClimbSafeFxmlView.REFRESH_EVENT, e -> {
      bookables.setItems(ClimbSafeFeatureSet5Controller.getAllBookableItems());

    });
    bookables.setOnMouseEntered((event) -> {
      fetchBookableItems();
    });
    fetchAllMembers();
    fetchBookableItems();
  }


  /**
   * Register a member
   *
   * @author Yazdan Zinati
   */
  // Event Listener on Button[#registerButton].onAction
  @FXML
  public void registerMember(ActionEvent event) {
    String nWeeks = nrWeeks.getText();
    String name = memberName.getText();
    String email = memberEmail.getText();
    String password = memberPassword.getText();
    String emergencyContact = memberEmergencyContact.getText();
    boolean guideReq = guideRequired.isSelected();
    boolean hotelReq = hotelRequired.isSelected();

    if (ViewUtils.successful(
        () -> ClimbSafeFeatureSet2Controller.registerMember(email, password, name, emergencyContact,
            Integer.parseInt(nWeeks), guideReq, hotelReq, new ArrayList<>(), new ArrayList<>()))) {
      fetchAllMembers();
      clearAllRegisterationBoxes();
      String toastMsg = "successfully registered member " + name + " <" + email + ">";
      ClimbSafeFxmlView.makeText(toastMsg);

    }

  }

  public void clearAllRegisterationBoxes() {
    memberEmail.clear();
    memberName.clear();
    memberPassword.clear();
    memberEmergencyContact.clear();
    nrWeeks.clear();
    guideRequired.setSelected(false);
    hotelRequired.setSelected(false);
  }

  /**
   * Fetch all members
   *
   * @author Yazdan Zinati
   */
  public void fetchAllMembers() {

    List<String> memberEmails = ClimbSafeFeatureSet6Controller.getMembersEmails();

    updateMemberChoiceBox.getItems().clear();
    updateMemberChoiceBox.getItems().addAll(memberEmails);

    deleteMemberChoiceBox.getItems().clear();
    deleteMemberChoiceBox.getItems().addAll(memberEmails);

    bookableMember.getItems().clear();
    bookableMember.getItems().addAll(memberEmails);
  }

  public void fetchBookableItems() {
    List<String> equips = ClimbSafeFeatureSet5Controller.getEquipmentNames();
    equips.addAll(ClimbSafeFeatureSet5Controller.getBundleNames());

    bookables.getItems().clear();
    bookables.getItems().addAll(equips);

  }

  // Event Listener on Button[#deleteButton].onAction

  /**
   * Delete a member
   *
   * @author Yazdan Zinati
   */
  @FXML
  public void deleteMember(ActionEvent event) {
    if (deleteMemberChoiceBox.getValue() != null) {
      String email = deleteMemberChoiceBox.getValue().toString();
      if (ViewUtils.successful(() -> ClimbSafeFeatureSet1Controller.deleteMember(email))) {
        fetchAllMembers();
      }
      String toastMsg = "successfully deleted member with email " + " <" + email + ">";
      ClimbSafeFxmlView.makeText(toastMsg);
    }
  }

  /**
   * Update a member
   *
   * @author Yazdan Zinati
   */
  // Event Listener on Button[#updateButton].onAction
  @FXML
  public void updateMember(ActionEvent event) {
    if (updateMemberChoiceBox.getValue() != null) {
      String email = updateMemberChoiceBox.getValue().toString();
      String name = updateMemberName.getText();
      String password = updateMemberPassword.getText();
      String emergencyContact = updateMemberEmergencyContact.getText();
      int nWeeks = Integer.parseInt(updateNrWeeks.getText());
      boolean guideReq = updateGuideRequired.isSelected();
      boolean hotelReq = updateHotelRequired.isSelected();

      if (ViewUtils.successful(
          () -> ClimbSafeFeatureSet2Controller.updateMember(email, password, name, emergencyContact,
              nWeeks, guideReq, hotelReq, new ArrayList<>(), new ArrayList<>()))) {
        fetchAllMembers();
        clearAllUpdateBoxes();
        String toastMsg = "successfully updated member " + name + " <" + email + ">";
        ClimbSafeFxmlView.makeText(toastMsg);

      }
    }
  }

  /**
   * Add Bookable Item
   *
   * @author Zeyuan Fu and Yazdan Zinati
   */
  @FXML
  public void addBookableItems(ActionEvent event) {
    if (bookableMember.getValue() != null) {
      String email = bookableMember.getValue().toString();
      List<String> bookedItemNames = ClimbSafeFeatureSet2Controller.getMemberItemNames(email);
      List<Integer> bookedItemQuantities =
          ClimbSafeFeatureSet2Controller.getMemberItemQuantities(email);

      String nameToAdd = bookables.getValue();

      if (nameToAdd == null) {
        ViewUtils.showError("Please select a bookable item");
        return;
      }

      if (bookedItemNames.contains(nameToAdd)) {
        int quantityToModify =
            bookedItemQuantities.get(bookedItemNames.indexOf(nameToAdd)) + Integer.parseInt(
                quantity.getText());
        var vvv = bookedItemNames.indexOf(nameToAdd);
        bookedItemQuantities.set(bookedItemNames.indexOf(nameToAdd), quantityToModify);
      } else {
        var f = quantity.getText();
        var ff = bookables.getValue().toString();
        bookedItemNames.add(bookables.getValue().toString());
        bookedItemQuantities.add(bookedItemQuantities.size(), Integer.parseInt(quantity.getText()));
        for (int i = 0; i < bookedItemQuantities.size(); i++) {
          System.out.println(bookedItemQuantities.get(i));
        }
      }

      ViewUtils.callController(() -> ClimbSafeFeatureSet2Controller.updateMember(email,
          ClimbSafeFeatureSet2Controller.getMemberPassword(email),
          ClimbSafeFeatureSet2Controller.getMemberName(email),
          ClimbSafeFeatureSet2Controller.getMemberEC(email),
          ClimbSafeFeatureSet2Controller.getMemberNrWeeks(email),
          ClimbSafeFeatureSet2Controller.getMemberGuideRequired(email),
          ClimbSafeFeatureSet2Controller.getMemberHotelRequired(email), bookedItemNames,
          bookedItemQuantities));
      fetchBookableItems();
      String toastMsg = "successfully added item " + nameToAdd;
      ClimbSafeFxmlView.makeText(toastMsg);
    }


  }

  /**
   * Remove Bookable Items
   *
   * @author Zeyuan Fu and Yazdan Zinati
   */
  @FXML
  public void removeBookableItems(ActionEvent event) {
    if (bookableMember.getValue() != null) {
      String email = bookableMember.getValue().toString();
      List<String> bookedItemNames = ClimbSafeFeatureSet2Controller.getMemberItemNames(email);
      List<Integer> bookedItemQuantities =
          ClimbSafeFeatureSet2Controller.getMemberItemQuantities(email);
      String nameToRemove = bookables.getValue().toString();

      if (nameToRemove == null) {
        ViewUtils.showError("Please select a bookable item");
        return;
      }

      for (String name : bookedItemNames) {
        if (nameToRemove.equals(name)) {
          if (bookedItemQuantities.get(bookedItemNames.indexOf(name))
              .equals(Integer.parseInt(quantity.getText()))) {
            bookedItemNames.remove(bookedItemNames.indexOf(name));
            bookedItemQuantities.remove(bookedItemNames.indexOf(name));
          } else if (bookedItemQuantities.get(bookedItemNames.indexOf(name)) > Integer
              .parseInt(quantity.getText())) {
            bookedItemQuantities.set(bookedItemNames.indexOf(name), (bookedItemQuantities
                .get(bookedItemNames.indexOf(name)) - Integer.parseInt(quantity.getText())));
          } else {
            ViewUtils.showError(
                "Cannot remove more booked items than the member currently has assigned");
            return;
          }
        }
      }

      ViewUtils.callController(() -> ClimbSafeFeatureSet2Controller.updateMember(email,
          ClimbSafeFeatureSet2Controller.getMemberPassword(email),
          ClimbSafeFeatureSet2Controller.getMemberName(email),
          ClimbSafeFeatureSet2Controller.getMemberEC(email),
          ClimbSafeFeatureSet2Controller.getMemberNrWeeks(email),
          ClimbSafeFeatureSet2Controller.getMemberGuideRequired(email),
          ClimbSafeFeatureSet2Controller.getMemberHotelRequired(email), bookedItemNames,
          bookedItemQuantities));
      String toastMsg = "successfully removed item " + nameToRemove;
      ClimbSafeFxmlView.makeText(toastMsg);

    }
  }

  /**
   * Remove All Bookable Items
   *
   * @author Zeyuan Fu and Yazdan Zinati
   */
  @FXML
  public void removeAllBookableItems(ActionEvent event) {
    if (bookableMember.getValue() != null) {
      String email = bookableMember.getValue().toString();
      List<String> bookedItemNames = new ArrayList<String>();
      List<Integer> bookedItemQuantities = new ArrayList<Integer>();

      ViewUtils.callController(() -> ClimbSafeFeatureSet2Controller.updateMember(email,
          ClimbSafeFeatureSet2Controller.getMemberPassword(email),
          ClimbSafeFeatureSet2Controller.getMemberName(email),
          ClimbSafeFeatureSet2Controller.getMemberEC(email),
          ClimbSafeFeatureSet2Controller.getMemberNrWeeks(email),
          ClimbSafeFeatureSet2Controller.getMemberGuideRequired(email),
          ClimbSafeFeatureSet2Controller.getMemberHotelRequired(email), bookedItemNames,
          bookedItemQuantities));
      String toastMsg = "successfully removed all items";
      ClimbSafeFxmlView.makeText(toastMsg);

    }
  }

  /**
   * Clear All Update Boxes
   *
   * @author Zeyuan Fu and Yazdan Zinati
   */
  public void clearAllUpdateBoxes() {
    updateMemberName.clear();
    updateNrWeeks.clear();
    updateMemberPassword.clear();
    updateMemberEmergencyContact.clear();
    updateGuideRequired.setSelected(false);
    updateHotelRequired.setSelected(false);
    hotelRequired.setSelected(false);
  }

}
