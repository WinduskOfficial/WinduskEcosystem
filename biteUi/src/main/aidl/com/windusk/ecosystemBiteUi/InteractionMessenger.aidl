package com.windusk.ecosystemBiteUi;

interface InteractionMessenger {
    Bundle getInteractionInfo();
    void interact(in Bundle interactionOutput);
}