package javaforce.media;

import java.util.ArrayList;
import javaforce.BE;
import javaforce.JFLog;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

/**
 * AudioInput.
 *
 * Samples are Big Endian
 *
 * @author pquiring
 */

public class AudioInput {
  private TargetDataLine tdl;
  private AudioFormat af;
  private byte[] buf8;

  public String[] listDevices() {
    ArrayList<String> mixers = new ArrayList<String>();
    Mixer.Info[] mi = AudioSystem.getMixerInfo();
    mixers.add("<default>");
    for (int a = 0; a < mi.length; a++) {
      String name = mi[a].getName();
      Mixer m = AudioSystem.getMixer(mi[a]);
      if (m.getTargetLineInfo().length == 0) {
        continue; //no target lines
      }
      mixers.add(name);
    }
    return mixers.toArray(new String[0]);
  }

  public boolean start(int chs, int freq, int bits, int bufsiz, String device) {
    JFLog.log("Input start");
    buf8 = new byte[bufsiz];
    if (device == null) {
      device = "<default>";
    }
    device = AudioDeviceNameUtil.fixAudioDeviceName(device);
    af = new AudioFormat((float) freq, bits, chs, true, true);
    JFLog.log("AudioInput:AudioFormat=" + af);
    JFLog.log("AudioInput:Device=" + device);
    Mixer.Info[] mi = AudioSystem.getMixerInfo();
    int idx = -1;
    for (int a = 0; a < mi.length; a++) {
      JFLog.log("AudioInput:Device to compare=" + mi[a].getName());
      String deviceToCompare = AudioDeviceNameUtil.fixAudioDeviceName(mi[a].getName());
      if (deviceToCompare.equalsIgnoreCase(device)) {
        JFLog.log("AudioInput:Device is matching");
        idx = a;
        break;
      }
    }
    try {
      if (idx == -1) {
        tdl = AudioSystem.getTargetDataLine(af);
      } else {
        tdl = AudioSystem.getTargetDataLine(af, mi[idx]);
      }
    } catch (Exception e) {
      JFLog.log(e);
      return false;
    }
    try {
      JFLog.log("Buffer size before: " + tdl.getBufferSize());
      tdl.open(af, 3 * bufsiz);
      JFLog.log("Buffer size after: " + tdl.getBufferSize());
    } catch (Exception e) {
      JFLog.log(e);
      return false;
    }
    tdl.start();
    JFLog.log("Input started: " + tdl);
    return true;
  }

  public boolean read(byte[] buf) {
    if (tdl.available() < buf.length) {
      return false; //do not block (causes audio glitches)
    }
    int ret = tdl.read(buf, 0, buf.length);
    if (ret != buf.length) {
      return false;
    }
    return true;
  }

  public boolean read(short[] buf16) {
    if (!read(buf8)) {
      return false;
    }
    BE.byteArray2shortArray(buf8, buf16);
    return true;
  }
  
  public void flush() {
    JFLog.log("Input flush");
    tdl.drain();
    tdl.flush();
  }

  public void flushOnly() {
    JFLog.log("Flush only: " + tdl);
    if (tdl != null) {
      JFLog.log("Flushing input audio buffer");
      tdl.flush();
    }
  }

  public boolean stop() {
    JFLog.log("Input stop");
    if (tdl == null) {
      return false;
    }
    tdl.stop();
    tdl.close();
    tdl = null;
    return true;
  }

}
