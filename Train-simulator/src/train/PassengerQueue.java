/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package train;

public class PassengerQueue {

    private Passenger[] queueArray;
    private int first;
    private int last;
    private int maxStayInQueue = 50;
    private int maxLength;
    private int count;         // current size of the queue

    // Constructor to initialize queue
    PassengerQueue(int size) {
        queueArray = new Passenger[size];
        maxLength = size;
        first = 0;
        last = -1;
        count = 0;
    }

    // Utility function to remove front element from the queue
    public void dequeue() {
        // check for queue underflow
        if (isEmpty()) {
            System.out.println("UnderFlow");
        }

//        System.out.println("Removing " + queueArray[first].getId());

        first = (first + 1) % maxLength;
        count--;
    }

    // Utility function to add an item to the queue
    public void enqueue(Passenger item) {
        // check for queue overflow
        if (isFull()) {
            System.out.println("OverFlow");
        }

//        System.out.println("Inserting " + item.getId());

        last = (last + 1) % maxLength;
        queueArray[last] = item;
        count++;
    }

    // Utility function to return front element in the queue
    public Passenger peek() {
        if (isEmpty()) {
            System.out.println("UnderFlow");       
        }
        return queueArray[first];
    }

    public Passenger getItem(int position) {
        return queueArray[first+position];
    }

    // Utility function to return the size of the queue
    public int size() {
        return count;
    }

    // Utility function to check if the queue is empty or not
    public Boolean isEmpty() {
        return (size() == 0);
    }

    // Utility function to check if the queue is empty or not
    public Boolean isFull() {
        return (size() == maxLength);
    }

    public void removeAll() {
        queueArray = new Passenger[maxLength];
        first = 0;
        last = -1;
        count = 0;
    }

}
