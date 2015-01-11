

package core.commands;

import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;

/**
 * Created by IntelliJ IDEA.
 * User: Mark Collin
 * Date: 24/11/11
 * Time: 11:24
 * To change this template use File | Settings | File Templates.
 */
public interface ControlObject {
    Mouse getMouseObject();

    Keyboard getKeyboardObject();
}
