package android.app;

/**
 * Created by panda on 2021/12/6 17:15
 */
public class BandageActivityThread {

  public static ActivityThread currentActivityThread() {
    try {
      return ActivityThread.currentActivityThread();
    } catch (Throwable th) {
      return null;
    }
  }
}
