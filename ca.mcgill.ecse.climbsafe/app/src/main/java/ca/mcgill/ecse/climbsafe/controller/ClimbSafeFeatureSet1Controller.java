package ca.mcgill.ecse.climbsafe.controller;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.Administrator;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Guide;
import ca.mcgill.ecse.climbsafe.model.Member;
import ca.mcgill.ecse.climbsafe.model.User;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;

import java.sql.Date;

public class ClimbSafeFeatureSet1Controller {

  /**
   * Sets up ClimbSafe general NMC information
   *
   * @param startDate           date of start of season
   * @param nrWeeks             number of weeks in the climbing season
   * @param priceOfGuidePerWeek price a guide charges per week
   * @throws InvalidInputException thrown if one of inputs (nrWeeks, priceOfGuidePerWeek) is
   *                               invalid
   * @author Enzo Calcagno
   */
  public static void setup(Date startDate, int nrWeeks, int priceOfGuidePerWeek)
      throws InvalidInputException {
    ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe(); //
    climbSafe.setStartDate(startDate);
    try {
        climbSafe.setStartDate(startDate);
        if (nrWeeks >= 0) {
          climbSafe.setNrWeeks(nrWeeks);
         
        } else {
          throw new InvalidInputException(
              "The number of climbing weeks must be greater than or equal to zero");
        } 
       
        
       
        if (priceOfGuidePerWeek >= 0) {
          climbSafe.setPriceOfGuidePerWeek(priceOfGuidePerWeek);
          ClimbSafePersistence.save();
        } else {
          throw new InvalidInputException(
              "The price of guide per week must be greater than or equal to zero");
        }
        ClimbSafePersistence.save(); // save all changes made
       } catch (RuntimeException e) {
    	   System.out.println("Persistence failed");
    	   System.out.println(e.getMessage());
           throw new RuntimeException(); // change this later on.
       }
  
  }

  /**
   * Deletes member from the system, while ensuring referential integrity
   *
   * @param email the member's email
   * @author Enzo Calcagno
   */
  public static void deleteMember(String email) {
    User currentUser = User.getWithEmail(email);
    try {
      if (currentUser instanceof Member) {
        currentUser.delete(); 
      }
      ClimbSafePersistence.save(); // save all changes made      
    } catch (RuntimeException e) {
      throw new RuntimeException(); // change this
    } 
  }

  /**
   * Deletes guide from the system, while ensuring referential integrity
   *
   * @param email the guide's email
   * @author Enzo Calcagno
   */
  public static void deleteGuide(String email) {
    User currentUser = User.getWithEmail(email);
    try {
      if (currentUser instanceof Guide) {
        currentUser.delete();
      }
      ClimbSafePersistence.save(); // save all changes made  
    } catch (RuntimeException e) {
      throw new RuntimeException();
    }
   

  }

  // this method needs to be implemented only by teams with seven team members
  public static void deleteHotel(String name) {
  }
}
