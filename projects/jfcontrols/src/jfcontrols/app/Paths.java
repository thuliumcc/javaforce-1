package jfcontrols.app;

/** App paths
 *
 * @author pquiring
 */

import java.io.*;

import javaforce.*;

public class Paths {
  public static String dataPath;
  public static String backupPath;
  public static String logsPath;
  public static String imagesPath;

  public static void init() {
    if (JF.isWindows()) {
      dataPath = System.getenv("ProgramData") + "/jfcontrols";
    } else {
      dataPath = "/var/jfcontrols";
    }
    logsPath = dataPath + "/logs";
    backupPath = dataPath + "/backups";
    imagesPath = dataPath + "/images";
    new File(logsPath).mkdirs();
    new File(backupPath).mkdirs();
    new File(imagesPath).mkdirs();
    JFLog.append(logsPath + "/service.log", true);
    JFLog.log("jfControls starting...");
  }
}
