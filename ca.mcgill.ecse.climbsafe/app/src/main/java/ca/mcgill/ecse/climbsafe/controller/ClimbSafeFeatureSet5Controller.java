package ca.mcgill.ecse.climbsafe.controller;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.javafx.fxml.pages.EquipmentViewObject;
import ca.mcgill.ecse.climbsafe.model.*;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ClimbSafeFeatureSet5Controller {

  /**
   * Adds an equipment bundle with the given name, discount, equipment and equipment quantities to
   * the ClimbSafe application
   *
   * @param name                Name of bundle to be added
   * @param discount            The discount of the new bundle
   * @param equipmentNames      List of the names of the equipment to add
   * @param equipmentQuantities List of the quantities of each equipment to add
   * @throws InvalidInputException if the input parameters violate the program constraints, or if
   *                               one of the inputs is invalid
   * @author Ze Yuan Fu
   */
  public static void addEquipmentBundle(String name, int discount, List<String> equipmentNames,
      List<Integer> equipmentQuantities) throws InvalidInputException {

    var error = "";

    Set<String> checkDuplicates = new HashSet<String>();

    if (Objects.equals(name, "") || name == null) {
      throw new InvalidInputException("Equipment bundle name cannot be empty");
    }

    if (discount < 0) {
      throw new InvalidInputException("Discount must be at least 0");
    }

    if (discount > 100) {
      throw new InvalidInputException("Discount must be no more than 100");
    }

    if (equipmentNames.size() < 2) {
      throw new InvalidInputException(
          "Equipment bundle must contain at least two distinct types of equipment");
    }

    for (String s : equipmentNames) {
      if (Equipment.getWithName(s) == null) {
        throw new InvalidInputException("Equipment " + s + " does not exist");
      }
    }

    if (equipmentNames.size() == 2) {
      for (String s : equipmentNames) {
        if (!checkDuplicates.add(s)) {
          throw new InvalidInputException(
              "Equipment bundle must contain at least two distinct types of equipment");
        }
      }
    }

    for (Integer i : equipmentQuantities) {
      if (i < 1) {
        throw new InvalidInputException(
            "Each bundle item must have quantity greater than or equal to 1");
      }
    }

    try {
      EquipmentBundle newBundle = new EquipmentBundle(name, discount,
          ClimbSafeApplication.getClimbSafe());

      for (int i = 0; i < equipmentNames.size(); i++) {
        new BundleItem(equipmentQuantities.get(i), ClimbSafeApplication.getClimbSafe(), newBundle,
            (Equipment) Equipment.getWithName(equipmentNames.get(i)));

      }
      ClimbSafePersistence.save();
    } catch (RuntimeException e) {
      error = e.getMessage();
      if (error.startsWith("Cannot create due to duplicate name.")) {
        error = "A bookable item called " + name + " already exists";
      }
      throw new InvalidInputException(error);
    }
    

  }

  /**
   * Updates an existing equipment bundle in the ClimbSafe application with a new name, discount,
   * equipment and equipment quantities
   *
   * @param oldName                Name of bundle to be updated
   * @param newName                Name to update the bundle with
   * @param newDiscount            Discount to update the bundle with
   * @param newEquipmentNames      List of equipment to update the bundle with
   * @param newEquipmentQuantities List of quantities of equipment to update the bundle with
   * @throws InvalidInputException if the input parameters violate the program constraints
   * @author Ze Yuan Fu
   */
  public static void updateEquipmentBundle(String oldName, String newName, int newDiscount,
      List<String> newEquipmentNames, List<Integer> newEquipmentQuantities)
      throws InvalidInputException {

    var error = "";

    Set<String> checkDuplicates = new HashSet<>();
    List<Equipment> existingEquipment = ClimbSafeApplication.getClimbSafe().getEquipment();
    List<EquipmentBundle> existingBundles = ClimbSafeApplication.getClimbSafe().getBundles();

    if (!existingBundles.contains(EquipmentBundle.getWithName(oldName))) {
      throw new InvalidInputException("Equipment bundle " + oldName + " does not exist");
    }

    if (Objects.equals(newName, "") || newName == null) {
      throw new InvalidInputException("Equipment bundle name cannot be empty");
    }

    if (existingEquipment.contains(Equipment.getWithName(newName))) {
      throw new InvalidInputException("A bookable item called " + newName + " already exists");
    }

    if (existingBundles.contains(EquipmentBundle.getWithName(newName))
        && EquipmentBundle.getWithName(oldName) != EquipmentBundle.getWithName(newName)) {
      throw new InvalidInputException("A bookable item called " + newName + " already exists");
    }

    if (newDiscount < 0) {
      throw new InvalidInputException("Discount must be at least 0");
    }

    if (newDiscount > 100) {
      throw new InvalidInputException(error = "Discount must be no more than 100");
    }

    if (newEquipmentNames.size() < 2) {
      throw new InvalidInputException(
          "Equipment bundle must contain at least two distinct types of equipment");
    }

    for (String s : newEquipmentNames) {
      if (Equipment.getWithName(s) == null) {
        throw new InvalidInputException("Equipment " + s + " does not exist");
      }
    }

    if (newEquipmentNames.size() == 2) {
      for (String s : newEquipmentNames) {
        if (!checkDuplicates.add(s)) {
          throw new InvalidInputException(
              "Equipment bundle must contain at least two distinct types of equipment");
        }
      }
    }

    for (Integer i : newEquipmentQuantities) {
      if (i < 1) {
        throw new InvalidInputException(
            "Each bundle item must have quantity greater than or equal to 1");
      }
    }

    try {

      EquipmentBundle toUpdate = (EquipmentBundle) EquipmentBundle.getWithName(oldName);
      List<BundleItem> itemsToDelete = toUpdate.getBundleItems();

      toUpdate.setName(newName);
      toUpdate.setDiscount(newDiscount);

      while (itemsToDelete.size() > 0) {
        itemsToDelete.get(0).delete();
      }

      for (int i = 0; i < newEquipmentNames.size(); i++) {
        new BundleItem(newEquipmentQuantities.get(i), ClimbSafeApplication.getClimbSafe(), toUpdate,
            (Equipment) Equipment.getWithName(newEquipmentNames.get(i)));
      }
      ClimbSafePersistence.save();
    } catch (RuntimeException e) {
      error = e.getMessage();
      if (error.startsWith("Cannot create due to duplicate name.")) {
        error = "A bookable item called " + newName + " already exists";
      }
      throw new InvalidInputException(error);
    }
    

  }




  /**
   * Returns a list containing the names of the bundles currently in the ClimbSafe application
   * 
   * @author Ze Yuan Fu
   */
  public static List<String> getBundleNames() {
    ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();
    List<String> bundleNames = new ArrayList<String>();

    for (EquipmentBundle bundle : climbSafe.getBundles()) {
      bundleNames.add((String) bundle.getName());
    }

    return bundleNames;

  }

  /**
   * Returns a list containing the names of the equipment items currently in the ClimbSafe
   * application
   * 
   * @author Ze Yuan Fu
   */
  public static List<String> getEquipmentNames() {
    ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();
    List<String> equipmentNames = new ArrayList<String>();

    for (Equipment e : climbSafe.getEquipment()) {
      equipmentNames.add((String) e.getName());
    }

    return equipmentNames;
  }

  /**
   * Returns a string containing the weight of a bundle
   * 
   * @param bundleName Name of bundle to get the weight of
   * 
   * @author Ze Yuan Fu
   */
  public static String getBundleWeight(String bundleName) {
    EquipmentBundle bundle = (EquipmentBundle) EquipmentBundle.getWithName(bundleName);
    int totalWeight = 0;

    for (BundleItem item : bundle.getBundleItems()) {
      totalWeight = totalWeight + item.getEquipment().getWeight();
    }

    return Integer.toString(totalWeight);
  }

  /**
   * Returns a string containing the discount of a bundle
   * 
   * @param bundleName Name of bundle to get the discount of
   * 
   * @author Ze Yuan Fu
   */
  public static String getBundleDiscount(String bundleName) {
    EquipmentBundle bundle = (EquipmentBundle) EquipmentBundle.getWithName(bundleName);

    return Integer.toString(bundle.getDiscount());
  }

  /**
   * Returns a list containing the names of the items in a bundle
   * 
   * @param bundleName Name of bundle for which to get item names
   * 
   * @author Ze Yuan Fu
   */
  public static List<String> getBundleItemNames(String bundleName) {
    EquipmentBundle bundle = (EquipmentBundle) EquipmentBundle.getWithName(bundleName);
    List<String> itemNames = new ArrayList<String>();

    for (BundleItem item : bundle.getBundleItems()) {
      itemNames.add(item.getEquipment().getName());
    }
    return itemNames;
  }

  /**
   * Returns a list containing the quantities of the items in a bundle
   * 
   * @param bundleName Name of bundle for which to get item quantities
   * 
   * @author Ze Yuan Fu
   */
  public static List<String> getBundleItemQuantities(String bundleName) {
    EquipmentBundle bundle = (EquipmentBundle) EquipmentBundle.getWithName(bundleName);
    List<String> itemQuantities = new ArrayList<String>();

    for (BundleItem item : bundle.getBundleItems()) {
      itemQuantities.add(Integer.toString(item.getQuantity()));
    }
    return itemQuantities;
  }

  /**
   * Returns an EquipmentBundle object corresponding to the name passed as input
   * 
   * @param bundleName Name of bundle to return
   * 
   * @author Ze Yuan Fu
   */
  public static EquipmentBundle getEquipmentBundle(String bundleName) {
    return (EquipmentBundle) EquipmentBundle.getWithName(bundleName);
  }

  /**
   * Returns an ObservableList of the names of the equipment bundles in the ClimbSafe application
   * 
   * Used to populate the equipment bundle list page
   * 
   * @author Ze Yuan Fu
   */
  public static ObservableList<String> getOLBundleNames() {
    List<String> bundleNames = ClimbSafeFeatureSet5Controller.getBundleNames();
    return FXCollections.observableArrayList(bundleNames);
  }

  /**
   * Returns an ObservableList of EquipmentViewObjects containing the names of the equipment items
   * in the ClimbSafe application
   * 
   * Used to reset the equipment item and quantities section of the add/edit bundle page
   * 
   * @author Ze Yuan Fu
   */
  public static ObservableList<EquipmentViewObject> getEmptyEquipmentViewObjectList() {
    ObservableList<EquipmentViewObject> viewObjectList = FXCollections.observableArrayList();
    List<String> equipmentNameList = ClimbSafeFeatureSet5Controller.getEquipmentNames();

    for (String name : equipmentNameList) {
      viewObjectList.add(new EquipmentViewObject(name, "0"));
    }

    return viewObjectList;
  }

  /**
   * Returns an ObservableList of EquipmentViewObjects containing the names and quantities of the
   * equipment items in an equipment bundle
   * 
   * Used to load the details of equipment bundles to be edited
   * 
   * @param bundleName Name of bundle to get an ObservableList of
   * 
   * @author Ze Yuan Fu
   */
  public static ObservableList<EquipmentViewObject> getBundleEquipmentViewObjectList(
      String bundleName) {
    ObservableList<EquipmentViewObject> viewObjectList = FXCollections.observableArrayList();
    List<BundleItem> itemsInBundle =
        ClimbSafeFeatureSet5Controller.getEquipmentBundle(bundleName).getBundleItems();
    List<String> equipmentNameList = ClimbSafeFeatureSet5Controller.getEquipmentNames();

    for (String name : equipmentNameList) {
      boolean isPresent = false;
      for (BundleItem bi : itemsInBundle) {
        if (bi.getEquipment().getName().equals(name)) {
          isPresent = true;
          viewObjectList.add(new EquipmentViewObject(name, String.valueOf(bi.getQuantity())));
        }
      }
      if (isPresent == false) {
        viewObjectList.add(new EquipmentViewObject(name, "0"));
      }
    }

    return viewObjectList;
  }

  /**
   * Returns an ObservableList of the names of the equipment items in the ClimbSafe application
   * 
   * @author Ze Yuan Fu
   */
  public static ObservableList<String> getOLEquipmentNames() {
    List<String> equipmentName = ClimbSafeFeatureSet4Controller.getEquipmentNames();
    return FXCollections.observableArrayList(equipmentName);
  }

  /**
   * Returns an ObservableList of the names of the equipment items and bundles in the ClimbSafe
   * application
   * 
   * Used to populate the Bookable Item choice box in the Member page
   * 
   * @author Ze Yuan Fu
   */
  public static ObservableList<String> getAllBookableItems() {
    ObservableList<String> bookableItems = getOLEquipmentNames();
    bookableItems.addAll(getOLBundleNames());
    return bookableItems;
  }

}
