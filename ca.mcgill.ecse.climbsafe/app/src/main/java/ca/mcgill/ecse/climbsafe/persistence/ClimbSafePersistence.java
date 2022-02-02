package ca.mcgill.ecse.climbsafe.persistence;
import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import java.sql.Date;

import ca.mcgill.ecse.climbsafe.model.Administrator;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;

public class ClimbSafePersistence {

  private static String filename = "data.climbsafe";

  public static void setFilename(String filename) {
    ClimbSafePersistence.filename = filename;
  }

  public static void save() {
    PersistenceObjectStream.setFilename(filename);
    save(ClimbSafeApplication.getClimbSafe());
  }

  public static void save(ClimbSafe climbsafe) {
    PersistenceObjectStream.setFilename(filename);
    PersistenceObjectStream.serialize(climbsafe);
  }

  public static ClimbSafe load() {
    PersistenceObjectStream.setFilename(filename);
    var climbSafe = (ClimbSafe) PersistenceObjectStream.deserialize();
    // model cannot be loaded - create empty climbsafe
    
    if (climbSafe == null) {
      Date d = new Date(0);
      climbSafe = new ClimbSafe(d,0,0);
      climbSafe.setAdministrator(new Administrator("admin@nmc.nt", "admin", climbSafe));
    } else {
      try {
      climbSafe.setAdministrator(new Administrator("admin@nmc.nt", "admin", climbSafe));
      }catch(Exception e){
    	  
      }
      climbSafe.reinitialize();
    }
    return climbSafe;
  }
}