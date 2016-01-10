package cc.easyandroid.listfiltermenu.widget;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by chenguoping on 16/1/10.
 */
public class SeletorUtils {
    public static StateListDrawable creatSelector(Drawable easynormal, Drawable easypressed) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = easynormal;
        Drawable pressed = easypressed;
        Drawable focused = easypressed;
        Drawable unable = easypressed;
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed}, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_selected  }, focused);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_checkable}, focused);
        // View.WINDOW_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_checked}, unable);
        bg.addState(new int[]{android.R.attr.state_activated}, pressed);
        // View.ENABLED_STATE_SET
        // View.ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        bg.addState(new int[]{android.R.attr.state_window_focused}, normal);
        bg.addState(new int[0], normal);
        return bg;
    }
}
