package com.vasyl.kafka.exactlyoncefanout.bindings;

import com.vasyl.kafka.model.PosInvoice;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface PosListenerBinding {

    @Input("pos-input-channel")
    KStream<String, PosInvoice> posInputStream();

}
