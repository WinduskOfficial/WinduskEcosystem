package com.windusk.ecosystem;

import com.windusk.ecosystem.CallbackMessenger;

interface BinderMessenger {
    int getEcosystemVersion();

    void setClientMessenger(CallbackMessenger messenger);
    void changeState(boolean state);
}