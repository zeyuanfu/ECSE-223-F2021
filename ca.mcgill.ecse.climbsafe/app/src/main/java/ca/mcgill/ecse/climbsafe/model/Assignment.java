/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.climbsafe.model;
import java.io.Serializable;

/**
 * @author Michelle Lee, Magnus Gao, Elie Abdo, Yazdan Zinati, Enzo Calcagno, Ze Yuan Fu
 */
// line 107 "../../../../../ClimbSafePersistence.ump"
// line 2 "../../../../../ClimbSafeStates.ump"
// line 94 "../../../../../ClimbSafe.ump"
public class Assignment implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Assignment Attributes
  private int startWeek;
  private int endWeek;
  private int discount;
  private String authCode;

  //Assignment State Machines
  public enum AssignmentStatus { Assigned, Banned, Cancelled, Paid, Started, Finished }
  private AssignmentStatus assignmentStatus;

  //Assignment Associations
  private Member member;
  private Guide guide;
  private Hotel hotel;
  private ClimbSafe climbSafe;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Assignment(int aStartWeek, int aEndWeek, Member aMember, ClimbSafe aClimbSafe)
  {
    startWeek = aStartWeek;
    endWeek = aEndWeek;
    discount = 0;
    authCode = null;
    boolean didAddMember = setMember(aMember);
    if (!didAddMember)
    {
      throw new RuntimeException("Unable to create assignment due to member. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddClimbSafe = setClimbSafe(aClimbSafe);
    if (!didAddClimbSafe)
    {
      throw new RuntimeException("Unable to create assignment due to climbSafe. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setAssignmentStatus(AssignmentStatus.Assigned);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStartWeek(int aStartWeek)
  {
    boolean wasSet = false;
    startWeek = aStartWeek;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndWeek(int aEndWeek)
  {
    boolean wasSet = false;
    endWeek = aEndWeek;
    wasSet = true;
    return wasSet;
  }

  public boolean setDiscount(int aDiscount)
  {
    boolean wasSet = false;
    discount = aDiscount;
    wasSet = true;
    return wasSet;
  }

  public boolean setAuthCode(String aAuthCode)
  {
    boolean wasSet = false;
    authCode = aAuthCode;
    wasSet = true;
    return wasSet;
  }

  public int getStartWeek()
  {
    return startWeek;
  }

  public int getEndWeek()
  {
    return endWeek;
  }

  public int getDiscount()
  {
    return discount;
  }

  public String getAuthCode()
  {
    return authCode;
  }

  public String getAssignmentStatusFullName()
  {
    String answer = assignmentStatus.toString();
    return answer;
  }

  public AssignmentStatus getAssignmentStatus()
  {
    return assignmentStatus;
  }

  public boolean cancelTrip()
  {
    boolean wasEventProcessed = false;
    
    AssignmentStatus aAssignmentStatus = assignmentStatus;
    switch (aAssignmentStatus)
    {
      case Assigned:
        if (!(isBanned()))
        {
        // line 6 "../../../../../ClimbSafeStates.ump"
          doCancelTrip();
          setAssignmentStatus(AssignmentStatus.Cancelled);
          wasEventProcessed = true;
          break;
        }
        break;
      case Banned:
        // line 28 "../../../../../ClimbSafeStates.ump"
        rejectCancelTrip();
        setAssignmentStatus(AssignmentStatus.Banned);
        wasEventProcessed = true;
        break;
      case Paid:
        // line 46 "../../../../../ClimbSafeStates.ump"
        doCancelTrip();
        setAssignmentStatus(AssignmentStatus.Cancelled);
        wasEventProcessed = true;
        break;
      case Started:
        // line 60 "../../../../../ClimbSafeStates.ump"
        doCancelTrip();
        setAssignmentStatus(AssignmentStatus.Cancelled);
        wasEventProcessed = true;
        break;
      case Finished:
        // line 75 "../../../../../ClimbSafeStates.ump"
        rejectCancelTrip();
        setAssignmentStatus(AssignmentStatus.Finished);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean confirmPayment(String authCode)
  {
    boolean wasEventProcessed = false;
    
    AssignmentStatus aAssignmentStatus = assignmentStatus;
    switch (aAssignmentStatus)
    {
      case Assigned:
        if (!(isBanned()))
        {
        // line 9 "../../../../../ClimbSafeStates.ump"
          doConfirmPayment(authCode);
          setAssignmentStatus(AssignmentStatus.Paid);
          wasEventProcessed = true;
          break;
        }
        break;
      case Banned:
        // line 25 "../../../../../ClimbSafeStates.ump"
        rejectPayment();
        setAssignmentStatus(AssignmentStatus.Banned);
        wasEventProcessed = true;
        break;
      case Cancelled:
        // line 37 "../../../../../ClimbSafeStates.ump"
        rejectPayment();
        setAssignmentStatus(AssignmentStatus.Cancelled);
        wasEventProcessed = true;
        break;
      case Paid:
        // line 50 "../../../../../ClimbSafeStates.ump"
        rejectPayment();
        setAssignmentStatus(AssignmentStatus.Paid);
        wasEventProcessed = true;
        break;
      case Started:
        // line 63 "../../../../../ClimbSafeStates.ump"
        rejectPayment();
        setAssignmentStatus(AssignmentStatus.Started);
        wasEventProcessed = true;
        break;
      case Finished:
        // line 69 "../../../../../ClimbSafeStates.ump"
        rejectPayment();
        setAssignmentStatus(AssignmentStatus.Finished);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean startTrip(int week)
  {
    boolean wasEventProcessed = false;
    
    AssignmentStatus aAssignmentStatus = assignmentStatus;
    switch (aAssignmentStatus)
    {
      case Assigned:
        if (hasMatchingStartWeek(week))
        {
          setAssignmentStatus(AssignmentStatus.Banned);
          wasEventProcessed = true;
          break;
        }
        break;
      case Banned:
        // line 19 "../../../../../ClimbSafeStates.ump"
        rejectStartTrip();
        setAssignmentStatus(AssignmentStatus.Banned);
        wasEventProcessed = true;
        break;
      case Cancelled:
        // line 34 "../../../../../ClimbSafeStates.ump"
        rejectStartTrip();
        setAssignmentStatus(AssignmentStatus.Cancelled);
        wasEventProcessed = true;
        break;
      case Paid:
        if (hasMatchingStartWeek(week))
        {
          setAssignmentStatus(AssignmentStatus.Started);
          wasEventProcessed = true;
          break;
        }
        break;
      case Finished:
        // line 72 "../../../../../ClimbSafeStates.ump"
        rejectStartTrip();
        setAssignmentStatus(AssignmentStatus.Finished);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean finishTrip()
  {
    boolean wasEventProcessed = false;
    
    AssignmentStatus aAssignmentStatus = assignmentStatus;
    switch (aAssignmentStatus)
    {
      case Assigned:
        // line 13 "../../../../../ClimbSafeStates.ump"
        rejectFinishTrip();
        setAssignmentStatus(AssignmentStatus.Assigned);
        wasEventProcessed = true;
        break;
      case Banned:
        // line 22 "../../../../../ClimbSafeStates.ump"
        rejectFinishTrip();
        setAssignmentStatus(AssignmentStatus.Banned);
        wasEventProcessed = true;
        break;
      case Cancelled:
        // line 40 "../../../../../ClimbSafeStates.ump"
        rejectFinishTrip();
        setAssignmentStatus(AssignmentStatus.Cancelled);
        wasEventProcessed = true;
        break;
      case Paid:
        // line 53 "../../../../../ClimbSafeStates.ump"
        rejectFinishTrip();
        setAssignmentStatus(AssignmentStatus.Paid);
        wasEventProcessed = true;
        break;
      case Started:
        setAssignmentStatus(AssignmentStatus.Finished);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setAssignmentStatus(AssignmentStatus aAssignmentStatus)
  {
    assignmentStatus = aAssignmentStatus;
  }
  /* Code from template association_GetOne */
  public Member getMember()
  {
    return member;
  }
  /* Code from template association_GetOne */
  public Guide getGuide()
  {
    return guide;
  }

  public boolean hasGuide()
  {
    boolean has = guide != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Hotel getHotel()
  {
    return hotel;
  }

  public boolean hasHotel()
  {
    boolean has = hotel != null;
    return has;
  }
  /* Code from template association_GetOne */
  public ClimbSafe getClimbSafe()
  {
    return climbSafe;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setMember(Member aNewMember)
  {
    boolean wasSet = false;
    if (aNewMember == null)
    {
      //Unable to setMember to null, as assignment must always be associated to a member
      return wasSet;
    }
    
    Assignment existingAssignment = aNewMember.getAssignment();
    if (existingAssignment != null && !equals(existingAssignment))
    {
      //Unable to setMember, the current member already has a assignment, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Member anOldMember = member;
    member = aNewMember;
    member.setAssignment(this);

    if (anOldMember != null)
    {
      anOldMember.setAssignment(null);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setGuide(Guide aGuide)
  {
    boolean wasSet = false;
    Guide existingGuide = guide;
    guide = aGuide;
    if (existingGuide != null && !existingGuide.equals(aGuide))
    {
      existingGuide.removeAssignment(this);
    }
    if (aGuide != null)
    {
      aGuide.addAssignment(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setHotel(Hotel aHotel)
  {
    boolean wasSet = false;
    Hotel existingHotel = hotel;
    hotel = aHotel;
    if (existingHotel != null && !existingHotel.equals(aHotel))
    {
      existingHotel.removeAssignment(this);
    }
    if (aHotel != null)
    {
      aHotel.addAssignment(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setClimbSafe(ClimbSafe aClimbSafe)
  {
    boolean wasSet = false;
    if (aClimbSafe == null)
    {
      return wasSet;
    }

    ClimbSafe existingClimbSafe = climbSafe;
    climbSafe = aClimbSafe;
    if (existingClimbSafe != null && !existingClimbSafe.equals(aClimbSafe))
    {
      existingClimbSafe.removeAssignment(this);
    }
    climbSafe.addAssignment(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Member existingMember = member;
    member = null;
    if (existingMember != null)
    {
      existingMember.setAssignment(null);
    }
    if (guide != null)
    {
      Guide placeholderGuide = guide;
      this.guide = null;
      placeholderGuide.removeAssignment(this);
    }
    if (hotel != null)
    {
      Hotel placeholderHotel = hotel;
      this.hotel = null;
      placeholderHotel.removeAssignment(this);
    }
    ClimbSafe placeholderClimbSafe = climbSafe;
    this.climbSafe = null;
    if(placeholderClimbSafe != null)
    {
      placeholderClimbSafe.removeAssignment(this);
    }
  }

  // line 83 "../../../../../ClimbSafeStates.ump"
   private void doConfirmPayment(String authCode){
    this.setAuthCode(authCode); // this is a domain method we added
  }

  // line 87 "../../../../../ClimbSafeStates.ump"
   private void doCancelTrip(){
    if (getAssignmentStatusFullName().equals("Assigned")) {
         this.setDiscount(0); // 100% refund
      } else if (getAssignmentStatusFullName().equals("Paid")) {
         this.setDiscount(50); // 50% refund
      } else if (getAssignmentStatusFullName().equals("Started")) {
         this.setDiscount(10);
      }
  }

  // line 98 "../../../../../ClimbSafeStates.ump"
   private void rejectPayment(){
    if (getAssignmentStatusFullName().equals("Paid")) {
      throw new RuntimeException("Trip has already been paid for");
    }
    if (getAssignmentStatusFullName().equals("Started")) {
      throw new RuntimeException("Trip has already been paid for");
    }
    if (getAssignmentStatusFullName().equals("Banned")) {
      throw new RuntimeException("Cannot pay for the trip due to a ban");
    }
    if (getAssignmentStatusFullName().equals("Finished")) {
      throw new RuntimeException("Cannot pay for a trip which has finished");
    }
    if (getAssignmentStatusFullName().equals("Cancelled")) {
      throw new RuntimeException("Cannot pay for a trip which has been cancelled");
    }
  }

  // line 117 "../../../../../ClimbSafeStates.ump"
   private void rejectStartTrip(){
    if (getAssignmentStatusFullName().equals("Banned")) {
      throw new RuntimeException("Cannot start the trip due to a ban");
    }
     if (getAssignmentStatusFullName().equals("Cancelled")) {
      throw new RuntimeException("Cannot start a trip which has been cancelled");
    }
     if (getAssignmentStatusFullName().equals( "Finished")) {
      throw new RuntimeException("Cannot start a trip which has finished");
    }
  }

  // line 130 "../../../../../ClimbSafeStates.ump"
   private void rejectFinishTrip(){
    if (getAssignmentStatusFullName().equals("Banned")) {
      throw new RuntimeException("Cannot finish the trip due to a ban");
    }
     if (getAssignmentStatusFullName().equals("Assigned")) {
      throw new RuntimeException("Cannot finish a trip which has not started");
    }
     if (getAssignmentStatusFullName().equals("Paid")) {
      throw new RuntimeException("Cannot finish a trip which has not started");
    }
    if (getAssignmentStatusFullName().equals("Cancelled")) {
      throw new RuntimeException("Cannot finish a trip which has been cancelled");
    }
  }

  // line 145 "../../../../../ClimbSafeStates.ump"
   private void rejectCancelTrip(){
    if (getAssignmentStatusFullName().equals("Banned")) {
      throw new RuntimeException("Cannot cancel the trip due to a ban");
    }
    if (getAssignmentStatusFullName().equals("Finished")) {
      throw new RuntimeException("Cannot cancel a trip which has finished");
    }
  }

  // line 153 "../../../../../ClimbSafeStates.ump"
   private boolean isBanned(){
    return getAssignmentStatusFullName().equals("Banned");
  }

  // line 157 "../../../../../ClimbSafeStates.ump"
   private boolean hasMatchingStartWeek(int week){
    Member member = this.getMember();
    return (week == member.getAssignment().getStartWeek());
  }


  public String toString()
  {
    return super.toString() + "["+
            "startWeek" + ":" + getStartWeek()+ "," +
            "endWeek" + ":" + getEndWeek()+ "," +
            "discount" + ":" + getDiscount()+ "," +
            "authCode" + ":" + getAuthCode()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "member = "+(getMember()!=null?Integer.toHexString(System.identityHashCode(getMember())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "guide = "+(getGuide()!=null?Integer.toHexString(System.identityHashCode(getGuide())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "hotel = "+(getHotel()!=null?Integer.toHexString(System.identityHashCode(getHotel())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "climbSafe = "+(getClimbSafe()!=null?Integer.toHexString(System.identityHashCode(getClimbSafe())):"null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 110 "../../../../../ClimbSafePersistence.ump"
  private static final long serialVersionUID = 11L ;

  
}