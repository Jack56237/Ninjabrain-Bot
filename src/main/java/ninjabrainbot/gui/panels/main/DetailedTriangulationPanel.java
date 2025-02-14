package ninjabrainbot.gui.panels.main;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;

import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

class DetailedTriangulationPanel extends ThemedPanel implements IDisposable {

	private static final long serialVersionUID = -9022636765337872342L;

	private static final int NUM_DETAILED_PANELS = 5;

	private NinjabrainBotPreferences preferences;

	private ChunkPanelHeader header;
	private List<ChunkPanel> panels;

	public DetailedTriangulationPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		super(styleManager);
		this.preferences = preferences;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(0);
		header = new ChunkPanelHeader(styleManager, preferences);
		add(header);
		panels = new ArrayList<ChunkPanel>();
		for (int i = 0; i < NUM_DETAILED_PANELS; i++) {
			ChunkPanel panel = new ChunkPanel(styleManager, preferences);
			panels.add(panel);
			add(panel);
		}

		setBackgroundColor(styleManager.currentTheme.COLOR_NEUTRAL);
	}

	public void setResult(ICalculatorResult result) {
		header.updateHeaderText(preferences.strongholdDisplayType.get());
		if (result == null) {
			for (ChunkPanel p : panels) {
				p.setPrediction(null);
			}
			return;
		}
		List<ChunkPrediction> predictions = result.getTopPredictions();
		for (int i = 0; i < NUM_DETAILED_PANELS; i++) {
			ChunkPanel p = panels.get(i);
			p.setPrediction(predictions.get(i));
		}
	}

	public void setAngleUpdatesEnabled(boolean b) {
		header.setAngleUpdatesEnabled(b);
		panels.forEach(p -> p.setAngleUpdatesEnabled(b));
	}

	@Override
	public void updateColors() {
		super.updateColors();
		for (int i = 0; i < NUM_DETAILED_PANELS; i++) {
			ChunkPanel p = panels.get(i);
			p.updateColors();
		}
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		setPreferredSize(new Dimension(0, (1 + NUM_DETAILED_PANELS) * (styleManager.size.PADDING + styleManager.size.TEXT_SIZE_MEDIUM)));
		super.updateSize(styleManager);
	}

	@Override
	public void dispose() {
		header.dispose();
	}

}