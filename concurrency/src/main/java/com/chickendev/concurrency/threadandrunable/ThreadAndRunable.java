package com.chickendev.concurrency.threadandrunable;

public class ThreadAndRunable {

    /**
     * Creating a thread by using lamda expression in java 8
     */
    Runnable r = () -> {
        PersonalInfo pr = new PersonalInfo();
        pr.setFullName("Nguyen Thanh Cong");
        pr.setEmail("cong1989.nt@gmail.com");

        System.out.println("Email: "
                + pr.getEmail()
                + "\nfull name: "
                + pr.getFullName()
                + "\nThread name: "
                + Thread.currentThread().getName()
        );
    };

    /**
     * Creating a thread by using anonymous class
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            PersonalInfo pr = new PersonalInfo();
            pr.setFullName("Nguyen Thanh Cong");
            pr.setEmail("cong1989.nt@gmail.com");

            System.out.println("Email: "
                    + pr.getEmail()
                    + "\nfull name: "
                    + pr.getFullName()
                    + "\nThread name: "
                    + Thread.currentThread().getName()
            );
        }
    };

    /**
     * Main method to run application on main thread
     * @param args
     */
    public static void main(String[] args) {
        PersionalInforThread inforThread = new PersionalInforThread();
        PersionalInforThread inforThread2 = new PersionalInforThread();
        inforThread.setName("thread-1");
        inforThread2.setName("thread-2");
//        Runnable r = new PersonalInforRunable();
//        Thread t = new Thread(r);
//        t.setName("Thread-1");
//        t.start();
        inforThread.start();
        inforThread2.start();

    }
}

/**
 * Creating a thread by extends from thread class.
 * Drawback: Because java can not support Multiple inheritance so that You
 * could not using this method to create an Thread in java.
 */
class PersionalInforThread extends Thread {
    @Override
    public void run() {
        PersonalInfo info = new PersonalInfo();
        info.setEmail("cong1989.nt@gmail.com");
        info.setFullName("Nguyen Thanh Cong");

        System.out.println("Email: "
                + info.getEmail()
                + "\nfull name: "
                + info.getFullName()
                + "\nThread name: "
                + Thread.currentThread().getName()
        );
    }
}

/**
 * Creating a thread by extends from thread class.
 * You should use this method to create an Thread object
 * because You can entends an class when using this method.
 */
class PersonalInforRunable implements Runnable {

    @Override
    public void run() {
        PersonalInfo info = new PersonalInfo();
        info.setEmail("cong1989.nt@gmail.com");
        info.setFullName("Nguyen Thanh Cong");

        System.out.println("Email: "
                + info.getEmail()
                + "\nfull name: "
                + info.getFullName()
                + "\nThread name: "
                + Thread.currentThread().getName()
        );
    }
}
