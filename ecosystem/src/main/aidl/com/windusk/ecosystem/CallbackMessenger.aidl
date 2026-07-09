package com.windusk.ecosystem;

import com.windusk.ecosystem.sharing.subscriber.PrioritySharingSubscriber;
import com.windusk.ecosystem.sharing.subscriber.SharingMultiSubscriber;
import com.windusk.ecosystem.sharing.SharingAssociation;

interface CallbackMessenger {
    void updateSharing(in SharingAssociation association, in Bundle sharingOutput);

    void subscribeToSharing(in PrioritySharingSubscriber subscriber, in Bundle subscribition);
    void unsubscribeFromSharing(in PrioritySharingSubscriber subscriber);

    void multiSubscribeToSharing(in SharingMultiSubscriber subscriber, in Bundle subscribition);
    void multiUnsubscribeFromSharing(in SharingMultiSubscriber subscriber);
    
    void updateSubscriberCall(in PrioritySharingSubscriber subscriber);
}