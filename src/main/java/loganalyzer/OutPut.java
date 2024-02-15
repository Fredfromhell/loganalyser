package loganalyzer;

import java.time.LocalTime;

public class OutPut {

    private LocalTime start;
    private LocalTime end;
    private float upTime;

    public OutPut(LocalTime start, LocalTime end, float upTime) {
        this.start = start;
        this.end = end;
        this.upTime = upTime;
    }
    public float getUpTime() {
        return upTime;
    }

    @Override
    public String toString() {
        return start + "    " + end + "     " + upTime;
    }
}
