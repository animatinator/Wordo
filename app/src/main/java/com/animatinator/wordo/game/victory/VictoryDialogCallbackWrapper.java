package com.animatinator.wordo.game.victory;

/**
 * Wraps a {@link VictoryDialogFragment.Callback} with special handling for when the dialog is dismissed without the
 * user selecting an option. In that case, we'll call the delegate {@link VictoryDialogFragment.Callback}'s {@link
 * VictoryDialogFragment.Callback#onChooseExit()} method.
 */
final class VictoryDialogCallbackWrapper implements VictoryDialogFragment.Callback {

    private final VictoryDialogFragment.Callback delegate;
    private boolean userChoseSomething = false;

    VictoryDialogCallbackWrapper(VictoryDialogFragment.Callback delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onChoosePlayAgain() {
        delegate.onChoosePlayAgain();
        userChoseSomething = true;
    }

    @Override
    public void onChooseExit() {
        delegate.onChooseExit();
        userChoseSomething = true;
    }

    void onDismiss() {
        if (!userChoseSomething) {
            delegate.onChooseExit();
        }
    }
}
