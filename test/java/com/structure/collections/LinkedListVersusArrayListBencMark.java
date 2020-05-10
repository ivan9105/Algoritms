package com.structure.collections;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * В большинстве источников из интернета можно найти следующее:
 * Если брать в расчет саму операцию без прохода то в принципе да
 * Описание      	        Операция	       ArrayList	LinkedList
 * Взятие элемента	          get	            Быстро	     Медленно
 * Присваивание элемента	  set	            Быстро	     Медленно
 * Добавление элемента	      add	            Быстро	      Быстро
 * Вставка элемента	          add(i, value)	    Медленно	  Быстро
 * Удаление элемента	      remove	        Медленно	  Быстро
 *
 * но с проходом по списку LinkedList выигрывает только лишь на маленьких объемах
 *
 * Преимущества LinkedList-a
 * Deque
 * getFirst/getLast
 * addFirst/addLast
 * removeFirst/removeLast
 * iterator.remove O(1)
 * iterator.add O(1)
 *
 *  Преимущества ArrayList-a
 *  get O(1)
 *  add O(1), без resize
 *  remove (index)
 *  add (index, element)
 *  не использует много мелких объектов в памяти (Node)
 *
 *  как вычисляется размерность массива:
 *
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Measurement(time = 1)
@Warmup(time = 1)
public class LinkedListVersusArrayListBencMark {
    //TODO https://github.com/reactor/reactor-core поковырять
    //TODO java.security package

    @Param({"10", "100", "1000", "10000", "100000", "1000000"})
    private int size;
    private Byte byteVal = 7;
    private List<Byte> arrayList = new ArrayList<>();
    private List<Byte> linkedList = new LinkedList<>();

    @Setup(Level.Invocation)
    public void setup() {
        for (int i = 0; i < size; i++) {
            arrayList.add(byteVal);
        }
        for (int i = 0; i < size; i++) {
            linkedList.add(byteVal);
        }
    }

    @TearDown(Level.Invocation)
    public void tearDown() {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
    }

    @Benchmark
    public void addInMiddleArrayList() {
        arrayList.add(size / 2, byteVal);
    }

    @Benchmark
    public void addInMiddleLinkedList() {
        linkedList.add(size / 2, byteVal);
    }

    @Benchmark
    public void getFromFirstPartFromThirdArrayList() {
        arrayList.get(size / 3);
    }

    @Benchmark
    public void getFromFirstPartFromThirdLinkedList() {
        linkedList.get(size / 3);
    }

    @Benchmark
    public void getFromSecondPartFromThirdArrayList() {
        arrayList.get(size / 3 * 2);
    }

    @Benchmark
    public void getFromSecondPartFromThirdLinkedList() {
        linkedList.get(size / 3 * 2);
    }

    @Benchmark
    public void getFromThirdPartFromThirdArrayList() {
        arrayList.get(size - 1);
    }

    @Benchmark
    public void getFromThirdPartFromThirdLinkedList() {
        linkedList.get(size - 1);
    }

    @Benchmark
    public void setInFirstPartFromThirdArrayList() {
        arrayList.set(size / 3, byteVal);
    }

    @Benchmark
    public void setInFirstPartFromThirdLinkedList() {
        linkedList.set(size / 3, byteVal);
    }

    @Benchmark
    public void setInSecondPartFromThirdArrayList() {
        arrayList.set(size / 3 * 2, byteVal);
    }

    @Benchmark
    public void setInSecondPartFromThirdLinkedList() {
        linkedList.set(size / 3 * 2, byteVal);
    }

    @Benchmark
    public void setInThirdPartFromThirdArrayList() {
        arrayList.set(size - 1, byteVal);
    }

    @Benchmark
    public void setInThirdPartFromThirdLinkedList() {
        linkedList.set(size - 1, byteVal);
    }

    @Benchmark
    public void removeFromArrayList() {
        arrayList.remove(byteVal);
    }

    @Benchmark
    public void removeFromLinkedList() {
        linkedList.remove(byteVal);
    }

    @Benchmark
    public void removeFromMiddleInArrayList() {
        arrayList.remove(size / 2);
    }

