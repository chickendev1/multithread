# Java Multithreading and Concurrency Best Practices
Sole purpose of using concurrency is to produce scalable and faster program. But always remember, speed comes after correctness. Your Java program must follow its invariant in all conditions, which it would, if executed in sequential manner. If you are new in concurrent Java programming, then take some time to get familiar yourself with different problem arises due to concurrent execution of program e.g. deadlock, race conditions, livelock, starvation etc.

## 1) Use Local Variables
Always try to use local variables instead of creating class or instance variables. Some time, developer use instance variable to save memory and reusing them, because they think creating local variable every time method invoked may take a lot of memory. One example of this is declaring Collection as member and reusing them by using clear() method. This introduce, a shared state in otherwise stateless class, which is designed for concurrent execution. Like in below code, where execute() method is called by multiple threads, and to implement a new functionality, you need a temp collection. In original code, a static List was used and developer's intention was to clear this at the end of execute() method for reuse. He thought that code is safe because of CopyOnWriteArrayList is thread-safe. What he failed to realize that, since this method get called by multiple threads, one thread may see data written by other thread in shared temp List. Synchronization provided by the list is not enough to protect method's invariant here.

public class ConcurrentTask {
	private static List temp = Collections.synchronizedList(new ArrayList());
	  @Override
	  public void execute(Message message) {
		//I need a temporary ArrayList here, use local
		//List temp = new ArrayList();
		//add something from Message into List temp.add("message.getId()"); 
		temp.add("message.getCode()"); 
		//combine id and code store result back to message
		temp.clear(); // Let's resuse it 
	} 
}

Problem :
One Message's data will go to other Message if two call of multiple thread interleaved. e.g. T1 adds Id from Message 1 then T2 adds Id from Message 2, which happens before List get cleared, so one of those message will have corrupted data.

Solution :
1) Add a synchronized block when one thread add something to temp list and clear() it. So that, no thread can access List until one is done with it. This will make that part single threaded and reduce overall application performance by that percentage.

2) Use a local List instead of a global one. Yes it will take few more bytes, but you are free from synchronization and code is much more readable. Also, you should be worrying too much about temporary objects, GC and JIT will take care of that.

This is just one of those cases, but I personally prefer a local variable rather than a member variable in multi-threading, until its part of design.
## 2) Prefer Immutable Classes
Another and most widely known Java multi-threading best practice is to prefer Immutable class. Immutable classes like String, Integer and other wrapper classes greatly simplify writing concurrent code in Java because you don't need to worry about there state. Immutable classes reduce amount of synchronization in code. Immutable classes, once created, can not be modified. One of the best example of this is java.lang.String, any modification on String e.g. converting it into uppercase, trim or substring would produce another String object, keeping original String object intact.

## 3) Minimize locking scope
Any code which is inside lock will not be executed concurrently and if you have 5% code inside lock than as per Amdahl's law, your application performance can not be improved more than 20 times. Main reason of this is those 5% code will always executed sequentially. You can reduce this amount by minimizing scope of locking, try to only lock critical sections. One of the best example of minimizing scope of locking is double checked locking idiom, which works by using volatile variable after Java 5 improvements on Java Memory model.
## 4) Prefer Thread Pool Executors instead of Threads
Creating Thread is expensive. If you want a scalable Java application, you need to use thread pool. Apart from cost, managing thread requires lots of boiler-plate code and mixing those with business logic reduces readability. Managing threads is a framework level task and should be left to Java or any proprietary framework you are using. JDK has a well built, rich and fully tested Thread pool also known as Executor framework, which should be utilized whenever needed.

## 5) Prefer Synchronization utility over wait notify

This Java multi-threading practice inspires from Java 1.5, which added lot of synchronization utilities like CycicBariier, CountDownLatch and Sempahore. You should always look to JDK concurrency and synchronization utility, before thinking of wait and notify. It's much easier to implement producer-consumer design with BlockingQueue than by implementing them using wait and notify. See those two links to compare yourself. Also, it's much easier to wait for 5 threads using CountDownLatch to complete there task rather than implementing same utility using wait and notify. Get yourself familiar with java.util.concurrent package for writing better Java concurrency code.



