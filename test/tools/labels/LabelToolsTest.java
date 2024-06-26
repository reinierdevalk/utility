package tools.labels;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import tools.labels.LabelTools;
import tools.path.PathTools;
import de.uos.fmt.musitech.utility.math.Rational;
import external.Tablature;
import external.Transcription;
import junit.framework.TestCase;
import tbp.symbols.TabSymbol;

public class LabelToolsTest  extends TestCase {

	private File midiTestpiece;
	private File encodingTestpiece; 
	
	private static final List<Double> V_0 = Transcription.createVoiceLabel(new Integer[]{0});
	private static final List<Double> V_1 = Transcription.createVoiceLabel(new Integer[]{1});
	private static final List<Double> V_2 = Transcription.createVoiceLabel(new Integer[]{2});
	private static final List<Double> V_3 = Transcription.createVoiceLabel(new Integer[]{3});
	private static final List<Double> V_4 = Transcription.createVoiceLabel(new Integer[]{4});
	private static final List<Double> V_01 = Transcription.createVoiceLabel(new Integer[]{0, 1});
	private static final List<Double> QUARTER = Transcription.createDurationLabel(new Integer[]{8*3});
	private static final List<Double> DOTTED_QUARTER = Transcription.createDurationLabel(new Integer[]{12*3});
	private static final List<Double> SIXTEENTH_EIGHTH = Transcription.createDurationLabel(new Integer[]{2*3, 4*3});
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		Map<String, String> paths = PathTools.getPaths();
		String ep = paths.get("ENCODINGS_PATH");
		String mp = paths.get("MIDI_PATH");
		String td = "test";

