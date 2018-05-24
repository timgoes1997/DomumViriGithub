package DomumViri.Managers;

import java.io.Serializable;
import java.util.BitSet;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class InputManager {

    /**
     * Bitset which registers if any {@link KeyCode} keeps being pressed or if
     * it is released.
     */
    private BitSet keyboardBitSet = new BitSet();

    // -------------------------------------------------
    // default key codes
    // will vary when you let the user customize the key codes or when you add support for a 2nd player
    // -------------------------------------------------
    
    // Movement
    private KeyCode upKey = KeyCode.W;
    private KeyCode downKey = KeyCode.S;
    private KeyCode leftKey = KeyCode.A;
    private KeyCode rightKey = KeyCode.D;
    
    // Attacks    
    private KeyCode normalAttackKey = KeyCode.J;
    private KeyCode heavyAttackKey = KeyCode.K;
    
    // Item pickup
    private KeyCode itemPickup = KeyCode.I;
    
    // Item use
    private KeyCode itemUse = KeyCode.E;
    
    // Block
    private KeyCode blockKey = KeyCode.L;
    
    // Debugging
    private KeyCode debug = KeyCode.P;
    private KeyCode debugTilePosition = KeyCode.O;
    
    // Menu
    private KeyCode escape = KeyCode.ESCAPE;
    
    // StealHealth
    private KeyCode steal = KeyCode.H;
    
    private Scene scene;

    public InputManager(Scene scene) {
        this.scene = scene;
        this.addListeners();
    }

    /**
     * "Key Pressed" handler for all input events: register pressed key in the
     * bitset
     */
    private EventHandler<KeyEvent> keyPressedEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            // register key down
            keyboardBitSet.set(event.getCode().ordinal(), true);
        }
    };
    
    /**
     * "Key Released" handler for all input events: unregister released key in
     * the bitset
     */
    private EventHandler<KeyEvent> keyReleasedEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            // register key up
            keyboardBitSet.set(event.getCode().ordinal(), false);
        }
    };
    
    private EventHandler<MouseEvent> mousePressedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            keyboardBitSet.set(event.getButton().ordinal(), true);
        }
    };
    
    private EventHandler<MouseEvent> mouseReleasedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            keyboardBitSet.set(event.getButton().ordinal(), false);
        }
    };
    
    /**
     * Adds the listeners.
     */
    public void addListeners() {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedEventHandler);
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedEventHandler);
    }

    /**
     * Removes the listeners
     */
    public void removeListeners() {
        scene.removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.removeEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
        scene.removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedEventHandler);
        scene.removeEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedEventHandler);
    }


    // -------------------------------------------------
    // Evaluate bitset of pressed keys and return the player input.
    // If direction and its opposite direction are pressed simultaneously, then the direction isn't handled.
    // -------------------------------------------------
    
    // Movement
    public boolean isMoveUp() {
        return keyboardBitSet.get(upKey.ordinal()) && !keyboardBitSet.get(downKey.ordinal());
    }

    public boolean isMoveDown() {
        return keyboardBitSet.get(downKey.ordinal()) && !keyboardBitSet.get(upKey.ordinal());
    }

    public boolean isMoveLeft() {
        return keyboardBitSet.get(leftKey.ordinal()) && !keyboardBitSet.get(rightKey.ordinal());
    }

    public boolean isMoveRight() {
        return keyboardBitSet.get(rightKey.ordinal()) && !keyboardBitSet.get(leftKey.ordinal());
    }

    // Attacks
    public boolean isFireHeavyAttack() {
        return keyboardBitSet.get(heavyAttackKey.ordinal());
    }
    
    public boolean isFireNormalAttack() {
        return keyboardBitSet.get(normalAttackKey.ordinal());
    }
    
    // Item pickup & use
    public boolean isFireItem() {
        return keyboardBitSet.get(itemPickup.ordinal());
    }
    
    // Item pickup & use
    public boolean isUseItem() {
        return keyboardBitSet.get(itemUse.ordinal());
    }    
    
    // Block
    public boolean isFireBlock() {
        return keyboardBitSet.get(blockKey.ordinal());
    }
    
    /* Input van game klasse */
    public boolean isDebug() {
        return keyboardBitSet.get(debug.ordinal());
    }
    
    public boolean isDebugTilePosition() {
        return keyboardBitSet.get(debugTilePosition.ordinal());
    }    
    
    public boolean isMenu(){
        return keyboardBitSet.get(escape.ordinal());
    }
    
    public boolean isStealHealth(){
        return keyboardBitSet.get(steal.ordinal());
    }
}