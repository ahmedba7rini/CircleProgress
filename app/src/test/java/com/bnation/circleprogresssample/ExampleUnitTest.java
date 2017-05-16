package com.bnation.circleprogresssample;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

	@Test
	public void addition_isCorrect() throws Exception {
		int width = 100;
		int strokeWidth = 4;

		assertEquals((width - strokeWidth) / 2f, width - strokeWidth / 2f);
		assertEquals(width - (strokeWidth / 2f), width - strokeWidth / 2f);
	}
}