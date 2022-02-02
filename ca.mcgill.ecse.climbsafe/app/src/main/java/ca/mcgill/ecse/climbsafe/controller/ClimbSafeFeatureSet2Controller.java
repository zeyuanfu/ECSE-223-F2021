package ca.mcgill.ecse.climbsafe.controller;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.*;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;

import java.util.ArrayList;
import java.util.List;

public class ClimbSafeFeatureSet2Controller {

  private enum UserAction {
    REGISTER,
    UPDATE
  }

  /**
   * Registers a new member to the ClimbSafe application
   *
   * @param email            The new member's email address
   * @param password         The new member's password
   * @param name             The new member's name
   * @param emergencyContact The new member's emergency contact
   * @param nrWeeks          The number of weeks the member will stay
   * @param guideRequired    Whether the new member requires a guide
   * @param hotelRequired    Whether the new member requires a hotel
   * @param itemNames        The Items (Equipment or Bundle) the user books.
   * @param itemQuantities   The quantities for each of the items the member books. The values at an
   *                         index correspond to the quantity of the item at the same index in
   *                         itemNames
   * @throws InvalidInputException thrown if one of the inputs (email, password, ...) is invalid,
   *                               item if invalid, or user(member/admin/guide) with the email
   *                               already exists
   * @author Yazdan Zinati
   */
  public static void registerMember(String email, String password, String name,
      String emergencyContact, int nrWeeks, boolean guideRequired, boolean hotelRequired,
      List<String> itemNames, List<Integer> itemQuantities) throws InvalidInputException {

    validateInputs(email, password, name, emergencyContact, nrWeeks, itemNames,
        UserAction.REGISTER);

    Member newMember = new Member(email, password, name, emergencyContact, nrWeeks, guideRequired,
        hotelRequired, ClimbSafeApplication.getClimbSafe());

    try {
        for (int i = 0; i < itemNames.size(); i++) {
          newMember.addBookedItem(itemQuantities.get(i), ClimbSafeApplication.getClimbSafe(),
              BookableItem.getWithName(itemNames.get(i)));
        }
        ClimbSafePersistence.save();
      } catch (RuntimeException e) {
        throw new RuntimeException(); // change this
      }
  }

  /**
   * Updates an existing member in the ClimbSafe application with new values
   *
   * @param email               The member's email address
   * @param newPassword         The member's new password
   * @param newName             The member's new name
   * @param newEmergencyContact the member's new emergency contact
   * @param newNrWeeks          The new number of weeks the member will stay
   * @param newGuideRequired    Whether the updated member requires a guide
   * @param newHotelRequired    Whether the updated member requires a hotel
   * @param newItemNames        The Items (Equipment or Bundle) that the updated member books
   * @param newItemQuantities   The quantities for each of the items the updated member books. The
   *                            values at an index correspond to the quantity of the item at the
   *                            same index in itemNames
   * @throws InvalidInputException thrown if one of the inputs (email, password, ...) is invalid,
   *                               member does not exist, or item is invalid
   * @author Yazdan Zinati
   */
  public static void updateMember(String email, String newPassword, String newName,
      String newEmergencyContact, int newNrWeeks, boolean newGuideRequired,
      boolean newHotelRequired, List<String> newItemNames, List<Integer> newItemQuantities)
      throws InvalidInputException {

    validateInputs(email, newPassword, newName, newEmergencyContact, newNrWeeks, newItemNames,
        UserAction.UPDATE);

    Member toUpdate = (Member) Member.getWithEmail(email);
    
    List<BookedItem> oldBookedItems = toUpdate.getBookedItems();
    try {
      // update member fields
      toUpdate.setPassword(newPassword);
      toUpdate.setName(newName);
      toUpdate.setEmergencyContact(newEmergencyContact);
      toUpdate.setNrWeeks(newNrWeeks);
      toUpdate.setGuideRequired(newGuideRequired);
      toUpdate.setHotelRequired(newHotelRequired);
      
      // delete old booked items
      while (oldBookedItems.size() > 0) {
        oldBookedItems.get(0).delete();
      }
      // add new requested items with their corresponding quantities
      for (int i = 0; i < newItemNames.size(); i++) {
        toUpdate.addBookedItem(newItemQuantities.get(i), ClimbSafeApplication.getClimbSafe(),
            BookableItem.getWithName(newItemNames.get(i)));
      }
      ClimbSafePersistence.save();
    } catch (RuntimeException e) {
      throw new RuntimeException(); // change this
    }
  }

