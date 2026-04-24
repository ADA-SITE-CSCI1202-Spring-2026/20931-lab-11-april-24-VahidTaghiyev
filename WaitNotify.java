public class WaitNotify {

    static class SharedResource {
        private int value;
        private boolean bChanged = false;

        public synchronized int get() throws InterruptedException {
            while (!bChanged) {   
                wait();           
            }
            bChanged = false;     
            return value;
        }

        
        public synchronized void set(int newValue) {
            this.value = newValue;
            bChanged = true;
            notify();           
        }
    }

    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Thread consumer = new Thread(() -> {
            try {
                System.out.println("Consumer: waiting for value...");
                int received = resource.get();
                System.out.println("Consumer: received value = " + received);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer interrupted");
            }
        });

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(2000); 
                int produced = 42;
                System.out.println("Producer: setting value = " + produced);
                resource.set(produced);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Producer interrupted");
            }
        });

        consumer.start();
        producer.start();
    }
}