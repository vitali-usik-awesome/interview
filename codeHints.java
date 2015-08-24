/*
* EQUALS/HASHCODE
* (equals - сравнение по ссылке по умолчанию, hashcode - адресс в мамяти - http://habrahabr.ru/post/168195/)
*/
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + varA;
        result = prime * result + varB;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BlackBox other = (BlackBox) obj;
        if (varA != other.varA)
            return false;
        if (varB != other.varB)
            return false;
        return true;
    }
	
public class TestClass {
    private boolean booleanValue = true;
    private char charValue = 'd';
    private String stringValue = "TestClass";
    private long longValue = 34829245849498300l;
    private float floatValue = 345832400.93f;
    private double doubleValue = 98584292348454.9834;
    private byte[] arrayValue = {1, 2, 3};
    
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + ( booleanValue ? 1 : 0 );
        result = 37 * result + (int) charValue;
        result = 37 * result + (stringValue == null ? 0 : stringValue.hashCode());
        result = 37 * result + (int)(longValue - (longValue >>> 32));
        result = 37 * result + Float.floatToIntBits(floatValue);
        long longBits = Double.doubleToLongBits(doubleValue);
        result = 37 * result + (int)(longBits - (longBits >>> 32));
        for( byte b : arrayValue )
            result = 37 * result + (int) b;
        return result;
    }
}

/*
* SINGLETON
* skipy
*/
public class SingletonImpl{

    private static SingletonImpl self = new SingletonImpl();

    private SingletonImpl(){
        super();
        // perform initialization here
    }

    public static  SingletonImpl getInstance(){
        return self;
    }
}

// skipy: создание объекта в тот момент когда он требуется
public class SingletonImpl{

    private static SingletonImpl self = null;

    private SingletonImpl(){
        super();
        // perform initialization here
        self = this;
    }

    public static SingletonImpl getInstance(){
        return (self == null) ? new SingletonImpl() : self;
    }
}

/* skipy: Для того, чтобы избежать такой ситуации, нужно синхронизировать доступ к переменной self. Наилучшим способом является следующий: объявить метод getInstance как synchronized. 
* В этом случае виртуальная машина гарантирует, что в каждый момент времени только один поток имеет возможность исполнять код метода getInstance, и подобных коллизий возникнуть не должно. 
* Таким образом, код превращается в следующий:
*/
public class SingletonImpl{

    private static SingletonImpl self = null;

    private SingletonImpl(){
        super();
        // perform initialization here
        self = this;
    }

    public static synchronized SingletonImpl getInstance(){
        return (self == null) ? new SingletonImpl() : self;
    }
}

// skipy: Этот прием носит специальное название – Double-checked locking.
public static SingletonImpl getInstance(){
    if (self == null){
        synchronized(SingletonImpl.class){
            if (self == null){
                self = new SingletonImpl();
            }
        }
    }
    return self;
}


public class Singleton { 
   // Private constructor prevents instantiation from other classes
   private Singleton() {
   } 
   /**
    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
   private static class SingletonHolder { 
     public static final Singleton INSTANCE = new Singleton();
   } 
   public static Singleton getInstance() {
     return SingletonHolder.INSTANCE;
   }
}

// Работает с новой семантикой volatile 
// Не работает в Java 1.4 и более ранних версиях из-за семантики volatile class Foo {     
	private volatile Helper helper = null;     
	public Helper getHelper() {         
		if (helper == null) {
	             synchronized(this) {
	                 if (helper == null) 
	                    helper = new Helper(); 
	            }
         	}         
	return helper;     
	}       
	// и остальные члены класса… 
}

/*
* Суть в том, что берем какой то центральный элемент, далее разбиваем на 2 пачки (левая и правая) по сторонам от этого центрального элемента.
* Далее сравниваем из каждой пачки с центральным элементом, и меняем местами, если из из левой пачки нашли элемент меньше центрального, с элементом из правой пачки, который больше центрального элемента.
* Далее повторяем такую операцию для этих 2-ух пачек. То есть берем левую пачку, разбиваем на 2 пачки опять и итд.
*/
import java.util.Random;

public class QuickSortExample {
    public static int ARRAY_LENGTH = 30;
    private static int[] array = new int[ARRAY_LENGTH];
    private static Random generator = new Random();

