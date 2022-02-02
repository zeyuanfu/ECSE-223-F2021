package ca.mcgill.ecse.climbsafe.controller;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.Assignment;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Guide;
import ca.mcgill.ecse.climbsafe.model.Member;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;
import java.util.Arrays;
import java.util.List;

public class AssignmentController {

  /**
   * Confirm a member's payment processed outside the ClimbSafe application using the provided
   * authentication code.
   *
   * @param memberEmail The member's email
   * @param authCode    The authentication code used to validate the payment
   * @author Ze Yuan Fu, Yazdan Zinati, Michelle Lee, Enzo Calcagno, Magnus Gao, Elie Abdo
   */
  public static void confirmPayment(String memberEmail, String authCode) {
    // Get the user from userEmail
    Member curMember = (Member) Member.getWithEmail(memberEmail);

    if (curMember == null) {
      throw new RuntimeException("Member with email address " + memberEmail + " does not exist");
    }
    if (authCode.isEmpty()) {
      throw new RuntimeException("Invalid authorization code");
    }

    try {
      // The curMember has a state machine
      curMember.getAssignment().confirmPayment(authCode); // calls the trigger in model
      ClimbSafePersistence.save();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Cancel a member's trip and reflect the cancellation in the corresponding assignment.
   *
   * @param memberEmail The member's email
   * @author Ze Yuan Fu, Yazdan Zinati, Michelle Lee, Enzo Calcagno, Magnus Gao, Elie Abdo
   */
  public static void cancelTrip(String memberEmail) {
    Member curMember = (Member) Member.getWithEmail(memberEmail);
    if (curMember == null) {
      throw new RuntimeException("Member with email address " + memberEmail + " does not exist");
    }
    try {
      curMember.getAssignment().cancelTrip(); // calls the SM trigger
      ClimbSafePersistence.save();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Start trips that correspond to the current week.
   *
   * @param weekNumber The starting week's number
   * @author Ze Yuan Fu, Yazdan Zinati, Michelle Lee, Enzo Calcagno, Magnus Gao, Elie Abdo
   */
  public static void startTrips(String weekNumber) {
    try {
      for (Assignment trip : ClimbSafeApplication.getClimbSafe().getAssignments()) {
        if (trip.getStartWeek() == Integer.parseInt(weekNumber)) {
          trip.startTrip(Integer.parseInt(weekNumber)); // calls the SM trigger
        }
      }
      ClimbSafePersistence.save();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Finish a member's trip by reflecting this change in the corresponding assignment
   *
   * @param memberEmail The member's email
   * @author Ze Yuan Fu, Yazdan Zinati, Michelle Lee, Enzo Calcagno, Magnus Gao, Elie Abdo
   */
  public static void finishTrip(String memberEmail) {
    Member curMember = (Member) Member.getWithEmail(memberEmail);

    if (curMember == null) {
      throw new RuntimeException("Member with email address " + memberEmail + " does not exist");
    }

    try {
      curMember.getAssignment().finishTrip(); // calls the trigger SVM method
      ClimbSafePersistence.save();
    } catch (Exception e) {

      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Initiate the assignment process by assigning mountain guides and climbing weeks to members. If
   * a member did not request a guide, the member is always assigned to the earliest weeks in the
   * climbing season. If a member has requested a guide, they will be assigned a guide and climbing
   * weeks based on guide availability.
   *
   * @throws Exception If one or more member couldn't be assigned a guide if they requested one.
   * @author Ze Yuan Fu, Yazdan Zinati, Michelle Lee, Enzo Calcagno, Magnus Gao, Elie Abdo
   */
  public static void initiateAssignment() throws Exception {
    ClimbSafe climbsafe = ClimbSafeApplication.getClimbSafe();

    List<Guide> guides = climbsafe.getGuides();
    List<Member> members = climbsafe.getMembers();
    int climbingWeeks = climbsafe.getNrWeeks();

    // initially, all guides are available for the whole climbing week
    int[] guideAvailableWeeks = new int[guides.size()];
    Arrays.fill(guideAvailableWeeks, climbingWeeks);
    try {
   // loop through all guides
      for (int i = 0; i < guides.size(); i++) {
        Guide guide = guides.get(i);

        // loop through all members
        for (Member member : members) {
          int nrWeeks = member.getNrWeeks();

          // skip assignment if member already has an assignment
          if (member.hasAssignment()) {
            continue;
          }

          // if a member did not request a guide, they are assigned the earliest weeks in the season
          if (!member.isGuideRequired()) {
            climbsafe.addAssignment(1, nrWeeks, member);
            continue;
          }

          // if the guide is available for the number of weeks requested by the member
          if (nrWeeks <= guideAvailableWeeks[i]) {
            int startWeek = climbingWeeks - guideAvailableWeeks[i] + 1;
            int endWeek = startWeek + nrWeeks - 1;
            Assignment assignment = climbsafe.addAssignment(startWeek, endWeek, member);
            assignment.setGuide(guide);

            // update guide availability
            guideAvailableWeeks[i] -= nrWeeks;
          }
        }
      }
      ClimbSafePersistence.save(); // save all changes made  
    } catch (RuntimeException e) {
      throw new RuntimeException(); // TODO: change this
    }
    
    // check if all members have been assigned
    for (Member member : members) {
      if (!member.hasAssignment()) {
        throw new Exception("Assignments could not be completed for all members");
      }
    }
  }
}
