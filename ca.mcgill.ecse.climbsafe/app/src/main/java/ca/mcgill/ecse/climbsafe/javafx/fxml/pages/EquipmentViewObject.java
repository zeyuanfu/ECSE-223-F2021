/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.climbsafe.javafx.fxml.pages;

// line 3 "../../../../../../../ClimbSafeViewObjects.ump"
public class EquipmentViewObject {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //EquipmentViewObject Attributes
  private String name;
  private String quantity;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public EquipmentViewObject(String aName, String aQuantity) {
    name = aName;
    quantity = aQuantity;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName) {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setQuantity(String aQuantity) {
    boolean wasSet = false;
    quantity = aQuantity;
    wasSet = true;
    return wasSet;
  }

  public String getName() {
    return name;
  }

  public String getQuantity() {
    return quantity;
  }

  public void delete() {
  }


  public String toString() {
    return super.toString() + "[" +
        "name" + ":" + getName() + "," +
        "quantity" + ":" + getQuantity() + "]";
  }
}