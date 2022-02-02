package ca.mcgill.ecse.climbsafe.javafx.fxml.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet4Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet5Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet6Controller;
import ca.mcgill.ecse.climbsafe.javafx.fxml.ClimbSafeFxmlView;
import javafx.event.ActionEvent;
import javafx.scene.control.TabPane;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;

public class EquipmentPageController {

  @FXML
  private TabPane equipmentTabPane;
  @FXML
  private Tab tab1;
  @FXML
  private ListView<String> equipmentListView;
  @FXML
  private Button addRentedEq;
  @FXML
  private Button editRentedEq;
  @FXML
  private Button deleteRentedEq;
  @FXML
  private TextField totalWeight;
  @FXML
  private TextField totalPrice;
  @FXML
  private Button saveRentedEq;
  @FXML
  private Tab tab2;
  @FXML
  private TextField eqNameText;
  @FXML
  private TextField newEqNameText;
  @FXML
  private TextField weight;
  @FXML
  private TextField price;
  @FXML
  private Button addEq;
  @FXML
  private Button updateEq;

  private String newEquName;
  private String weightString;
  private String priceString;
  private int newWeight;
  private int newPrice;


  /**
   * refreshes the equipment list page and resets the fields in the add/edit page
   *
   * @author Michelle Lee
   */
  @FXML
  public void initialize() {

    eqNameText.setText("");
    newEqNameText.setText("");
    weight.setText("");
    price.setText("");

    equipmentListView.addEventHandler(ClimbSafeFxmlView.REFRESH_EVENT, e -> {
      equipmentListView.setItems(ClimbSafeFeatureSet5Controller.getOLEquipmentNames());
    });

    ClimbSafeFxmlView.getInstance().registerRefreshEvent(equipmentListView);
  }

  /**
   * user clicks on the button to add an equipment to the list
   *
   * @param event brings the user to the add/edit page
   * @author Michelle Lee
   */
  @FXML
  public void addRentedEq(ActionEvent event) {

    initialize();
    switchToAddEdit();
  }

  /**
   * user has to select an equipment to edit it
   *
   * @param event brings the user to the add/edit page sets the fields with their corresponding
   *              information
   * @author Michelle Lee
   */
  @FXML
  public void editRentedEq(ActionEvent event) {

    String equipmentName = equipmentListView.getSelectionModel().getSelectedItem();

    if (equipmentName == null) {
      ViewUtils.showError("Please select a piece of equipment to edit");
    } else {
      String equipmentWeight = ClimbSafeFeatureSet4Controller.getEquipmentWeight(equipmentName);
      String equipmentPrice = ClimbSafeFeatureSet4Controller.getEquipmentPrice(equipmentName);
      eqNameText.setText(equipmentName);
      newEqNameText.setText("");
      weight.setText(equipmentWeight);
      price.setText(equipmentPrice);
      switchToAddEdit();
    }
  }

  /**
   * user has to select an equipment to delete it
   *
   * @param event deletes the equipment from the list
   * @author Michelle Lee
   */
  public void deleteEq(ActionEvent event) {

    String equipmentName = equipmentListView.getSelectionModel().getSelectedItem();

    if (equipmentName == null) {
      ViewUtils.showError("Please select a piece of equipment to delete");
    } else {
      if (ViewUtils
          .successful(() -> ClimbSafeFeatureSet6Controller.deleteEquipment(equipmentName))) {
        equipmentListView.getItems().remove(equipmentName);
        String toastMsg = "successfully deleted equipment " + equipmentName;
        ClimbSafeFxmlView.makeText(toastMsg);
      }
    }
  }


  /**
   * user enters the information for the equipment to add on the list name, weight and price of the
   * equipment
   *
   * @param event adds the equipment and its associated info to the list
   * @author Michelle Lee
   */
  @FXML
  public void addEq(ActionEvent event) {

    String equipmentName = eqNameText.getText();
    weightString = weight.getText();
    priceString = price.getText();

    newWeight = Integer.parseInt(weightString);
    newPrice = Integer.parseInt(priceString);

    ViewUtils.successful(
        () -> ClimbSafeFeatureSet4Controller.addEquipment(equipmentName, newWeight, newPrice));

    switchToEquipment();
    initialize();
    String toastMsg = "successfully added equipment " + "'" + equipmentName + "'";
    ClimbSafeFxmlView.makeText(toastMsg);

  }


  /**
   * user enters the new name, new weight or new price of the equipment to update new name field
   * cannot be empty same name can be entered in the new name field if it isnt changed
   *
   * @param event updates the equipment and its associated info to the list
   * @author Michelle Lee
   */
  @FXML
  public void updateEq(ActionEvent event) {

    String equipmentName = equipmentListView.getSelectionModel().getSelectedItem();

    newEquName = newEqNameText.getText();

    weightString = weight.getText();
    priceString = price.getText();

    newWeight = Integer.parseInt(weightString);
    newPrice = Integer.parseInt(priceString);

    ViewUtils.successful(() -> ClimbSafeFeatureSet4Controller.updateEquipment(equipmentName,
        newEquName, newWeight, newPrice));

    switchToEquipment();
    initialize();
    String toastMsg = "successfully updated equipment " + "'" + newEquName + "'";
    ClimbSafeFxmlView.makeText(toastMsg);

  }


  /**
   * Event Listener on Button[#addRentedEq].onAction and Button[#editRentedEq].onAction
   * <p>
   * switches to the add/edit page
   *
   * @author Michelle Lee
   */
  @FXML
  public void switchToAddEdit() {
    equipmentTabPane.getSelectionModel().select(tab2);
  }

  /**
   * Event Listener on Button[#addEq].onAction and Button[#editEq].onAction
   * <p>
   * switches to the equipment list tab
   *
   * @author Michelle Lee
   */
  @FXML
  public void switchToEquipment() {
    equipmentTabPane.getSelectionModel().select(tab1);
  }
}

