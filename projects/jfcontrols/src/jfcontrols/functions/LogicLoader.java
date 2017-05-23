package jfcontrols.functions;

/** Logic class loader.
 *
 * @author pquiring
 */

import java.io.*;

import javaforce.*;

public class LogicLoader extends ClassLoader {
  public Class findClass(String cls) throws ClassNotFoundException {
    if (!cls.startsWith("code.")) return super.findClass(cls);
    byte[] b = loadClassData(cls);
    return defineClass(cls, b, 0, b.length);
  }

  private byte[] loadClassData(String cls) {
    //cls = work/class/*.class
    try {
      String fn = "work/class/" + cls + ".class";
      FileInputStream fis = new FileInputStream(fn);
      byte data[] = JF.readAll(fis);
      fis.close();
      return data;
    } catch (Exception e) {
      JFLog.log(e);
    }
    return null;
  }
}
