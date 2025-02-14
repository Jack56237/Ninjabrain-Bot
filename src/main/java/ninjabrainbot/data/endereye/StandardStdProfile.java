package ninjabrainbot.data.endereye;

import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.Modifiable;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class StandardStdProfile extends Modifiable<IStdProfile> implements IStdProfile, IDisposable {

	public static final int NORMAL = 0;
	public static final int ALTERNATIVE = 1;
	public static final int MANUAL = 2;

	private double[] stds;
	private Subscription[] subs;

	public StandardStdProfile(NinjabrainBotPreferences preferences) {
		stds = new double[] { preferences.sigma.get(), preferences.sigmaAlt.get(), preferences.sigmaManual.get() };
		subs = new Subscription[] { preferences.sigma.whenModified().subscribe(newStd -> setStd(NORMAL, newStd)), preferences.sigmaAlt.whenModified().subscribe(newStd -> setStd(ALTERNATIVE, newStd)),
				preferences.sigmaManual.whenModified().subscribe(newStd -> setStd(MANUAL, newStd)) };
	}

	@Override
	public double getStd(int profileNumber) {
		return stds[profileNumber];
	}

	@Override
	public int getNumberOfProfiles() {
		return stds.length;
	}

	private void setStd(int id, double std) {
		if (stds[id] == std)
			return;
		stds[id] = std;
		notifySubscribers(this);
	}

	@Override
	public void dispose() {
		for (Subscription s : subs) {
			s.cancel();
		}
	}

	@Override
	public int getInitialProfileNumber(IThrow t) {
		return t.isMcVersion1_12() ? MANUAL : NORMAL;
	}

}