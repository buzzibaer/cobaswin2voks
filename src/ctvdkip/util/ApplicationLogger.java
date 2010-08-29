package ctvdkip.util;

import java.util.logging.*;

/**
 * <p>Title: Distiller Script Generator</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: mediacs AG</p>
 * @author Ralf Bust
 * @version 1.0
 */

public class ApplicationLogger{

  /**
   * A handle to the unique Singleton instance.
   */
  private static ApplicationLogger _instance = null;

  private Logger LoggingService = Logger.getLogger("Application");

  private ApplicationLogger() {
    //configuring the Logging Device for Writing Log File
    this.addFileHandler();
  };

  public static ApplicationLogger getInstance(){
    if (null == _instance){
      ApplicationLogger._instance = new ApplicationLogger();
    };
    return ApplicationLogger._instance;
  };

  public Logger getLogger(){
    return Logger.getLogger("Application");
  };

  private void addFileHandler(){
    try{
     Handler fh = new FileHandler("Application.log");
     LoggingService.addHandler(fh);
     LoggingService.setLevel(Level.ALL);
   }
   catch(java.io.IOException iox){
     System.out.println(
         "Loggin Device for FileWriting could not be inited.\n"+
         "Exception: \n"+
         iox
         );
    };
  }
};