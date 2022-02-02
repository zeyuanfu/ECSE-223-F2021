package ca.mcgill.ecse.climbsafe.controller;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.*;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;
import java.util.ArrayList;
import java.util.List;

public class ClimbSafeFeatureSet6Controller {

  /**
   * this method deletes Equipment if input is valid
   *
   * @param name finds the equipment to delete from its name
   * @throws InvalidInputException if the equipment is in a bundle and can't be deleted
   * @author Elie Dimitri Abdo
   */
  public static void deleteEquipment(String name) throws InvalidInputException {
    // Delete a piece of equipment that does not exist in the system
    BookableItem curEquipment = Equipment.getWithName(name);
    if (curEquipment == null || curEquipment instanceof EquipmentBundle) {
      return; // does not do anything
    }

    // Unsuccessfully delete a piece of equipment that is in an existing bundle
    Equipment equipmentToDelete = (Equipment) curEquipment;
    if (equipmentToDelete.getBundleItems().size() > 0) {
      throw new InvalidInputException(
          "The piece of equipment is in a bundle and cannot be deleted");
    }

    // Successfully delete a piece of equipment
    try {
      equipmentToDelete.delete();
      ClimbSafePersistence.save();
    } catch (RuntimeException e) {
      throw new RuntimeException(); // TODO: change
    }
    
  }

  /**
   * this method deletes EquipmentBundle if input is valid
   *
   * @param name finds EquipmentBundle to delete from name
   * @ Elie Dimitri Abdo
   */
  public static void deleteEquipmentBundle(String name) {
    // Delete a piece of equipment that does not exist in the system
    BookableItem curEquipmentBundle = EquipmentBundle.getWithName(name);
    if (curEquipmentBundle == null || curEquipmentBundle instanceof Equipment) {
      return; // does not do anything
    }

    // Successfully delete equipment bundle that has been requested by a member
    EquipmentBundle equipmentBundleToDelete = (EquipmentBundle) curEquipmentBundle;
    
    try {
      // Successfully delete equipment bundle
      equipmentBundleToDelete.delete();
      ClimbSafePersistence.save();
    } catch (RuntimeException e) {
      throw new RuntimeException(); // TODO: change
    }
  }

  /**
   * this method views the assignments
   *
   * @return list toReturn with all the assignments
   * @author Magnus Gao
   * @author Elie Dimitri Abdo
   */
  public static List<TOAssignment> getAssignments() {
    ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();
    List<Assignment> assignments = climbSafe.getAssignments();

    List<TOAssignment> toReturn = new ArrayList<>();

    // For each of the assignments, get the appropriate values
    for (Assignment assignment : assignments) {
      String memberEmail = assignment.getMember().getEmail();
      String memberName = assignment.getMember().getName();

      Guide guide = assignment.getGuide();
      String status = assignment.getAssignmentStatusFullName();
      String guideEmail = null;
      String guideName = null;
      int guideOrNot = 1;

      if (guide != null) {
        guideEmail = guide.getEmail();
        guideName = guide.getName();
      } else {
        guideOrNot = 0;
      }

      Hotel hotel = assignment.getHotel();
      String hotelName = null;
      if (hotel != null) {
        hotelName = hotel.getName();
      }
      int startWeek = assignment.getStartWeek();
      int endWeek = assignment.getEndWeek();
      int numberOfWeeks = assignment.getMember().getNrWeeks();

      int totalCostForGuide = climbSafe.getPriceOfGuidePerWeek() * numberOfWeeks * guideOrNot;

      // Get all the member's bookedItems
      int totalCostForEquipment = 0;
      Member curMember = assignment.getMember();
      List<BookedItem> bookedItems = curMember.getBookedItems();

      if (!bookedItems.isEmpty()) {

        // For all the booked items
        for (BookedItem bookedItem : bookedItems) {
          BookableItem bookableItem = bookedItem.getItem();

          // If it's a equipment, it's the cost * quantity
          if (bookableItem instanceof Equipment) {
            totalCostForEquipment += ((Equipment) bookableItem).getPricePerWeek()
                * bookedItem.getQuantity() * numberOfWeeks;

          } else {
            // If it's a bundle, we get the associated bundleItems, get quantity, and apply them discount
            EquipmentBundle eb = (EquipmentBundle) bookableItem;
            List<BundleItem> bundleItems = eb.getBundleItems();
            for (BundleItem bundleItem : bundleItems) {
              if (guide == null) {
                totalCostForEquipment += bundleItem.getEquipment().getPricePerWeek()
                    * bookedItem.getQuantity() * bundleItem.getQuantity() * numberOfWeeks;
              } else {
                totalCostForEquipment += bundleItem.getEquipment().getPricePerWeek()
                    * bookedItem.getQuantity() * bundleItem.getQuantity() * numberOfWeeks
                    * (1 - (double) eb.getDiscount() / 100);
              }
            }
          }
        }
      }
      String authCode = assignment.getAuthCode();
      int refund = assignment.getDiscount();

      TOAssignment temp = new TOAssignment(memberEmail, memberName, guideEmail, guideName,
          hotelName, startWeek, endWeek, totalCostForGuide, totalCostForEquipment, refund, authCode, status);
      
      //public TOAssignment(String aMemberEmail, String aMemberName, String aGuideEmail, String aGuideName, String aHotelName, int aStartWeek, int aEndWeek, int aTotalCostForGuide, int aTotalCostForEquipment, int aDiscount, String aAuthCode, String aStatus)
      
      toReturn.add(temp);
    }

    return toReturn;
  }

  /**
   * this method returns the guides' names
   *
   * @return list toReturn with Strings of Member's Names and Email, separated by a ","
   * @author Magnus Gao
   */
  public static List<String> getGuidesName() {
    ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();
    List<Guide> memberList = climbSafe.getGuides();
    List<String> toReturn = new ArrayList<>();

    for(Guide m : memberList){
      toReturn.add(m.getName() + ", " + m.getEmail());
    }

    return toReturn;
  }

  /**
   * this method returns name of the members
   *
   * @return list toReturn with Strings of Member's Names
   */
  public static List<String> getMembersEmails() {
    ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();
    List<Member> memberList = climbSafe.getMembers();
    List<String> toReturn = new ArrayList<>();

    for(Member m : memberList){
      toReturn.add(m.getEmail());
    }

    return toReturn;
  }
}
