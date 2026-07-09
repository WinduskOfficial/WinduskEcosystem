package com.windusk.ecosystem.sharing.subscriber;

import com.windusk.ecosystem.sharing.subscriber.PrioritySharingSubscriber;

interface SharingMultiSubscriber {
    PrioritySharingSubscriber generateNewSubscriber();
    void killSubscriber(in PrioritySharingSubscriber subscriber);
}