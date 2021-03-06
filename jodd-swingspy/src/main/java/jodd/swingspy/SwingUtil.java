// Copyright (c) 2003-2014, Jodd Team (jodd.org). All Rights Reserved.

package jodd.swingspy;

import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Font;

/**
 * Some common swing utilities.
 */
public class SwingUtil {

	/**
	 * Turns on anti-aliased text. Call it before any other Swing usage.
	 * It works for JDK5.
	 */
	public static void enableJDK5AntiAliasedText() {
		System.setProperty("swing.aatext", "true");
	}

	/**
	 * Scrolls scroll pane to the top left corner.
	 */
	public static void scrollToTop(final JScrollPane scrollPane) {
		JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
		JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
		verticalScrollBar.setValue(verticalScrollBar.getMinimum());
		horizontalScrollBar.setValue(horizontalScrollBar.getMinimum());
	}

	/**
	 * Scrolls scroll pane to the top left corner a bit later.
	 * @see #scrollToTop(JScrollPane)
	 */
	public static void scrollToTopLater(final JScrollPane scrollPane) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				scrollToTop(scrollPane);
			}
		});
	}

	/**
	 * Center JFrame.
	 */
	public static void center(JFrame frame) {
		Dimension frameSize = frame.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screenSize.width - frameSize.width) >> 1, (screenSize.height - frameSize.height) >> 1);
	}

	/**
	 * Center JDialog.
	 */
	public static void center(JDialog dialog) {
		Dimension dialogSize = dialog.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((screenSize.width - dialogSize.width) >> 1, (screenSize.height - dialogSize.height) >> 1);
	}

	/**
	 * Enforces JEditorPane font.
	 * Once the content type of a JEditorPane is set to text/html the font on the Pane starts to be managed by Swing.
	 * This method forces using provided font.
	 */
	public static void enforceJEditorPaneFont(JEditorPane jEditorPane, Font font) {
		jEditorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		jEditorPane.setFont(font);
	}


}
