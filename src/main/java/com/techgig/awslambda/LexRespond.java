package com.techgig.awslambda;


public class LexRespond {
    private DialogAction dialogAction;

    public LexRespond(DialogAction dialogAction) {

        this.dialogAction = dialogAction;
    }

    public DialogAction getDialogAction() {
        return dialogAction;
    }
}
