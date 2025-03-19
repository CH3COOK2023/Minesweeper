package functions;

public class Queue<E>{

    // head--tail  <---- add here
    private final E[] queue;
    private static final int CAPACITY = Constant.maxConcurrent;
    private int tail = -1;
    private int head = -1;
    private boolean empty = true;
    @SuppressWarnings("unchecked")
    public Queue(){
        queue = (E[]) new Object[CAPACITY];
    }
    public boolean add(E e){
        if(empty)
        {
            head = tail = 0;
            queue[head] = e;
            empty = false;
            return true;
        }
        int temp = tail + 1;
        if(temp == CAPACITY)
            temp = 0;
        if(temp == head) return false;
        tail = temp;
        queue[tail] = e;
        return true;
    }
    private void deleteTail(){
        if(isEmpty()) return;
        if(head == tail) // 只有一个元素
        {
            head = tail = -1;
            empty = true;
            return;
        }
        --tail;
        if(tail == -1)
            tail = CAPACITY-1;
    }
    public E getTail(){
        if(isEmpty()) return null;
        return queue[tail];
    }
    public E popTail(){
        if(isEmpty()) return null;
        E e = queue[tail];
        deleteTail();
        return e;
    }
    public boolean isEmpty(){
        return queue == null || empty;
    }
}
