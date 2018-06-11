package co.early.asaf.adapters;


import android.support.v7.util.DiffUtil;

import co.early.asaf.core.Affirm;
import co.early.asaf.core.time.SystemTimeWrapper;

/**
 * Wraps a DiffResult with a timestamp so that we can abandon it when it gets old. This helps
 * the ChangeAware* classes manage adapter updates appropriately
 */
public class DiffSpec {

    public final DiffUtil.DiffResult diffResult;
    public final long timeStamp;

    /**
     * @param diffResult can be null to indicate no changes
     * @param systemTimeWrapper wrapper for the system time (can not be null)
     */
    public DiffSpec(DiffUtil.DiffResult diffResult, SystemTimeWrapper systemTimeWrapper) {
        this.diffResult = diffResult;
        this.timeStamp = Affirm.notNull(systemTimeWrapper).currentTimeMillis();
    }
}