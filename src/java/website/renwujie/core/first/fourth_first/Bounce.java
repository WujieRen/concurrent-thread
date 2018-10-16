package website.renwujie.core.first.fourth_first;

import website.renwujie.core.first.fourteenth_second_wharIsThread.BounceFrame;

import javax.swing.*;
import java.awt.*;

/**
 * <p>
 * </p>
 *
 * @author renwujie
 * @since 2018-10-15 10:58
 */
public class Bounce {
    public static void main(String[] args){
        EventQueue.invokeLater(() -> {
            JFrame jFrame = new BounceFrame();
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setVisible(true);
        });
    }
}
