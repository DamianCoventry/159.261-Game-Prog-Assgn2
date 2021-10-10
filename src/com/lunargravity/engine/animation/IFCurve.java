package com.lunargravity.engine.animation;

public interface IFCurve {
    enum EndBehaviour { STOP, LOOP }
    EndBehaviour getEndBehaviour();
    void setEndBehaviour(EndBehaviour endBehaviour);
    void update(long nowMs);
    long getEndTime();
    void completed(long elapsedTime);
}