  /**
   * Validates that the parameters for registering or updating new members are valid
   *
   * @param email            Member's email
   * @param password         Member's password
   * @param name             Member's name
   * @param emergencyContact Member's emergencyContact
   * @param nrWeeks          The new number of weeks the member will stay
   * @param itemNames        The Items (Equipment or Bundle) that the updated member books
   * @param userAction       Indicates if validation is done for updating or registering a member
   * @throws InvalidInputException thrown if an input is invalid or violates constraints
   * @author Yazdan Zinati
   */
  private static void validateInputs(String email, String password, String name,
      String emergencyContact, int nrWeeks,
      List<String> itemNames, UserAction userAction) throws InvalidInputException {
    if (userAction == UserAction.UPDATE) {
        if (Member.getWithEmail(email) == null) {
            throw new InvalidInputException("Member not found");
        }
    }
    if (userAction == UserAction.REGISTER) {
      User user = User.getWithEmail(email);
      if (user != null) {
        // check if the existing user is a member or guide
          if (user instanceof Member) {
              throw new InvalidInputException("A member with this email already exists");
          }
          if (user instanceof Guide) {
              throw new InvalidInputException("A guide with this email already exists");
          }
      }
    }
      if (password == null || password.equals("")) {
          throw new InvalidInputException("The password cannot be empty");
      }
      if (name == null || name.equals("")) {
          throw new InvalidInputException("The name cannot be empty");
      }
      if (emergencyContact == null || emergencyContact.equals("")) {
          throw new InvalidInputException("The emergency contact cannot be empty");
      }
      if (nrWeeks <= 0 || nrWeeks > ClimbSafeApplication.getClimbSafe().getNrWeeks()) {
          throw new InvalidInputException(
              "The number of weeks must be greater than zero and less than or equal to the number of climbing weeks in the climbing season");
      }

      if (email.contains(" ")) {
          throw new InvalidInputException("The email must not contain any spaces");
      }
      if (email.equals("admin@nmc.nt")) {
          throw new InvalidInputException("The email entered is not allowed for members");
      }

    // validate email using regex
    // based on this answer https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
    String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
    java.util.regex.Matcher m = p.matcher(email);
    if (!m.matches()) {
      throw new InvalidInputException("Invalid email");
    }

      if (itemNames == null) {
          throw new InvalidInputException("Requested item not found");
      }
    for (String item : itemNames) {
      if (BookableItem.getWithName(item) == null) {
        throw new InvalidInputException("Requested item not found");
      }
    }
  }
  
  
  public static String getMemberName(String email) {
    NamedUser user = (NamedUser) Member.getWithEmail(email);
    return user.getName();
  }
  
  public static String getMemberPassword(String email) {
    return Member.getWithEmail(email).getPassword();
  }
  
  public static String getMemberEC(String email) {
    NamedUser user = (NamedUser) Member.getWithEmail(email);
    return user.getEmergencyContact();
  }
  
  public static int getMemberNrWeeks(String email) {
    Member member = (Member) Member.getWithEmail(email);
    return member.getNrWeeks();
  }
  
  public static boolean getMemberGuideRequired(String email) {
    Member member = (Member) Member.getWithEmail(email);
    return member.getGuideRequired();
  }
  
  public static boolean getMemberHotelRequired(String email) {
    Member member = (Member) Member.getWithEmail(email);
    return member.getHotelRequired();
  }
  
  public static List<String> getMemberItemNames(String email) {
    Member member = (Member) Member.getWithEmail(email);
    List<BookedItem> bookedItemList = member.getBookedItems();
    List<String> bookedItemNames = new ArrayList<String>();
    
    for (BookedItem bi: bookedItemList) {
      bookedItemNames.add(bi.getItem().getName());
    }
    
    return bookedItemNames;
  }
  
  public static List<Integer> getMemberItemQuantities(String email) {
    Member member = (Member) Member.getWithEmail(email);
    List<BookedItem> bookedItemList = member.getBookedItems();
    List<Integer> bookedItemQuantities = new ArrayList<Integer>();
    
    for (BookedItem bi: bookedItemList) {
      bookedItemQuantities.add(bi.getQuantity());
    }
    
    return bookedItemQuantities;
  }
}
