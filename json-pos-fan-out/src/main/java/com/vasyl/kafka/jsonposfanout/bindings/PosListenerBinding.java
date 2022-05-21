package com.vasyl.kafka.jsonposfanout.bindings;

import com.vasyl.kafka.jsonposfanout.model.HadoopRecord;
import com.vasyl.kafka.jsonposfanout.model.Notification;
import com.vasyl.kafka.jsonposfanout.model.PosInvoice;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface PosListenerBinding {
    @Input("notification-input-channel")
    KStream<String, PosInvoice> notificationInputStream();

    @Output("notification-output-channel")
    KStream<String, Notification> notificationOutputStream();

    @Input("hadoop-input-channel")
    KStream<String, PosInvoice> hadoopInputStream();

    @Output("hadoop-output-channel")
    KStream<String, HadoopRecord> hadoopOutputStream();

}
