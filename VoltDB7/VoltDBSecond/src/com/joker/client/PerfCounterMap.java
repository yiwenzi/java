package com.joker.client;


import org.voltdb.client.ClientResponse;
import org.voltdb.client.exampleutils.PerfCounter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hunter on 2017/11/30.
 * a thread-safe map of performance counters used to trace procedures/statement execution statistics
 */
public class PerfCounterMap {
    private final ConcurrentHashMap<String, PerfCounter> counters = new ConcurrentHashMap<>();

    /**
     * 得到PerfCounter
     *
     * @param counter
     * @return
     */
    public PerfCounter get(String counter) {
        if (!this.counters.containsKey(counter)) {
            this.counters.put(counter, new PerfCounter(false));
        }
        return this.counters.get(counter);
    }

    /**
     * Tracks a call execution by processing the ClientResponse sent back by the VoltDB server.
     *
     * @param counter  the name of the counter to update.
     * @param response the response sent by the VoltDB server, containing details about the procedure/statement execution.
     */
    public void update(String counter, ClientResponse response) {
        this.get(counter).update(response);
    }

    /**
     * Tracks a generic call execution by reporting the execution duration.  This method should be used for successful calls only.
     *
     * @param counter           the name of the counter to update.
     * @param executionDuration the duration of the execution call to track in this counter.
     * @see #update(String counter, long executionDuration, boolean success)
     */
    public void update(String counter, long executionDuration) {
        this.get(counter).update(executionDuration);
    }

    /**
     * Tracks a generic call execution by reporting the execution duration.  This method should be used for successful calls only.
     *
     * @param counter           the name of the counter to update.
     * @param executionDuration the duration of the execution call to track in this counter.
     * @param success           the flag indicating whether the execution call was successful.
     */
    public void update(String counter, long executionDuration, boolean success) {
        this.get(counter).update(executionDuration, success);
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean useSimpleFormat) {
        StringBuilder result = new StringBuilder();
        for (String counter : this.counters.keySet()) {
            if(useSimpleFormat)
                result.append(String.format("%1$-24s:", counter));
            else
                result.append(String.format("---- %1$-24s -------------------------------------------------------\n", counter));
            result.append(this.counters.get(counter).toString(useSimpleFormat));
            result.append("\n\n");
        }
        return result.toString();
    }

    /**
     * 把每一项用指定的分隔符分割
     * @param delimiter
     * @return
     */
    public String toRawString(char delimiter) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, PerfCounter> e : counters.entrySet()) {
            result.append(e.getKey())
                    .append(delimiter)
                    .append(e.getValue().toRawString(delimiter))
                    .append('\n');
        }
        return result.toString();
    }
}
