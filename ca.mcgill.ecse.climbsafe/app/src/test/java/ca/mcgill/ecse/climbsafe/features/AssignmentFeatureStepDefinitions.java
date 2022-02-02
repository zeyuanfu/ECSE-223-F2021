package ca.mcgill.ecse.climbsafe.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.controller.AssignmentController;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet6Controller;
import ca.mcgill.ecse.climbsafe.controller.TOAssignment;
import ca.mcgill.ecse.climbsafe.model.Assignment;
import ca.mcgill.ecse.climbsafe.model.BookableItem;
import ca.mcgill.ecse.climbsafe.model.BundleItem;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Equipment;
import ca.mcgill.ecse.climbsafe.model.EquipmentBundle;
import ca.mcgill.ecse.climbsafe.model.Guide;
import ca.mcgill.ecse.climbsafe.model.Member;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AssignmentFeatureStepDefinitions {

  private ClimbSafe climbSafe;
  private String error;

  /**
   * @param dataTable datatable
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Given("the following ClimbSafe system exists:")
  public void the_following_climb_safe_system_exists(io.cucumber.datatable.DataTable dataTable) {

    this.error = null;

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    this.climbSafe = ClimbSafeApplication.getClimbSafe();
    for (Map<String, String> row : rows) {

      Date startDate = java.sql.Date.valueOf(row.get("startDate"));
      int nrWeeks = Integer.parseInt(row.get("nrWeeks"));
      int weeklyGuidePrice = Integer.parseInt(row.get("priceOfGuidePerWeek"));

      this.climbSafe.setStartDate(startDate);
      this.climbSafe.setNrWeeks(nrWeeks);
      this.climbSafe.setPriceOfGuidePerWeek(weeklyGuidePrice);
    }
  }

  /**
   * @param dataTable datatable
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Given("the following pieces of equipment exist in the system:")
  public void the_following_pieces_of_equipment_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      String name = row.get("name");
      int weight = Integer.parseInt(row.get("weight"));
      int weeklyPrice = Integer.parseInt(row.get("pricePerWeek"));

      new Equipment(name, weight, weeklyPrice, this.climbSafe);
    }
  }

  /**
   * @param dataTable datatable
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Given("the following equipment bundles exist in the system:")
  public void the_following_equipment_bundles_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      String name = row.get("name");
      int discount = Integer.parseInt(row.get("discount"));
      List<Integer> quantity = Arrays.stream(row.get("quantity").split(",")).map(String::trim)
          .mapToInt(Integer::parseInt).boxed().toList();
      List<String> items = Arrays.asList(row.get("items").split(","));

      EquipmentBundle equipmentBundle = new EquipmentBundle(name, discount, this.climbSafe);

      for (int i = 0; i < items.size(); i++) {
        new BundleItem(quantity.get(i), this.climbSafe, equipmentBundle,
            (Equipment) Equipment.getWithName(items.get(i)));
      }
    }
  }

  /**
   * @param dataTable datatable
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Given("the following guides exist in the system:")
  public void the_following_guides_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(); // retrieve the information from the
    // dataTable

    for (Map<String, String> row : rows) { // Create guides with the informations provided in the
      // dataTable
      String email = row.get("email");
      String password = row.get("password");
      String name = row.get("name");
      String emergencyContact = row.get("emergencyContact");
      climbSafe.addGuide(email, password, name, emergencyContact);
    }
  }

  /**
   * @param dataTable datatable
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Given("the following members exist in the system:")
  public void the_following_members_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {

      String email = row.get("email");
      String name = row.get("name");
      String password = row.get("password");
      String emergencyContact = row.get("emergencyContact");
      int nrWeeks = Integer.parseInt(row.get("nrWeeks"));
      boolean guideRequired = Boolean.parseBoolean(row.get("guideRequired"));
      boolean hotelRequired = Boolean.parseBoolean(row.get("hotelRequired"));
      List<Integer> requestedQuantities = Arrays.stream(row.get("bookedItemQuantities").split(","))
          .map(String::trim).mapToInt(Integer::parseInt).boxed().toList();
      List<String> bookableItems = Arrays.asList(row.get("bookedItems").split(","));

      Member member = new Member(email, password, name, emergencyContact, nrWeeks, guideRequired,
          hotelRequired, this.climbSafe);

      for (int i = 0; i < bookableItems.size(); i++) {
        BookableItem bookableItem = BookableItem.getWithName(bookableItems.get(i));
        member.addBookedItem(requestedQuantities.get(i), this.climbSafe, bookableItem);
      }

    }
  }

  /**
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @When("the administrator attempts to initiate the assignment process")
  public void the_administrator_attempts_to_initiate_the_assignment_process() {
    try {
      AssignmentController.initiateAssignment();
    } catch (Exception e) {
      error = e.getMessage();
    }
  }

  /**
   * @param dataTable datatable
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Then("the following assignments shall exist in the system:")
  public void the_following_assignments_shall_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    List<TOAssignment> assignmentList = ClimbSafeFeatureSet6Controller.getAssignments();
    System.out.println(assignmentList);
    TOAssignment particularAssignment = null;

    for (Map<String, String> row : rows) {
      String email = row.get("memberEmail");

      for (TOAssignment assignment : assignmentList) {

        if (assignment.getMemberEmail().equals(email)) {
          particularAssignment = assignment;
          break;
        }
      }

      assertNotNull(particularAssignment);
      // todo: what if guideEmail is "", and getGuideEmail() returns null?
      assertEquals(row.get("guideEmail"), particularAssignment.getGuideEmail());
      assertEquals(Integer.parseInt(row.get("startWeek")), particularAssignment.getStartWeek());
      assertEquals(Integer.parseInt(row.get("endWeek")), particularAssignment.getEndWeek());

    }

  }

  /**
   * @param memberEmail Member's email for whom to check assignment state
   * @param State       Expected assignment state
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Then("the assignment for {string} shall be marked as {string}")
  public void the_assignment_for_shall_be_marked_as(String memberEmail, String State) {

    Member curMember = (Member) Member.getWithEmail(memberEmail);
    Assignment curAssignment = curMember.getAssignment();
    assertEquals(State, curAssignment.getAssignmentStatus().toString());

  }

  /**
   * @param expectedNumber Expected number of assignments in the system
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Then("the number of assignments in the system shall be {string}")
  public void the_number_of_assignments_in_the_system_shall_be(String expectedNumber) {

    assertEquals(Integer.parseInt(expectedNumber),
        ClimbSafeFeatureSet6Controller.getAssignments().size());

  }

  /**
   * @param expectedError Error expected to be raised
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Then("the system shall raise the error {string}")
  public void the_system_shall_raise_the_error(String expectedError) {

    assertEquals(expectedError, error);

  }

  /**
   * @param dataTable datatable
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Given("the following assignments exist in the system:")
  public void the_following_assignments_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      //todo:  can we have guides and members with the same email? I think we can't but just to be sure
      Member member = (Member) Member.getWithEmail(row.get("memberEmail"));
      Guide guide = (Guide) Guide.getWithEmail(row.get("guideEmail"));
      int startWeek = Integer.parseInt(row.get("startWeek"));
      int endWeek = Integer.parseInt(row.get("endWeek"));

      Assignment newAssignment = new Assignment(startWeek, endWeek, member, this.climbSafe);
      newAssignment.setGuide(guide);

    }
  }

  /**
   * @param memberEmail Paying member's email
   * @param authCode    Authentication code of the payment for verification
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @When("the administrator attempts to confirm payment for {string} using authorization code {string}")
  public void the_administrator_attempts_to_confirm_payment_for_using_authorization_code(
      String memberEmail, String authCode) {
    try {
      AssignmentController.confirmPayment(memberEmail, authCode);
    } catch (Exception e) {
      error = e.getMessage();
    }
  }

  /**
   * @param memberEmail Paying member's email
   * @param authCode    Authentication code of the payment for verification
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Then("the assignment for {string} shall record the authorization code {string}")
  public void the_assignment_for_shall_record_the_authorization_code(String memberEmail,
      String authCode) {
    List<Assignment> assignmentList = climbSafe.getAssignments();
    Assignment particularAssignment = null;

    // get the assignment with the userEmail
    for (Assignment assignment : assignmentList) {

      if (assignment.getMember().getEmail().equals(memberEmail)) {
        particularAssignment = assignment;
      }
    }

    assert particularAssignment != null;
    assertEquals(authCode, particularAssignment.getAuthCode());
  }

  /**
   * @param memberEmail Member's email
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Then("the member account with the email {string} does not exist")
  public void the_member_account_with_the_email_does_not_exist(String memberEmail) {

    assertNull(Member.getWithEmail(memberEmail));
  }

  /**
   * @param expectedNumber expected number of members in the system
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Then("there are {string} members in the system")
  public void there_are_members_in_the_system(String expectedNumber) {

    assertEquals(Integer.parseInt(expectedNumber), this.climbSafe.getMembers().size());
  }

  /**
   * @param expectedError Error expected to be raised
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Then("the error {string} shall be raised")
  public void the_error_shall_be_raised(String expectedError) {
    assertEquals(expectedError, error);
  }

  /**
   * @param memberEmail The member's email
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @When("the administrator attempts to cancel the trip for {string}")
  public void the_administrator_attempts_to_cancel_the_trip_for(String memberEmail) {
    try {
      AssignmentController.cancelTrip(memberEmail);
    } catch (Exception e) {
      error = e.getMessage();
    }
  }

  /**
   * @param memberEmail The member's email
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Given("the member with {string} has paid for their trip")
  public void the_member_with_has_paid_for_their_trip(String memberEmail) {
    Member curMember = (Member) Member.getWithEmail(memberEmail);
    curMember.getAssignment().confirmPayment(null);
  }

  /**
   * @param memberEmail    The email of the member to issue refund for
   * @param expectedRefund Expected refund on user's account
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Then("the member with email address {string} shall receive a refund of {string} percent")
  public void the_member_with_email_address_shall_receive_a_refund_of_percent(String memberEmail,
      String expectedRefund) {
    Member curMember = (Member) Member.getWithEmail(memberEmail);
    Assignment curAssignment = curMember.getAssignment();
    int expectedRefundInt = Integer.parseInt(expectedRefund);

    assertEquals(expectedRefundInt, curAssignment.getDiscount());
  }

  /**
   * @param memberEmail The member's email for whom to start trip
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Given("the member with {string} has started their trip")
  public void the_member_with_has_started_their_trip(String memberEmail) {
    // todo: Call the state machine method
    Member curMember = (Member) Member.getWithEmail(memberEmail);
    curMember.getAssignment().confirmPayment(null);
    curMember.getAssignment().startTrip(curMember.getAssignment().getStartWeek());
  }

  /**
   * @param memberEmail The member's email for whom to finish trip
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @When("the administrator attempts to finish the trip for the member with email {string}")
  public void the_administrator_attempts_to_finish_the_trip_for_the_member_with_email(
      String memberEmail) {
    try {
      AssignmentController.finishTrip(memberEmail);
    } catch (Exception e) {
      error = e.getMessage();
    }
  }

  /**
   * @param memberEmail The email of the member to ban
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Given("the member with {string} is banned")
  public void the_member_with_is_banned(String memberEmail) {
    Member curMember = (Member) Member.getWithEmail(memberEmail);

    // start trip without pay will get the member banned
    curMember.getAssignment().startTrip(curMember.getAssignment().getStartWeek());
  }

  /**
   * @param memberEmail   Member's email
   * @param expectedState Expected state of the member's assignment
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Then("the member with email {string} shall be {string}")
  public void the_member_with_email_shall_be(String memberEmail, String expectedState) {
    Member curMember = (Member) Member.getWithEmail(memberEmail);
    Assignment curAssignment = curMember.getAssignment();
    assertEquals(expectedState, curAssignment.getAssignmentStatusFullName());
  }

  /**
   * @param weekNumber Week number to start
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @When("the administrator attempts to start the trips for week {string}")
  public void the_administrator_attempts_to_start_the_trips_for_week(String weekNumber) {
    try {
      AssignmentController.startTrips(weekNumber);
    } catch (Exception e) {
      error = e.getMessage();
    }
  }

  /**
   * @param memberEmail Member's email for whom to cancel trip
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Given("the member with {string} has cancelled their trip")
  public void the_member_with_has_cancelled_their_trip(String memberEmail) {
    Member curMember = (Member) Member.getWithEmail(memberEmail);
    curMember.getAssignment().cancelTrip();
  }

  /**
   * @param memberEmail Member's email for whom to finish trip
   * @author Enzo Calcagno, Michelle Lee, Yazdan Zinati, Elie Abdo, Ze Yuan Fu, Magnus Gao,
   */
  @Given("the member with {string} has finished their trip")
  public void the_member_with_has_finished_their_trip(String memberEmail) {
    Member curMember = (Member) Member.getWithEmail(memberEmail);
    curMember.getAssignment().confirmPayment(null);
    curMember.getAssignment().startTrip(curMember.getAssignment().getStartWeek());
    curMember.getAssignment().finishTrip();

  }
}
