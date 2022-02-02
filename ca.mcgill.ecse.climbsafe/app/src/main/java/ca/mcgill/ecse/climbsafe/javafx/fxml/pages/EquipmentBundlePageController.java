package ca.mcgill.ecse.climbsafe.javafx.fxml.pages;

import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet5Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet6Controller;
import ca.mcgill.ecse.climbsafe.javafx.fxml.ClimbSafeFxmlView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.ListView;

public class EquipmentBundlePageController {

  @FXML
  private TabPane equipmentBundleTabPane;
  @FXML
  private ListView<String> equipmentBundleListView;
  @FXML
  private Button viewButton;
  @FXML
  private Button addButton;
  @FXML
  private Button editButton;
  @FXML
  private Button deleteButton;
  @FXML
  private TextField bundleNameTextField;
  @FXML
  private TextField bundleDiscountTextField;
  @FXML
  private TableView<EquipmentViewObject> bundleTableView;
  @FXML
  private TableColumn<EquipmentViewObject, String> equipmentNameTableColumn;
  @FXML
  private TableColumn<EquipmentViewObject, String> equipmentQuantityTableColumn;
  @FXML
  private Button updateBundleButton;
  @FXML
  private Button newBundleButton;

  private String toUpdateBundleName;

  /**
   * Initializes the equipment bundle pages
   *
   * @author Ze Yuan Fu
   */
  @FXML
  public void initialize() {
    bundleNameTextField.setText("");
    bundleDiscountTextField.setText("");
    bundleTableView.setEditable(true);

    equipmentBundleListView.setItems(ClimbSafeFeatureSet5Controller.getOLBundleNames());

    equipmentNameTableColumn
        .setCellValueFactory(new PropertyValueFactory<EquipmentViewObject, String>("name"));
    equipmentQuantityTableColumn
        .setCellValueFactory(new PropertyValueFactory<EquipmentViewObject, String>("quantity"));
    equipmentQuantityTableColumn
        .setCellFactory(TextFieldTableCell.<EquipmentViewObject>forTableColumn());

    equipmentQuantityTableColumn
        .setOnEditCommit(new EventHandler<CellEditEvent<EquipmentViewObject, String>>() {
          @Override
          public void handle(CellEditEvent<EquipmentViewObject, String> t) {
            ((EquipmentViewObject) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                .setQuantity(t.getNewValue());
          }
        });

    ClimbSafeFxmlView.getInstance().registerRefreshEvent(equipmentBundleListView);

  }

  /**
   * Displays a text box containing the details of the selected equipment bundle
   *
   * @author Ze Yuan Fu
   */
  @FXML
  public void viewButtonClicked(ActionEvent event) {

    String bundleName = equipmentBundleListView.getSelectionModel().getSelectedItem();

    if (bundleName == null) {
      ViewUtils.showError("Please select a valid bundle");
    } else {
      String bundleWeight = ClimbSafeFeatureSet5Controller.getBundleWeight(bundleName);
      String bundleDiscount = ClimbSafeFeatureSet5Controller.getBundleDiscount(bundleName);
      List<String> itemNames = ClimbSafeFeatureSet5Controller.getBundleItemNames(bundleName);
      List<String> itemQuantities =
          ClimbSafeFeatureSet5Controller.getBundleItemQuantities(bundleName);
      String message = "Bundle Name: " + bundleName + System.lineSeparator() + "Bundle Weight: "
          + bundleWeight + System.lineSeparator() + "Bundle Discount: " + bundleDiscount
          + System.lineSeparator() + "Bundle Items: " + System.lineSeparator();

      for (int i = 0; i < itemNames.size(); i++) {
        message = message + itemQuantities.get(i) + " " + itemNames.get(i) + System.lineSeparator();
      }

      ViewUtils.makePopupWindow(bundleName, message);
    }
  }

  /**
   * Makes the add/edit bundle page the active tab in the TabPane and clears all fields
   *
   * @author Ze Yuan Fu
   */
  @FXML
  public void addButtonClicked(ActionEvent event) {
    bundleNameTextField.setText("");
    bundleDiscountTextField.setText("");
    bundleTableView.setItems(ClimbSafeFeatureSet5Controller.getEmptyEquipmentViewObjectList());
    switchToEditBundlePage();
  }

  /**
   * Makes the add/edit bundle page the active tab in the TabPane and loads information about the
   * selected bundle
   *
   * @author Ze Yuan Fu
   */
  @FXML
  public void editButtonClicked(ActionEvent event) {
    String bundleName = equipmentBundleListView.getSelectionModel().getSelectedItem();

    if (bundleName == null) {
      ViewUtils.showError("Please select a valid bundle");
    } else {
      toUpdateBundleName = bundleName;
      bundleNameTextField.setText(bundleName);
      bundleDiscountTextField.setText(ClimbSafeFeatureSet5Controller.getBundleDiscount(bundleName));
      bundleTableView
          .setItems(ClimbSafeFeatureSet5Controller.getBundleEquipmentViewObjectList(bundleName));
    }

    switchToEditBundlePage();

  }

  /**
   * Deletes the selected bundle
   *
   * @author Ze Yuan Fu
   */
  @FXML
  public void deleteButtonClicked(ActionEvent event) {

    String bundleName = equipmentBundleListView.getSelectionModel().getSelectedItem();

    if (bundleName == null) {
      ViewUtils.showError("Please select a valid bundle");
    } else {
      ViewUtils
          .callController(() -> ClimbSafeFeatureSet6Controller.deleteEquipmentBundle(bundleName));
      equipmentBundleListView.setItems(ClimbSafeFeatureSet5Controller.getOLBundleNames());
      String toastMsg = "successfully deleted bundle " + "'" + bundleName + "'";
      ClimbSafeFxmlView.makeText(toastMsg);

    }

  }

  /**
   * Updates the selected bundle with the information in the text fields and table
   *
   * @author Ze Yuan Fu
   */
  @FXML
  public void updateBundleButtonClicked(ActionEvent event) {
    List<String> newEquipmentNames = new ArrayList<String>();
    List<Integer> newEquipmentQuantities = new ArrayList<Integer>();

    for (EquipmentViewObject evo : bundleTableView.getItems()) {
      int equipmentQuantity = Integer.parseInt(equipmentQuantityTableColumn.getCellData(evo));
      if (equipmentQuantity > 0) {
        newEquipmentNames.add(equipmentNameTableColumn.getCellData(evo));
        newEquipmentQuantities.add(equipmentQuantity);
      }
    }

    ViewUtils.callController(
        () -> ClimbSafeFeatureSet5Controller.updateEquipmentBundle(toUpdateBundleName,
            bundleNameTextField.getText(), Integer.parseInt(bundleDiscountTextField.getText()),
            newEquipmentNames, newEquipmentQuantities));

    equipmentBundleListView.setItems(ClimbSafeFeatureSet5Controller.getOLBundleNames());
    switchToBundleList();

    bundleTableView.setItems(ClimbSafeFeatureSet5Controller.getEmptyEquipmentViewObjectList());
    bundleNameTextField.setText("");
    bundleDiscountTextField.setText("");
    bundleTableView.setItems(ClimbSafeFeatureSet5Controller.getEmptyEquipmentViewObjectList());
    String toastMsg = "successfully updated bundle " + "'" + bundleNameTextField.getText() + "'";
    ClimbSafeFxmlView.makeText(toastMsg);

  }

  /**
   * Creates a new bundle with the information in the text fields and table
   *
   * @author Ze Yuan Fu
   */
  @FXML
  public void newBundleButtonClicked(ActionEvent event) {
    List<String> newEquipmentNames = new ArrayList<String>();
    List<Integer> newEquipmentQuantities = new ArrayList<Integer>();

    for (EquipmentViewObject evo : bundleTableView.getItems()) {
      int equipmentQuantity = Integer.parseInt(equipmentQuantityTableColumn.getCellData(evo));
      if (equipmentQuantity > 0) {
        newEquipmentNames.add(equipmentNameTableColumn.getCellData(evo));
        newEquipmentQuantities.add(equipmentQuantity);
      }
    }

    ViewUtils.callController(() -> ClimbSafeFeatureSet5Controller.addEquipmentBundle(
        bundleNameTextField.getText(), Integer.parseInt(bundleDiscountTextField.getText()),
        newEquipmentNames, newEquipmentQuantities));

    equipmentBundleListView.setItems(ClimbSafeFeatureSet5Controller.getOLBundleNames());
    switchToBundleList();

    bundleTableView.setItems(ClimbSafeFeatureSet5Controller.getEmptyEquipmentViewObjectList());
    bundleNameTextField.setText("");
    bundleDiscountTextField.setText("");
    bundleTableView.setItems(ClimbSafeFeatureSet5Controller.getEmptyEquipmentViewObjectList());
    String toastMsg = "successfully added bundle " + "'" + bundleNameTextField.getText() + "'";
    ClimbSafeFxmlView.makeText(toastMsg);

  }

  /**
   * Makes the bundle list page the active tab
   *
   * @author Ze Yuan Fu
   */
  @FXML
  public void switchToBundleList() {
    equipmentBundleTabPane.getSelectionModel().select(0);
  }

  /**
   * Makes the add/edit bundle page the active tab
   *
   * @author Ze Yuan Fu
   */
  @FXML
  public void switchToEditBundlePage() {
    equipmentBundleTabPane.getSelectionModel().select(1);
  }

}