		encodingTestpiece = new File(PathTools.getPathString(
			Arrays.asList(ep, td)) + "testpiece.tbp"
		);
		midiTestpiece = new File(PathTools.getPathString(
			Arrays.asList(mp, td)) + "testpiece.mid"
		);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}


	public static List<Double> combineLabels(List<Double> vl1, List<Double> vl2) {
		List<Double> combined = new ArrayList<Double>(vl1);
		combined.set(vl2.indexOf(1.0), 1.0);
		return combined;
	}


	public void testConvertIntoListOfVoices() {		    
		List<List<Integer>> expected = new ArrayList<List<Integer>>();
		expected.add(Arrays.asList(new Integer[]{0, 1}));
		expected.add(Arrays.asList(new Integer[]{2}));
		expected.add(Arrays.asList(new Integer[]{3, 4}));

		List<List<Integer>> actual = new ArrayList<List<Integer>>();
		actual.add(LabelTools.convertIntoListOfVoices(combineLabels(V_0, V_1)));
		actual.add(LabelTools.convertIntoListOfVoices(V_2));
		actual.add(LabelTools.convertIntoListOfVoices(combineLabels(V_3, V_4)));

		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i).size(), actual.get(i).size());
			for (int j = 0; j < expected.get(i).size(); j++) {
				assertEquals(expected.get(i).get(j), actual.get(i).get(j));
			}
		}
	}


	public void testConvertIntoVoiceLabel() {		
		List<Integer> predictedVoices1 = Arrays.asList(new Integer[]{0, 1});
		List<Integer> predictedVoices2 = Arrays.asList(new Integer[]{2});
		List<Integer> predictedVoices3 = Arrays.asList(new Integer[]{3, 4});

		List<List<Double>> expected = new ArrayList<List<Double>>();
		expected.add(combineLabels(V_0, V_1));
		expected.add(V_2); 
		expected.add(combineLabels(V_3, V_4)); 

		List<List<Double>> actual = new ArrayList<List<Double>>();
		actual.add(LabelTools.convertIntoVoiceLabel(predictedVoices1));
		actual.add(LabelTools.convertIntoVoiceLabel(predictedVoices2));
		actual.add(LabelTools.convertIntoVoiceLabel(predictedVoices3));

		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i).size(), actual.get(i).size());
			for (int j = 0; j < expected.get(i).size(); j++) {
				assertEquals(expected.get(i).get(j), actual.get(i).get(j));
			}
		}
	}


	public void testConvertIntoDurationLabel() {		
		List<Integer> predictedDurations1 = Arrays.asList(new Integer[]{8});
		List<Integer> predictedDurations2 = Arrays.asList(new Integer[]{12});
		List<Integer> predictedDurations3 = Arrays.asList(new Integer[]{2, 4});

		List<List<Double>> expected = new ArrayList<List<Double>>();
		expected.add(QUARTER);
		expected.add(DOTTED_QUARTER); 
		expected.add(SIXTEENTH_EIGHTH);

		List<List<Double>> actual = new ArrayList<List<Double>>();
		actual.add(LabelTools.convertIntoDurationLabel(predictedDurations1));
		actual.add(LabelTools.convertIntoDurationLabel(predictedDurations2));
		actual.add(LabelTools.convertIntoDurationLabel(predictedDurations3));

		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i).size(), actual.get(i).size());
			for (int j = 0; j < expected.get(i).size(); j++) {
				assertEquals(expected.get(i).get(j), actual.get(i).get(j));
			}
		}
	}


	public void testGetVoiceAssignment() {
		Tablature tablature = new Tablature(encodingTestpiece);
		Transcription transcription = new Transcription(midiTestpiece, encodingTestpiece);

		List<List<Integer>> expected = new ArrayList<List<Integer>>();
		expected.add(Arrays.asList(new Integer[]{3, 2, 1, 0, -1}));
		expected.add(Arrays.asList(new Integer[]{2, 3, 1, 0, -1}));
		expected.add(Arrays.asList(new Integer[]{-1, -1, -1, 0, -1}));
		expected.add(Arrays.asList(new Integer[]{3, 3, 2, 1, 0}));
		expected.add(Arrays.asList(new Integer[]{-1, -1, -1, -1, 0}));
		expected.add(Arrays.asList(new Integer[]{4, 3, 2, 1, 0}));
		expected.add(Arrays.asList(new Integer[]{2, 3, 1, -1, 0}));
		expected.add(Arrays.asList(new Integer[]{1, -1, 0, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{3, 2, 1, 0, -1}));
		expected.add(Arrays.asList(new Integer[]{0, -1, -1, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{0, -1, -1, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{0, -1, -1, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{0, -1, -1, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{0, -1, -1, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{0, -1, -1, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{3, 2, 1, 0, -1}));

		List<List<Integer>> actual = new ArrayList<List<Integer>>();
		List<List<Double>> voiceLabels = transcription.getVoiceLabels();
//		List<List<Double>> voiceLabels = Arrays.asList(
//			V_3, V_2, V_1, V_0,
//			V_3, V_2, V_0, V_1,
//			V_3,
//			V_4, V_3, V_2, V_01,
//			V_4, 
//			V_4, V_3, V_2, V_1, V_0,
//			V_4, V_2, V_0, V_1,
//			V_2, V_0, 
//			V_3, V_2, V_1, V_0,
//			V_0, V_0, V_0, V_0, V_0, V_0,
//			V_3, V_2, V_0, V_1
//		);
//		int largestNumberOfChordVoices = transcription.getNumberOfVoices();
		int largestNumberOfChordVoices = 5;
		int lowestOnsetIndex = 0;
		for (int i = 0; i < tablature.getChords().size(); i++) {
			List<TabSymbol> currentChord = tablature.getChords().get(i);
			List<List<Double>> currentChordVoiceLabels = 
				voiceLabels.subList(lowestOnsetIndex, lowestOnsetIndex + currentChord.size());
			actual.add(LabelTools.getVoiceAssignment(currentChordVoiceLabels, largestNumberOfChordVoices));
			lowestOnsetIndex += currentChord.size();
		}

		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i).size(), actual.get(i).size());
			for (int j = 0; j < expected.get(i).size(); j++) {
				assertEquals(expected.get(i).get(j), actual.get(i).get(j));
			}
		}
		assertEquals(expected, actual);
	}


	public void testGetVoiceAssignmentFromListOfVoices() {
		Transcription transcription = new Transcription(midiTestpiece, encodingTestpiece);

		List<List<Integer>> expected = new ArrayList<List<Integer>>();
		expected.add(Arrays.asList(new Integer[]{3, 2, 1, 0, -1}));
		expected.add(Arrays.asList(new Integer[]{2, 3, 1, 0, -1}));
		expected.add(Arrays.asList(new Integer[]{-1, -1, -1, 0, -1}));
		expected.add(Arrays.asList(new Integer[]{3, 3, 2, 1, 0}));
		expected.add(Arrays.asList(new Integer[]{-1, -1, -1, -1, 0}));
		expected.add(Arrays.asList(new Integer[]{4, 3, 2, 1, 0}));
		expected.add(Arrays.asList(new Integer[]{2, 3, 1, -1, 0}));
		expected.add(Arrays.asList(new Integer[]{1, -1, 0, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{3, 2, 1, 0, -1}));
		expected.add(Arrays.asList(new Integer[]{0, -1, -1, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{0, -1, -1, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{0, -1, -1, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{0, -1, -1, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{0, -1, -1, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{0, -1, -1, -1, -1}));
		expected.add(Arrays.asList(new Integer[]{3, 2, 1, 0, -1}));

		List<List<Integer>> actual = new ArrayList<List<Integer>>();
		List<List<List<Double>>> chordVoiceLabels = transcription.getChordVoiceLabels();
		int largestNumberOfVoices = transcription.getNumberOfVoices();
		for (int i = 0; i < chordVoiceLabels.size(); i++) {
			List<List<Integer>> currentListOfChordVoices = new ArrayList<List<Integer>>();
			List<List<Double>> currentChordVoiceLabels = chordVoiceLabels.get(i);
			// Turn the voice label of each onset into a List of voices and add that to currentListOfChordVoices
			for (int j = 0; j < currentChordVoiceLabels.size(); j++) {
				List<Double> currentOnsetVoiceLabel = currentChordVoiceLabels.get(j);
				currentListOfChordVoices.add(LabelTools.convertIntoListOfVoices(currentOnsetVoiceLabel));
			}
			// Use getVoiceAssignmentFromListOfVoices to determine the actual voiceAssignments
			actual.add(LabelTools.getVoiceAssignmentFromListOfVoices(currentListOfChordVoices, largestNumberOfVoices));
		}

		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i).size(), actual.get(i).size());
			for (int j = 0; j < expected.get(i).size(); j++) {
				assertEquals(expected.get(i).get(j), actual.get(i).get(j));
			}
		}
		assertEquals(expected, actual);
	}


	public void testGetChordVoiceLabels() {
		Transcription transcription = new Transcription(midiTestpiece, encodingTestpiece);

		List<List<List<Double>>> expected = new ArrayList<List<List<Double>>>();
		// Chord 0
		List<List<Double>> expected0 = new ArrayList<List<Double>>();
		expected0.add(V_3); expected0.add(V_2); 
		expected0.add(V_1); expected0.add(V_0);
		// Chord 1
		List<List<Double>> expected1 = new ArrayList<List<Double>>();
		expected1.add(V_3); expected1.add(V_2); 
		expected1.add(V_0); expected1.add(V_1);
		// Chord 2
		List<List<Double>> expected2 = new ArrayList<List<Double>>();
		expected2.add(V_3); 
		// Chord 3
		List<List<Double>> expected3 = new ArrayList<List<Double>>();
		expected3.add(V_4); expected3.add(V_3); 
		expected3.add(V_2); expected3.add(combineLabels(V_0, V_1)); 
		// Chord 4
		List<List<Double>> expected4 = new ArrayList<List<Double>>();
		expected4.add(V_4);  
		// Chord 5
		List<List<Double>> expected5 = new ArrayList<List<Double>>();
		expected5.add(V_4); expected5.add(V_3); 
		expected5.add(V_2); expected5.add(V_1); 
		expected5.add(V_0); 
		// Chord 6
		List<List<Double>> expected6 = new ArrayList<List<Double>>();
		expected6.add(V_4); expected6.add(V_2); 
		expected6.add(V_0); expected6.add(V_1);
		// Chord 7
		List<List<Double>> expected7 = new ArrayList<List<Double>>();
		expected7.add(V_2); expected7.add(V_0); 
		// Chord 8
		List<List<Double>> expected8 = new ArrayList<List<Double>>();
		expected8.add(V_3); expected8.add(V_2); 
		expected8.add(V_1); expected8.add(V_0); 
		// Chords 9-14
		List<List<Double>> expected9 = new ArrayList<List<Double>>();
		expected9.add(V_0);
		List<List<Double>> expected10 = new ArrayList<List<Double>>();
		expected10.add(V_0);
		List<List<Double>> expected11 = new ArrayList<List<Double>>();
		expected11.add(V_0);
		List<List<Double>> expected12 = new ArrayList<List<Double>>();
		expected12.add(V_0);
		List<List<Double>> expected13 = new ArrayList<List<Double>>();
		expected13.add(V_0);
		List<List<Double>> expected14 = new ArrayList<List<Double>>();
		expected14.add(V_0);
		// Chord 15
		List<List<Double>> expected15 = new ArrayList<List<Double>>();
		expected15.add(V_3); expected15.add(V_2); 
		expected15.add(V_1); expected15.add(V_0);

		expected.add(expected0); expected.add(expected1); expected.add(expected2); expected.add(expected3); 
		expected.add(expected4); expected.add(expected5); expected.add(expected6); expected.add(expected7);
		expected.add(expected8); expected.add(expected9); expected.add(expected10); expected.add(expected11);
		expected.add(expected12); expected.add(expected13); expected.add(expected14); expected.add(expected15);

		// For each chord: use getVoiceAssignment() to make the List of actual voice labels and add them to
		// actualVoiceLabels
		List<List<List<Double>>> actual = new ArrayList<List<List<Double>>>();
		int highestNumberOfVoices = transcription.getNumberOfVoices();
		List<List<Integer>> voiceAssignments = transcription.getVoiceAssignments(/*tablature,*/ highestNumberOfVoices);
		for (int i = 0; i < transcription.getChords().size(); i++) {
			actual.add(LabelTools.getChordVoiceLabels(voiceAssignments.get(i)));
		}

		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i).size(), actual.get(i).size()); 
			for (int j = 0; j < expected.get(i).size(); j++) {
			assertEquals(expected.get(i).get(j).size(), actual.get(i).get(j).size());
				for (int k = 0; k < expected.get(i).get(j).size(); k++) {
					assertEquals(expected.get(i).get(j).get(k), actual.get(i).get(j).get(k));
				}
			}
		}
		assertEquals(expected, actual);
	}


	public void testGetVoicesInChord() {
		Tablature tablature = new Tablature(encodingTestpiece);
		Transcription transcription = new Transcription(midiTestpiece, encodingTestpiece);

		// For each chord: make the List of expected voices and add them to expectedVoices
		List<Integer> voice0 = Arrays.asList(new Integer[]{0});
		List<Integer> voice1 = Arrays.asList(new Integer[]{1});
		List<Integer> voice2 = Arrays.asList(new Integer[]{2});
		List<Integer> voice3 = Arrays.asList(new Integer[]{3});
		List<Integer> voice4 = Arrays.asList(new Integer[]{4});
		List<Integer> voice0And1 = Arrays.asList(new Integer[]{0, 1});
		List<List<List<Integer>>> expected = new ArrayList<List<List<Integer>>>();
		// Chord 0
		List<List<Integer>> expected0 = new ArrayList<List<Integer>>();
		expected0.add(voice3); expected0.add(voice2); expected0.add(voice1); expected0.add(voice0);
		// Chord 1
		List<List<Integer>> expected1 = new ArrayList<List<Integer>>();
		expected1.add(voice3); expected1.add(voice2); expected1.add(voice0); expected1.add(voice1);
		// Chord 2
		List<List<Integer>> expected2 = new ArrayList<List<Integer>>();
		expected2.add(voice3); 
		// Chord 3
		List<List<Integer>> expected3 = new ArrayList<List<Integer>>();
		expected3.add(voice4); expected3.add(voice3); expected3.add(voice2); expected3.add(voice0And1);
		// Chord 4
		List<List<Integer>> expected4 = new ArrayList<List<Integer>>();
		expected4.add(voice4);  
		// Chord 5
		List<List<Integer>> expected5 = new ArrayList<List<Integer>>();
		expected5.add(voice4); expected5.add(voice3); expected5.add(voice2); expected5.add(voice1); expected5.add(voice0); 
		// Chord6
		List<List<Integer>> expected6 = new ArrayList<List<Integer>>();
		expected6.add(voice4); expected6.add(voice2); expected6.add(voice0); expected6.add(voice1); 
		// Chord 7
		List<List<Integer>> expected7 = new ArrayList<List<Integer>>();
		expected7.add(voice2); expected7.add(voice0); 
		// Chord 8
		List<List<Integer>> expected8 = new ArrayList<List<Integer>>();
		expected8.add(voice3); expected8.add(voice2); expected8.add(voice1); expected8.add(voice0);
		// Chords 9-14
		List<List<Integer>> expected9 = new ArrayList<List<Integer>>();
		expected9.add(voice0); 
		List<List<Integer>> expected10 = new ArrayList<List<Integer>>();
		expected10.add(voice0); 
		List<List<Integer>> expected11 = new ArrayList<List<Integer>>();
		expected11.add(voice0); 
		List<List<Integer>> expected12 = new ArrayList<List<Integer>>();
		expected12.add(voice0); 
		List<List<Integer>> expected13 = new ArrayList<List<Integer>>();
		expected13.add(voice0); 
		List<List<Integer>> expected14 = new ArrayList<List<Integer>>();
		expected14.add(voice0); 
		// Chord 15
		List<List<Integer>> expected15 = new ArrayList<List<Integer>>();
		expected15.add(voice3); expected15.add(voice2); expected15.add(voice1); expected15.add(voice0);

		expected.add(expected0); expected.add(expected1); expected.add(expected2); expected.add(expected3); 
		expected.add(expected4); expected.add(expected5); expected.add(expected6); expected.add(expected7); 
		expected.add(expected8); expected.add(expected9); expected.add(expected10); expected.add(expected11);
		expected.add(expected12); expected.add(expected13); expected.add(expected14); expected.add(expected15);

		// For each chord: calculate the List of actual voices and add them to actualVoices
		List<List<List<Integer>>> actual = new ArrayList<List<List<Integer>>>();
		List<List<Double>> voiceLabels = transcription.getVoiceLabels();
		int lowestOnsetIndex = 0;
		for (int i = 0; i < tablature.getChords().size(); i++) {
			List<TabSymbol> currentChord = tablature.getChords().get(i);
			List<List<Double>> currentChordVoiceLabels = 
				voiceLabels.subList(lowestOnsetIndex, lowestOnsetIndex + currentChord.size());
			actual.add(LabelTools.getVoicesInChord(currentChordVoiceLabels));
			lowestOnsetIndex += currentChord.size();
		}

		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i).size(), actual.get(i).size()); 
			for (int j = 0; j < expected.get(i).size(); j++) {
				assertEquals(expected.get(i).get(j).size(), actual.get(i).get(j).size());
				for (int k = 0; k < expected.get(i).get(j).size(); k++) {
					assertEquals(expected.get(i).get(j).get(k), actual.get(i).get(j).get(k));
				}
			}
		}
		assertEquals(expected, actual);
	}


	public void testConvertIntoDuration() {
		Transcription transcription = new Transcription(midiTestpiece, encodingTestpiece);

		Rational[] thirtysecond = new Rational[]{new Rational(1, 32)};
		Rational[] sixteenth = new Rational[]{new Rational(2, 32)};
		Rational[] eighth = new Rational[]{new Rational(4, 32)};
		Rational[] dottedEighth = new Rational[]{new Rational(6, 32)};
		Rational[] quarter = new Rational[]{new Rational(8, 32)};
		Rational[] half = new Rational[]{new Rational(16, 32)};
		Rational[] quarterAndEighth = new Rational[]{new Rational(8, 32), new Rational(4, 32)};

		// Determine expected
		List<Rational[]> expected = new ArrayList<Rational[]>();
		// Chord 0
		expected.add(quarter); expected.add(quarter); expected.add(quarter); expected.add(quarter);
		// Chord 1
		expected.add(dottedEighth); expected.add(quarter); expected.add(quarter); expected.add(eighth);
		// Chord 2
		expected.add(sixteenth);
		// Chord 3
		expected.add(eighth); expected.add(quarter); expected.add(quarter); expected.add(quarterAndEighth);
		// Chord 4
		expected.add(eighth);
		// Chord 5
		expected.add(quarter); expected.add(half); expected.add(quarter); expected.add(quarter); 
		expected.add(quarter);
		// Chord 6
		expected.add(quarter); expected.add(eighth); expected.add(eighth); expected.add(quarter);
		// Chord 7
		expected.add(eighth); expected.add(eighth);
		// Chord 8
		expected.add(half); expected.add(half); expected.add(half); expected.add(sixteenth);
		// Chords 9-14
		expected.add(sixteenth); expected.add(thirtysecond); expected.add(thirtysecond);
		expected.add(thirtysecond); expected.add(thirtysecond); expected.add(quarter);
		// Chord 15
		expected.add(quarter); expected.add(quarter); expected.add(quarter); expected.add(quarter);

		List<Rational[]> actual = new ArrayList<Rational[]>();
		List<List<Double>> durationLabels = transcription.getDurationLabels();
		for (List<Double> l : durationLabels) {
			actual.add(LabelTools.convertIntoDuration(l));
		}

		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i).length, actual.get(i).length); 
			for (int j = 0; j < expected.get(i).length; j++) {
				assertEquals(expected.get(i)[j], actual.get(i)[j]);
			}
		}
	}
	
	
	public void testGetIntegerEncoding() {
		List<Rational> durs = Arrays.asList(new Rational[]{
			new Rational(1, 16),
			new Rational(1, 8),
			new Rational(5, 8),
			new Rational(13, 16)
		});

		int dlm = Transcription.DUR_LABEL_MULTIPLIER;
		List<Integer> expected = Arrays.asList(
			2*dlm, 4*dlm, 20*dlm, 26*dlm
		);

		List<Integer> actual = new ArrayList<Integer>();
		for (Rational r : durs) {
			actual.add(LabelTools.getIntegerEncoding(r));	
		}

		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i), actual.get(i));
		}
	}
}
