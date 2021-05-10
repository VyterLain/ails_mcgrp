package structure;

import org.apache.commons.lang3.ArrayUtils;
import util.MyParameter;

import java.util.Arrays;
import java.util.List;

public class Segment {
    /* shortest path from A to B*/
    public int start;
    public int end;
    public int cost;
    public int[] list;
    public final Data data;

    public Segment(List<Integer> path, int cost, Data data) {
        this.data = data;
        list = path.stream().mapToInt(Integer::intValue).toArray();
        start = list[0];
        end = list[list.length - 1];
        this.cost = cost;
    }

    public Segment(int[] path, int cost, Data data) {
        this.data = data;
        list = path;
        start = list[0];
        end = list[list.length - 1];
        this.cost = cost;
    }

    /**
     * 连接两个Segment
     *
     * @param segment 需要被连接的Segment
     * @return 结果
     */
    public Segment connect(Segment segment) {
        int c = this.cost;
        int[] l;
        if (data.raw_dist[end][segment.start] < MyParameter.BIG_NUM) {
            c += (segment.cost + data.raw_dist[end][segment.start]);
            l = ArrayUtils.addAll(this.list, segment.list);
        } else {
            Segment s = data.segments[end][segment.start];
            c += (segment.cost + s.cost + data.raw_dist[end][s.start] + data.raw_dist[s.end][segment.start]);
            l = ArrayUtils.addAll(ArrayUtils.addAll(this.list, s.list), segment.list);
        }
        return new Segment(l, c, this.data);
    }

    public boolean domain(Segment segment) {
        int object_p = 0;
        int this_p = 0;
        while (this_p < list.length) {
            if (list[this_p] == segment.list[object_p])
                break;
            this_p++;
        }
        while (this_p < list.length && object_p < segment.list.length) {
            if (list[this_p] == segment.list[object_p]) {
                this_p++;
                object_p++;
            } else break;
        }
        return object_p == segment.list.length;
    }

    public void calculateDist(Data data) {
        cost = 0;
        int pre = list[0];
        for (int i = 1; i < list.length; i++) {
            cost += data.raw_dist[pre][list[i]];
        }
    }

    @Override
    public String toString() {
        return "Segment{" + Arrays.toString(list) + '}';
    }
}
