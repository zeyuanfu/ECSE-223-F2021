package ca.mcgill.ecse.climbsafe.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Equipment;
import ca.mcgill.ecse.climbsafe.model.EquipmentBundle;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;

public class ClimbSafeFeatureSet4Controller {

  /**
   * Adds equipment to the system
   *
   * @param name         The name of the equipment
   * @param weight       The weight of the equipment
   * @param pricePerWeek The price for a one-week rental
   * @throws InvalidInputException if any of the parameters are invalid or there is an unsuccessful
   *                               attempt to add equipment to the system
   * @author Michelle Lee
   */
  public static void addEquipment(String name, int weight, int pricePerWeek)
      throws InvalidInputException {

    if (name == null || name.equals("")) {
      throw new InvalidInputException("The name must not be empty");
    }

    if (weight <= 0) {
      throw new InvalidInputException("The weight must be greater than 0");
    }

    if (pricePerWeek < 0) {
      throw new InvalidInputException("The price per week must be greater than or equal to 0");
    }

    if (Equipment.getWithName(name) instanceof Equipment && Equipment.getWithName(name) != null) {
      throw new InvalidInputException("The piece of equipment already exists");
    }

    if (EquipmentBundle.getWithName(name) instanceof EquipmentBundle
        && EquipmentBundle.getWithName(name) != null) {
      throw new InvalidInputException("The equipment bundle already exists");
    }
    try {
      new Equipment(name, weight, pricePerWeek, ClimbSafeApplication.getClimbSafe());
      ClimbSafePersistence.save();
    } catch (RuntimeException e){
      throw new RuntimeException();// change later
    }
    
  }

  /**
   * Updates equipment that is already in the system
   *
   * @param oldName         The previous name of the equipment to update
   * @param newName         The new name of the equipment to update
   * @param newWeight       The new weight of the equipment to update
   * @param newPricePerWeek The new price for a one week rental of the equipment to update
   * @throws InvalidInputException if any of the parameters are invalid or there is an unsuccessful
   *                               attempt to update an equipment in the system
   * @author Michelle Lee
   */
  public static void updateEquipment(String oldName, String newName, int newWeight,
      int newPricePerWeek) throws InvalidInputException {

    if (newName == null || newName.equals("")) {
      throw new InvalidInputException("The name must not be empty");
    }

    System.out.println(ClimbSafeApplication.getClimbSafe().getEquipment());
    if (!newName.equals(oldName) && Equipment.getWithName(newName) instanceof Equipment
        && Equipment.getWithName(newName) != null) {
      throw new InvalidInputException("The piece of equipment already exists");
    }

    if (EquipmentBundle.getWithName(newName) instanceof EquipmentBundle
        && EquipmentBundle.getWithName(newName) != null) {
      throw new InvalidInputException("An equipment bundle with the same name already exists");
    }

    if (newWeight <= 0) {
      throw new InvalidInputException("The weight must be greater than 0");
    }

    if (newPricePerWeek < 0) {
      throw new InvalidInputException("The price per week must be greater than or equal to 0");
    }

    Equipment toUpdate = (Equipment) Equipment.getWithName(oldName);
    if (toUpdate == null) {
      throw new InvalidInputException("The piece of equipment does not exist");
    }
    try {
      toUpdate.setName(newName);
      toUpdate.setWeight(newWeight);
      toUpdate.setPricePerWeek(newPricePerWeek);
      ClimbSafePersistence.save();
    } catch (RuntimeException e) {
      throw new RuntimeException(); //change this @TODO
    }
  }


  
  /**
   * get a list of equipment names in ClimbSafe
   * @return list of equipment names
   */
  public static List<String> getEquipmentNames() {
	    ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();
	    List<String> equipmentName = new ArrayList<String>();

	    for (Equipment e : climbSafe.getEquipment()) {
	      equipmentName.add((String) e.getName());
	    }

	    return equipmentName;
	  }
  
  /**
   * get the weight of a specific piece of equipment
   */ 
  public static String getEquipmentWeight(String EquipmentName) {
	    Equipment reqEquipment = (Equipment) Equipment.getWithName(EquipmentName);
	    int equipmentWeight = reqEquipment.getWeight();
	    return Integer.toString(equipmentWeight);
	  }
  
  /**
   * get the price of a specific piece of equipment
   */
  public static String getEquipmentPrice(String EquipmentName) {
	    Equipment reqEquipment = (Equipment) Equipment.getWithName(EquipmentName);
	    int equipmentPrice = reqEquipment.getPricePerWeek();
	    return Integer.toString(equipmentPrice);
	    }
}
