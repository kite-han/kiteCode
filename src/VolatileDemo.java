import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyData {

    volatile int  num =0;

    public void addNumTo(int num){
        this.num = num;
    }

    public void plusNum(){
        num ++;
    }

    AtomicInteger atomicInteger = new AtomicInteger();

    public void plusAtomic(){
        atomicInteger.getAndIncrement();
    }
}



/**
 * 关于volatile的验证
 * 1.可见性
 * 2.不保证原子性
 * 3.禁止指令重排
 */
public class VolatileDemo {
    public static void main(String[] args) {

        volatileAtomic();

        //volatileVisibility();
    }

    /**
     * 验证volatile 的原子性
     * 通过20个线程 for循环加1000  由于num被 volatile修饰 不保证原子性 所以会产生加塞的情况 导致最后值不符合预计的20000
     *
     * JUC 包 中的Atomic 类解决相关原子性问题
     */
    private static void volatileAtomic() {
        MyData myData = new MyData();

        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000 ; j++) {
                    myData.plusNum();
                    myData.plusAtomic();
                }
            }).start();
        }

        while (Thread.activeCount() >2){
            Thread.yield();
        }


        System.out.println("finally data.num = "+ myData.num);
        System.out.println("finally data.atomic = "+ myData.atomicInteger);
    }

    /**
     * volatile 可见性实现
     */
    private static void volatileVisibility() {
        MyData data = new MyData();

        new Thread(()->{

            //暂停一会线程
            try {
                TimeUnit.SECONDS.sleep(4);
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "\t come in num ="+data.num);
            data.addNumTo(60);
            System.out.println(Thread.currentThread().getName() + "\t update num ="+ data.num);
        },"AAA").start();

        new Thread(()->{

            //暂停一会线程
         /*   try {
                TimeUnit.SECONDS.sleep(4);
            }catch (Exception e){
                e.printStackTrace();
            }*/
            System.out.println(Thread.currentThread().getName() + "\t come in data =" + data.num);

            data.addNumTo(80);
            System.out.println(Thread.currentThread().getName() + "\t update num ="+ data.num);
        },"BBBB").start();


        while (data.num == 0){
            //
           // System.out.println("线程等待中");
        }

        System.out.println(Thread.currentThread().getName() + " over num = " + data.num);
    }


}
