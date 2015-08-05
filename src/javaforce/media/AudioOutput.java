package javaforce.media;

import java.util.ArrayList;
import javaforce.BE;
import javaforce.JFLog;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/** Audio Output.
 *
 * Samples are Big Endian.
 *
 * @author pquiring
 */

public class AudioOutput {
  private SourceDataLine sdl;
  private AudioFormat af;
  private byte[] buf8;

  public String[] listDevices() {
    ArrayList<String> mixers = new ArrayList<String>();
    Mixer.Info[] mi = AudioSystem.getMixerInfo();
    mixers.add("<default>");
    for (int a = 0; a < mi.length; a++) {
      String name = mi[a].getName();
      Mixer m = AudioSystem.getMixer(mi[a]);
      if (m.getSourceLineInfo().length == 0) {
        continue; //no source lines
      }
      mixers.add(name);
    }
    return mixers.toArray(new String[0]);
  }

  public boolean start(int chs, int freq, int bits, int bufsiz, String device) {
    buf8 = new byte[bufsiz];
    if (device == null) {
      device = "<default>";
    }
    af = new AudioFormat((float) freq, bits, chs, true, true);
    Mixer.Info[] mi = AudioSystem.getMixerInfo();
    int idx = -1;
    for (int a = 0; a < mi.length; a++) {
      if (mi[a].getName().equalsIgnoreCase(device)) {
        idx = a;
        break;
      }
    }
    try {
      if (idx == -1) {
        sdl = AudioSystem.getSourceDataLine(af);
      } else {
        sdl = AudioSystem.getSourceDataLine(af, mi[idx]);
      }
      /*
      try {
      FloatControl vol = (FloatControl)sdl.getControl(FloatControl.Type.VOLUME);
      if (vol != null) {
      vol.setValue(100);
      }
      } catch (Exception e) {
      JFLog.log(e);
      }
      try {
      FloatControl vol = (FloatControl)sdl.getControl(FloatControl.Type.MASTER_GAIN);
      if (vol != null) {
      vol.setValue(100);
      }
      } catch (Exception e) {
      JFLog.log(e);
      }
       */
    } catch (Exception e) {
      JFLog.log(e);
      return false;
    }
    try {
      sdl.open(af);
    } catch (Exception e) {
      JFLog.log(e);
      return false;
    }
    sdl.start();
    return true;
  }

  public boolean write(byte[] buf) {
    sdl.write(buf, 0, buf.length);
    return true;
  }

  public boolean write(short[] buf16) {
    sdl.write(BE.shortArray2byteArray(buf16, buf8), 0, buf16.length * 2);
    return true;
  }

  public boolean stop() {
    if (sdl == null) {
      return false;
    }
    sdl.stop();
    sdl.close();
    sdl = null;
    return true;
  }

}