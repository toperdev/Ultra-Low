package net.UltraLow.Profiling;

/**
 * Created with IntelliJ IDEA.
 * User: James
 * Date: 3/2/13
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfilingPart {

    public String name;

    public long partBeginTime = 0;
    public long partEndTime = 0;

    public ProfilingPart(String name) {
        this.name = name;
    }
}
