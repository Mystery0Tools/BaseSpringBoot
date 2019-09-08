package vip.mystery0.base.springboot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;

/**
 * 53 bits unique id:
 * <p>
 * |--------|--------|--------|--------|--------|--------|--------|--------|
 * |00000000|00011111|11111111|11111111|11111111|11111111|11111111|11111111|
 * |--------|---xxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxx-----|--------|--------|
 * |--------|--------|--------|--------|--------|---xxxxx|xxxxxxxx|xxx-----|
 * |--------|--------|--------|--------|--------|--------|--------|---xxxxx|
 * <p>
 * Maximum ID = 11111_11111111_11111111_11111111_11111111_11111111_11111111
 * <p>
 * Maximum TS = 11111_11111111_11111111_11111111_111
 * <p>
 * Maximum NT = ----- -------- -------- -------- ---11111_11111111_111 = 65535
 * <p>
 * Maximum SH = ----- -------- -------- -------- -------- -------- ---11111 = 31
 * <p>
 * It can generate 64k unique id per IP and up to 2106-02-07T06:28:15Z.
 */
@Component
public class SnowflakeIdWorker {
        private static final Logger logger = LoggerFactory.getLogger(SnowflakeIdWorker.class);
    private final long startEpoch = LocalDate.of(2019, 1, 1).atStartOfDay(ZoneId.of("Asia/Shanghai")).toEpochSecond();
    private long workerId = -1L;
    private static final long MAX_WORKER_ID = ~(-1L << 5);
    private long offset = 0L;
    private static final long MAX_OFFSET = ~(-1L << 16);
    private long lastEpoch = -1L;

    public SnowflakeIdWorker() {
    }

    public SnowflakeIdWorker(long workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        this.workerId = workerId;
    }

    public long nextId() {
        return nextId(System.currentTimeMillis() / 1000);
    }

    private synchronized long nextId(long epochSecond) {
        if (epochSecond < lastEpoch) {
            // warning: clock is turn back:
            logger.warn("clock is back: " + epochSecond + " from previous:" + lastEpoch);
            epochSecond = lastEpoch;
        }
        if (lastEpoch != epochSecond) {
            lastEpoch = epochSecond;
            reset();
        }
        offset++;
        long next = offset & MAX_OFFSET;
        if (next == 0) {
            logger.warn("maximum id reached in 1 second in epoch: " + epochSecond);
            return nextId(epochSecond + 1);
        }
        if (workerId == -1L) {
            return generateId(epochSecond, next, new Random().nextInt(32));
        } else {
            return generateId(epochSecond, next, workerId);
        }
    }

    private void reset() {
        offset = 0;
    }

    private long generateId(long epochSecond, long next, long workerId) {
        return ((epochSecond - startEpoch) << 21) | (next << 5) | workerId;
    }
}