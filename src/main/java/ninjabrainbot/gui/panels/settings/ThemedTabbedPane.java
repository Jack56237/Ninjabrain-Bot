package ninjabrainbot.gui.panels.settings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.panels.ThemedOpaquePanel;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;
import ninjabrainbot.gui.style.WrappedColor;

public class ThemedTabbedPane extends ThemedPanel {

	private static final long serialVersionUID = -3291029177930511395L;

	ArrayList<TabButton> tabs;
	StyleManager styleManager;
	ThemedPanel tabPanel;
	JPanel mainPanel;

	public ThemedTabbedPane(StyleManager styleManager) {
		super(styleManager);
		this.styleManager = styleManager;
		tabs = new ArrayList<TabButton>();
		setLayout(null);
		tabPanel = new ThemedOpaquePanel(styleManager);
		tabPanel.setBackgroundColor(styleManager.currentTheme.COLOR_DIVIDER);
		tabPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		tabPanel.setOpaque(true);
		mainPanel.setOpaque(false);
		add(tabPanel);
		add(mainPanel);
	}

	public void addTab(String title, JComponent component) {
		mainPanel.add(component);
		component.setVisible(tabs.size() == 0);
		TabButton tabButton = new TabButton(this.styleManager, this, title, component);
		tabs.add(tabButton);
		tabPanel.add(tabButton);
	}

	void setVisible(TabButton tab) {
		for (TabButton t : tabs) {
			t.setComponentVisible(tab == t);
		}
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		setFont(styleManager.fontSize(getTextSize(styleManager.size), true));
	}

	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_MEDIUM;
	}

	public Color getBackgroundColor(Theme theme) {
		return null;
	}

	public Color getForegroundColor(Theme theme) {
		return null;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		int tabPanelHeight = tabPanel.getPreferredSize().height;
		tabPanel.setBounds(0, 0, width, tabPanelHeight);
		mainPanel.setBounds(0, tabPanelHeight, width, height - tabPanelHeight);
	}

	@Override
	public Dimension getPreferredSize() {
		if (tabs.size() == 0)
			return super.getPreferredSize();
		Dimension reference = tabs.get(5).component.getPreferredSize();
		return new Dimension(reference.width, reference.height + tabPanel.getPreferredSize().height);
	}

}

class TabButton extends FlatButton {

	private static final long serialVersionUID = 1874343984790503904L;

	ThemedTabbedPane parent;
	JComponent component;

	WrappedColor selectedBackgroundColor, selectedTextColor;

	public TabButton(StyleManager styleManager, ThemedTabbedPane parent, String title, JComponent component) {
		super(styleManager, title);
		this.parent = parent;
		this.component = component;
		label.setCursor(null);
		addActionListener(p -> onClicked());
		setBackgroundColor(styleManager.currentTheme.COLOR_DIVIDER);
		setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_STRONG);
		selectedBackgroundColor = styleManager.currentTheme.COLOR_NEUTRAL;
		selectedTextColor = styleManager.currentTheme.TEXT_COLOR_NEUTRAL;
	}

	void setComponentVisible(boolean bool) {
		component.setVisible(bool);
		updateColors();
	}

	private void onClicked() {
		parent.setVisible(this);
	}

	@Override
	protected Color getHoverColor() {
		if (component.isVisible())
			return selectedBackgroundColor.color();
		return super.getHoverColor();
	}

	@Override
	protected Color getBackgroundColor() {
		if (component.isVisible())
			return selectedBackgroundColor.color();
		return super.getBackgroundColor();
	}

	@Override
	protected Color getForegroundColor() {
		if (component.isVisible())
			return selectedTextColor.color();
		return super.getForegroundColor();
	}

}
