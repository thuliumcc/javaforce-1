package javaforce.media;

public class AudioDeviceNameUtil {

    public static String fixAudioDeviceName(String device) {
        return device == null ? "" : device.replaceAll("[^a-zA-Z0-9<> ]+", "_");
    }
}