    @Benchmark
    public void removeFromMiddleInLinkedList() {
        linkedList.remove(size / 2);
    }

    /**
     * addInMiddleArrayList                       10  avgt    5       447.487 ±       176.962  ns/op
     * addInMiddleArrayList                      100  avgt    5       424.810 ±        75.578  ns/op
     * addInMiddleArrayList                     1000  avgt    5       900.979 ±       975.932  ns/op
     * addInMiddleArrayList                    10000  avgt    5      4978.673 ±      4685.486  ns/op
     * addInMiddleArrayList                   100000  avgt    5     62602.067 ±     23050.490  ns/op
     * addInMiddleArrayList                  1000000  avgt    5   6585493.704 ±  43180917.456  ns/op
     * addInMiddleLinkedList                      10  avgt    5       213.894 ±         7.278  ns/op
     * addInMiddleLinkedList                     100  avgt    5       256.046 ±        19.395  ns/op
     * addInMiddleLinkedList                    1000  avgt    5      1540.446 ±       374.216  ns/op
     * addInMiddleLinkedList                   10000  avgt    5     17961.669 ±      2845.697  ns/op
     * addInMiddleLinkedList                  100000  avgt    5    263728.538 ±     59885.395  ns/op
     * addInMiddleLinkedList                 1000000  avgt    5  18233769.142 ± 100319489.777  ns/op
     * getFromFirstPartFromThirdArrayList         10  avgt    5       200.780 ±         6.280  ns/op
     * getFromFirstPartFromThirdArrayList        100  avgt    5       161.898 ±        41.769  ns/op
     * getFromFirstPartFromThirdArrayList       1000  avgt    5       162.744 ±        77.001  ns/op
     * getFromFirstPartFromThirdArrayList      10000  avgt    5       424.773 ±       767.273  ns/op
     * getFromFirstPartFromThirdArrayList     100000  avgt    5      1563.688 ±      1083.613  ns/op
     * getFromFirstPartFromThirdArrayList    1000000  avgt    5      5912.125 ±     11479.793  ns/op
     * getFromFirstPartFromThirdLinkedList        10  avgt    5       220.702 ±        15.381  ns/op
     * getFromFirstPartFromThirdLinkedList       100  avgt    5       209.312 ±        51.237  ns/op
     * getFromFirstPartFromThirdLinkedList      1000  avgt    5      1284.644 ±       579.522  ns/op
     * getFromFirstPartFromThirdLinkedList     10000  avgt    5     16108.469 ±      7196.189  ns/op
     * getFromFirstPartFromThirdLinkedList    100000  avgt    5    218234.834 ±     55182.328  ns/op
     * getFromFirstPartFromThirdLinkedList   1000000  avgt    5   1561968.900 ±   1021601.715  ns/op
     * getFromSecondPartFromThirdArrayList        10  avgt    5       223.965 ±        23.733  ns/op
     * getFromSecondPartFromThirdArrayList       100  avgt    5       174.575 ±        20.518  ns/op
     * getFromSecondPartFromThirdArrayList      1000  avgt    5       167.732 ±        21.159  ns/op
     * getFromSecondPartFromThirdArrayList     10000  avgt    5       351.913 ±       926.840  ns/op
     * getFromSecondPartFromThirdArrayList    100000  avgt    5      1848.299 ±      4332.482  ns/op
     * getFromSecondPartFromThirdArrayList   1000000  avgt    5      5707.203 ±      7871.935  ns/op
     * getFromSecondPartFromThirdLinkedList       10  avgt    5       234.081 ±        19.041  ns/op
     * getFromSecondPartFromThirdLinkedList      100  avgt    5       213.413 ±        35.943  ns/op
     * getFromSecondPartFromThirdLinkedList     1000  avgt    5      1226.874 ±      1176.045  ns/op
     * getFromSecondPartFromThirdLinkedList    10000  avgt    5     21687.431 ±     22315.038  ns/op
     * getFromSecondPartFromThirdLinkedList   100000  avgt    5    185870.084 ±    128865.954  ns/op
     * getFromSecondPartFromThirdLinkedList  1000000  avgt    5   4244205.913 ±  22126683.163  ns/op
     * getFromThirdPartFromThirdArrayList         10  avgt    5       271.635 ±        97.680  ns/op
     * getFromThirdPartFromThirdArrayList        100  avgt    5       178.090 ±        78.725  ns/op
     * getFromThirdPartFromThirdArrayList       1000  avgt    5       168.084 ±       109.036  ns/op
     * getFromThirdPartFromThirdArrayList      10000  avgt    5       697.699 ±      2001.483  ns/op
     * getFromThirdPartFromThirdArrayList     100000  avgt    5      1870.521 ±      2870.505  ns/op
     * getFromThirdPartFromThirdArrayList    1000000  avgt    5      4936.583 ±      3950.904  ns/op
     * getFromThirdPartFromThirdLinkedList        10  avgt    5       225.711 ±        36.992  ns/op
     * getFromThirdPartFromThirdLinkedList       100  avgt    5       191.798 ±       133.249  ns/op
     * getFromThirdPartFromThirdLinkedList      1000  avgt    5       154.852 ±        55.716  ns/op
     * getFromThirdPartFromThirdLinkedList     10000  avgt    5       250.185 ±       440.387  ns/op
     * getFromThirdPartFromThirdLinkedList    100000  avgt    5      4408.261 ±     23812.197  ns/op
     * getFromThirdPartFromThirdLinkedList   1000000  avgt    5     11529.063 ±      8417.946  ns/op
     * removeFromArrayList                        10  avgt    5       502.056 ±       150.240  ns/op
     * removeFromArrayList                       100  avgt    5       622.054 ±       227.259  ns/op
     * removeFromArrayList                      1000  avgt    5       833.719 ±       531.821  ns/op
     * removeFromArrayList                     10000  avgt    5     11219.935 ±     11101.953  ns/op
     * removeFromArrayList                    100000  avgt    5    109270.029 ±     38797.509  ns/op
     * removeFromArrayList                   1000000  avgt    5  27941289.175 ±  99784673.955  ns/op
     * removeFromLinkedList                       10  avgt    5       269.481 ±       121.498  ns/op
     * removeFromLinkedList                      100  avgt    5       176.046 ±        67.097  ns/op
     * removeFromLinkedList                     1000  avgt    5       235.359 ±       282.182  ns/op
     * removeFromLinkedList                    10000  avgt    5       380.572 ±       663.177  ns/op
     * removeFromLinkedList                   100000  avgt    5      5619.017 ±     29460.676  ns/op
     * removeFromLinkedList                  1000000  avgt    5    143539.671 ±   1105459.104  ns/op
     * removeFromMiddleInArrayList                10  avgt    5       525.664 ±       244.334  ns/op
     * removeFromMiddleInArrayList               100  avgt    5       524.386 ±       257.857  ns/op
     * removeFromMiddleInArrayList              1000  avgt    5       901.343 ±       745.549  ns/op
     * removeFromMiddleInArrayList             10000  avgt    5      5870.162 ±      5672.082  ns/op
     * removeFromMiddleInArrayList            100000  avgt    5     61519.017 ±     49579.457  ns/op
     * removeFromMiddleInArrayList           1000000  avgt    5  33507539.013 ± 147039469.061  ns/op
     * removeFromMiddleInLinkedList               10  avgt    5       286.551 ±        98.894  ns/op
     * removeFromMiddleInLinkedList              100  avgt    5       398.659 ±       207.658  ns/op
     * removeFromMiddleInLinkedList             1000  avgt    5      2131.555 ±      1447.195  ns/op
     * removeFromMiddleInLinkedList            10000  avgt    5     28209.087 ±     19778.865  ns/op
     * removeFromMiddleInLinkedList           100000  avgt    5    376504.241 ±    576476.372  ns/op
     * removeFromMiddleInLinkedList          1000000  avgt    5   7872421.688 ±  28419716.494  ns/op
     * setInFirstPartFromThirdArrayList           10  avgt    5       259.933 ±       101.160  ns/op
     * setInFirstPartFromThirdArrayList          100  avgt    5       204.238 ±       125.271  ns/op
     * setInFirstPartFromThirdArrayList         1000  avgt    5       253.342 ±       200.847  ns/op
     * setInFirstPartFromThirdArrayList        10000  avgt    5      1330.561 ±      2402.859  ns/op
     * setInFirstPartFromThirdArrayList       100000  avgt    5      2322.553 ±      1910.235  ns/op
     * setInFirstPartFromThirdArrayList      1000000  avgt    5     29900.775 ±    161049.267  ns/op
     * setInFirstPartFromThirdLinkedList          10  avgt    5       462.670 ±        75.577  ns/op
     * setInFirstPartFromThirdLinkedList         100  avgt    5       625.362 ±       307.346  ns/op
     * setInFirstPartFromThirdLinkedList        1000  avgt    5      2081.737 ±      1728.157  ns/op
     * setInFirstPartFromThirdLinkedList       10000  avgt    5     20964.366 ±      6418.407  ns/op
     * setInFirstPartFromThirdLinkedList      100000  avgt    5    211692.095 ±    131740.268  ns/op
     * setInFirstPartFromThirdLinkedList     1000000  avgt    5   5231126.571 ±  13915338.432  ns/op
     * setInSecondPartFromThirdArrayList          10  avgt    5       212.380 ±        16.343  ns/op
     * setInSecondPartFromThirdArrayList         100  avgt    5       168.725 ±        37.883  ns/op
     * setInSecondPartFromThirdArrayList        1000  avgt    5       184.981 ±        87.801  ns/op
     * setInSecondPartFromThirdArrayList       10000  avgt    5       553.356 ±      1816.699  ns/op
     * setInSecondPartFromThirdArrayList      100000  avgt    5      2145.323 ±      3485.324  ns/op
     * setInSecondPartFromThirdArrayList     1000000  avgt    5     10666.458 ±     19205.630  ns/op
     * setInSecondPartFromThirdLinkedList         10  avgt    5       233.264 ±        64.640  ns/op
     * setInSecondPartFromThirdLinkedList        100  avgt    5       333.006 ±       206.277  ns/op
     * setInSecondPartFromThirdLinkedList       1000  avgt    5      1272.410 ±       661.315  ns/op
     * setInSecondPartFromThirdLinkedList      10000  avgt    5     13705.509 ±     10995.738  ns/op
     * setInSecondPartFromThirdLinkedList     100000  avgt    5    232708.312 ±    295357.602  ns/op
     * setInSecondPartFromThirdLinkedList    1000000  avgt    5  14173268.383 ± 102534902.517  ns/op
     * setInThirdPartFromThirdArrayList           10  avgt    5       242.525 ±        39.272  ns/op
     * setInThirdPartFromThirdArrayList          100  avgt    5       183.831 ±       104.926  ns/op
     * setInThirdPartFromThirdArrayList         1000  avgt    5       190.926 ±        43.198  ns/op
     * setInThirdPartFromThirdArrayList        10000  avgt    5       732.465 ±      1465.848  ns/op
     * setInThirdPartFromThirdArrayList       100000  avgt    5      1784.819 ±      1515.965  ns/op
     * setInThirdPartFromThirdArrayList      1000000  avgt    5      9431.804 ±     17371.802  ns/op
     * setInThirdPartFromThirdLinkedList          10  avgt    5       227.369 ±        24.568  ns/op
     * setInThirdPartFromThirdLinkedList         100  avgt    5       176.440 ±        77.388  ns/op
     * setInThirdPartFromThirdLinkedList        1000  avgt    5       354.553 ±       990.873  ns/op
     * setInThirdPartFromThirdLinkedList       10000  avgt    5      1238.128 ±      3240.003  ns/op
     * setInThirdPartFromThirdLinkedList      100000  avgt    5      2947.125 ±      7537.532  ns/op
     * setInThirdPartFromThirdLinkedList     1000000  avgt    5     19573.179 ±     71126.384  ns/op
     */
    
    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(LinkedListVersusArrayListBencMark.class.getSimpleName())
                .forks(1)
                .threads(8)
                .build();

        new Runner(opt).run();
    }

}
