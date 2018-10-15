package website.renwujie.core.first.fourth_first;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * <p>
 * </p>
 *
 * @author renwujie
 * @since 2018-10-15 11:02
 */
public class BounceFrame extends JFrame {
    private Ballcomponent comp;
    public static final int STEPS = 1000;
    public static final int DELAY = 3;

    /**
     * Constructs the frame with the component for showing the bouncing ball and Start and Close buttons.
     */
    public BounceFrame() {
        setTitle("BOUNCE");
        comp = new Ballcomponent();
        add(comp, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        addButton(buttonPanel, "Start", event -> addBall());
        addButton(buttonPanel, "Close", event -> System.exit(0));
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
    }

    /**
     * Adds a button to a Container.
     * @param c
     * @param title
     * @param listener
     */
    public void addButton(Container c, String title, ActionListener listener) {
        JButton button = new JButton(title);
        c.add(button);
        button.addActionListener(listener);
    }

    public void addBall() {
        try{
            Ball ball = new Ball();
            comp.add(ball);
            for(int i = 1; i < STEPS; i++) {
                ball.move(comp.getBounds());
                comp.paint(comp.getGraphics());
                Thread.sleep(DELAY);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
