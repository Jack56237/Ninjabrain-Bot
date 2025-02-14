package ninjabrainbot.data.calculator;

import java.util.List;

import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IDisposable;

public interface ICalculatorResult extends IDisposable {

	public ChunkPrediction getBestPrediction();

	public List<ChunkPrediction> getTopPredictions();

	public boolean success();

}
