package ninjabrainbot.io;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.util.concurrent.atomic.AtomicBoolean;

import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.Throw;
import ninjabrainbot.data.endereye.Throw1_12;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class ClipboardReader implements Runnable {

	private NinjabrainBotPreferences preferences;

	Clipboard clipboard;
	String lastClipboardString;

	private AtomicBoolean forceReadLater;

	private IModificationLock modificationLock;
	private ObservableProperty<IThrow> whenNewThrowInputed;
	private ObservableProperty<Fossil> whenNewFossilInputed;

	public ClipboardReader(NinjabrainBotPreferences preferences, IModificationLock modificationLock) {
		this.preferences = preferences;
		this.modificationLock = modificationLock;
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		lastClipboardString = "";
		forceReadLater = new AtomicBoolean(false);
		whenNewThrowInputed = new ObservableProperty<IThrow>();
		whenNewFossilInputed = new ObservableProperty<Fossil>();
	}

	public void forceRead() {
		forceReadLater.set(true);
	}

	public ISubscribable<IThrow> whenNewThrowInputed() {
		return whenNewThrowInputed;
	}

	public ISubscribable<Fossil> whenNewFossilInputed() {
		return whenNewFossilInputed;
	}

	@Override
	public void run() {
		while (true) {
			boolean read = !preferences.altClipboardReader.get();
			if (preferences.altClipboardReader.get() && forceReadLater.get()) {
				read = true;
				// Sleep 0.1 seconds to let the game update the clipboard
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (read && clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
				String clipboardString = null;
				try {
					clipboardString = (String) clipboard.getData(DataFlavor.stringFlavor);
				} catch (Exception e) {
				}
				if (clipboardString != null && !lastClipboardString.equals(clipboardString)) {
					onClipboardUpdated(clipboardString);
					lastClipboardString = clipboardString;
				}
			}
			// Sleep 0.1 seconds
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void onClipboardUpdated(String clipboard) {
		final IThrow t = Throw.parseF3C(clipboard, preferences.crosshairCorrection.get(), modificationLock);
		if (t != null) {
			whenNewThrowInputed.notifySubscribers(t);
			return;
		}
		final IThrow t2 = Throw1_12.parseF3C(clipboard, preferences.crosshairCorrection.get(), modificationLock);
		if (t2 != null) {
			whenNewThrowInputed.notifySubscribers(t);
			return;
		}
		final Fossil f = Fossil.parseF3I(clipboard);
		if (f != null) {
			whenNewFossilInputed.notifySubscribers(f);
		}
	}

}
