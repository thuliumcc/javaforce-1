package jfcontrols.logic;

/** Calls another function.
 *
 * @author pquiring
 */

import javaforce.*;
import javaforce.controls.*;

public class CALL extends Logic {

/*
  private int fid;
  private int types[];
  public void setFunction(int fid, int types[]) {
    this.fid = fid;
    this.types = types;
  }
*/

  public boolean isBlock() {
    return true;
  }

  public String getDesc() {
    return "Call";
  }

  public String getCode(int[] types, boolean[] array, boolean[] unsigned) {return null;}

  public String getCode(String func) {
    return "  if (enabled) enabled = new func_" + func + "().code(tags);";
  }

  public int getTagsCount() {
    return 1;
  }

  public String getTagName(int idx) {
    return "func";
  }

  public int getTagType(int idx) {
    return TagType.function;
  }
}