    public static void initArray() {
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            array[i] = generator.nextInt(100);
        }
    }

    public static void printArray() {
        for (int i = 0; i < ARRAY_LENGTH - 1; i++) {
            System.out.print(array[i] + ", ");
        }
        System.out.println(array[ARRAY_LENGTH - 1]);
    }

    public static void quickSort() {
        int startIndex = 0;
        int endIndex = ARRAY_LENGTH - 1;
        doSort(startIndex, endIndex);
    }

    private static void doSort(int start, int end) {
        if (start >= end)
            return;
        int i = start;
        int j = end;
        int center = i - (i - j) / 2;
        while (i < j) {
            // из первой половины ищем индекс, значения для которого меньше либо равно центральному значению
            while (i < center && (array[i] <= array[center])) {
                i++;
            }
            // из второй половины ищем индекс, значения для которого больше значения из центрального
            while (j > center && (array[center] <= array[j])) {
                j--;
            }

            // если при прогоне еще недостигли середины
            if (i < j) {
                // то меняем местами значения из 1 и 2 половин
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                if (i == center)
                    center = j;
                else if (j == center)
                    center = i;
            }
        }
        doSort(start, center);
        doSort(center + 1, end);
    }

    public static void main(String[] args) {
        initArray();
        printArray();
        quickSort();
        printArray();
    }
}

/*
* Напишите класс, который будет принимать аргумент (имя файла) и выводить строчки из этого файла в консоль.
*/
public Class Test{ 
public void testMethod(String filename) 
  try {
    Scanner sc = new Scanner (new File(filename));
    while (sc.hasNext()) {
        System.out.println(sc.next());
    }
  }
  catch (FileNotFoundException e) {
    ///smth
  }
 }
}

/*
* Threads
*/
public class SampleThread extends Thread{

    public SampleThread(){
        super();
    }

    public void run(){
        System.out.println("Hello, threads world!");
    }

    public static void main(String[] args){
        Thread t = new SampleThread();
        t.start();
    }
}

public class SampleRunnable implements Runnable{

    public SampleRunnable(){
        super();
    }

    public void run(){
        System.out.println("Hello, threads world!");
    }

    public static void main(String[] args){
        Runnable r = new SampleRunnable();
        Thread t = new Thread(r);
        t.start();
    }

}



/**
 * DeadLockTest
 *
 * @author Eugene Matyushkin
 * @version 1.0
 */
public class DeadLockTest{

    public static void main(String[] args){
        A a1 = new A();
        A a2 = new A();
        Thread t1 = new Thread(new Tester(a1,a2));
        Thread t2 = new Thread(new Tester(a2,a1));
        t1.start();
        t2.start();
    }

    public static class Tester implements Runnable{

        static int nextId = 1;

        private A obj1;
        private A obj2;
        private int id = 0;

        public Tester(A obj1, A obj2){
            this.obj1 = obj1;
            this.obj2 = obj2;
            id = nextId++;
        }

        public void run(){
            print("Setting value to obj1... ");
            obj1.setValue(id);
            print("done.");
            print("Comparing objects... ");
            print("Done. Result: "+((obj1.equals(obj2)) ? "equal" : "not equal"));
        }

        private void print(String msg){
            System.out.println("Thread #"+id+": "+msg);
        }
    }

    public static class A{

        private int value = 0;

        synchronized void setValue(int value){
            this.value = value;
        }

        synchronized int getValue(){
            return value;
        }

        public synchronized boolean equals(Object o){
            A a = (A) o;
            try{
                Thread.sleep(1000);
            }catch(InterruptedException ex){
                System.err.println("Interrupted!");
            }
            return value == a.getValue();
        }
    }
}






