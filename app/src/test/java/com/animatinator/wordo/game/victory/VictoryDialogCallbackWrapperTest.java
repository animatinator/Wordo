package com.animatinator.wordo.game.victory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class VictoryDialogCallbackWrapperTest {

    private FakeCallback delegateCallback;
    private VictoryDialogCallbackWrapper callbackWrapper;

    @Before
    public void setUp() {
        delegateCallback = new FakeCallback();
        callbackWrapper = new VictoryDialogCallbackWrapper(delegateCallback);
    }

    @Test
    public void chooseToPlayAgain() {
        callbackWrapper.onChoosePlayAgain();

        assertTrue(delegateCallback.choseToPlayAgain());
    }

    @Test
    public void chooseToExit() {
        callbackWrapper.onChooseExit();

        assertTrue(delegateCallback.choseToExit());
    }

    @Test
    public void dismissBeforeChoosing() {
        callbackWrapper.onDismiss();

        assertTrue(delegateCallback.choseToExit());
    }

    @Test
    public void chooseBeforeDismissing() {
        callbackWrapper.onChoosePlayAgain();
        callbackWrapper.onDismiss();

        assertTrue(delegateCallback.choseToPlayAgain());
    }

    private static final class FakeCallback implements VictoryDialogFragment.Callback {
        private enum Choice {NONE, PLAY_AGAIN, EXIT}

        private Choice choice = Choice.NONE;

        @Override
        public void onChoosePlayAgain() {
            choice = Choice.PLAY_AGAIN;
        }

        @Override
        public void onChooseExit() {
            choice = Choice.EXIT;
        }

        boolean choseToPlayAgain() {
            return choice == Choice.PLAY_AGAIN;
        }

        boolean choseToExit() {
            return choice == Choice.EXIT;
        }
    }
}