## 6) Prefer BlockingQueue for producer-consumer design

This multi-threading and concurrency best practice is related to earlier advice, but I have made it explicitly because of it's importance in real world concurrent applications. Many of concurrency problem are based on producer-consumer design pattern and BlockingQueue is best way to implement them in Java. Unlike Exchanger synchronization utility which can be used to implement single producer-consumer design, blocking queue can also handle multiple producer and consumers. See producer consumer with BlockingQueue in Java to learn more about this tip.



## 7) Prefer Concurrent Collections over synchronized Collection

As mentioned in my post about Top 5 Concurrent Collections in Java, they tend to provide more scalablility and performance than there synchronized counterpart. ConcurrentHashMap, which is I guess one of the most popular of all concurrent collection provide much better performance than synchronized HashMap or Hashtable if number of reader thread outnumber writers. Another advantage of Concurrent collections are that, they are built using new locking mechanism provided by Lock interface and better poised to take advantage of native concurrency construct provided by underlying hardware and JVM. In the same line, consider using CopyOnWriteArrayList in place of synchronized List, if List is mostly for reading purpose with rare updates.



## 8) Use Semaphore to create bounds

In order to build a reliable and stable system, you must have bounds on resources like database, file system, sockets etc. In no situation, your code create or use infinite number of resources. Semaphore is a good choice to have a limit on expensive resource like database connection, by the way leave that to your Connection pool. Semaphore is very helpful to creating bounds and blocking thread if resource is not available. You can follow this tutorial to learn how to use use Semaphore in Java.



## 9) Prefer synchronized block over synchronized method

This Java multi-threading best practice is an extension of earlier best practice about minimizing scope of locking.  Using synchronized block is one way to reduce scope of lock and it also allow you to lock on object other than "this", which represent current object. Today, your first choice should be atomic variable, followed by volatile variable if your synchronization requirement is satisfied by using them. Only if you need mutual exclusion you can consider using ReentrantLock followed by plain old synchronized keyword. If you are new to concurrency and not writing code for high frequency trading or any other mission critical application, stick with synchronized keyword because its much safer and easy to use. If you are new to Lock interface, see my tutorial how to use Lock in multi-threaded Java program for step by step guide.



## 10) Avoid Using static variables

As shown in first multi-threading best practice, static variables can create lots of issues during concurrent execution. If you happen to use static variable, consider it making static final constants and if static variables are used to store Collections like List or Map then consider using only read only collections. If you are thinking of reusing Collection to save memory, please see the example in first best practice to learn how static variables can cause problem in concurrent programs.



## 11) Prefer Lock over synchronized keyword

This is a bonus multi-threading best practice, but it's double edge sword at same time. Lock interface is powerful but every power comes with responsibility. Different locks for read and write operation allows to build scalable data structures like ConcurrentHashMap, but it also require lot of care during coding. Unlike synchronized keyword, thread doesn't release lock automatically. You need to call unlock() method to release a lock and best practice is to call it on finally block to ensure release in all conditions. here is an idiom to use explicitly lock in Java :

lock.lock();
try { 
	//do something ... 
} finally { 
	lock.unlock(); 
}

By the way, this article is in line with 10 JDBC best practices and 10 code comments best practices, if you haven't read them already, you may find them worth reading. As some of you may agree that there is no end of best practices, It evolves and get popular with time. If you guys have any advice, experience, which can help any one writing concurrent program in Java, please share.


That's all on this list of Java multithreading and concurrency best practices. Once again, reading Concurrency Practice in Java and Effective Java is worth reading again and again. Also developing a sense for concurrent execution by doing code review helps a lot on visualizing problem during development. On closing note, let us know what best practices you follow while writing concurrent applications in Java?

<br/>
[1]. http://javarevisited.blogspot.com/2015/05/top-10-java-multithreading-and.html#ixzz5AGe0sm1B