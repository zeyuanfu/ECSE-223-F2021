package ca.mcgill.ecse.climbsafe.controller;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Guide;
import ca.mcgill.ecse.climbsafe.model.Member;
import ca.mcgill.ecse.climbsafe.model.NamedUser;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;

public class ClimbSafeFeatureSet3Controller {

  /**
   * Registers a new member to the ClimbSafe application
   *
   * @param email            The new guide's email address
   * @param password         The new guide's password
   * @param name             The new guide's name
   * @param emergencyContact The new guide's emergency contact
   * @throws InvalidInputException thrown if one of the inputs (email, password, ...) is invalid,
   *                               (blank, empty, or user with same email exists already).
   * @author Magnus Gao
   */
  public static void registerGuide(String email, String password, String name,
      String emergencyContact) throws InvalidInputException {
    ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();

    // null checks
    if (email == null || password == null || name == null || emergencyContact == null) {
      return;
    }

    // validate the email
    if (email.equals("admin@nmc.nt")) {
      throw new InvalidInputException("Email cannot be admin@nmc.nt");
    }

    // Because getWithEmail get a nameUser, not a guide or member
    NamedUser existingUser = (NamedUser) Guide.getWithEmail(email);
    if (existingUser instanceof Guide) {
      throw new InvalidInputException("Email already linked to a guide account");
    }

    if (existingUser instanceof Member) {
      throw new InvalidInputException("Email already linked to a member account");
    }

    if (email.contains(" ")) {
      throw new InvalidInputException("Email must not contain any spaces");
    }

    if (email.isBlank()) {
      throw new InvalidInputException("Email cannot be empty");
    }

    if (!isValidEmailAddress(email)) {
      throw new InvalidInputException("Invalid email");
    }

    if (password.isBlank()) {
      throw new InvalidInputException("Password cannot be empty");
    }

    if (name.isBlank()) {
      throw new InvalidInputException("Name cannot be empty");
    }

    if (emergencyContact.isBlank()) {
      throw new InvalidInputException("Emergency contact cannot be empty");
    }
    
    try {
      // Create the Guide
      new Guide(email, password, name, emergencyContact, climbSafe);
      ClimbSafePersistence.save();
    } catch (RuntimeException e) {
      throw new RuntimeException();//change this
    }
    
    
  }

  /**
   * This method checks the validity of an email. Retrieved from stackOverFlow
   *
   * @param email: str
   * @return true if it's a valid email
   * @author Pujan @ StackOverFlow
   * @link https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
   */
  public static boolean isValidEmailAddress(String email) {
    String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
    java.util.regex.Matcher m = p.matcher(email);
    return m.matches();
  }

  /**
   * Registers a new member to the ClimbSafe application
   *
   * @param email               The guide's email address with which we want to update with
   * @param newPassword         The guide's new password
   * @param newName             The guide's new name
   * @param newEmergencyContact The guide's new emergency contact
   * @throws InvalidInputException thrown if one of the inputs (email, password, ...) is invalid,
   *                               (blank, empty, or user with same email exists already).
   * @author Magnus Gao
   */
  public static void updateGuide(String email, String newPassword, String newName,
      String newEmergencyContact) throws InvalidInputException {
    if (email == null || newPassword == null || newName == null || newEmergencyContact == null) {
      return;
    }

    Guide toUpdate = (Guide) Guide.getWithEmail(email);
    if (toUpdate == null) {
      throw new InvalidInputException("Guide with this email does not exist");
    }

    if (newPassword.isBlank()) {
      throw new InvalidInputException("Password cannot be empty");
    }

    if (newName.isBlank()) {
      throw new InvalidInputException("Name cannot be empty");
    }

    if (newEmergencyContact.isBlank()) {
      throw new InvalidInputException("Emergency contact cannot be empty");
    }
    try {
      toUpdate.setPassword(newPassword);
      toUpdate.setName(newName);
      toUpdate.setEmergencyContact(newEmergencyContact);
      ClimbSafePersistence.save();
    } catch (RuntimeException e) {
      throw new RuntimeException(); // change later      
    }
    
  }

}
